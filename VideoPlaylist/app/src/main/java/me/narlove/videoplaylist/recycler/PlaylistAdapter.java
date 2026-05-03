package me.narlove.videoplaylist.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import me.narlove.videoplaylist.R;
import me.narlove.videoplaylist.persistence.PlaylistEntry;

// swapped to listadapter here instead of RecyclerView.Adapter from last project
// as listadapter basically automatically implements some of the methods i was manually implementing
// previously; also, makes it easier to handle database accessing methods on alternative threads.
public class PlaylistAdapter extends ListAdapter<PlaylistEntry, PlaylistAdapter.ViewHolder> {

    private Context context;
    private OnRecyclerViewItemClick listener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView displayUrl;

        public ViewHolder(View v) {
            super(v);

            displayUrl = (TextView) v.findViewById(R.id.displayUrl);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        PlaylistEntry item = PlaylistAdapter.this.getItem(position);

                        listener.onUrlClicked(item.getEntryUrl());
                    }
                }
            });
        }

        public TextView getDisplayUrl() {
            return displayUrl;
        }
    }

    // as inspired by android developer website
    // https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
    public PlaylistAdapter(Context context, OnRecyclerViewItemClick listener) {
        super(PlaylistEntry.DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_playlist_item,
                viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getDisplayUrl().setText(getItem(position).getEntryUrl());
    }

    // need to override as i am using stable ids
    // https://stackoverflow.com/questions/44081579/sethasstableidstrue-in-recyclerview
    @Override
    public long getItemId(int position) {
        return getItem(position).getEntryId();
    }
}
