package me.narlove.videoplaylist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.narlove.videoplaylist.R;
import me.narlove.videoplaylist.persistence.DatabaseViewModel;
import me.narlove.videoplaylist.persistence.PlaylistEntry;
import me.narlove.videoplaylist.persistence.UserWithPlaylistEntries;
import me.narlove.videoplaylist.utilities.GenericUtils;
import me.narlove.videoplaylist.utilities.UserTrackViewModel;

public class HomeFragment extends Fragment {

    private EditText inputUrl;
    private Button playButton;
    private Button addButton;
    private Button viewButton;
    private Button logoutButton;

    private DatabaseViewModel dbvm;
    private UserTrackViewModel userVm;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
        userVm = new ViewModelProvider(requireActivity()).get(UserTrackViewModel.class);

        inputUrl = v.findViewById(R.id.inputUrl);
        playButton = v.findViewById(R.id.playButton);
        addButton = v.findViewById(R.id.addToPlaylistButton);
        viewButton = v.findViewById(R.id.accessPlaylistButton);
        logoutButton = v.findViewById(R.id.logoutButton);

        playButton.setOnClickListener(clicked ->
        {
            if (GenericUtils.isValidYoutubeUrl(inputUrl.getText().toString()))
            {
                PlayFragment nextFragment = PlayFragment.newInstance(inputUrl.getText().toString());
                GenericUtils.switchFragment(this, nextFragment, true);
            }
            else
            {
                Toast.makeText(requireContext(), "You need to provide a valid Youtube URL.", Toast.LENGTH_SHORT).show();
            }
        });

        addButton.setOnClickListener(clicked -> addToPlaylist(inputUrl.getText().toString()));

        viewButton.setOnClickListener(clicked ->
                GenericUtils.switchFragment(this, new PlaylistFragment(), true));

        logoutButton.setOnClickListener(clicked -> logout());

        return v;
    }

    private void addToPlaylist(String url)
    {
        UserWithPlaylistEntries user = userVm.getCurrentUser().getValue();

        if (user == null)
        {
            throw new IllegalStateException("user should not be null when we're on the home screen");
        }

        if (url == null || url.isBlank() || !GenericUtils.isValidYoutubeUrl(url))
        {
            Toast.makeText(requireContext(), "Please ensure you're providing a valid Youtube url.", Toast.LENGTH_SHORT).show();
            return;
        }

        dbvm.insertPlaylistEntry(new PlaylistEntry(url, user.user.getUserId()));

        Toast.makeText(requireContext(),
                String.format("URl %s added to user %s's playlist", url, user.user.getFullName()),
                Toast.LENGTH_LONG).show();
    }

    private void logout()
    {
        userVm.setCurrentUser(null);
        GenericUtils.switchFragment(this, new LoginFragment(), false);
    }
}