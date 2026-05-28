package me.narlove.enhancedlearningapp.api.responses;

public class CreateTaskResponse {
    private String taskId;

    public CreateTaskResponse(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
