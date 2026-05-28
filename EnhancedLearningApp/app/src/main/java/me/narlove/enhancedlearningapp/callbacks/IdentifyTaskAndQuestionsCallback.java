package me.narlove.enhancedlearningapp.callbacks;

import me.narlove.enhancedlearningapp.persistence.datatypes.TaskWithQuestions;

public interface IdentifyTaskAndQuestionsCallback {
    void onSuccess(TaskWithQuestions tasks);
    void onFailure();
}
