package me.narlove.chatbot.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.narlove.chatbot.R;
import me.narlove.chatbot.utilities.GenericUtils;

public class LoginFragment extends Fragment {

    private Button goButton;
    private EditText usernameEntry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        goButton = v.findViewById(R.id.goButton);
        usernameEntry = v.findViewById(R.id.usernameInput);

        goButton.setOnClickListener(clicked ->
        {
            String username = usernameEntry.getText().toString();
            
            if (username.isBlank())
            {
                Toast.makeText(requireContext(), "You need to provide a username.",
                        Toast.LENGTH_LONG).show();

                return;
            }

            MessagesFragment frag = MessagesFragment.newInstance(username);
            GenericUtils.switchFragment(this, frag,false);
        });

        return v;
    }
}