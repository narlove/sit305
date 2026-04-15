package me.narlove.calendar.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InvalidClassException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import me.narlove.calendar.custom.BrokenDateTime;
import me.narlove.calendar.custom.SingleItem;
import me.narlove.calendar.helpers.DatePickerFragment;
import me.narlove.calendar.R;
import me.narlove.calendar.helpers.EventsViewModel;
import me.narlove.calendar.helpers.EventsViewModelFactory;
import me.narlove.calendar.helpers.TimePickerFragment;
import me.narlove.calendar.listeners.CustomOnDateSetListener;
import me.narlove.calendar.listeners.CustomOnTimeSetListener;

public class CreateFragment extends Fragment {

    private Button pickTime;
    private Button pickDate;
    private Button submitButton;

    private EditText titleEntry;
    private EditText categoryEntry;
    private EditText locationEntry;

    private String selectedTime;
    private String selectedDate;

    private BrokenDateTime broken;

    public CreateFragment() {
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
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        pickTime = (Button) view.findViewById(R.id.pickTime);
        pickDate = (Button) view.findViewById(R.id.pickDate);
        submitButton = (Button) view.findViewById(R.id.submitButton);

        titleEntry = view.findViewById(R.id.eventTitle);
        categoryEntry = view.findViewById(R.id.eventCategory);
        locationEntry = view.findViewById(R.id.eventLocation);

        assignDatePicker();
        assignTimePicker();

        broken = new BrokenDateTime();
        EventsViewModelFactory factory = new EventsViewModelFactory(requireContext());
        EventsViewModel viewModel = new ViewModelProvider(requireActivity(), factory).get(EventsViewModel.class);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!broken.allFieldsValid())
                {
                    Toast.makeText(getContext(), "Please ensure you have picked a valid date AND time", Toast.LENGTH_SHORT).show();
                    return;
                }

                // we know this actually wont run because we already return if allfieldsvalid are false
                // but my IDE gets annoyed with me if this isn't here
                // welcome to java, the land of boilerplate
                boolean valid;

                try {
                    valid = requiredFieldsAreValid(titleEntry.getText().toString(), broken.getMergedDateObject());
                } catch (InvalidClassException e) {
                    throw new RuntimeException(e);
                }

                if (valid)
                {
                    // call add
                    try {
                        viewModel.addItem(new SingleItem(titleEntry.getText().toString(),
                                categoryEntry.getText().toString(),
                                locationEntry.getText().toString(),
                                broken.getMergedDateObject()));
                    } catch (InvalidClassException e) {
                        throw new RuntimeException(e);
                    }

                    // swap fragments
                    ((BottomNavigationView) requireActivity().findViewById(R.id.bottomNavigationView)).setSelectedItemId(R.id.dashboard);
                } else
                {
                    Toast.makeText(getContext(), "please ensure all fields are filled out correctly.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean requiredFieldsAreValid(String title, Date date)
    {
        return title != null && !title.trim().equalsIgnoreCase("")
                && date != null;
    }

    private void assignTimePicker()
    {
        // define here what we want to do when the time is set so that we can access variables
        // local here instead of passing it all in the constructor of timepickerfragment
        CustomOnTimeSetListener timeSetListener = new CustomOnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);

                String formattedTime = new SimpleDateFormat("HH:mm").format(cal.getTime());

                // show for user
                pickTime.setText(String.format("Selected: %s", formattedTime));

                broken.setHour(hourOfDay);
                broken.setMinute(minute);
            }
        };

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerFragment(timeSetListener).show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });
    }

    private void assignDatePicker()
    {
        CustomOnDateSetListener dateSetListener = new CustomOnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String formattedTime = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());

                pickDate.setText(String.format("Selected: %s", formattedTime));

                broken.setYear(year);
                broken.setMonth(month);
                broken.setDay(dayOfMonth);
            }
        };

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment(dateSetListener, true).show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
    }
}