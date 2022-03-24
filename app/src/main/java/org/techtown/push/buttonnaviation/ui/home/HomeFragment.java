package org.techtown.push.buttonnaviation.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.push.buttonnaviation.OnSurveyResultItemClickListener;
import org.techtown.push.buttonnaviation.R;
import org.techtown.push.buttonnaviation.databinding.FragmentHomeBinding;
import org.techtown.push.buttonnaviation.ui.SurveyViewModel;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ColumnChartView columnChartView;
    private ColumnChartData columnChartData;
    private LinearLayoutManager layoutManager;
    private SurveyResultAdapter surveyResultAdapter;
    private RecyclerView homeRecyclerView;
    private List<SurveyResult> surveyResultList;
    private SurveyViewModel surveyViewModel;
    private final String[] candidates = {"이재명", "윤석열", "심상정", "안철수"};
    private final int[] party_colors = {0, Color.parseColor("#FF004EA1"), Color.parseColor("#FFe61e2b"),
            Color.parseColor("#FFFFED00"), Color.parseColor("#FFEA5504"), 0};
    private int type = 0;
    private final int mPageCnt = 15;
    private int mLoadItemCnt = 15;
    private boolean isLoading = false;
    private boolean isLastLoading = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        surveyViewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);

        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        homeRecyclerView = binding.homeRecyclerView;
        homeRecyclerView.setLayoutManager(layoutManager);
        surveyResultAdapter = new SurveyResultAdapter();

        //set listener
        OnSurveyResultItemClickListener home_recycler_listener = new OnSurveyResultItemClickListener() {
            @Override
            public void onSurveyResultItemClick(SurveyResultAdapter.SurveyViewHolder holder, View view, int position) {
                SurveyResult item = surveyResultAdapter.getItem(position);
                //draw graph
                generateColumnData(new String[]{"0", item.getC1(), item.getC2(), item.getC3(), item.getC4(), "0"});
                //set upper text
                binding.dateTextView.setText(String.format("%s월 %s일", item.getUploadMonth(), item.getUploadDay()));
                binding.orgTextView.setText(String.format("%s", item.getRequester()));
            }
            @Override
            public void onSurveyResultSpecifyClick(SurveyResultAdapter.SurveyViewHolder holder, View view, int position) {
                //see web browser
                String url = surveyResultAdapter.getItem(position).getUrl();
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(url));
                startActivity(webIntent);
            }
        };
        surveyResultAdapter.setOnSurveyResultItemClickListener(home_recycler_listener);
        //(end) set listener

        //add item to adapter, 맨위로, 더보기 등등 넣기
        getSurveyResult();
        //(end) add item

        //set 15 loader
        homeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = layoutManager.findLastVisibleItemPosition();

                if(!isLoading & !isLastLoading){
                    if(layoutManager != null && lastItem == surveyResultAdapter.getItemCount() - 1){
                        isLoading = true;
                        onLoadMore(type, surveyResultAdapter.getItemCount());
                    }
                }
            }
        });


        //graph setting
        columnChartView = (ColumnChartView) binding.columnChartView;
//        //set padding
        float density = getActivity().getResources().getDisplayMetrics().density;
        int padding = ChartUtils.dp2px(density, 15);//for default settings use ChartUtils.dp2px(density, 6 + 4)
        int padding2 = ChartUtils.dp2px(density, 0);//for default settings use ChartUtils.dp2px(density, 6 + 4)
        columnChartView.setPadding(padding2, padding, padding2, padding);
        //graph setting2


