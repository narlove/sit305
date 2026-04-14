package me.narlove.calendar.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.narlove.calendar.helpers.CustomAdapter;
import me.narlove.calendar.helpers.CustomItemDetailsLookup;
import me.narlove.calendar.R;
import me.narlove.calendar.custom.SingleItem;
import me.narlove.calendar.helpers.EventsViewModel;

public class DashboardFragment extends Fragment {

    private Button deleteButton;
    private Button editButton;

    public DashboardFragment() {
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
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    // need to perform logic relating to views here as per the following references
    // https://developer.android.com/reference/androidx/fragment/app/Fragment.html#onViewCreated(android.view.View,android.os.Bundle)
    // https://stackoverflow.com/questions/6495898/findviewbyid-in-fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventsViewModel viewModel = new ViewModelProvider(requireActivity()).get(EventsViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        CustomAdapter adapter = new CustomAdapter(viewModel);
        adapter.setHasStableIds(true);

        recyclerView.setAdapter(adapter);

        SelectionTracker<Long> tracker = new SelectionTracker.Builder<Long>(
                "mySelection",
                recyclerView,
                new StableIdKeyProvider(recyclerView),
                new CustomItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()
        )
                .withSelectionPredicate(SelectionPredicates.createSelectSingleAnything())
                .build();

        adapter.setSelectionTracker(tracker);

        viewModel.getItems().observe(getViewLifecycleOwner(), newItems ->
        {
            adapter.updateData(newItems);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layoutManager);

        deleteButton = view.findViewById(R.id.delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tracker.hasSelection())
                {
                    Selection<Long> selection = tracker.getSelection();
                    selection.forEach(viewModel::removeItemById);
                }
            }
        });

        editButton = view.findViewById(R.id.edit);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tracker.hasSelection())
                {
                    long id = DashboardFragment.getId(tracker);

                    SingleItem selectedItem = viewModel.getItemWithIdAtCurrentTime(id);

                    // need to swap to editfragment here passing details of selected item.
                    EditFragment frag = EditFragment.newInstance(selectedItem.getTitle(),
                            selectedItem.getCategory(), selectedItem.getLocation(), selectedItem.getDate(),
                            id);

                    frag.show(getParentFragmentManager(), "editpopup");
                }
            }
        });
    }

    private static long getId(SelectionTracker<Long> tracker) {
        long id = -1;

        Selection<Long> selection = tracker.getSelection();

        // this is a stupid work around because i have no idea what type selection
        // is but its like an array but i cant access it with square brackets?
        // i am not googling this i am just going with this method.
        for (Long pos : selection)
        {
            id = pos;
            break; // only one iteration as i limited selection capacity to 1
        }

        if (id == -1)
        {
            throw new RuntimeException("i have been working on this project way too long and i'm mostly confident this error wont raise");
        }
        return id;
    }

}