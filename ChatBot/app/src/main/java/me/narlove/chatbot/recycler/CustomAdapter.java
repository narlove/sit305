package me.narlove.chatbot.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.narlove.chatbot.R;
import me.narlove.chatbot.utilities.Author;
import me.narlove.chatbot.persistence.MessageObject;

public class CustomAdapter extends ListAdapter<MessageObject, CustomAdapter.ViewHolder>
{
    private static final int TYPE_USER = 0;
    private static final int TYPE_BOT = 1;

    private Context context;

    public CustomAdapter(Context context)
    {
        super(MessageObject.DIFF_CALLBACK);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView messageContent;
        public final TextView sentTimestamp;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            messageContent = itemView.findViewById(R.id.messageContent);
            sentTimestamp = itemView.findViewById(R.id.sentTimestamp);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (getItem(position).getAuthor() == Author.USER)
        {
            return TYPE_USER;
        }
        else
        {
            return TYPE_BOT;
        }
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        int layoutId;
        if (viewType == TYPE_USER)
        {
            layoutId = R.layout.item_user_message_sent;
        }
        else
        {
            layoutId = R.layout.item_ai_message_sent;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        MessageObject message = getItem(position);
        holder.messageContent.setText(message.getContent());
        holder.sentTimestamp.setText(message.getTimestampReadable());
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getMessageId();
    }
}
