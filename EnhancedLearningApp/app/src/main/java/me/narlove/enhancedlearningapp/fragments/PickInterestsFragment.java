package me.narlove.enhancedlearningapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import me.narlove.enhancedlearningapp.R;
import me.narlove.enhancedlearningapp.datatypes.Interest;
import me.narlove.enhancedlearningapp.persistence.DatabaseViewModel;
import me.narlove.enhancedlearningapp.utilities.GenericUtils;

public class PickInterestsFragment extends Fragment {

    private static final String ARG_UID = "uid";
    private DatabaseViewModel dbvm;

    private long mUid;
    
    private Button submitButton;
    private ChipGroup chipGroup;

    public PickInterestsFragment() {
        // Required empty public constructor
    }

    public static PickInterestsFragment newInstance(long uid) {
        PickInterestsFragment fragment = new PickInterestsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUid = getArguments().getLong(ARG_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pick_interests, container, false);
        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
        
        chipGroup = v.findViewById(R.id.interestChipGroup);
        submitButton = v.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(clicked -> {
            List<Integer> selectedChipIds = chipGroup.getCheckedChipIds();

            if (selectedChipIds.size() > 4) {
                Toast.makeText(requireContext(), "You can only select up to 4 topics", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Interest> selectedInterests = new ArrayList<>();
            for (Integer id : selectedChipIds) {
                Chip chip = v.findViewById(id);
                Interest debug = Interest.getInterestFromCompatible(chip.getText().toString());
                selectedInterests.add(debug);
            }

            // we've got access to uid to perform add interests to user thru uid
            if (!selectedInterests.isEmpty())
            {
                dbvm.overrideInterests(mUid, selectedInterests);
                Log.i("tag", String.format("%d", mUid));

                // then move back to login page
                GenericUtils.switchFragment(PickInterestsFragment.this,
                        new LoginFragment(), false);
            }
            else
            {
                Toast.makeText(requireContext(), "Please select at least one interest", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
}