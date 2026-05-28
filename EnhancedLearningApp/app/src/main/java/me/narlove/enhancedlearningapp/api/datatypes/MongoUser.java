package me.narlove.enhancedlearningapp.api.datatypes;


import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import me.narlove.enhancedlearningapp.Interest;

public class MongoUser {
    @SerializedName("_id")
    private String userId;
    // no duplicates -> used for lookup in authentication flow
    private String username;
    private String name;
    private String password; // hashing beyond the scope of this proof of concept project.
    private String email;
    private String phone;
    private List<Interest> interests;
    // a few changes need to be made like this one
    // as tasks are now embedded in user, not referenced by them
    private List<MongoTask> tasks;

    // interests cannot be null, can be empty
    public MongoUser(String username, String name, String password, String email, String phone,
                     @NotNull List<Interest> interests, @NotNull List<MongoTask> tasks) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.interests = interests;
        this.tasks = tasks;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public List<MongoTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<MongoTask> tasks) {
        this.tasks = tasks;
    }
}