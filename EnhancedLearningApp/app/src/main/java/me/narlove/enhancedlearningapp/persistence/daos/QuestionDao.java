package me.narlove.enhancedlearningapp.persistence.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import me.narlove.enhancedlearningapp.datatypes.Question;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM question WHERE questionId LIKE :qid")
    Question getQuestionById(long qid);
}
