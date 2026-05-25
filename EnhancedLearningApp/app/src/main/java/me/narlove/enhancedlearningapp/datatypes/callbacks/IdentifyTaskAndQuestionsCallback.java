package me.narlove.enhancedlearningapp.datatypes.callbacks;

import me.narlove.enhancedlearningapp.persistence.datatypes.TaskWithQuestions;

public interface IdentifyTaskAndQuestionsCallback {
    void onSuccess(TaskWithQuestions tasks);
    void onFailure();
}
