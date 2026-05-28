package me.narlove.enhancedlearningapp.api.datatypes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

import me.narlove.enhancedlearningapp.Interest;
import me.narlove.enhancedlearningapp.persistence.datatypes.Question;
import me.narlove.enhancedlearningapp.persistence.datatypes.Task;

public class MongoTask {
    private String taskId;
    private Interest interest;
    private String genPrompt;
    private String genRes;
    private String taskDesc;
    private List<Question> questions;

    public MongoTask(Interest interest, String genPrompt, String genRes, String taskDesc,
                     List<Question> questions) {
        this.interest = interest;
        this.genPrompt = genPrompt;
        this.genRes = genRes;
        this.taskDesc = taskDesc;
        this.questions = questions;
    }

    // to be used in the customviewadapter diffcallback
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MongoTask other = (MongoTask) obj;

        return this.getTaskId().equals(other.getTaskId());
    }

    public static final DiffUtil.ItemCallback<MongoTask> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MongoTask>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull MongoTask oldTask, @NonNull MongoTask newTask) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldTask.getTaskId().equals(newTask.getTaskId());
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull MongoTask oldTask, @NonNull MongoTask newTask) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldTask.equals(newTask);
                }
            };


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
