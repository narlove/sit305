package me.narlove.enhancedlearningapp.persistence.datatypes;

import androidx.lifecycle.LiveData;

import java.util.List;

import me.narlove.enhancedlearningapp.Interest;
import me.narlove.enhancedlearningapp.callbacks.IdentifyQuestionCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyTaskAndQuestionsCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserIdCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUsernameExistsCallback;

public interface IRepository {
    void getUserByUsername(String username, IdentifyUserCallback callback);
    void insert(User user, IdentifyUserIdCallback callback);
    void overrideInterests(long userId, List<Interest> newInterests);
    void doesUsernameExist(String username, IdentifyUsernameExistsCallback callback);
    LiveData<List<Task>> getTasksByUserId(long uid);
    void getTaskAndQuestionsByTaskId(long taskId,
                                     IdentifyTaskAndQuestionsCallback callback);
    void deleteTaskById(long taskId);
    void insertTaskWithQuestions(Task task, List<Question> questions);
    LiveData<Integer> getNumberOfTasksByUserId(long uid);
    void getQuestionByQuestionId(long questionId,
                                 IdentifyQuestionCallback callback);

}
