package me.narlove.enhancedlearningapp.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import me.narlove.enhancedlearningapp.R;
import me.narlove.enhancedlearningapp.persistence.datatypes.Task;

public class CustomAdapter extends ListAdapter<Task, CustomAdapter.ViewHolder>
{
    private Context context;
    private OnRecyclerItemClick listener;

    public CustomAdapter(Context context, OnRecyclerItemClick listener)
    {
        super(Task.DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView taskTitle;
        public final TextView taskDesc;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDesc = itemView.findViewById(R.id.taskDescription);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task item = getItem(position);

                    listener.clicked(item.getTaskId());
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Task task = getItem(position);
        holder.taskTitle.setText(String.format("Generated Task %d", task.getTaskId()));
        holder.taskDesc.setText(task.getTaskDesc());
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getTaskId();
    }
}
