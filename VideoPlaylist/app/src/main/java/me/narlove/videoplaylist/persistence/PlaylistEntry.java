package me.narlove.videoplaylist.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlistEntries",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "userId",
                childColumns = "owningUserId",
                onDelete = ForeignKey.CASCADE
        ))
public class PlaylistEntry {
    @PrimaryKey(autoGenerate = true)
    private long entryId;
    private long owningUserId;
    private String entryUrl;

    public PlaylistEntry(String entryUrl, long owningUserId) {
        this.owningUserId = owningUserId;
        this.entryUrl = entryUrl;
    }

    // boilerplate on overriding the equals method
    // https://www.sitepoint.com/implement-javas-equals-method-correctly/
    // to be used in the customviewadapter diffcallback
    @Override
    @Ignore
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PlaylistEntry other = (PlaylistEntry) obj;

        return this.entryId == other.entryId;
    }

    // code borrowed from official android developer website
    // https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
    @Ignore
    public static final DiffUtil.ItemCallback<PlaylistEntry> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<PlaylistEntry>() {
            @Override
            public boolean areItemsTheSame(
                    @NonNull PlaylistEntry oldEntry, @NonNull PlaylistEntry newEntry) {
                // User properties may have changed if reloaded from the DB, but ID is fixed
                return oldEntry.getEntryId() == newEntry.getEntryId();
            }
            @Override
            public boolean areContentsTheSame(
                    @NonNull PlaylistEntry oldEntry, @NonNull PlaylistEntry newEntry) {
                // NOTE: if you use equals, your object must properly override Object#equals()
                // Incorrectly returning false here will result in too many animations.
                return oldEntry.equals(newEntry);
            }
        };

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public long getOwningUserId() {
        return owningUserId;
    }

    public void setOwningUserId(long owningUserId) {
        this.owningUserId = owningUserId;
    }

    public String getEntryUrl() {
        return entryUrl;
    }

    public void setEntryUrl(String entryUrl) {
        this.entryUrl = entryUrl;
    }
}
