package me.narlove.enhancedlearningapp.datatypes.callbacks;

import me.narlove.enhancedlearningapp.datatypes.Question;

public interface IdentifyQuestionCallback {
    void onSuccess(Question question);
    void onFailure();
}
