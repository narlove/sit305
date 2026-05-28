package me.narlove.enhancedlearningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import me.narlove.enhancedlearningapp.R;
import me.narlove.enhancedlearningapp.persistence.datatypes.User;
import me.narlove.enhancedlearningapp.callbacks.IdentifyUsernameExistsCallback;
import me.narlove.enhancedlearningapp.persistence.DatabaseViewModel;
import me.narlove.enhancedlearningapp.utilities.GenericUtils;

public class SignupFragment extends Fragment {

    private EditText usernameEntry, firstNameEntry, emailEntry, phoneEntry,
            passwordEntry, confirmPasswordEntry;
    private Button createButton;
    private TextView loginText;

    private DatabaseViewModel dbvm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);

        usernameEntry = v.findViewById(R.id.usernameSignupEntry);
        firstNameEntry = v.findViewById(R.id.nameEntry);
        emailEntry = v.findViewById(R.id.emailEntry);
        phoneEntry = v.findViewById(R.id.phoneEntry);
        passwordEntry = v.findViewById(R.id.passwordSignupEntry);
        confirmPasswordEntry = v.findViewById(R.id.confirmPasswordEntry);
        createButton = v.findViewById(R.id.createButton);
        loginText = v.findViewById(R.id.loginRedirect);

        createButton.setOnClickListener(clicked ->
        {
            String user = usernameEntry.getText().toString();
            String name = firstNameEntry.getText().toString();
            String email = emailEntry.getText().toString();
            String phone = phoneEntry.getText().toString();
            String pass = passwordEntry.getText().toString();
            String confirm = confirmPasswordEntry.getText().toString();

            if (user.isBlank() || name.isBlank() || pass.isBlank() || confirm.isBlank())
            {
                Toast.makeText(requireContext(),
                        "Username, your name, the password and the confirm password are all required fields. " +
                                "Please make sure to fill them out.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (!pass.equals(confirm))
            {
                Toast.makeText(requireContext(), "Passwords need to be the same",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // confirm username is not already taken
            dbvm.doesUsernameExist(user, new IdentifyUsernameExistsCallback() {
                @Override
                public void onUsernameExists() {
                    if (isAdded())
                    {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(requireContext(), "Username already exists, pick another username.",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    usernameEntry.getText().clear();
                    return;
                }

                @Override
                public void onUsernameDoesNotExist() {
                    User newUser = new User(
                            user,
                            name,
                            pass,
                            email,
                            phone,
                            new ArrayList<>()
                    );

                    dbvm.insert(newUser, uid ->
                    {
                        GenericUtils.switchFragment(SignupFragment.this, PickInterestsFragment.newInstance(uid), false);
                    });

                }
            });
        });

        loginText.setOnClickListener(clicked ->
        {
            GenericUtils.switchFragment(SignupFragment.this, new LoginFragment(), true);
        });

        return v;
    }
}