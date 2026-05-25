package me.narlove.enhancedlearningapp.persistence.datatypes;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import me.narlove.enhancedlearningapp.datatypes.Question;
import me.narlove.enhancedlearningapp.datatypes.Task;

public class TaskWithQuestions {
    @Embedded
    public Task task;

    @Relation(
            parentColumn = "taskId",
            entityColumn = "owningTaskId"
    )
    public List<Question> questions;
}
