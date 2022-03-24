package org.techtown.push.buttonnaviation.ui.home;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.push.buttonnaviation.OnSurveyResultItemClickListener;
import org.techtown.push.buttonnaviation.R;
import org.techtown.push.buttonnaviation.databinding.LoadmoreLayoutBinding;
import org.techtown.push.buttonnaviation.databinding.SurveyResultBinding;

import java.util.ArrayList;
import java.util.List;

public class SurveyResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnSurveyResultItemClickListener{
    private ArrayList<SurveyResult> items = new ArrayList<SurveyResult>();
    private SurveyResultBinding binding;
    private LoadmoreLayoutBinding loadBinding;
    private OnSurveyResultItemClickListener listener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return new SurveyViewHolder(SurveyResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
        } else {
            return new LoadingViewHolder(LoadmoreLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SurveyResult item = items.get(position);
        if(getItemViewType(position) == VIEW_TYPE_ITEM) {
            SurveyViewHolder viewHolder = (SurveyViewHolder) holder;
            viewHolder.setItem(item);
        } else {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            viewHolder.mProgressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void removeItem(int pos) {
        items.remove(pos);
    }

    public void addItem(SurveyResult item) {
        items.add(item);
    }

    public void addList(List<SurveyResult> list) {
        if(list == null) return;
        for(int i = 0; i < list.size(); i++) {
            items.add(list.get(i));
        }
    }

    public SurveyResult getItem(int position) {return items.get(position);}

    public void setOnSurveyResultItemClickListener(OnSurveyResultItemClickListener listener) {
        this.listener = listener;
    }

    public void onSurveyResultItemClick(SurveyViewHolder holder, View view, int position){
        if (listener != null) {
            listener.onSurveyResultItemClick(holder, view, position);
        }
    }

    @Override
    public void onSurveyResultSpecifyClick(SurveyViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onSurveyResultSpecifyClick(holder, view, position);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar mProgressBar;

        public LoadingViewHolder(LoadmoreLayoutBinding b) {
            super(b.getRoot());
            mProgressBar = b.loadmore;
        }
    }

    public static class SurveyViewHolder extends RecyclerView.ViewHolder {
        TextView monthView, dayView, requestOrgView, executeOrgView;
        Button urlButton;

        public SurveyViewHolder(SurveyResultBinding b, final OnSurveyResultItemClickListener listener) {
            super(b.getRoot());
            monthView = b.monthView;
            dayView = b.dayView;
            requestOrgView = b.requestOrgView;
            executeOrgView = b.executeOrgView;
            urlButton = b.urlButton;

            //button click event
            urlButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null) {
                        listener.onSurveyResultSpecifyClick(SurveyViewHolder.this, view, position);
                    }
                }
            });

            //item click event
            b.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null) {
                        listener.onSurveyResultItemClick(SurveyViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(SurveyResult item) {
            monthView.setText(item.getMonthStr());
            dayView.setText(String.format("%s", item.getUploadDay()));
            requestOrgView.setText(item.getRequester());
            executeOrgView.setText(item.getExecuter());
        }
    }
}
