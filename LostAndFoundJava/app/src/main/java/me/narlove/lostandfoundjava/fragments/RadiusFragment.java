package me.narlove.lostandfoundjava.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import me.narlove.lostandfoundjava.R;
import me.narlove.lostandfoundjava.utilities.FilterOptions;
import me.narlove.lostandfoundjava.viewmodels.FiltersViewModel;

public class RadiusFragment extends DialogFragment {

    private SeekBar radiusSeekBar;
    private Button submitButton;
    private Switch enabledSwitch;
    private int selectedRadius = -1;
    private FiltersViewModel filtersVm;
    private boolean passEnabled;

    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;
    private ActivityResultLauncher<String[]> requestLocationPermissions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        passEnabled = false;

        requestLocationPermissions =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted ->
                {
                    if (isGranted.containsValue(false))
                    {
                        // this function will only be called once view is created so i can reference
                        // view objects
                        Toast.makeText(requireContext(), "Cannot sort items" +
                                "by location because you have not opted to share your location", Toast.LENGTH_LONG).show();
                        enabledSwitch.setChecked(false);
                        passEnabled = false;
                    }
                    else
                    {
                        passEnabled = true;
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_radius, container, false);

        filtersVm = new ViewModelProvider(requireActivity()).get(FiltersViewModel.class);

        radiusSeekBar = v.findViewById(R.id.radiusSeekBar);
        submitButton = v.findViewById(R.id.confirmButton);
        enabledSwitch = v.findViewById(R.id.enabledSwitch);

        FilterOptions currentOptions = filtersVm.getFilterOptions().getValue();
        if (currentOptions != null) {
            passEnabled = currentOptions.isEnabled();
            selectedRadius = currentOptions.getRadius();

            enabledSwitch.setChecked(passEnabled);

            // convert radius back to progress (0-100)
            int progress = ((selectedRadius - 100) * 100) / (100000 - 100);
            radiusSeekBar.setProgress(progress);
        }

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // progress is given from 0-100, like a percentage
                // we assume min is 100m and maximum is 100km (10,000m)
                int newRange = 100000 - 100;
                selectedRadius = (((progress) * newRange) / 100) + 100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        enabledSwitch.setOnCheckedChangeListener((btn, checked) ->
        {
            if (!checked)
            {
                passEnabled = false;
                return;
            }

            // below this we are checked, this next for loop is just for if we have perms
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                passEnabled = true;
            }
            else
            {
                // request access to both permissions
                requestLocationPermissions.launch(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION });
            }
        });

        submitButton.setOnClickListener(clicked ->
        {
            if (selectedRadius == -1) selectedRadius = 100;

            filtersVm.setRadius(selectedRadius);
            filtersVm.setIsEnabled(passEnabled);

            Toast.makeText(requireContext(), String.format("Radius is enabled: %b, set radius to: %d", passEnabled, selectedRadius), Toast.LENGTH_LONG).show();

            dismiss();
        });

        return v;
    }
}