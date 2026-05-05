package me.narlove.lostandfoundjava.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.narlove.lostandfoundjava.R;
import me.narlove.lostandfoundjava.utilities.GenericUtils;

public class HomeFragment extends Fragment {

    private Button createButton;
    private Button viewButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        createButton = v.findViewById(R.id.newAdvertButton);
        viewButton = v.findViewById(R.id.viewAdvertsButton);

        createButton.setOnClickListener(clicked ->
                GenericUtils.switchFragment(this, new CreateFragment(), true));

        viewButton.setOnClickListener(clicked ->
            GenericUtils.switchFragment(this, new ViewFragment(), true));

        return v;
    }
}