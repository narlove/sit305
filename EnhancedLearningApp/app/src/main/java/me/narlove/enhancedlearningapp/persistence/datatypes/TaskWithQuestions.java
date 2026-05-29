package me.narlove.enhancedlearningapp.persistence.datatypes;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TaskWithQuestions {
    @Embedded
    public Task task;

    @Relation(
            parentColumn = "taskId",
            entityColumn = "owningTaskId"
    )
    public List<Question> questions;

    public TaskWithQuestions()
    {

    }

    public TaskWithQuestions(Task task, List<Question> questions)
    {
        this.task = task;
        this.questions = questions;
    }
}
