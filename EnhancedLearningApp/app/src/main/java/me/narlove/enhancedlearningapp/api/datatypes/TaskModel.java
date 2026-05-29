package me.narlove.enhancedlearningapp.api.datatypes;

import java.util.List;

import me.narlove.enhancedlearningapp.Interest;
import me.narlove.enhancedlearningapp.persistence.datatypes.Question;

public class TaskModel {
    private long taskId;
    private Interest interest;
    private String genPrompt;
    private String genRes;
    private String taskDesc;
    private List<QuestionModel> questions;

    public TaskModel(Interest interest, String genPrompt, String genRes, String taskDesc,
                     List<QuestionModel> questions) {
        this.interest = interest;
        this.genPrompt = genPrompt;
        this.genRes = genRes;
        this.taskDesc = taskDesc;
        this.questions = questions;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
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

    public List<QuestionModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionModel> questions) {
        this.questions = questions;
    }
}
