package me.narlove.lostandfoundjava.fragments;

import static android.location.Location.FORMAT_DEGREES;
import static me.narlove.lostandfoundjava.utilities.GenericUtils.formatTimestampAsString;
import static me.narlove.lostandfoundjava.utilities.GenericUtils.switchFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Preconditions;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.DatabaseView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.CircularBounds;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.SearchNearbyRequest;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.PlaceAutocomplete;
import com.google.android.libraries.places.widget.PlaceAutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.narlove.lostandfoundjava.BuildConfig;
import me.narlove.lostandfoundjava.R;
import me.narlove.lostandfoundjava.persistence.Post;
import me.narlove.lostandfoundjava.utilities.GenericUtils;
import me.narlove.lostandfoundjava.utilities.PostCategory;
import me.narlove.lostandfoundjava.utilities.PostType;
import me.narlove.lostandfoundjava.viewmodels.DatabaseViewModel;

// TODO: REFACTOR THIS FRAGMENT ESPECIALLY
// THE HANDLESUBMIT DOES TOO MANY THINGS
// ALSO CALLBACK FUNCTIONS COULD BE MADE INTO THEIR OWN PRIVATE METHODS
// THERE SHOULD BE A RECORD THAT ALLOWS US TO EASILY PASS ALL OF THESE VALUES BETWEEN FUNCTIONS
// TIMESTAMP SHOULD HAVE ITS OWN CLASS FOR EASILY HANDLING CONVERSIONS TO AND FROM STRING
// WE SHOULD PROBABLY MAKE A DATE HANDLER CLASS TOO, SO THAT WHEN WE INEVITABLY SWAP TO THE MODERN
// WAY OF USING DATES IN JAVA (INSTEAD OF THE DATE CLASS) THAT WE CAN DO IT EASILY.
public class CreateFragment extends Fragment {
    private RadioGroup postTypeRadioGroup;
    private EditText nameEntry;
    private EditText phoneEntry;
    private EditText descriptionEntry;
    private Button selectDateButton;
    private EditText locationEntry;
    private Button selectCurrentLocationButton;
    private Button selectPhotoButton;
    private Spinner categorySpinner;
    private Button submitButton;

    // may be null if not yet selected
    // parse this value to the submit callback function
    @Nullable
    private Date selectedDate;
    @Nullable
    private Uri selectedImageUri;
    private Place selectedPlace;

    private DatabaseViewModel dbvm;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private ActivityResultLauncher<String[]> requestLocationPermissions;
    private ActivityResultLauncher<Intent> placeAutocomplete;

    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;
    private PlacesClient placesClient;

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // got an error when attempting to register for activity result and was told
        // to perform the method call in oncreate
        pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                // selected image is nullable, we will complain when the user presses
                // submit if it's still null
                selectedImageUri = uri;

