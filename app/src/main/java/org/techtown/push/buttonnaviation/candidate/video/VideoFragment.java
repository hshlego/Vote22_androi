package org.techtown.push.buttonnaviation.candidate.video;

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
import org.techtown.push.buttonnaviation.databinding.FragmentVideoBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoFragment extends Fragment {
    private FragmentVideoBinding binding;
    private VideoDataAdapter videoDataAdapter;
    private RecyclerView videoRecyclerview;
    private List<VideoData> videoDataList;
    private final int mPageCnt = 15;
    private int mLoadItemCnt = 15;
    private boolean isLoading = false;
    private boolean isLastLoading = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //get data from intent
        String candidate_num = getArguments().getString("candidate_num");

        //set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        videoRecyclerview = binding.videoRecyclerview;
        videoRecyclerview.setLayoutManager(layoutManager);
        //set videoDataAdapter
        videoDataAdapter = new VideoDataAdapter();
        videoDataAdapter.setListener(new UrlOpenListener() {
            @Override
            public void onItemClick(View view, String url) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(url));
                startActivity(webIntent);
            }
        });

        //set scroll listener
        videoRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = layoutManager.findLastVisibleItemPosition();
                if(!isLoading & !isLastLoading){
                    if(layoutManager != null && lastItem == videoDataAdapter.getItemCount() - 1){
                        isLoading = true;
                        onLoadMore(videoDataAdapter.getItemCount());
                    }
                }
            }
        });

        //load data according to candidate Number, or request data according to number;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getVideoData(candidate_num);
//            }
//        }, 500);
        getVideoData(candidate_num);

        return root;
    }

    private void onLoadMore(int startIdx) {
        videoDataAdapter.addItem(null);
        videoDataAdapter.notifyItemInserted(videoDataAdapter.getItemCount()-1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startLoadMore(startIdx, startIdx+mPageCnt);
            }
        }, 1000);
    }

    private void startLoadMore(int startIdx, int endIdx) {
        videoDataAdapter.removeItem(videoDataAdapter.getItemCount()-1);
        int scrollPosition = videoDataAdapter.getItemCount();
        videoDataAdapter.notifyItemRemoved(scrollPosition);
        if (startIdx >= videoDataList.size()) {
            isLastLoading = true;
        } else {
            endIdx = Math.min(videoDataList.size(), endIdx);
            videoDataAdapter.addList(videoDataList.subList(startIdx, endIdx));
            mLoadItemCnt = scrollPosition;
            mLoadItemCnt += mPageCnt;
            isLastLoading = false;
        }

        isLoading = false;
    }

    private void getVideoData(String candidate_num) {
        VideoDataRetrofit videoDataRetrofit = new VideoDataRetrofit();
        videoDataRetrofit.select.getAllByCandidate(candidate_num).enqueue(new Callback<List<VideoData>>() {
            @Override
            public void onResponse(Call<List<VideoData>> call, Response<List<VideoData>> response) {
                if (response.isSuccessful()) {
                    videoDataSetter(response.body());
                    videoDataAdder();
                    videoRecyclerviewSetter();
                    showRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<List<VideoData>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void videoDataSetter(List<VideoData> list) {
        videoDataList = list;
    }

    private void videoDataAdder() {
        videoDataAdapter.addList(videoDataList.subList(0, mPageCnt));
    }

    private void videoRecyclerviewSetter() {
        videoRecyclerview.setAdapter(videoDataAdapter);
    }

    private void showRecyclerView() {
        videoRecyclerview.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }
}
