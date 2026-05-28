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
import me.narlove.enhancedlearningapp.persistence.datatypes.Question;
import me.narlove.enhancedlearningapp.callbacks.IdentifyTaskAndQuestionsCallback;
import me.narlove.enhancedlearningapp.persistence.DatabaseViewModel;
import me.narlove.enhancedlearningapp.persistence.datatypes.TaskWithQuestions;
import me.narlove.enhancedlearningapp.utilities.GenericUtils;
import me.narlove.enhancedlearningapp.utilities.UserViewModel;

public class ResultsFragment extends Fragment {

    private static final String ARG_USER_ANSWER_1 = "userAnswer1";
    private static final String ARG_USER_ANSWER_2 = "userAnswer2";
    private static final String ARG_USER_ANSWER_3 = "userAnswer3";
    private static final String ARG_TASK_ID = "taskId";

    private int userAnswer1;
    private int userAnswer2;
    private int userAnswer3;
    private long taskId;

    private UserViewModel userVm;
    private DatabaseViewModel dbvm;

    private MaterialCardView promptAnswerBannerCard;
    private TextView promptAnswerBannerText;

    private TextView questionText1;
    private RadioGroup optionsGroup1;
    private RadioButton option1a, option1b, option1c;

    private TextView questionText2;
    private RadioGroup optionsGroup2;
    private RadioButton option2a, option2b, option2c;

    private TextView questionText3;
    private RadioGroup optionsGroup3;
    private RadioButton option3a, option3b, option3c;

