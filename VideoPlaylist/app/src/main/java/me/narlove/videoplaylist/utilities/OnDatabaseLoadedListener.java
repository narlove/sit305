package me.narlove.videoplaylist.utilities;

import me.narlove.videoplaylist.persistence.UserWithPlaylistEntries;

public interface OnDatabaseLoadedListener {
    void onDataLoaded(UserWithPlaylistEntries user);
}
