package me.narlove.calendar.helpers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EventsViewModelFactory implements ViewModelProvider.Factory {
    private final Context param;

    public EventsViewModelFactory(Context param) {
        this.param = param;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EventsViewModel.class)) {
            return (T) new EventsViewModel(param);
        }
        throw new IllegalArgumentException("unknown viewmodel");
    }
}