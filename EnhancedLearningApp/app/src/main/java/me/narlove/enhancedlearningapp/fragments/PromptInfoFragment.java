package me.narlove.enhancedlearningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import me.narlove.enhancedlearningapp.R;

public class PromptInfoFragment extends DialogFragment {

    private static final String ARG_PROMPT = "prompt";
    private static final String ARG_RESPONSE = "response";

    private String prompt;
    private String response;

    public PromptInfoFragment() {
        // Required empty public constructor
    }

    public static PromptInfoFragment newInstance(String prompt, String response) {
        PromptInfoFragment fragment = new PromptInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROMPT, prompt);
        args.putString(ARG_RESPONSE, response);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prompt = getArguments().getString(ARG_PROMPT);
            response = getArguments().getString(ARG_RESPONSE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prompt_info, container, false);

        TextView promptText = v.findViewById(R.id.promptContentText);
        TextView responseText = v.findViewById(R.id.responseContentText);
        Button closeButton = v.findViewById(R.id.closeButton);

        promptText.setText(prompt);
        responseText.setText(response);

        closeButton.setOnClickListener(view -> dismiss());

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}