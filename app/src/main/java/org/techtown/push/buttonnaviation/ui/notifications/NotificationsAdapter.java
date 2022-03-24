package org.techtown.push.buttonnaviation.ui.notifications;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.push.buttonnaviation.databinding.SurveyArrayRecyclerviewItemBinding;
import org.techtown.push.buttonnaviation.ui.home.SurveyResult;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ArrayList<SurveyResult>> items = new ArrayList<>();
    private SurveyArrayRecyclerviewItemBinding binding;
    private NotificationsFragment.OnSurveyArrayClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SurveyArrayViewHolder(SurveyArrayRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArrayList<SurveyResult> item = items.get(position);
        SurveyArrayViewHolder viewHolder = (SurveyArrayViewHolder) holder;
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ArrayList<SurveyResult> item) {
        items.add(item);
    }

    public void clearItems() {
        items = new ArrayList<>();
    }

    public void addList(ArrayList<ArrayList<SurveyResult>> list) {
        for(int i = 0; i < list.size(); i++) {
            items.add(list.get(i));
        }
    }

    public ArrayList<SurveyResult> getItem(int position) {
        return items.get(position);
    }
    public void setOnSurveyArrayClickListener(NotificationsFragment.OnSurveyArrayClickListener listener) {
        this.listener = listener;
    }

    public void onSurveyArrayItemClick(NotificationsAdapter.SurveyArrayViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onSurveyArrayItemClick(holder, view, position);
        }
    }

    public static class SurveyArrayViewHolder extends RecyclerView.ViewHolder {
        TextView mainText, sizeText;

        public SurveyArrayViewHolder(SurveyArrayRecyclerviewItemBinding b, final NotificationsFragment.OnSurveyArrayClickListener listener) {
            super(b.getRoot());
            mainText = b.surveyArrayText;
            sizeText = b.surveyArraySize;

            b.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null) {
                        listener.onSurveyArrayItemClick(SurveyArrayViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(List<SurveyResult> item) {
            this.mainText.setText(item.get(0).getRequester());
            this.sizeText.setText(item.size() + "개");
        }
    }
}
