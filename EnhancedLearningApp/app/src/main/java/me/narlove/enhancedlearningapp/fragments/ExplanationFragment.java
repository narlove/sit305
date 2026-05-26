package me.narlove.enhancedlearningapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.GenerationConfig;
import com.google.firebase.ai.type.GenerativeBackend;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.narlove.enhancedlearningapp.R;
import me.narlove.enhancedlearningapp.datatypes.Question;
import me.narlove.enhancedlearningapp.datatypes.callbacks.IdentifyQuestionCallback;
import me.narlove.enhancedlearningapp.persistence.DatabaseViewModel;

public class ExplanationFragment extends DialogFragment {

    private static final String ARG_QID = "questionId";

    private long questionId;

    private TextView explanationTextView, popupPromptTextView, popupPromptLabel;
    private Button closeButton, viewPromptButton;

    private DatabaseViewModel dbvm;

    private GenerativeModelFutures model;
    private Executor executor = Executors.newSingleThreadExecutor();

    public ExplanationFragment() {
        // Required empty public constructor
    }

    public static ExplanationFragment newInstance(long questionId) {
        ExplanationFragment fragment = new ExplanationFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_QID, questionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) throw new IllegalStateException("do not use constructor for explanation fragment");;

        questionId = getArguments().getLong(ARG_QID, -1);

        if (questionId == -1) {
            // something went wrong and we don't have access to the question
            dismiss();
        }

        GenerationConfig generationConfig = new GenerationConfig.Builder()
                .setResponseMimeType("text/plain")
                .build();

        GenerativeModel firebaseAI = FirebaseAI.getInstance(GenerativeBackend.googleAI())
                .generativeModel(
                        "gemini-2.5-flash",
                        generationConfig
                );

        model = GenerativeModelFutures.from(firebaseAI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_explanation, container, false);
        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);

        explanationTextView = v.findViewById(R.id.popupExplanationText);
        closeButton = v.findViewById(R.id.closeButton);
        viewPromptButton = v.findViewById(R.id.viewPromptButton);
        popupPromptTextView = v.findViewById(R.id.popupPromptText);
        popupPromptLabel = v.findViewById(R.id.popupPromptLabel);

        // don't need question specific information to assign this click listener
        closeButton.setOnClickListener(c -> dismiss());

        dbvm.getQuestionByQuestionId(questionId, new IdentifyQuestionCallback() {
            @Override
            public void onSuccess(Question question) {
                if (!isAdded()) return;
                // if not is added we cant actually show anything, we can't even dismiss

                requireActivity().runOnUiThread(() ->
                {
                    String prompt = generateExplanationText(question);

                    // now that we know what our prompt is, we can assign the view prompt button
                    viewPromptButton.setOnClickListener(c ->
                    {
                        popupPromptTextView.setText(prompt);
                        popupPromptLabel.setVisibility(View.VISIBLE);
                        popupPromptTextView.setVisibility(View.VISIBLE);
                        viewPromptButton.setEnabled(false);
                    });

                    Content content = new Content.Builder()
                            .addText(prompt)
                            .build();

                    ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
                    Futures.addCallback(
                            response,
                            new FutureCallback<GenerateContentResponse>() {
                                @Override
                                public void onSuccess(GenerateContentResponse result) {
                                    if (!isAdded()) return;

                                    requireActivity().runOnUiThread(() ->
                                    {
                                        String response = result.getText();

                                        explanationTextView.setText(response);
                                    });
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    t.printStackTrace();
                                }
                            },
                            executor);
                });
            }

            @Override
            public void onFailure() {
                dismiss();
                Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private String generateExplanationText(Question q)
    {
        return String.format("You are going to be provided with a technology-related question, " +
                        "and then three multiple choice answers. You need to give a brief, strictly 3-or-less " +
                        "sentence explanation, as to why that answer is correct. The question is: '%s', and the " +
                        "three possible answers are A) '%s', B) '%s', C) '%s'.",
                q.getQuestionText(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC());
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}