package me.narlove.enhancedlearningapp.datatypes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import me.narlove.enhancedlearningapp.utilities.InterestConversionHandler;

@Entity(tableName = "task")
@TypeConverters(InterestConversionHandler.class)
public class Task {
    @PrimaryKey(autoGenerate = true)
    private long taskId;
    // this a FK lowk
    private long owningUserId;
    private Interest interest;
    private String genPrompt;
    private String genRes;
    private String taskDesc;

    public Task(long owningUserId, Interest interest, String genPrompt, String genRes, String taskDesc) {
        this.owningUserId = owningUserId;
        this.interest = interest;
        this.genPrompt = genPrompt;
        this.genRes = genRes;
        this.taskDesc = taskDesc;
    }

    // boilerplate on overriding the equals method
    // https://www.sitepoint.com/implement-javas-equals-method-correctly/
    // to be used in the customviewadapter diffcallback
    @Override
    @Ignore
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Task other = (Task) obj;

        return this.getTaskId() == other.getTaskId();
    }

    // code borrowed from official android developer website
    // https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
    @Ignore
    public static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Task>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Task oldTask, @NonNull Task newTask) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldTask.getTaskId() == newTask.getTaskId();
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull Task oldTask, @NonNull Task newTask) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldTask.equals(newTask);
                }
            };


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getOwningUserId() {
        return owningUserId;
    }

    public void setOwningUserId(long owningUserId) {
        this.owningUserId = owningUserId;
    }

    public Interest getInterest() {
        return interest;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }

    public String getGenPrompt() {
        return genPrompt;
    }

    public void setGenPrompt(String genPrompt) {
        this.genPrompt = genPrompt;
    }

    public String getGenRes() {
        return genRes;
    }

    public void setGenRes(String genRes) {
        this.genRes = genRes;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }
}
