package me.narlove.chatbot.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.narlove.chatbot.utilities.Author;

@Entity(tableName = "messages")
@TypeConverters(CustomTypeConverter.class)
public class MessageObject {
    @PrimaryKey(autoGenerate = true)
    private long messageId;

    private String content;
    private Author author;
    private Date timestamp;

    public MessageObject(String content, Author author)
    {
        this.content = content;
        this.author = author;
        this.timestamp = new Date(System.currentTimeMillis());
    }

    // boilerplate on overriding the equals method
    // https://www.sitepoint.com/implement-javas-equals-method-correctly/
    // to be used in the customviewadapter diffcallback
    @Override
    @Ignore
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MessageObject other = (MessageObject) obj;

        return this.getMessageId() == other.getMessageId();
    }

    // code borrowed from official android developer website
    // https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
    @Ignore
    public static final DiffUtil.ItemCallback<MessageObject> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MessageObject>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull MessageObject oldMessage, @NonNull MessageObject newMessage) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldMessage.getMessageId() == newMessage.getMessageId();
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull MessageObject oldMessage, @NonNull MessageObject newMessage) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldMessage.equals(newMessage);
                }
            };

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getTimestampReadable()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(this.timestamp);
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
