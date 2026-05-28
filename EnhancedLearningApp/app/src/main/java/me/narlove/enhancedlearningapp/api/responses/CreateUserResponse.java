package me.narlove.enhancedlearningapp.api.responses;

public class CreateUserResponse {
    private String userId;

    public CreateUserResponse(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
