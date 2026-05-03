package me.narlove.videoplaylist.utilities;

import me.narlove.videoplaylist.persistence.UserWithPlaylistEntries;

public interface AuthCallback {
    void onSuccess(UserWithPlaylistEntries user);
    void onFailure();
}
