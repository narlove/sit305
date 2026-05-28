package me.narlove.enhancedlearningapp.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import me.narlove.enhancedlearningapp.persistence.datatypes.Question;
import me.narlove.enhancedlearningapp.persistence.datatypes.Task;
import me.narlove.enhancedlearningapp.persistence.datatypes.User;
import me.narlove.enhancedlearningapp.persistence.daos.QuestionDao;
import me.narlove.enhancedlearningapp.persistence.daos.TaskDao;
import me.narlove.enhancedlearningapp.persistence.daos.UserDao;

@Database(entities = {User.class, Task.class, Question.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TaskDao taskDao();
    public abstract QuestionDao questionDao();
}
