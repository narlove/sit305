package me.narlove.sportsnewsfeed.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.narlove.sportsnewsfeed.R;
import me.narlove.sportsnewsfeed.persistence.FeedItem;
import me.narlove.sportsnewsfeed.persistence.FeedItemsViewModel;
import me.narlove.sportsnewsfeed.recycler.MainFeedViewAdapter;
import me.narlove.sportsnewsfeed.utilities.Category;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "posterImageName";
    private static final String ARG_PARAM2 = "title";
    private static final String ARG_PARAM3 = "description";
    private static final String ARG_PARAM4 = "category";
    private static final String ARG_PARAM5 = "isFavourite";
    // id used only for update as know an executor handles that
    // otherwise NO DATABASE METHODS ON MAIN THREAD
    private static final String ARG_PARAM6 = "id";

    private String posterImageName;
    private String title;
    private String description;
    private Category category;
    private boolean isFavourite;
    private long id;

    private ImageView poster;
    private TextView vTitle;
    private TextView vDescription;
    private CheckBox vIsFavourite;

    private RecyclerView recycler;
    private RecyclerView.LayoutManager layout;
    private MainFeedViewAdapter adapter;

    private FeedItemsViewModel viewModel;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(String posterImageName, String title,
                                             String description, Category category,
                                             boolean isFavourite, long id) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, posterImageName);
        args.putString(ARG_PARAM2, title);
        args.putString(ARG_PARAM3, description);
        args.putString(ARG_PARAM4, category.toString());
        args.putBoolean(ARG_PARAM5, isFavourite);
        args.putLong(ARG_PARAM6, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            posterImageName = getArguments().getString(ARG_PARAM1);
            title = getArguments().getString(ARG_PARAM2);
            description = getArguments().getString(ARG_PARAM3);
            category = Category.valueOf(getArguments().getString(ARG_PARAM4));
            isFavourite = getArguments().getBoolean(ARG_PARAM5);
            id = getArguments().getLong(ARG_PARAM6);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(FeedItemsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        poster = v.findViewById(R.id.itemThumbnail);
        vTitle = v.findViewById(R.id.itemTitle);
        vDescription = v.findViewById(R.id.itemDescription);
        vIsFavourite = v.findViewById(R.id.favouriteCheckbox);

        vTitle.setText(title);
        vDescription.setText(description);
        // make sure it aligns with what the user knows
        vIsFavourite.setChecked(isFavourite);

        System.out.println(posterImageName);

        if (posterImageName == null)
        {
            poster.setImageResource(R.drawable.placeholder_image);
        }
        else
        {
            // load using glide
            Glide.with(requireContext()).load(posterImageName)
                    .into(poster);
        }

        // set up favourite checkbox functionality to actually be responsive
        vIsFavourite.setOnCheckedChangeListener((btn, isChecked) ->
        {
            // only database methods allowed are WRITE methods because repo auto handles
            // assigning to executor
            viewModel.setFavouriteById(id, isChecked);
        });

        // set up related recycler view
        // primarily for the main feed view
        adapter = new MainFeedViewAdapter(requireContext(), this::openCorrespondingDetailFragment);
        adapter.setHasStableIds(true);

        // lol somehow accidentally ended up with primaryRecyclerView referring to two recyclers depending on context
        recycler = v.findViewById(R.id.bookmarksRecyclerView);

        layout = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layout);

        viewModel.getItemByCategory(category).observe(getViewLifecycleOwner(), feedItems ->
        {
            adapter.submitList(feedItems);
        });

        return v;
    }

    private void openCorrespondingDetailFragment(FeedItem item)
    {
        DetailFragment frag = DetailFragment.newInstance(item.getPosterImageName(),
                item.getTitle(), item.getDescription(), item.getCategory(), item.isFavourite(), item.getId());

        FragmentManager manager = getParentFragmentManager();

        manager.beginTransaction()
                .setReorderingAllowed(false)
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack(null)
                .commit();
    }
}