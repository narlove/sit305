package me.narlove.enhancedlearningapp.datatypes.callbacks;

import me.narlove.enhancedlearningapp.datatypes.User;

public interface IdentifyUserCallback {
    void onSuccess(User user);
    void onFailure();
}
