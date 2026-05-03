package me.narlove.videoplaylist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.narlove.videoplaylist.R;
import me.narlove.videoplaylist.persistence.DatabaseViewModel;
import me.narlove.videoplaylist.persistence.PlaylistEntry;
import me.narlove.videoplaylist.persistence.Repository;
import me.narlove.videoplaylist.persistence.UserWithPlaylistEntries;
import me.narlove.videoplaylist.recycler.PlaylistAdapter;
import me.narlove.videoplaylist.utilities.GenericUtils;
import me.narlove.videoplaylist.utilities.UserTrackViewModel;

public class PlaylistFragment extends Fragment {

    private PlaylistAdapter adapter;
    private RecyclerView recycler;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseViewModel dbvm;
    private UserTrackViewModel userVm;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);

        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
        userVm = new ViewModelProvider(requireActivity()).get(UserTrackViewModel.class);

        adapter = new PlaylistAdapter(requireContext(), this::openPrefilledPlayFragment);
        adapter.setHasStableIds(true);

        recycler = v.findViewById(R.id.recycler);

        layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);

        long currentUserId = getCurrentUserId();

        dbvm.getUser(currentUserId).observe(getViewLifecycleOwner(), updatedUser ->
        {
            adapter.submitList(updatedUser.playlistEntries);
        });

        return v;
    }

    private long getCurrentUserId() {
        UserWithPlaylistEntries currentUser = userVm.getCurrentUser().getValue();
        if (currentUser == null)
        {
            throw new IllegalStateException("user should not be null in playlist fragment");
        }

        return currentUser.user.getUserId();
    }

    private void openPrefilledPlayFragment(String url)
    {
        PlayFragment frag = PlayFragment.newInstance(url);;

        GenericUtils.switchFragment(this, frag, true);
    }
}