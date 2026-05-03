package me.narlove.videoplaylist.utilities;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.narlove.videoplaylist.persistence.User;
import me.narlove.videoplaylist.persistence.UserWithPlaylistEntries;

// swap to AndroidViewModel if we require access to ApplicationContext
// swapping will require updated constructor
public class UserTrackViewModel extends ViewModel {

    private final MutableLiveData<UserWithPlaylistEntries> currentUser = new MutableLiveData<>();

    public UserTrackViewModel()
    {
        currentUser.setValue(null);
    }

    public void setCurrentUser(@Nullable UserWithPlaylistEntries user)
    {
        currentUser.setValue(user);
    }

    // will always return a value but using .getValue on the returned item may produce null
    public LiveData<UserWithPlaylistEntries> getCurrentUser()
    {
        return currentUser;
    }
}
