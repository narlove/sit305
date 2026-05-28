package me.narlove.enhancedlearningapp.callbacks;

import me.narlove.enhancedlearningapp.persistence.datatypes.Question;

public interface IdentifyQuestionCallback {
    void onSuccess(Question question);
    void onFailure();
}
