package me.narlove.enhancedlearningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.narlove.enhancedlearningapp.R;
import me.narlove.enhancedlearningapp.datatypes.Question;
import me.narlove.enhancedlearningapp.datatypes.callbacks.IdentifyTaskAndQuestionsCallback;
import me.narlove.enhancedlearningapp.persistence.DatabaseViewModel;
import me.narlove.enhancedlearningapp.persistence.datatypes.TaskWithQuestions;
import me.narlove.enhancedlearningapp.utilities.GenericUtils;
import me.narlove.enhancedlearningapp.utilities.UserViewModel;

public class TaskFragment extends Fragment {

    private static final String ARG_TID = "taskId";
    private long taskId;

    // Task Header elements
    private MaterialCardView taskBanner;
    private TextView taskTitle;
    private TextView taskDescription;

    // question set 1
    private TextView questionText1;
    private RadioGroup optionsGroup1;
    private RadioButton option1a, option1b, option1c;
    private Button hintButton1;

    // question set 2
    private TextView questionText2;
    private RadioGroup optionsGroup2;
    private RadioButton option2a, option2b, option2c;
    private Button hintButton2;

    // question set 3
    private TextView questionText3;
    private RadioGroup optionsGroup3;
    private RadioButton option3a, option3b, option3c;
    private Button hintButton3;

    private Button submitButton;
    private Button backButton;

    private UserViewModel userVm;
    private DatabaseViewModel dbvm;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(long taskId) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TID, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getLong(ARG_TID, -1);
        }

        if (taskId == -1)
        {
            // if the task lookup has failed for whatever reason,
            // then we revert back to taskdashboard screen.
            GenericUtils.switchFragment(this, new TaskDashboardFragment(), false);
        }

        userVm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);

        if (userVm.getCurrentUser().getValue() == null)
        {
            // user has become logged out at some point, revert to login screen, do not save progress
            GenericUtils.switchFragment(this, new LoginFragment(), false);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, container, false);

        taskBanner = v.findViewById(R.id.promptTaskBannerCard);
        taskTitle = v.findViewById(R.id.taskTitle);
        taskDescription = v.findViewById(R.id.taskDescription);

        questionText1 = v.findViewById(R.id.questionText1);
        optionsGroup1 = v.findViewById(R.id.optionsGroup1);
        option1a = v.findViewById(R.id.option1a);
        option1b = v.findViewById(R.id.option1b);
        option1c = v.findViewById(R.id.option1c);
        hintButton1 = v.findViewById(R.id.hintButton1);

        questionText2 = v.findViewById(R.id.questionText2);
        optionsGroup2 = v.findViewById(R.id.optionsGroup2);
        option2a = v.findViewById(R.id.option2a);
        option2b = v.findViewById(R.id.option2b);
        option2c = v.findViewById(R.id.option2c);
        hintButton2 = v.findViewById(R.id.hintButton2);

        questionText3 = v.findViewById(R.id.questionText3);
        optionsGroup3 = v.findViewById(R.id.optionsGroup3);
        option3a = v.findViewById(R.id.option3a);
        option3b = v.findViewById(R.id.option3b);
        option3c = v.findViewById(R.id.option3c);
        hintButton3 = v.findViewById(R.id.hintButton3);

        submitButton = v.findViewById(R.id.submitButton);
        backButton = v.findViewById(R.id.backButton);

        dbvm.getTaskAndQuestionsByTaskId(taskId, new IdentifyTaskAndQuestionsCallback() {
            @Override
            public void onSuccess(TaskWithQuestions taskItem) {
                if (!isAdded()) return;
                // if not is added we cant actually show anything, the user will be left on a blankscreen

                requireActivity().runOnUiThread(() ->
                {
                    taskTitle.setText(String.format("Generated Task %d", taskItem.task.getTaskId()));
                    taskDescription.setText(taskItem.task.getTaskDesc());

                    taskBanner.setOnClickListener(clicked ->
                    {
                        PromptInfoFragment infoFrag = PromptInfoFragment.newInstance(taskItem.task.getGenPrompt(),
                                taskItem.task.getGenRes());
                        infoFrag.show(getParentFragmentManager(), "prompt-info-fragment");
                    });

                    backButton.setOnClickListener(clicked ->
                    {
                        GenericUtils.switchFragment(TaskFragment.this,
                                new TaskDashboardFragment(), true);
                    });

                    List<Question> questions = taskItem.questions;
                    if (questions != null && questions.size() >= 3) {
                        // sort by order index to ensure correct sequence
                        Collections.sort(questions, new Comparator<Question>() {
                            @Override
                            public int compare(Question q1, Question q2) {
                                return Integer.compare(q1.getOrderIndex(), q2.getOrderIndex());
                            }
                        });

                        // q1
                        Question q1 = questions.get(0);
                        questionText1.setText(q1.getQuestionText());
                        option1a.setText(q1.getOptionA());
                        option1b.setText(q1.getOptionB());
                        option1c.setText(q1.getOptionC());

                        // q2
                        Question q2 = questions.get(1);
                        questionText2.setText(q2.getQuestionText());
                        option2a.setText(q2.getOptionA());
                        option2b.setText(q2.getOptionB());
                        option2c.setText(q2.getOptionC());

                        // q3
                        Question q3 = questions.get(2);
                        questionText3.setText(q3.getQuestionText());
                        option3a.setText(q3.getOptionA());
                        option3b.setText(q3.getOptionB());
                        option3c.setText(q3.getOptionC());
                    }
                });
            }

            @Override
            public void onFailure() {
                // on failure we want the error to occur if the activity is not added
                // because otherwise we can't really give the user any indication the program has failed
                // if it crashes, that is an indication in its own sense.
                requireActivity().runOnUiThread(() ->
                {
                    GenericUtils.switchFragment(TaskFragment.this, new TaskDashboardFragment(), false);
                });
            }
        });

        return v;
    }
}