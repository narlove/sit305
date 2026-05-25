package me.narlove.enhancedlearningapp.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import me.narlove.enhancedlearningapp.R;
import me.narlove.enhancedlearningapp.datatypes.User;
import me.narlove.enhancedlearningapp.persistence.DatabaseViewModel;
import me.narlove.enhancedlearningapp.recycler.CustomAdapter;
import me.narlove.enhancedlearningapp.utilities.GenericUtils;
import me.narlove.enhancedlearningapp.utilities.UserViewModel;

public class TaskDashboardFragment extends Fragment {

    private TextView titleText;
    private MaterialCardView taskBannerCard;
    private TextView taskBannerText;
    private RecyclerView recyclerView;
    private Button generateTaskButton;
    private Button goBackButton;

    private DatabaseViewModel dbvm;
    private UserViewModel userVm;

    private User currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
        userVm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        currentUser = userVm.getCurrentUser().getValue();

        // if the user has become signed out, move them to the login fragment again
        if (currentUser == null)
        {
            GenericUtils.switchFragment(this, new LoginFragment(), false);
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_task_dashboard, container, false);

        titleText = v.findViewById(R.id.titleText);
        taskBannerCard = v.findViewById(R.id.taskBannerCard);
        taskBannerText = v.findViewById(R.id.taskBannerText);
        recyclerView = v.findViewById(R.id.taskRecyclerView);
        generateTaskButton = v.findViewById(R.id.generateTaskButton);
        goBackButton = v.findViewById(R.id.logoutButton);

        titleText.setText(String.format("Hello, %s", currentUser.getName()));

        CustomAdapter adapter = new CustomAdapter(requireContext());
        adapter.setHasStableIds(true);

        LinearLayoutManager layout = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);

        dbvm.getTasksByUserId(currentUser.getUserId()).observe(getViewLifecycleOwner(), tasks ->
        {
            adapter.submitList(tasks);
        });

        dbvm.getNumberOfTasksByUserId(currentUser.getUserId()).observe(getViewLifecycleOwner(), number ->
        {
            // prim for primitive
            int prim = number;
            if (prim == 0)
            {
                taskBannerCard.setVisibility(GONE);
            }
            else if (prim == 1)
            {
                taskBannerCard.setVisibility(VISIBLE);
                taskBannerText.setText("You have 1 task due!");
            }
            else
            {
                taskBannerCard.setVisibility(VISIBLE);
                taskBannerText.setText(String.format("You have %d tasks due!", prim));
            }
        });

        goBackButton.setOnClickListener(clicked ->
        {
            userVm.setCurrentUser(null);
            GenericUtils.switchFragment(TaskDashboardFragment.this,
                    new LoginFragment(), false);
        });

        return v;
    }
}