package me.narlove.enhancedlearningapp.api.responses;

public class TaskCountResponse {
    private int count;

    public TaskCountResponse(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
