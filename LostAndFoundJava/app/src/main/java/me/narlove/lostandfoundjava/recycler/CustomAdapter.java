package me.narlove.lostandfoundjava.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import me.narlove.lostandfoundjava.R;
import me.narlove.lostandfoundjava.persistence.Post;

public class CustomAdapter extends ListAdapter<Post, CustomAdapter.ViewHolder> {
    private Context context;
    private OnRecyclerViewItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;

        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = CustomAdapter.this.getItem(position);

                        listener.onItemClick(post);
                    }
                }
            });
        }

        public TextView getTitle() {
            return title;
        }
    }

    // as inspired by android developer website
    // https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
    public CustomAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        super(Post.DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item,
                viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getTitle().setText(getItem(position).getPostName());
    }

    // need to override as i am using stable ids
    // https://stackoverflow.com/questions/44081579/sethasstableidstrue-in-recyclerview
    @Override
    public long getItemId(int position) {
        return getItem(position).getPostId();
    }
}
