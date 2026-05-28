package me.narlove.enhancedlearningapp.api;

import java.util.List;

import me.narlove.enhancedlearningapp.api.datatypes.MongoQuestion;
import me.narlove.enhancedlearningapp.api.datatypes.MongoTask;
import me.narlove.enhancedlearningapp.api.datatypes.MongoUser;
import me.narlove.enhancedlearningapp.api.requests.UpdateInterestsRequest;
import me.narlove.enhancedlearningapp.api.responses.CreateTaskResponse;
import me.narlove.enhancedlearningapp.api.responses.CreateUserResponse;
import me.narlove.enhancedlearningapp.api.responses.TaskCountResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("users/{username}")
    Call<MongoUser> getUserByUsername(@Path("username") String username);

    // have to have these wrapper classes for gson
    // thank you intellij for the "generate getters/settings/constructor" methods :prayingemoji:
    @POST("users/")
    Call<CreateUserResponse> createUser(@Body MongoUser user);

    @PUT("users/{id}")
    Call<Void> updateInterests(@Path("id") String id, @Body UpdateInterestsRequest body);

    @GET("users/{id}/tasks")
    Call<List<MongoTask>> getUserTasks(@Path("id") String id);

    @GET("users/{id}/tasks/count")
    Call<TaskCountResponse> getUserTaskCount(@Path("id") String id);

    @POST("users/{id}/tasks")
    Call<CreateTaskResponse> createTask(@Path("id") String id, @Body MongoTask task);

    @GET("tasks/{id}")
    Call<MongoTask> getTaskById(@Path("id") String id);

    @DELETE("tasks/{id}")
    Call<Void> deleteTask(@Path("id") String id);

    @GET("questions/{id}")
    Call<MongoQuestion> getQuestionById(@Path("id") String id);
}
