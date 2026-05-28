package me.narlove.enhancedlearningapp.callbacks;

import me.narlove.enhancedlearningapp.persistence.datatypes.User;

public interface IdentifyUserCallback {
    void onSuccess(User user);
    void onFailure();
}
