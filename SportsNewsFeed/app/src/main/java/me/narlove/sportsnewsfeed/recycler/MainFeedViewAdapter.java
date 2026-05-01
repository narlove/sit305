package me.narlove.sportsnewsfeed.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import me.narlove.sportsnewsfeed.R;
import me.narlove.sportsnewsfeed.persistence.FeedItem;

// swapped to listadapter here instead of RecyclerView.Adapter from last project
// as listadapter basically automatically implements some of the methods i was manually implementing
// previously; also, makes it easier to handle database accessing methods on alternative threads.
public class MainFeedViewAdapter extends ListAdapter<FeedItem, MainFeedViewAdapter.ViewHolder> {

    private Context context;
    private OnRecyclerViewItemClick listener;

    // the class used to identify how data/information is bound to each item,
    // and what parts of that data is accessible.
    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleView;
        private final TextView descView;
        private final ImageView thumbnailView;

        public ViewHolder(View v) {
            super(v);

            titleView = (TextView) v.findViewById(R.id.itemTitle);
            descView = (TextView) v.findViewById(R.id.itemDescription);
            thumbnailView = (ImageView) v.findViewById(R.id.itemThumbnail);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        FeedItem item = MainFeedViewAdapter.this.getItem(position);

                        listener.onItemClick(item);
                    }
                }
            });
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getDescView() {
            return descView;
        }

        public ImageView getThumbnailView() {
            return thumbnailView;
        }
    }

    // as inspired by android developer website
    // https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
    public MainFeedViewAdapter(Context context, OnRecyclerViewItemClick listener) {
        super(FeedItem.DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainFeedViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_item_layout,
                viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFeedViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getTitleView().setText(getItem(position).getTitle());
        viewHolder.getDescView().setText(getItem(position).getDescription());

        String imageName = getItem(position).getThumbnailImageName();

        if (imageName == null)
        {
            viewHolder.getThumbnailView().setImageResource(R.drawable.placeholder_image);
        }
        else
        {
            // load using glide
            Glide.with(context).load(imageName)
                    .into(viewHolder.getThumbnailView());
        }
    }

    // need to override as i am using stable ids
    // https://stackoverflow.com/questions/44081579/sethasstableidstrue-in-recyclerview
    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
