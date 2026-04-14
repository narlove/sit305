package me.narlove.calendar.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import me.narlove.calendar.helpers.CustomAdapter;
import me.narlove.calendar.helpers.CustomItemDetailsLookup;
import me.narlove.calendar.R;
import me.narlove.calendar.custom.SingleItem;

public class DashboardFragment extends Fragment {

    private Button deleteButton;

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

        // handle recyclerview and associates
        List<SingleItem> dataset = new ArrayList<>(Arrays.asList(
                new SingleItem("Finish 4.1p", "Uni", "7 Edward St, Greensborough",
                        new GregorianCalendar(2026, 4, 21, 15, 0, 0)
                                .getTime()),

                new SingleItem("Clean bedroom", "Home", "Home",
                        new GregorianCalendar(2026, 4, 22, 15, 0, 0)
                                .getTime())
        ));

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        CustomAdapter adapter = new CustomAdapter(dataset);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layoutManager);

        deleteButton = view.findViewById(R.id.delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tracker.hasSelection())
                {
                    Selection<Long> selection = tracker.getSelection();
                    selection.forEach(adapter::removeItemById);
                }
            }
        });
    }

}