package me.narlove.enhancedlearningapp.api;

import java.util.List;

import me.narlove.enhancedlearningapp.api.datatypes.QuestionModel;
import me.narlove.enhancedlearningapp.api.datatypes.TaskModel;
import me.narlove.enhancedlearningapp.api.datatypes.UserModel;
import me.narlove.enhancedlearningapp.api.requests.UpdateInterestsRequest;
import me.narlove.enhancedlearningapp.api.responses.TaskIdResponse;
import me.narlove.enhancedlearningapp.api.responses.UserIdResponse;
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
    Call<UserModel> getUserByUsername(@Path("username") String username);

    // have to have these wrapper classes for gson
    // thank you intellij for the "generate getters/settings/constructor" methods :prayingemoji:
    @POST("users/")
    Call<UserIdResponse> createUser(@Body UserModel user);

    @PUT("users/{id}")
    Call<Void> updateInterests(@Path("id") long id, @Body UpdateInterestsRequest body);

    @GET("users/{id}/tasks")
    Call<List<TaskModel>> getUserTasks(@Path("id") long id);

    @GET("users/{id}/tasks/count")
    Call<TaskCountResponse> getUserTaskCount(@Path("id") long id);

    @POST("users/{id}/tasks")
    Call<TaskIdResponse> createTask(@Path("id") long id, @Body TaskModel task);

    @GET("tasks/{id}")
    Call<TaskModel> getTaskById(@Path("id") long id);

    @GET("tasks/owningUser/{id}")
    Call<UserIdResponse> getOwningUserId(@Path("id") long id);

    @DELETE("tasks/{id}")
    Call<Void> deleteTask(@Path("id") long id);

    @GET("questions/{id}")
    Call<QuestionModel> getQuestionById(@Path("id") long id);
}
