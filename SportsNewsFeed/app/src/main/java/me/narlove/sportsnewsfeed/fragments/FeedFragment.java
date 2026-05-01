package me.narlove.sportsnewsfeed.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import me.narlove.sportsnewsfeed.R;
import me.narlove.sportsnewsfeed.persistence.FeedItem;
import me.narlove.sportsnewsfeed.persistence.FeedItemsViewModel;
import me.narlove.sportsnewsfeed.persistence.FiltersViewModel;
import me.narlove.sportsnewsfeed.recycler.MainFeedViewAdapter;
import me.narlove.sportsnewsfeed.recycler.TopFeedViewAdapter;

public class FeedFragment extends Fragment {

    private RecyclerView mainRecycler;
    private RecyclerView.LayoutManager mainLayoutManager;
    private MainFeedViewAdapter mainFeedAdapter;
    private RecyclerView topRecycler;
    private RecyclerView.LayoutManager topLayoutManager;
    private TopFeedViewAdapter topFeedAdapter;
    private FeedItemsViewModel viewModel;
    private FiltersViewModel filterViewModel;
    private Button filterButton;

    public FeedFragment() {
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
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // architecture overview: Database -> DAO -> Repository -> ViewModel
        // repo is accessed exclusively by viewModel, do not attempt to access directly;
        // repo primarily handles distributing database sync methods to threads other than main
        // viewmodel exposes those "cleaned" business-logic methods to UI side of things.
        viewModel = new ViewModelProvider(requireActivity()).get(FeedItemsViewModel.class);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FiltersViewModel.class);

        // primarily for the main feed view
        mainFeedAdapter = new MainFeedViewAdapter(requireContext(), this::openCorrespondingDetailFragment);
        mainFeedAdapter.setHasStableIds(true);

        mainRecycler = view.findViewById(R.id.bookmarksRecyclerView);

        mainLayoutManager = new GridLayoutManager(requireContext(), 2);

        mainRecycler.setAdapter(mainFeedAdapter);
        mainRecycler.setLayoutManager(mainLayoutManager);

        // for the top feed view
        topFeedAdapter = new TopFeedViewAdapter(requireContext(), this::openCorrespondingDetailFragment);
        topFeedAdapter.setHasStableIds(true);

        topRecycler = view.findViewById(R.id.topView);

        topLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        topRecycler.setAdapter(topFeedAdapter);
        topRecycler.setLayoutManager(topLayoutManager);

        // observers
        LiveData<List<FeedItem>> filteredResults = Transformations.switchMap(filterViewModel.getCurrentCategories(),
                filters -> viewModel.getItemByCategories(filters)
        );

        // observe from filtered results should update whenever the filtered results do
        filteredResults.observe(getViewLifecycleOwner(), feedItems ->
        {
            mainFeedAdapter.submitList(feedItems);
        });

        viewModel.getAllItems().observe(getViewLifecycleOwner(), feedItems ->
        {
            topFeedAdapter.submitList(feedItems);
        });

        filterButton = view.findViewById(R.id.filterButton);

        filterButton.setOnClickListener(v -> {
            FilterDialogFragment filter = new FilterDialogFragment();
            filter.show(getParentFragmentManager(), "filter-dialog");
        });
    }

    private void openCorrespondingDetailFragment(FeedItem item)
    {
        DetailFragment frag = DetailFragment.newInstance(item.getPosterImageName(),
                item.getTitle(), item.getDescription(), item.getCategory(), item.isFavourite(), item.getId());;

        FragmentManager manager = getParentFragmentManager();

        manager.beginTransaction()
                .setReorderingAllowed(false)
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack(null)
                .commit();
    }
}