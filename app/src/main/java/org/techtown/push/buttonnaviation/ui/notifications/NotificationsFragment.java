package org.techtown.push.buttonnaviation.ui.notifications;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.push.buttonnaviation.R;
import org.techtown.push.buttonnaviation.databinding.FragmentNotificationsBinding;
import org.techtown.push.buttonnaviation.ui.SurveyViewModel;
import org.techtown.push.buttonnaviation.ui.home.SurveyResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class NotificationsFragment extends Fragment {
    private FragmentNotificationsBinding binding;
    private LineChartView chartTop;
    private LineChartData lineData;
    private ArrayList<SurveyResult> surveyResultList;
    private HashMap<String, ArrayList<SurveyResult>> surveyResultHashMap;
    private ArrayList<ArrayList<SurveyResult>> sortedList;
    private PriorityQueue<ArrayList<SurveyResult>> surveyPQ;
    private NotificationsAdapter notificationsAdapter;
    private RecyclerView notificationRecyclerView;
    private GridLayoutManager notificationLayoutManager;
    private int[] party_colors = {R.color.c1_color, R.color.c2_color, R.color.c3_color, R.color.c4_color, R.color.black};


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SurveyViewModel surveyViewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);

        chartTop = (LineChartView) binding.chartTop;
        notificationRecyclerView = binding.notificationRecyclerview;

        //set layoutManager
        notificationLayoutManager = new GridLayoutManager(this.getContext(), 2);
        notificationRecyclerView = binding.notificationRecyclerview;
        notificationRecyclerView.setLayoutManager(notificationLayoutManager);

        //set listener
        notificationsAdapter = new NotificationsAdapter();
        OnSurveyArrayClickListener surveyArrayClickListener = new OnSurveyArrayClickListener() {
            @Override
            public void onSurveyArrayItemClick(NotificationsAdapter.SurveyArrayViewHolder holder, View view, int position) {
                ArrayList<SurveyResult> item = notificationsAdapter.getItem(position);
                generateLineData(item);
                binding.linesMainText.setText(item.get(0).getRequester());
                binding.notificationZoomScroll.setVisibility(View.INVISIBLE);
            }
        };
        notificationsAdapter.setOnSurveyArrayClickListener(surveyArrayClickListener);

        //get survey array data
        this.surveyResultList = (ArrayList<SurveyResult>) surveyViewModel.getSurveyResultList().getValue();
        if(surveyResultList != null) {
            put2HashMap(surveyResultList);
            getSurveyArrayResult();
        }

        //set live data on changed listener
        surveyViewModel.getSurveyResultList().observe(getViewLifecycleOwner(), new Observer<List<SurveyResult>>() {
            @Override
            public void onChanged(List<SurveyResult> surveyResultList) {
                if(surveyResultList != null && surveyResultList.size() != getNotificationSurveyResultList().size()) {
                    put2HashMap((ArrayList<SurveyResult>) surveyResultList);
                    getNotificationsAdapter().clearItems();
                    getSurveyArrayResult();
                }
            }
        });


        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public ArrayList<SurveyResult> getNotificationSurveyResultList() {
        return this.surveyResultList;
    }

    public NotificationsAdapter getNotificationsAdapter() {
        return this.notificationsAdapter;
    }

    private void getSurveyArrayResult() {
        notificationsAdapter.addList(sortedList);
        notificationRecyclerView.setAdapter(notificationsAdapter);
        showPos0Graph();
    }

    private void showPos0Graph() {
        binding.notificationProgressBar.setVisibility(View.INVISIBLE);
        binding.chartTop.setVisibility(View.VISIBLE);
        ArrayList<SurveyResult> item = notificationsAdapter.getItem(0);
        generateLineData(item);
        binding.linesMainText.setText(item.get(0).getRequester());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void put2HashMap(ArrayList<SurveyResult> surveyResultList) {
        surveyResultHashMap = new HashMap<>();
        for(int i = surveyResultList.size()-1; i >= 0; i--) {
            SurveyResult nowResult = surveyResultList.get(i);
            String key = nowResult.getRequester();
            if(!surveyResultHashMap.containsKey(key)) {
                surveyResultHashMap.put(key, new ArrayList<SurveyResult>());
            }
            surveyResultHashMap.get(key).add(nowResult);
        }
        surveyPQ = new PriorityQueue<ArrayList<SurveyResult>>(((t1, t2) -> t2.size() - t1.size()));
        for(ArrayList<SurveyResult> list : surveyResultHashMap.values()) {
            if(list.size() >= 2) {
                surveyPQ.add(list);
            }
        }
        sortedList = new ArrayList<>();
        while(!surveyPQ.isEmpty()) {
            sortedList.add(surveyPQ.poll());
        }
    }

    private void generateLineData(ArrayList<SurveyResult> data) {
        generateInitialLineData(data);
        generateNewLineData(data);
    }

    private void generateInitialLineData(ArrayList<SurveyResult> data) {
        int numValues = data.size();
        List<Line> lines = new ArrayList<Line>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        SimpleLineChartValueFormatter formatter = new SimpleLineChartValueFormatter(1);
        for(int j = 0; j < 5; j++) {//4 candidates + 1 black line
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; i++) {//number of dates
                values.add(new PointValue(i, 0));//x, y
                if(j == 0) {
                    axisValues.add(new AxisValue(i).setLabel(data.get(i).getUploadMonth() + "/" + data.get(i).getUploadDay()));
                }
            }
            Line line = new Line(values);
            line.setHasLabels(true);
            line.setColor(ContextCompat.getColor(getContext(), party_colors[j])).setCubic(false);
            line.setFormatter(formatter);
            lines.add(line);
        }

        List<AxisValue> yvals = new ArrayList<>();
        for(int i = 0; i <= 50; i += 5) {
            yvals.add(new AxisValue(i).setLabel("" + i));
        }
        lineData = new LineChartData(lines);
        lineData.setValueLabelBackgroundAuto(false);
        lineData.setValueLabelBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
        lineData.setValueLabelsTextColor(ContextCompat.getColor(getContext(), R.color.black));

        //draw axis label
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(false)
                .setTextColor(ContextCompat.getColor(getContext(), R.color.home_fragment_text)));
        lineData.setAxisYLeft(new Axis(yvals).setHasLines(true)
                .setTextColor(ContextCompat.getColor(getContext(), R.color.home_fragment_text)));

        chartTop.setLineChartData(lineData);
        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);
        Viewport maxViewport = new Viewport(0, 55, numValues-1, 0);
        chartTop.setMaximumViewport(maxViewport);
        Viewport v = new Viewport(chartTop.getMaximumViewport());
        v.left = Math.max(0, numValues-7);
        v.right = numValues-1;
        chartTop.setCurrentViewport(v);
        chartTop.setZoomEnabled(true);
        chartTop.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        chartTop.setMaxZoom(4.0f);//pass value >= 1
        //set padding
        float density = getActivity().getResources().getDisplayMetrics().density;
        int padding = ChartUtils.dp2px(density, 15);//for default settings use ChartUtils.dp2px(density, 6 + 4)
        int padding2 = ChartUtils.dp2px(density, 10);//for default settings use ChartUtils.dp2px(density, 6 + 4)
        chartTop.setPadding(0, padding, padding2, padding);

    }

    private void generateNewLineData(ArrayList<SurveyResult> data) {
        //modify data targets
        List<PointValue> values1 = lineData.getLines().get(0).getValues();
        List<PointValue> values2 = lineData.getLines().get(1).getValues();
        List<PointValue> values3 = lineData.getLines().get(2).getValues();
        List<PointValue> values4 = lineData.getLines().get(3).getValues();
        for(int i = 0; i < data.size(); i++) {
            SurveyResult nowResult = data.get(i);
            values1.get(i).setTarget(values1.get(i).getX(), Float.parseFloat(nowResult.getC1()));
            values2.get(i).setTarget(values2.get(i).getX(), Float.parseFloat(nowResult.getC2()));
            values3.get(i).setTarget(values3.get(i).getX(), Float.parseFloat(nowResult.getC3()));
            values4.get(i).setTarget(values4.get(i).getX(), Float.parseFloat(nowResult.getC4()));
        }
        //delete line 5
        lineData.getLines().remove(4);

        // Start new data animation with 300ms duration;
        chartTop.startDataAnimation(700);
    }

    private void copyLineChartViewSetting(LineChartView v1, LineChartView v2) {
        v2.setViewportCalculationEnabled(v1.isViewportCalculationEnabled());
        v2.setMaximumViewport(v1.getMaximumViewport());
        v2.setCurrentViewport(v1.getCurrentViewport());
        v2.setZoomEnabled(v1.isZoomEnabled());
        v2.setZoomType(v1.getZoomType());
        v2.setMaxZoom(v1.getMaxZoom());
        v2.setPadding(v1.getPaddingLeft(), v1.getPaddingTop(), v1.getPaddingRight(), v1.getPaddingBottom());
    }

    public interface OnSurveyArrayClickListener{
        public void onSurveyArrayItemClick(NotificationsAdapter.SurveyArrayViewHolder holder, View view, int position);

    }
}