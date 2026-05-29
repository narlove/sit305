package me.narlove.enhancedlearningapp.api.responses;

public class TaskIdResponse {
    private long taskId;

    public TaskIdResponse(long taskId) {
        this.taskId = taskId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
}
