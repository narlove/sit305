package me.narlove.calendar.helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import me.narlove.calendar.listeners.CustomOnDateSetListener;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private final CustomOnDateSetListener listener;
    private boolean shouldLimitMin;

    public DatePickerFragment(CustomOnDateSetListener listener, boolean shouldLimitMin)
    {
        this.listener = listener;
        this.shouldLimitMin = shouldLimitMin;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        listener.onDateSet(view, year, month, dayOfMonth);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(requireContext(), this, year, month, day);
        if (shouldLimitMin) dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        return dialog;
    }
}
