package me.narlove.calendar.listeners;

import android.widget.TimePicker;

public interface CustomOnTimeSetListener {
    void onTimeSet(TimePicker view, int hourOfDay, int minute);
}
