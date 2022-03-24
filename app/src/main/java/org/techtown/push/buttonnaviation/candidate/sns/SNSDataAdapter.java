package org.techtown.push.buttonnaviation.candidate.sns;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.push.buttonnaviation.R;
import org.techtown.push.buttonnaviation.candidate.UrlOpenListener;
import org.techtown.push.buttonnaviation.databinding.SnsRecyclerviewItemBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SNSDataAdapter extends RecyclerView.Adapter<SNSDataAdapter.ViewHolder> {
    private ArrayList<SNSData> items = new ArrayList<SNSData>();
    private SnsRecyclerviewItemBinding binding;
    private UrlOpenListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(SnsRecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SNSData item = items.get(position);
        holder.setItem(item, position);
    }

    public void setListener(UrlOpenListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(SNSData item) {items.add(item);}

    public void addList(List<SNSData> list) {
        if(list == null) return;
        for (int i = 0; i < list.size(); i++) {
            items.add(list.get(i));
        }
    }

    public SNSData getItem(int position) {return items.get(position);}

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private String url;
        private TextView mediaType, contentText, timeText;
        private ImageView mediaImage;

        public ViewHolder(SnsRecyclerviewItemBinding b, final UrlOpenListener listener) {
            super(b.getRoot());
            mediaType = b.mediaTypeText;
            contentText = b.contentText;
            mediaImage = b.mediaImage;
            timeText = b.timeText;
            //item click event
            b.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) listener.onItemClick(view, url);
                }
            });
        }

        public void setItem(SNSData item, int position) {
            mediaType.setText(item.getMedia());
            timeText.setText(calcTime(item.getUpload()));
            contentText.setText(item.getContent());
            mediaImage.setImageResource(getMediaImage(item.getMedia()));
            url = item.getUrl();
        }

        public String calcTime(String time) {
            return time;
        }

        public int getMediaImage(String media) {
            switch(media) {
                case "Twitter":
                    return R.drawable.twitter;
                case "Facebook":
                    return R.drawable.facebook;
                case "Instagram":
                    return R.drawable.instagram;
                default:
                    return 0;
            }
        }

    }
}
