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
}
