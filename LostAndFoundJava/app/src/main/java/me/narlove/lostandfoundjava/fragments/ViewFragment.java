package me.narlove.lostandfoundjava.fragments;

import static me.narlove.lostandfoundjava.utilities.GenericUtils.switchFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import me.narlove.lostandfoundjava.R;
import me.narlove.lostandfoundjava.persistence.Post;
import me.narlove.lostandfoundjava.recycler.CustomAdapter;
import me.narlove.lostandfoundjava.recycler.OnRecyclerViewItemClickListener;
import me.narlove.lostandfoundjava.viewmodels.DatabaseViewModel;
import me.narlove.lostandfoundjava.viewmodels.SelectionsViewModel;

public class ViewFragment extends Fragment implements OnRecyclerViewItemClickListener {

    private DatabaseViewModel dbvm;
    private SelectionsViewModel selections;
    private Button setFiltersButton;
    private Button homeButton;

    public ViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view, container, false);
        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
        selections = new ViewModelProvider(requireActivity()).get(SelectionsViewModel.class);

        setFiltersButton = v.findViewById(R.id.setFiltersButton);
        homeButton = v.findViewById(R.id.homeButton);

        CustomAdapter adapter = new CustomAdapter(requireContext(), this);
        adapter.setHasStableIds(true);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = v.findViewById(R.id.viewRecycler);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);

        LiveData<List<Post>> filteredResults = Transformations.switchMap(selections.getCurrentSelection(),
                selection -> dbvm.getPostsByCategoryOfType(selection.getCategories(), selection.getTypes())
        );

        filteredResults.observe(getViewLifecycleOwner(), posts ->
        {
            adapter.submitList(posts);
        });

        setFiltersButton.setOnClickListener(clicked ->
        {
            FilterDropdownDialogFragment filter = new FilterDropdownDialogFragment();
            filter.show(getParentFragmentManager(), "filter-dialog");
        });

        homeButton.setOnClickListener(clicked ->
        {
            switchFragment(this, new HomeFragment(), true);
        });

        return v;
    }

    // on a recyclerview item click
    @Override
    public void onItemClick(Post clicked) {
        DetailFragment fragment = DetailFragment.newInstance(
                clicked.getPostName(),
                clicked.getPostContactPhone(),
                clicked.getPostDescription(),
                clicked.getPostDate(),
                clicked.getPostLocation(),
                clicked.getPostUploadDate(),
                clicked.getImageUri()
        );

        switchFragment(this, fragment, true);
    }
}