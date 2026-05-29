package me.narlove.enhancedlearningapp.api;

import java.util.ArrayList;
import java.util.List;

import me.narlove.enhancedlearningapp.api.datatypes.QuestionModel;
import me.narlove.enhancedlearningapp.api.datatypes.TaskModel;
import me.narlove.enhancedlearningapp.api.datatypes.UserModel;
import me.narlove.enhancedlearningapp.persistence.datatypes.Question;
import me.narlove.enhancedlearningapp.persistence.datatypes.Task;
import me.narlove.enhancedlearningapp.persistence.datatypes.User;

public class ApiDatatypeMapper {
    public static User userModelToDatatype(UserModel model)
    {
        return new User(
                model.getUserId(),
                model.getUsername(),
                model.getName(),
                model.getPassword(),
                model.getEmail(),
                model.getPhone(),
                model.getInterests()
        );
    }

    public static UserModel userDatatypeToModel(User user)
    {
        // empty arraylist for now because we don't have information about the users
        // tasks here and it doesnt matter because this call is made outbound from app towards
        // dev backend
        return new UserModel(
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getInterests(),
                new ArrayList<>()
        );
    }

    public static Task taskModelToDatatype(TaskModel model) {
        return new Task(
                model.getTaskId(),
                // owning user id is not embedded in TaskModel — pass 0 as a safe default.
                // callers that need the real owning id should use getTaskAndQuestionsByTaskId
                // which resolves it via a second api call.
                0,
                model.getInterest(),
                model.getGenPrompt(),
                model.getGenRes(),
                model.getTaskDesc()
        );
    }

    public static Question questionModelToDatatype(QuestionModel model) {
        return new Question(
                model.getQuestionId(),
                // owning task id is not embedded in QuestionModel — pass 0 as a safe default.
                // callers that need the real owning id should use getTaskAndQuestionsByTaskId.
                0,
                model.getQuestionText(),
                model.getOptionA(),
                model.getOptionB(),
                model.getOptionC(),
                model.getCorrectOption(),
                model.getHint(),
                model.getOrderIndex()
        );
    }

    public static TaskModel taskAndQuestionsDatatypeToModel(Task task, List<Question> questions) {
        // map each Question datatype to the QuestionModel that the api expects
        List<QuestionModel> questionModels = new ArrayList<>();
        for (Question q : questions)
        {
            questionModels.add(new QuestionModel(
                    q.getQuestionText(),
                    q.getOptionA(),
                    q.getOptionB(),
                    q.getOptionC(),
                    q.getCorrectOption(),
                    q.getHint(),
                    q.getOrderIndex()
            ));
        }

        return new TaskModel(
                task.getInterest(),
                task.getGenPrompt(),
                task.getGenRes(),
                task.getTaskDesc(),
                questionModels
        );
    }

}
