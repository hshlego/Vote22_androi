package org.techtown.push.buttonnaviation.candidate.sns;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.techtown.push.buttonnaviation.candidate.UrlOpenListener;
import org.techtown.push.buttonnaviation.databinding.FragmentSnsBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SNSFragment extends Fragment {
    private FragmentSnsBinding binding;
    private RecyclerView snsRecyclerView;
    private SNSDataAdapter snsDataAdapter;
    private List<SNSData> snsDataList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSnsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        String candidate_num = getArguments().getString("candidate_num", "1");

        //set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        snsRecyclerView = binding.snsRecyclerview;
        snsRecyclerView.setLayoutManager(layoutManager);

        //set videoDataAdapter
        snsDataAdapter = new SNSDataAdapter();
        snsDataAdapter.setListener(new UrlOpenListener() {
            @Override
            public void onItemClick(View view, String url) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(url));
                startActivity(webIntent);
            }
        });

        //load data according to candidate Number, or request data according to number;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSNSData(candidate_num);
            }
        }, 500);

        return root;
    }

    private void getSNSData(String candidate_num) {
        SNSRetrofit snsRetrofit = new SNSRetrofit();
        snsRetrofit.select.getAllByCandidate(candidate_num).enqueue(new Callback<List<SNSData>>() {
            @Override
            public void onResponse(Call<List<SNSData>> call, Response<List<SNSData>> response) {
                if(response.isSuccessful()) {
                    snsDataListSetter(response.body());
                    //set adapter to recyclerview
                    snsDataListAdder();
                    snsRecyclerViewAdapterSetter();
                    showRecyclerView();
                } else {
                }
            }
            @Override
            public void onFailure(Call<List<SNSData>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private void snsDataListSetter(List<SNSData> list) {
        snsDataList = list;
    }
    private void snsDataListAdder() {
        snsDataAdapter.addList(snsDataList);
    }
    private void snsRecyclerViewAdapterSetter() {
        snsRecyclerView.setAdapter(snsDataAdapter);
    }
    private void showRecyclerView() {
        binding.snsProgressBar.setVisibility(View.INVISIBLE);
        snsRecyclerView.setVisibility(View.VISIBLE);
    }

}
