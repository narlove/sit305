package me.narlove.videoplaylist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.DatabaseView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.narlove.videoplaylist.R;
import me.narlove.videoplaylist.persistence.DatabaseViewModel;
import me.narlove.videoplaylist.persistence.User;
import me.narlove.videoplaylist.utilities.GenericUtils;

public class RegisterFragment extends Fragment {

    private EditText inputUser;
    private EditText inputFullName;
    private EditText inputPassword;
    private EditText inputConfirmPassword;

    private Button submitButton;

    private DatabaseViewModel dbvm;

    public RegisterFragment() {
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
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);

        inputUser = v.findViewById(R.id.create_inputUser);
        inputFullName = v.findViewById(R.id.create_inputFullName);
        inputPassword = v.findViewById(R.id.create_inputPassword);
        inputConfirmPassword = v.findViewById(R.id.create_inputConfirmPassword);

        submitButton = v.findViewById(R.id.createAccountButton);

        submitButton.setOnClickListener(clicked ->
        {
            String grabFullName = inputFullName.getText().toString();
            String grabUser = inputUser.getText().toString();
            String grabPass = inputPassword.getText().toString();

            // do not bother to save confirm password to a variable as it will only be used once
            if (!validateFields(grabFullName, grabUser, grabPass,
                    inputConfirmPassword.getText().toString()))
            {
                Toast.makeText(requireContext(),
                        "You have incorrectly filled out one of the fields. Please note that all fields are required.",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                // fields must be valid, create new user
                // do not auto log in as user, make the cust required to do it themselves
                dbvm.insertUser(new User(grabFullName, grabUser, grabPass));

                // then show Toast to signify that user was created successfully
                Toast.makeText(requireContext(),
                        String.format("User with username %s was successfully created.", grabUser),
                        Toast.LENGTH_SHORT).show();

                // then switchFragment back to login page
                GenericUtils.switchFragment(this, new LoginFragment(), false);
            }
        });

        return v;
    }

    private boolean validateFields(String fullName, String user, String pass, String confirmPass)
    {
        // full name validation
        if (fullName == null || fullName.isBlank()) return false;
        // user validation
        if (user == null || user.isBlank()) return false;
        // password validaton
        if (pass == null || pass.isBlank() || !pass.equals(confirmPass))
            return false;

        return true;
    }
}