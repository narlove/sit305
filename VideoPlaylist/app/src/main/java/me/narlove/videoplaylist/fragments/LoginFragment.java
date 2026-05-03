package me.narlove.videoplaylist.fragments;

import static me.narlove.videoplaylist.utilities.GenericUtils.switchFragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.narlove.videoplaylist.R;
import me.narlove.videoplaylist.persistence.DatabaseViewModel;
import me.narlove.videoplaylist.persistence.User;
import me.narlove.videoplaylist.persistence.UserWithPlaylistEntries;
import me.narlove.videoplaylist.utilities.AuthCallback;
import me.narlove.videoplaylist.utilities.UserTrackViewModel;

public class LoginFragment extends Fragment implements AuthCallback {

    private Button loginButton;
    private Button registerButton;

    private EditText userInput;
    private EditText passInput;

    private DatabaseViewModel dbvm;
    private UserTrackViewModel userVm;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = v.findViewById(R.id.loginButton);
        registerButton = v.findViewById(R.id.registerButton);
        userInput = v.findViewById(R.id.inputUser);
        passInput = v.findViewById(R.id.inputPassword);

        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
        userVm = new ViewModelProvider(requireActivity()).get(UserTrackViewModel.class);

        // allow the user to back track from create account to login via back button.
        registerButton.setOnClickListener(clickedView -> switchFragment(this, new RegisterFragment(),
                true));

        loginButton.setOnClickListener(clickedView ->
        {
            dbvm.authenticate(userInput.getText().toString(),
                passInput.getText().toString(), this);
        });

        return v;
    }

    @Override
    public void onSuccess(UserWithPlaylistEntries user) {
        // handle update the current user view model to remember which user is currently
        // logged in
        requireActivity().runOnUiThread(() ->
        {
            userVm.setCurrentUser(user);
        });

        // then switch fragment
        // do not allow switch base to login page from home, the correct UX
        // to get back to login page is to logout
        switchFragment(this, new HomeFragment(), false);
    }

    @Override
    public void onFailure() {
        requireActivity().runOnUiThread(() ->
        {
            Toast.makeText(requireContext(), "Credentials are invalid.", Toast.LENGTH_SHORT).show();
        });
    }
}