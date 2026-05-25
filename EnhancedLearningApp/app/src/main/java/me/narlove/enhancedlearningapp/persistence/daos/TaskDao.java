package me.narlove.enhancedlearningapp.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import me.narlove.enhancedlearningapp.datatypes.Task;
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

    @Query("SELECT COUNT(*) FROM task WHERE owningUserId LIKE :uid")
    LiveData<Integer> getNumberOfTasksByUserId(long uid);
}
