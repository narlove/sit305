package me.narlove.enhancedlearningapp.api.responses;

public class UserIdResponse {
    private long userId;

    public UserIdResponse(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
