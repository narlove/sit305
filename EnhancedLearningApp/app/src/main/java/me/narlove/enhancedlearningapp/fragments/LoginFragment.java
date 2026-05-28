package me.narlove.enhancedlearningapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.narlove.enhancedlearningapp.R;
import me.narlove.enhancedlearningapp.persistence.datatypes.User;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUserCallback;
import me.narlove.enhancedlearningapp.persistence.DatabaseViewModel;
import me.narlove.enhancedlearningapp.utilities.GenericUtils;
import me.narlove.enhancedlearningapp.utilities.UserViewModel;

public class LoginFragment extends Fragment {

    private EditText usernameEntry;
    private EditText passwordEntry;
    private Button loginButton;
    private TextView signupText;

    private DatabaseViewModel dbvm;
    private UserViewModel userVm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
        userVm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        usernameEntry = v.findViewById(R.id.usernameEntry);
        passwordEntry = v.findViewById(R.id.passwordEntry);
        loginButton = v.findViewById(R.id.loginButton);
        signupText = v.findViewById(R.id.signupText);

        loginButton.setOnClickListener(clicked ->
        {
            String user = usernameEntry.getText().toString();
            String pass = passwordEntry.getText().toString();

            if (user.isBlank() || pass.isBlank())
            {
                Toast.makeText(requireContext(), "Please enter a valid username and password",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // we need to authenticate with the database here
            dbvm.getUserByUsername(user, new IdentifyUserCallback() {
                @Override
                public void onSuccess(User user) {
                    if (!user.getPassword().equals(pass))
                    {
                        this.onFailure();
                        return;
                    }

                    // authenticated + account exists
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userVm.setCurrentUser(user);
                        }
                    });

                    // then, we switch fragment to TaskDashboard
                    GenericUtils.switchFragment(LoginFragment.this,
                            new TaskDashboardFragment(), false);
                }

                @Override
                public void onFailure() {
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(requireContext(), "Either the username or password provided was incorrect.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        });

        signupText.setOnClickListener(clicked ->
        {
            GenericUtils.switchFragment(LoginFragment.this, new SignupFragment(), true);
        });

        return v;
    }
}