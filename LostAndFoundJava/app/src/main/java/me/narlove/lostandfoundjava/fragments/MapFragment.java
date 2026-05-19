package me.narlove.lostandfoundjava.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;

import me.narlove.lostandfoundjava.R;
import me.narlove.lostandfoundjava.persistence.Post;
import me.narlove.lostandfoundjava.viewmodels.DatabaseViewModel;

public class MapFragment extends Fragment {

    private DatabaseViewModel dbvm;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationClient;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
            dbvm.getAllPosts().observe(getViewLifecycleOwner(), posts ->
            {
                for (Post post : posts)
                {
                    addMarkerById(post.getPostPlaceId(), googleMap, post.getPostName());
                }
            });

            LatLng location = new LatLng(-37.840935, 144.946457);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f));
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placesClient = Places.createClient(requireContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void addMarkerById(String placeId, GoogleMap map, String title)
    {
        if (placeId != null) // else do not fill it with anything
        {
            FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId,
                    Arrays.asList(Place.Field.DISPLAY_NAME, Place.Field.LOCATION));

            placesClient.fetchPlace(request)
                .addOnSuccessListener(requireActivity(), res ->
                {
                    LatLng location = res.getPlace().getLocation();

                    if (location == null) return;

                    map.addMarker(new MarkerOptions().position(location).title(title));
                });
        }
    }
}