package me.narlove.enhancedlearningapp.persistence.datatypes;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithTasks {
    @Embedded
    public User user;

    @Relation(
            parentColumn = "userId",
            entityColumn = "owningUserId"
    )
    public List<Task> tasks;
}