                // update button to indicate we've received photo and provide visual feedback
                // this section will error if it is evaluated at compile time
                // BUT IT DOESNT - IT IS EVALUATED AT RUNTIME AND THEREFORE WE'RE FINE
                if (selectedImageUri == null)
                {
                    selectPhotoButton.setText("Select image (none yet selected)");
                }
                else
                {
                    selectPhotoButton.setText("Select image (selected)");
                }
            });

        requestLocationPermissions =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted ->
                {
                    if (isGranted.containsValue(false))
                    {
                        // this function will only be called once view is created so i can reference
                        // view objects
                        Toast.makeText(requireContext(), "Cannot use 'Get current location' button because you have not" +
                                "accepted the location permission.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        assignCurrentLocation();
                    }
                });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // Create a new PlacesClient instance
        placesClient = Places.createClient(requireContext());

        placeAutocomplete = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();

                if (result.getResultCode() == PlaceAutocompleteActivity.RESULT_OK) {
                    // get prediction object
                    if (intent == null) return;

                    AutocompletePrediction prediction =
                            PlaceAutocomplete.getPredictionFromIntent(intent);

                    AutocompleteSessionToken sessionToken =
                            PlaceAutocomplete.getSessionTokenFromIntent(intent);

                    FetchPlaceRequest request =
                            FetchPlaceRequest.builder(prediction.getPlaceId(),
                                            Arrays.asList(Place.Field.DISPLAY_NAME,
                                                    Place.Field.ADDRESS_COMPONENTS,
                                                    Place.Field.ID,
                                                    Place.Field.LOCATION))
                                    .setSessionToken(sessionToken)
                                    .build();

                    placesClient.fetchPlace(request).addOnSuccessListener(requireActivity(),
                            res ->
                    {
                        selectedPlace = res.getPlace();
                        locationEntry.setText(selectedPlace.getDisplayName());
                    });
                }
            });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create, container, false);
        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);

        // ensure references are valid
        postTypeRadioGroup = v.findViewById(R.id.postTypeRadioGroup);
        nameEntry = v.findViewById(R.id.nameEntry);
        phoneEntry = v.findViewById(R.id.phoneEntry);
        descriptionEntry = v.findViewById(R.id.descriptionEntry);
        selectDateButton = v.findViewById(R.id.selectDate);
        locationEntry = v.findViewById(R.id.locationEntry);
        selectCurrentLocationButton = v.findViewById(R.id.selectCurrentLocation);
        selectPhotoButton = v.findViewById(R.id.selectImage);
        categorySpinner = v.findViewById(R.id.categoryEntry);
        submitButton = v.findViewById(R.id.submitButton);

        locationEntry.setFocusable(false);

        // populate category spinner
        List<String> enumValues = Stream.of(PostCategory.values())
                .map(val -> val.toString().toLowerCase())
                .collect(Collectors.toList());

        // code adapted from: https://developer.android.com/develop/ui/views/components/spinner#java
        ArrayAdapter<String> categorySpinnerAdapter =
                new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item,
                        enumValues);

        categorySpinner.setAdapter(categorySpinnerAdapter);

        // create datepicker
        selectDateButton.setOnClickListener(clicked ->
        {
            // get current date to parse as default value
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, y, m, d) ->
                {
                    selectedDate = new GregorianCalendar(y, m, d).getTime();

                    // format the date and change the text on the button to visually update user
                    String readableString = String.format("Select date (current: %s)",
                            GenericUtils.formatDateAsString(selectedDate));

                    selectDateButton.setText(readableString);
                },
                year, month, day);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        selectPhotoButton.setOnClickListener(clicked ->
        {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        selectCurrentLocationButton.setOnClickListener(clicked ->
        {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            {
                assignCurrentLocation();
            }
            else
            {
                // request access to both permissions
                requestLocationPermissions.launch(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION });
            }
        });

        locationEntry.setOnClickListener(clicked ->
        {
            LatLng center = new LatLng(-37.840935, 144.946457);
            CircularBounds circle = CircularBounds.newInstance(center, /* radius = */ 5000);

            // Build and launch the intent with fullscreen mode
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .setCountries(Arrays.asList("au"))
                    .setLocationBias(circle)
                    .build(requireContext());

            placeAutocomplete.launch(intent);
        });

        submitButton.setOnClickListener(clicked -> handleSubmit());

        return v;
    }

    private void assignCurrentLocation()
    {
        // proceed with getting the users current location, we have the permission.
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        CreateFragment.this.lastKnownLocation = location;

                        if (location != null)
                        {
                            LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            CircularBounds circle = CircularBounds.newInstance(userLatLng, 100);

                            List<Place.Field> placeFields = Arrays.asList(
                                    Place.Field.DISPLAY_NAME,
                                    Place.Field.ADDRESS_COMPONENTS,
                                    Place.Field.ID,
                                    Place.Field.LOCATION);

                            SearchNearbyRequest searchNearbyRequest = SearchNearbyRequest.builder(circle, placeFields)
                                    .setMaxResultCount(1)
                                    .build();

                            placesClient.searchNearby(searchNearbyRequest)
                                    .addOnSuccessListener(response -> {
                                        List<Place> places = response.getPlaces();
                                        // we use a for loop but really its just one place object
                                        for (Place place : places) {
                                            selectedPlace = place;
                                            locationEntry.setText("Your current location!");
                                        }
                                    })
                                    .addOnFailureListener(exception -> {
                                        if (exception instanceof ApiException) {
                                            Toast.makeText(requireContext(), "could not identify location name," +
                                                    " please enter using autocomplete", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });
    }

    private void handleSubmit()
    {
        // get all of the appropriate data
        int checkedPostTypeId = postTypeRadioGroup.getCheckedRadioButtonId();
        String postName = nameEntry.getText().toString();
        String phone = phoneEntry.getText().toString();
        String description = descriptionEntry.getText().toString();

        // already have access to, but we need to be careful about handling null
        // Date selectedDate
        // Uri selectedImageUri;

        PostCategory category = PostCategory.fromSpinnerVal(categorySpinner.getSelectedItem().toString());

        // perform validation
        // the only validation we want is that postName, postType, selectedDate, selectedPhoto, category cannot be null.
        if (checkedPostTypeId == -1 || areAnyNull(postName, selectedDate, selectedImageUri, category,
                selectedPlace))
        {
            Toast.makeText(requireContext(),
                    "One or more required values have been incorrectly entered or are empty. Please fix this. No changes have been saved.",
                    Toast.LENGTH_LONG).show();

            return;
        }

        // transform values into database applicable
        // database should auto handle converting enums
        PostType postType = checkedPostTypeId == R.id.lostRadio ? PostType.LOST : PostType.FOUND;
        // we check above if selected date is null, and we return, so we know it's not
        String safeDate = GenericUtils.formatDateAsString(selectedDate);
        String safeUri = String.valueOf(selectedImageUri);

        String uploadTimestamp = formatTimestampAsString(new Date());

        // use instance variable view model reference to add to the databaseselectedPhoto

        Post toInsert = new Post(postType, category, postName, phone, description,
                safeDate, selectedPlace.getId(), uploadTimestamp, safeUri);

        dbvm.insert(toInsert);

        // once user has submitted, transfer thru to view fragment so they can view their upload
        // they are not allowed to come back because it will re autopopulate their values
        // and i dont want that.
        switchFragment(this, new ViewFragment(), false);
    }

    private boolean areAnyNull(Object... vals)
    {
        for (Object val : vals)
        {
            if (val == null)
            {
                return true;
            }
        }

        return false;
    }
}