package me.narlove.enhancedlearningapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import me.narlove.enhancedlearningapp.R;
public class HintFragment extends DialogFragment {

    private static final String ARG_HINT = "hint";

    private TextView hintText;
    private Button closeButton;

    private String hint;

    public HintFragment() {
        // Required empty public constructor
    }

    public static HintFragment newInstance(String hint) {
        HintFragment fragment = new HintFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HINT, hint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hint = getArguments().getString(ARG_HINT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hint, container, false);

        hintText = v.findViewById(R.id.popupHintText);
        closeButton = v.findViewById(R.id.hintCloseButton);

        hintText.setText(hint);

        closeButton.setOnClickListener(c -> dismiss());

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