package me.narlove.enhancedlearningapp.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import me.narlove.enhancedlearningapp.persistence.datatypes.Question;
import me.narlove.enhancedlearningapp.persistence.datatypes.Task;
import me.narlove.enhancedlearningapp.persistence.datatypes.TaskWithQuestions;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task WHERE owningUserId LIKE :uid")
    LiveData<List<Task>> getTasksByUserId(long uid);

    @Transaction
    @Query("SELECT * FROM task WHERE taskId LIKE :taskId")
    TaskWithQuestions getTaskAndQuestionsByTaskId(long taskId);

    @Query("DELETE FROM task WHERE taskId LIKE :taskId")
    void deleteTaskById(long taskId);

    @Insert
    void insertTask(Task task);

    // necessary to return the tid to add questions to afterwards
    @Insert
    long insertTaskReturnId(Task task);

    // here will be the function that updates questions by tid
    @Transaction
    default void insertTaskWithQuestions(Task task, List<Question> questions)
    {
        long taskId = this.insertTaskReturnId(task);

        for (Question q : questions)
        {
            q.setOwningTaskId(taskId);
            this.insertQuestion(q);
        }
    }

    @Query("SELECT COUNT(*) FROM task WHERE owningUserId LIKE :uid")
    LiveData<Integer> getNumberOfTasksByUserId(long uid);

    // only one method relating to Question so it's not part of its own dao
    // also i need a reference to this one here
    @Insert
    void insertQuestion(Question question);
}