//        generateInitialLineData();
        generateInitialColumnData();

        return root;
    }

    private void onLoadMore(int type, int startIdx) {
        surveyResultAdapter.addItem(null);
        surveyResultAdapter.notifyItemInserted(surveyResultAdapter.getItemCount() - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startLoadMore(type, startIdx, startIdx+mPageCnt);
            }
        }, 1000);
    }

    private void startLoadMore(int type, int startIdx, int endIdx) {
        surveyResultAdapter.removeItem(surveyResultAdapter.getItemCount()-1);
        int scrollPosition = surveyResultAdapter.getItemCount();
        surveyResultAdapter.notifyItemRemoved(scrollPosition);
        if (startIdx >= surveyResultList.size()) {
            isLastLoading = true;
        } else {
            endIdx = Math.min(surveyResultList.size(), endIdx);
            surveyResultAdapter.addList(surveyResultList.subList(startIdx, endIdx));
            mLoadItemCnt = scrollPosition;
            mLoadItemCnt += mPageCnt;
            isLastLoading = false;
        }

        isLoading = false;
    }

    private void getSurveyResult() {
        SurveyResultRetrofit surveyResultRetrofit = new SurveyResultRetrofit();
        surveyResultRetrofit.select.getAllSurveyResult().enqueue(new Callback<List<SurveyResult>>() {
            @Override
            public void onResponse(Call<List<SurveyResult>> call, Response<List<SurveyResult>> response) {
                if(response.isSuccessful()) {
                    surveyResultListSetter(response.body());
                    surveyResultListAdder();
                    parse2ViewModel();
                    surveyResultRecyclerViewAdapterSetter();
                    binding.homeProgressBar.setVisibility(View.INVISIBLE);
                    homeRecyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<SurveyResult>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void parse2ViewModel() {
        surveyViewModel.setSurveyResultList(surveyResultList);

    }

    private void surveyResultListSetter(List<SurveyResult> list) {
        this.surveyResultList = list;
    }

    private void surveyResultListAdder() {
        surveyResultAdapter.addList(surveyResultList.subList(0, mPageCnt));
    }

    private void surveyResultRecyclerViewAdapterSetter() {
        homeRecyclerView.setAdapter(surveyResultAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void generateInitialColumnData() {
        int numValues = 4;
        columnChartData = new ColumnChartData();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>(numValues);
        List<SubcolumnValue> values;
        Column column;
        SimpleColumnChartValueFormatter formatter = new SimpleColumnChartValueFormatter(1);
        for (int i = 0; i <= numValues+1; ++i) {
            values = new ArrayList<SubcolumnValue>(1);
            values.add(new SubcolumnValue(0, party_colors[i]));
            column = new Column(values).setFormatter(formatter);
            if (1 <= i && i <= numValues) {
                axisValues.add(new AxisValue(i).setLabel(candidates[i-1]));
                column.setHasLabels(true);
            } else {
                column.setHasLabels(false);
            }
            columns.add(column);
        }

        columnChartData = new ColumnChartData(columns);
        columnChartData.setValueLabelBackgroundAuto(false);
        columnChartData.setValueLabelBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
        columnChartData.setValueLabelsTextColor(ContextCompat.getColor(getContext(), R.color.home_fragment_text));
        columnChartData.setAxisXBottom(new Axis(axisValues).setHasLines(false)
                .setTextColor(ContextCompat.getColor(getContext(), R.color.home_fragment_text)));
        columnChartData.setFillRatio(0.5f);

        columnChartView.setColumnChartData(columnChartData);
        // For build-up animation you have to disable viewport recalculation.
        columnChartView.setViewportCalculationEnabled(false);
        // And set initial max viewport and current viewport- remember to set viewports after data.
        // top graph row, columns
        Viewport v = new Viewport(0, 55, 5, 0);
        columnChartView.setMaximumViewport(v);
        columnChartView.setCurrentViewport(v);
        columnChartView.setZoomEnabled(false);
        columnChartView.setZoomType(ZoomType.VERTICAL);
    }

    private void generateColumnData(String[] data) {
        // Cancel last animation if not finished.
        columnChartView.cancelDataAnimation();

        // Modify data targets
        List<Column> columnList = columnChartData.getColumns();
        for(int i = 0; i < columnList.size(); i++) {
            List<SubcolumnValue> values = columnList.get(i).getValues();
            SubcolumnValue value = values.get(0);
            value.setTarget(Float.parseFloat(data[i]));
        }

        // Start new data animation with 300ms duration;
        columnChartView.startDataAnimation(300);
    }

}