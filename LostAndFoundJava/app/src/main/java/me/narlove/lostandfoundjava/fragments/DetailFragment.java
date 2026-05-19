package me.narlove.lostandfoundjava.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;

import me.narlove.lostandfoundjava.R;

// this class is so bad but i cant perform database lookup on the main ui thread and i couldn't
// be bothered just passing the idea and then making a callback function.

public class DetailFragment extends Fragment {

    private static final String ARG_POSTNAME = "postname";
    private static final String ARG_POSTCONTACTPHONE = "postcontactphone";
    private static final String ARG_POSTDESCRIPTION = "postdescription";
    private static final String ARG_POSTDATE = "postdate";
    private static final String ARG_POSTPLACEID = "postplaceid";
    private static final String ARG_POSTUPLOADDATE = "postuploaddate";
    private static final String ARG_IMAGEURI = "imageuri";

    private String postName;
    private String postContactPhone;
    private String postDescription;
    private String postDate;
    private String postPlaceId;
    private String postUploadDate;
    private String imageUri;

    private PlacesClient placesClient;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(String postName,
                                             String postContactPhone,
                                             String postDescription,
                                             String postDate,
                                             String postLocationName,
                                             String postUploadDate,
                                             String imageUri) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POSTNAME, postName);
        args.putString(ARG_POSTCONTACTPHONE, postContactPhone);
        args.putString(ARG_POSTDESCRIPTION, postDescription);
        args.putString(ARG_POSTDATE, postDate);
        args.putString(ARG_POSTPLACEID, postLocationName);
        args.putString(ARG_POSTUPLOADDATE, postUploadDate);
        args.putString(ARG_IMAGEURI, imageUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postName = getArguments().getString(ARG_POSTNAME);
            postContactPhone = getArguments().getString(ARG_POSTCONTACTPHONE);
            postDescription = getArguments().getString(ARG_POSTDESCRIPTION);
            postDate = getArguments().getString(ARG_POSTDATE);
            postUploadDate = getArguments().getString(ARG_POSTUPLOADDATE);
            imageUri = getArguments().getString(ARG_IMAGEURI);
            postPlaceId = getArguments().getString(ARG_POSTPLACEID, null);
        }

        placesClient = Places.createClient(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        ((TextView) v.findViewById(R.id.fPostName)).setText(postName);
        ((TextView) v.findViewById(R.id.fPostContactPhone)).setText(postContactPhone);
        ((TextView) v.findViewById(R.id.fPostDescription)).setText(postDescription);
        ((TextView) v.findViewById(R.id.fPostDate)).setText(postDate);
        // get the place name
        if (postPlaceId != null) // else do not fill it with anything
        {
            ((TextView) v.findViewById(R.id.fPostLocation)).setText("loading...");
            FetchPlaceRequest request = FetchPlaceRequest.newInstance(postPlaceId,
                    Arrays.asList(Place.Field.DISPLAY_NAME));

            placesClient.fetchPlace(request)
                    .addOnSuccessListener(requireActivity(), res ->
                            ((TextView) v.findViewById(R.id.fPostLocation))
                                    .setText(res.getPlace().getDisplayName()));
        }

        ((TextView) v.findViewById(R.id.fPostUploadDate)).setText(postUploadDate);

        ImageView imageView = v.findViewById(R.id.fImage);
        Glide.with(requireContext()).load(imageUri).into(imageView);

        return v;
    }
}
