package org.techtown.push.buttonnaviation.candidate.video;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import org.techtown.push.buttonnaviation.R;
import org.techtown.push.buttonnaviation.candidate.UrlOpenListener;
import org.techtown.push.buttonnaviation.databinding.LoadmoreLayoutBinding;
import org.techtown.push.buttonnaviation.databinding.VideoRecyclerviewItemBinding;
import java.util.ArrayList;
import java.util.List;

public class VideoDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<VideoData> items = new ArrayList<VideoData>();
    private VideoRecyclerviewItemBinding binding;
    private UrlOpenListener listener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            return new VideoViewHolder(VideoRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
        } else {
            return new LoadingViewHolder(LoadmoreLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoData item = items.get(position);
        if(getItemViewType(position) == VIEW_TYPE_ITEM) {
            VideoViewHolder viewHolder = (VideoViewHolder) holder;
            Glide.with(viewHolder.itemView)
                    .load(item.getThumbnail())
                    .placeholder(R.color.light_gray)
                    .fallback(R.color.black)
                    .error(R.color.black)
                    .into(viewHolder.thumbNail);

            viewHolder.setItem(item);

        } else {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            viewHolder.mProgressBar.setIndeterminate(true);
        }
    }

    public void setListener(UrlOpenListener listener) {
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(VideoData item) {
        items.add(item);
        if(item == null) return;
    }

    public void addList(List<VideoData> list) {
        for(int i = 0; i < list.size(); i++) {
            addItem(list.get(i));
        }
    }

    public void removeItem(int position) {
        items.remove(position);
    }

    public int getItemViewType(int position) {
        return this.items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public VideoData getItem(int position) {return items.get(position);}

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar mProgressBar;

        public LoadingViewHolder(LoadmoreLayoutBinding b) {
            super(b.getRoot());
            mProgressBar = b.loadmore;
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, runtimeText, dayText;
        private String url;
        private ImageView thumbNail;

        public VideoViewHolder(VideoRecyclerviewItemBinding b, final UrlOpenListener listener) {
            super(b.getRoot());
            titleText = b.titleText;
            runtimeText = b.runtimeText;
            dayText = b.dayBeforeText;
            thumbNail = b.thumbnail;

            //item click event
            b.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) listener.onItemClick(view, url);
                }
            });
        }

        public void setItem(VideoData item, Bitmap bitmap) {
            titleText.setText(item.getTitle());
            runtimeText.setText(item.getRuntime());
            dayText.setText(item.getUpload().substring(2, 10));
            thumbNail.setImageBitmap(bitmap);
            url = item.getUrl();
        }

        public void setItem(VideoData item) {
            titleText.setText(item.getTitle());
            runtimeText.setText(item.getRuntime());
            dayText.setText(item.getUpload().substring(2, 10));
            url = item.getUrl();
        }
    }
}

