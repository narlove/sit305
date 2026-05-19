package me.narlove.lostandfoundjava.fragments;

import static me.narlove.lostandfoundjava.utilities.GenericUtils.switchFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.security.cert.CertificateRevokedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import me.narlove.lostandfoundjava.R;
import me.narlove.lostandfoundjava.persistence.Post;
import me.narlove.lostandfoundjava.recycler.CustomAdapter;
import me.narlove.lostandfoundjava.recycler.OnRecyclerViewItemClickListener;
import me.narlove.lostandfoundjava.utilities.FilterOptions;
import me.narlove.lostandfoundjava.viewmodels.DatabaseViewModel;
import me.narlove.lostandfoundjava.viewmodels.FiltersViewModel;
import me.narlove.lostandfoundjava.viewmodels.SelectionsViewModel;

public class ViewFragment extends Fragment implements OnRecyclerViewItemClickListener {

    private DatabaseViewModel dbvm;
    private SelectionsViewModel selections;
    private Button setRadiusButton;
    private Button setFiltersButton;
    private Button homeButton;
    private FiltersViewModel filtersVm;
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;

    private FilterOptions selectedOptions = null;

    private final Map<String, LatLng> locationCache = new HashMap<>();

    public ViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        placesClient = Places.createClient(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view, container, false);
        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
        selections = new ViewModelProvider(requireActivity()).get(SelectionsViewModel.class);
        filtersVm = new ViewModelProvider(requireActivity()).get(FiltersViewModel.class);

        setFiltersButton = v.findViewById(R.id.setFiltersButton);
        homeButton = v.findViewById(R.id.homeButton);
        setRadiusButton = v.findViewById(R.id.setRadiusButton);

        CustomAdapter adapter = new CustomAdapter(requireContext(), this);
        adapter.setHasStableIds(true);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = v.findViewById(R.id.viewRecycler);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);

        this.setupCombinedDataStream(adapter);

        setFiltersButton.setOnClickListener(clicked ->
        {
            FilterDropdownDialogFragment filter = new FilterDropdownDialogFragment();
            filter.show(getParentFragmentManager(), "filter-dialog");
        });

        setRadiusButton.setOnClickListener(clicked ->
        {
            RadiusFragment frag = new RadiusFragment();
            frag.show(getParentFragmentManager(), "radius-dialog");
        });

        homeButton.setOnClickListener(clicked ->
        {
            switchFragment(this, new HomeFragment(), true);
        });

        return v;
    }

    private void setupCombinedDataStream(CustomAdapter adapter) {
        LiveData<List<Post>> dbResults = Transformations.switchMap(selections.getCurrentSelection(),
                selection -> dbvm.getPostsByCategoryOfType(selection.getCategories(), selection.getTypes())
        );

        MediatorLiveData<List<Post>> finalDataStream = new MediatorLiveData<>();

        Runnable updateTrigger = () -> {
            List<Post> posts = dbResults.getValue();
            FilterOptions options = filtersVm.getFilterOptions().getValue();
            combineAndFilter(posts, options, adapter);
        };

        finalDataStream.addSource(dbResults, posts -> updateTrigger.run());

        finalDataStream.addSource(filtersVm.getFilterOptions(), options -> updateTrigger.run());

        finalDataStream.observe(getViewLifecycleOwner(), posts -> {
            // do i need to have this here? i dont use any content for it anyway
        });
    }

    private void combineAndFilter(List<Post> posts, FilterOptions options, CustomAdapter adapter) {
        if (posts == null) return;

        if (options == null || !options.isEnabled() || !hasLocationPermission()) {
            // create new object, therefore new memory, therefore forces a refresh
            // this will solve the problem of not adjusting posts correctly when disabling radius
            adapter.submitList(new ArrayList<>(posts));
            return;
        }

        try
        {
            fusedLocationClient.getLastLocation().addOnSuccessListener(userLocation -> {
                // have to recheck in case user changed values while we were calling
                FilterOptions currentOptions = filtersVm.getFilterOptions().getValue();

                if (currentOptions == null || !currentOptions.isEnabled())
                {
                    adapter.submitList(new ArrayList<>(posts));
                    return;
                }

                if (userLocation == null)
                {
                    adapter.submitList(posts);
                    return;
                }

                filterPostsByDistance(posts, userLocation, options.getRadius(), adapter);
            });
        } catch (SecurityException ex)
        {
            adapter.submitList(new ArrayList<>(posts));
        }
    }

    private boolean hasLocationPermission()
    {
        return ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void filterPostsByDistance(List<Post> posts, Location userLoc, int radius, CustomAdapter adapter) {
        List<Post> validPosts = new ArrayList<>();
        AtomicInteger processedCount = new AtomicInteger(0);

        for (Post post : posts) {
            String placeId = post.getPostPlaceId();

            // minimise api calls
            if (locationCache.containsKey(placeId)) {
                checkDistanceAndAdd(post, locationCache.get(placeId), userLoc, radius, validPosts);
                // logic works by
                if (processedCount.incrementAndGet() == posts.size()) adapter.submitList(validPosts);
                continue;
            }

            // fetch from api and cache
            FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, Arrays.asList(Place.Field.LOCATION));
            placesClient.fetchPlace(request).addOnSuccessListener(resp -> {
                LatLng latLng = resp.getPlace().getLocation();
                if (latLng != null) {
                    locationCache.put(placeId, latLng);
                    checkDistanceAndAdd(post, latLng, userLoc, radius, validPosts);
                }
                if (processedCount.incrementAndGet() == posts.size()) adapter.submitList(new ArrayList<>(validPosts));
            }).addOnFailureListener(e -> {
                if (processedCount.incrementAndGet() == posts.size()) adapter.submitList(new ArrayList<>(validPosts));
            });
        }
    }

    private void checkDistanceAndAdd(Post post, LatLng target, Location userLoc, int radius, List<Post> list) {
        Location targetLoc = new Location("");
        targetLoc.setLatitude(target.latitude);
        targetLoc.setLongitude(target.longitude);
        // separate var so i can use debugger
        float distance = userLoc.distanceTo(targetLoc);
        if (distance <= radius) {
            list.add(post);
        }
    }

    // on a recyclerview item click
    @Override
    public void onItemClick(Post clicked) {
        DetailFragment fragment = DetailFragment.newInstance(
                clicked.getPostName(),
                clicked.getPostContactPhone(),
                clicked.getPostDescription(),
                clicked.getPostDate(),
                clicked.getPostPlaceId(),
                clicked.getPostUploadDate(),
                clicked.getImageUri()
        );

        switchFragment(this, fragment, true);
    }
}