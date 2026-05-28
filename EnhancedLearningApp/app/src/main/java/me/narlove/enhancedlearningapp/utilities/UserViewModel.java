package me.narlove.enhancedlearningapp.utilities;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import me.narlove.enhancedlearningapp.persistence.datatypes.User;

public class UserViewModel extends ViewModel {
    MutableLiveData<User> currentUser = new MutableLiveData<>();

    public UserViewModel() {
        this.currentUser.setValue(null);
    }

    public void setCurrentUser(@Nullable User newUser) {
        this.currentUser.setValue(newUser);
    }

    public LiveData<User> getCurrentUser()
    {
        return currentUser;
    }
}
