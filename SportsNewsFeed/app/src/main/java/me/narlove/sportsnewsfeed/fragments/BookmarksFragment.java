package me.narlove.sportsnewsfeed.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.narlove.sportsnewsfeed.R;
import me.narlove.sportsnewsfeed.persistence.FeedItem;
import me.narlove.sportsnewsfeed.persistence.FeedItemsViewModel;
import me.narlove.sportsnewsfeed.recycler.MainFeedViewAdapter;

public class BookmarksFragment extends Fragment {

    private FeedItemsViewModel viewModel;
    private RecyclerView recycler;
    private MainFeedViewAdapter adapter;
    private RecyclerView.LayoutManager layout;

    public BookmarksFragment() {
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
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(FeedItemsViewModel.class);

        recycler = view.findViewById(R.id.bookmarksRecyclerView);

        adapter = new MainFeedViewAdapter(requireContext(), this::openCorrespondingDetailFragment);
        adapter.setHasStableIds(true);

        layout = new GridLayoutManager(requireContext(), 2);

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layout);

        // observers
        viewModel.getFavouriteItems().observe(getViewLifecycleOwner(), items ->
        {
            adapter.submitList(items);
        });
    }

    // copied directly from feed fragment, am not making it public as fragments
    // should not rely on one another
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