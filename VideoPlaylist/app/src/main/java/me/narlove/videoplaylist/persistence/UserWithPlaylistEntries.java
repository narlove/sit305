package me.narlove.videoplaylist.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.List;

public class UserWithPlaylistEntries {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "owningUserId"
    )
    public List<PlaylistEntry> playlistEntries;
}