    private Button continueButton, explainButton1, explainButton2, explainButton3;

    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance(int userAnswer1,
                                              int userAnswer2,
                                              int userAnswer3,
                                              long taskId) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ANSWER_1, userAnswer1);
        args.putInt(ARG_USER_ANSWER_2, userAnswer2);
        args.putInt(ARG_USER_ANSWER_3, userAnswer3);
        args.putLong(ARG_TASK_ID, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) throw new IllegalStateException("do not use constructor for results fragment");

        userAnswer1 = getArguments().getInt(ARG_USER_ANSWER_1, -1);
        userAnswer2 = getArguments().getInt(ARG_USER_ANSWER_2, -1);
        userAnswer3 = getArguments().getInt(ARG_USER_ANSWER_3, -1);

        taskId = getArguments().getLong(ARG_TASK_ID, -1);

        if (taskId == -1) {
            GenericUtils.switchFragment(this, new TaskDashboardFragment(), false);
            return;
        }

        if (userAnswer1 == -1 || userAnswer2 == -1 || userAnswer3 == -1) {
            GenericUtils.switchFragment(this, TaskFragment.newInstance(taskId), false);
            return;
        }

        userVm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);

        if (userVm.getCurrentUser().getValue() == null) {
            GenericUtils.switchFragment(this, new LoginFragment(), false);
            return;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_results, container, false);

        promptAnswerBannerCard = v.findViewById(R.id.promptAnswerBannerCard);

        questionText1 = v.findViewById(R.id.questionText1);
        optionsGroup1 = v.findViewById(R.id.optionsGroup1);
        option1a = v.findViewById(R.id.option1a);
        option1b = v.findViewById(R.id.option1b);
        option1c = v.findViewById(R.id.option1c);
        explainButton1 = v.findViewById(R.id.explanationButton1);

        questionText2 = v.findViewById(R.id.questionText2);
        optionsGroup2 = v.findViewById(R.id.optionsGroup2);
        option2a = v.findViewById(R.id.option2a);
        option2b = v.findViewById(R.id.option2b);
        option2c = v.findViewById(R.id.option2c);
        explainButton2 = v.findViewById(R.id.explanationButton2);

        questionText3 = v.findViewById(R.id.questionText3);
        optionsGroup3 = v.findViewById(R.id.optionsGroup3);
        option3a = v.findViewById(R.id.option3a);
        option3b = v.findViewById(R.id.option3b);
        option3c = v.findViewById(R.id.option3c);
        explainButton3 = v.findViewById(R.id.explanationButton3);

        continueButton = v.findViewById(R.id.continueButton);

        dbvm.getTaskAndQuestionsByTaskId(taskId, new IdentifyTaskAndQuestionsCallback() {
            @Override
            public void onSuccess(TaskWithQuestions taskItem) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    promptAnswerBannerCard.setOnClickListener(clicked -> {
                        PromptInfoFragment.newInstance(
                                taskItem.task.getGenPrompt(),
                                taskItem.task.getGenRes()
                        ).show(getParentFragmentManager(), "prompt-info-fragment");
                    });

                    continueButton.setOnClickListener(clicked ->
                    {
                        dbvm.deleteTaskById(taskId);

                        GenericUtils.switchFragment(ResultsFragment.this, new TaskDashboardFragment(),
                                false);
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
                        explainButton1.setOnClickListener(c ->
                                ExplanationFragment.newInstance(q1.getQuestionId()).show(getParentFragmentManager(), "explanation-dialog"));

                        handleShowingVisualFeedback(q1, userAnswer1, option1a, option1b, option1c);

                        // q2
                        Question q2 = questions.get(1);
                        questionText2.setText(q2.getQuestionText());
                        option2a.setText(q2.getOptionA());
                        option2b.setText(q2.getOptionB());
                        option2c.setText(q2.getOptionC());
                        explainButton2.setOnClickListener(c ->
                                ExplanationFragment.newInstance(q2.getQuestionId()).show(getParentFragmentManager(), "explanation-dialog"));

                        handleShowingVisualFeedback(q2, userAnswer2, option2a, option2b, option2c);

                        // q3
                        Question q3 = questions.get(2);
                        questionText3.setText(q3.getQuestionText());
                        option3a.setText(q3.getOptionA());
                        option3b.setText(q3.getOptionB());
                        option3c.setText(q3.getOptionC());
                        explainButton3.setOnClickListener(c ->
                                ExplanationFragment.newInstance(q3.getQuestionId()).show(getParentFragmentManager(), "explanation-dialog"));

                        // handle showing visual feedback.
                        handleShowingVisualFeedback(q3, userAnswer3, option3a, option3b, option3c);
                    }
                });
            }

            @Override
            public void onFailure() {
                // on failure we want the error to occur if the activity is not added
                // because otherwise we can't really give the user any indication the program has failed
                // if it crashes, that is an indication in its own sense.

                // i would put the user back to the Task page, but if this callback is being
                // called its because the task was unable to be identified, so moving back to dash
                requireActivity().runOnUiThread(() ->
                {
                    GenericUtils.switchFragment(ResultsFragment.this,
                            new TaskDashboardFragment(), false);
                });
            }
        });

        return v;
    }

    private void handleShowingVisualFeedback(Question q, int userAnswer, RadioButton optionA,
                                             RadioButton optionB, RadioButton optionC) {
        RadioButton selection = mapIndexToOption(userAnswer,
                optionA, optionB, optionC);

        selection.setChecked(true);

        if (userAnswer == q.getCorrectOption())
        {
            selection.setBackgroundResource(R.color.answer_correct);
        }
        else
        {
            RadioButton correct = mapIndexToOption(q.getCorrectOption(),
                    optionA, optionB, optionC);

            selection.setBackgroundResource(R.color.answer_incorrect);
            correct.setBackgroundResource(R.color.answer_correct);
        }
    }

    private RadioButton mapIndexToOption(int i, RadioButton optionA,
                                         RadioButton optionB, RadioButton optionC)
    {
        if (i < 0 || i > 2) throw new IllegalArgumentException("i needs to be 0-2 inclusive");

        switch (i)
        {
            case 0: return optionA;
            case 1: return optionB;
            case 2: return optionC;
            default: throw new IllegalArgumentException("i needs to be 0-2 inclusive");
        }
    }
}