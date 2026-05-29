package me.narlove.enhancedlearningapp.persistence;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.narlove.enhancedlearningapp.Interest;
import me.narlove.enhancedlearningapp.api.ApiDatatypeMapper;
import me.narlove.enhancedlearningapp.api.ApiService;
import me.narlove.enhancedlearningapp.api.ApiViewModel;
import me.narlove.enhancedlearningapp.api.RetrofitHelper;
import me.narlove.enhancedlearningapp.api.datatypes.QuestionModel;
import me.narlove.enhancedlearningapp.api.datatypes.TaskModel;
import me.narlove.enhancedlearningapp.api.datatypes.UserModel;
import me.narlove.enhancedlearningapp.api.requests.UpdateInterestsRequest;
import me.narlove.enhancedlearningapp.api.responses.TaskIdResponse;
import me.narlove.enhancedlearningapp.api.responses.TaskCountResponse;
import me.narlove.enhancedlearningapp.api.responses.UserIdResponse;
import me.narlove.enhancedlearningapp.persistence.datatypes.Question;
import me.narlove.enhancedlearningapp.persistence.datatypes.Task;
import me.narlove.enhancedlearningapp.persistence.datatypes.TaskWithQuestions;
import me.narlove.enhancedlearningapp.persistence.datatypes.User;
import me.narlove.enhancedlearningapp.callbacks.IdentifyQuestionCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyTaskAndQuestionsCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserIdCallback;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUsernameExistsCallback;
import me.narlove.enhancedlearningapp.persistence.datatypes.IRepository;
import me.narlove.enhancedlearningapp.utilities.InterestConversionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MongoRepo implements IRepository {

    private ApiService service;
    private static ApiViewModel apiVm;

    public MongoRepo()
    {
        service = RetrofitHelper.getApiService();
    }

    // this needs to be called by mainactivity before anything else
    public static void assignViewModel(ApiViewModel vm)
    {
        apiVm = vm;
    }

    @Override
    public void getUserByUsername(String username, IdentifyUserCallback callback) {
        service.getUserByUsername(username).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (!response.isSuccessful()) callback.onFailure();

                UserModel res = response.body();
                if (res == null) throw new IllegalStateException("something went wrong in mongorepo with response");

                User user = ApiDatatypeMapper.userModelToDatatype(res);

                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void insert(User user, IdentifyUserIdCallback callback) {
        UserModel model = ApiDatatypeMapper.userDatatypeToModel(user);

        service.createUser(model).enqueue(new Callback<UserIdResponse>() {
            @Override
            public void onResponse(Call<UserIdResponse> call, Response<UserIdResponse> response) {
                if (!response.isSuccessful()) throw new IllegalStateException("something went wrong in mongorepo with response");

                UserIdResponse res = response.body();
                if (res == null) throw new IllegalStateException("something went wrong in mongorepo with response");

                long userId = res.getUserId();

                callback.onSuccess(userId);
            }

            @Override
            public void onFailure(Call<UserIdResponse> call, Throwable t) {
                // print stack trace to identify problem that occurred if it ever does come up
                t.printStackTrace();
                // throw to stop program from progressing, force crash so user has feedback
                throw new IllegalArgumentException("failure triggered for db user insert");
            }
        });
    }

    @Override
    public void overrideInterests(long userId, List<Interest> newInterests) {
        List<String> transformedInterests = newInterests.stream()
                .map(InterestConversionHandler::interestToString)
                .collect(Collectors.toList());

        UpdateInterestsRequest req = new UpdateInterestsRequest(transformedInterests);

        service.updateInterests(userId, req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) throw new IllegalStateException("something went wrong in mongorepo with response");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // print stack trace to identify problem that occurred if it ever does come up
                t.printStackTrace();
                // throw to stop program from progressing, force crash so user has feedback
                throw new IllegalArgumentException("failure updating addr");
            }
        });
    }

    @Override
    public void doesUsernameExist(String username, IdentifyUsernameExistsCallback callback) {
        // going to call get user by username and if we receive a 404 error, we return success
        // otherwise, a success return means user does exist
        service.getUserByUsername(username).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() == 404)
                {
                    callback.onUsernameDoesNotExist();
                }

                UserModel res = response.body();
                if (res == null) callback.onUsernameDoesNotExist();

                // otherwise assume positive
                callback.onUsernameExists();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                // refers to network failure, throw error to be aware
                throw new IllegalStateException("network failed, unresolvable");
            }
        });
    }

    @Override
    public LiveData<List<Task>> getTasksByUserId(long uid) {
        service.getUserTasks(uid).enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                if (!response.isSuccessful()) throw new IllegalStateException("something went wrong in mongorepo with response");

                List<TaskModel> res = response.body();
                if (res == null) throw new IllegalStateException("something went wrong in mongorepo with response");

                List<Task> tasks = res.stream()
                        .map(ApiDatatypeMapper::taskModelToDatatype)
                        .collect(Collectors.toList());

                apiVm.getTasksList().postValue(tasks);
            }

            @Override
            public void onFailure(Call<List<TaskModel>> call, Throwable t) {
                t.printStackTrace();
                throw new IllegalArgumentException("failure triggered for db get tasks");
            }
        });

        return apiVm.getTasksList();
    }

    @Override
    public void getTaskAndQuestionsByTaskId(long taskId, IdentifyTaskAndQuestionsCallback callback) {
        service.getTaskById(taskId).enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                if (!response.isSuccessful()) callback.onFailure();

                TaskModel res = response.body();
                if (res == null) throw new IllegalStateException("something went wrong in mongorepo with response");

                List<Question> questionObjects = new ArrayList<>();

                // parse questions as question objects, translating to relational instead of embedded
                for (QuestionModel q : res.getQuestions())
                {
                    questionObjects.add(
                            new Question(
                                    q.getQuestionId(),
                                    res.getTaskId(),
                                    q.getQuestionText(),
                                    q.getOptionA(),
                                    q.getOptionB(),
                                    q.getOptionC(),
                                    q.getCorrectOption(),
                                    q.getHint(),
                                    q.getOrderIndex()
                            )
                    );
                }

                // need to make a call to get the owning user id
                service.getOwningUserId(taskId).enqueue(new Callback<UserIdResponse>() {
                    @Override
                    public void onResponse(Call<UserIdResponse> call, Response<UserIdResponse> resInner) {
                        if (!resInner.isSuccessful()) callback.onFailure();

                        UserIdResponse idRes = resInner.body();
                        if (idRes == null) throw new IllegalStateException("something went wrong in mongorepo with response");

                        // now map task to an actual model
                        Task parsedTask = new Task(
                                res.getTaskId(),
                                idRes.getUserId(),
                                res.getInterest(),
                                res.getGenPrompt(),
                                res.getGenRes(),
                                res.getTaskDesc()
                        );

                        // generate new previous database combination type
                        TaskWithQuestions tnq = new TaskWithQuestions(parsedTask, questionObjects);

                        callback.onSuccess(tnq);
                    }

                    @Override
                    public void onFailure(Call<UserIdResponse> call, Throwable t) {
                        throw new IllegalStateException("something went wrong in mongorepo with response");
                    }
                });
            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                throw new IllegalStateException("something went wrong in mongorepo with response");
            }
        });
    }

    @Override
    public void deleteTaskById(long taskId) {
        service.deleteTask(taskId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) throw new IllegalStateException("something went wrong in mongorepo with response");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // print stack trace to identify problem that occurred if it ever does come up
                t.printStackTrace();
                // throw to stop program from progressing, force crash so user has feedback
                throw new IllegalArgumentException("failure triggered for db task delete");
            }
        });
    }

    @Override
    public void insertTaskWithQuestions(Task task, List<Question> questions) {
        TaskModel model = ApiDatatypeMapper.taskAndQuestionsDatatypeToModel(task, questions);

        service.createTask(task.getOwningUserId(), model).enqueue(new Callback<TaskIdResponse>() {
            @Override
            public void onResponse(Call<TaskIdResponse> call, Response<TaskIdResponse> response) {
                if (!response.isSuccessful()) throw new IllegalStateException("something went wrong in mongorepo with response");
            }

            @Override
            public void onFailure(Call<TaskIdResponse> call, Throwable t) {
                // print stack trace to identify problem that occurred if it ever does come up
                t.printStackTrace();
                // throw to stop program from progressing, force crash so user has feedback
                throw new IllegalArgumentException("failure triggered for db task insert");
            }
        });
    }

    @Override
    public LiveData<Integer> getNumberOfTasksByUserId(long uid) {
        service.getUserTaskCount(uid).enqueue(new Callback<TaskCountResponse>() {
            @Override
            public void onResponse(Call<TaskCountResponse> call, Response<TaskCountResponse> response) {
                if (!response.isSuccessful()) throw new IllegalStateException("something went wrong in mongorepo with response");

                TaskCountResponse res = response.body();
                if (res == null) throw new IllegalStateException("something went wrong in mongorepo with response");

                apiVm.getTasksCount().postValue(res.getCount());
            }

            @Override
            public void onFailure(Call<TaskCountResponse> call, Throwable t) {
                t.printStackTrace();
                throw new IllegalArgumentException("failure triggered for db get task count");
            }
        });

        return apiVm.getTasksCount();
    }

    @Override
    public void getQuestionByQuestionId(long questionId, IdentifyQuestionCallback callback) {
        service.getQuestionById(questionId).enqueue(new Callback<QuestionModel>() {
            @Override
            public void onResponse(Call<QuestionModel> call, Response<QuestionModel> response) {
                if (!response.isSuccessful()) callback.onFailure();

                QuestionModel res = response.body();
                if (res == null) throw new IllegalStateException("something went wrong in mongorepo with response");

                Question question = ApiDatatypeMapper.questionModelToDatatype(res);

                callback.onSuccess(question);
            }

            @Override
            public void onFailure(Call<QuestionModel> call, Throwable t) {
                t.printStackTrace();
                throw new IllegalArgumentException("failure triggered for db get question");
            }
        });
    }
}