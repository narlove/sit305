package me.narlove.lostandfoundjava.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.narlove.lostandfoundjava.utilities.FilterOptions;
import me.narlove.lostandfoundjava.utilities.PostCategory;
import me.narlove.lostandfoundjava.utilities.PostSelection;
import me.narlove.lostandfoundjava.utilities.PostType;

public class FiltersViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> radius = new MutableLiveData<>(100);

    private final MediatorLiveData<FilterOptions> options = new MediatorLiveData<>();

    public FiltersViewModel() {
        options.addSource(isEnabled, val -> updateOptions());
        options.addSource(radius, val -> updateOptions());
    }

    private void updateOptions() {
        options.setValue(new FilterOptions(isEnabled.getValue(), radius.getValue()));
    }

    public LiveData<FilterOptions> getFilterOptions() {
        return options;
    }

    public void setIsEnabled(boolean val) {
        isEnabled.setValue(val);
    }

    public void setRadius(int val) {
        radius.setValue(val);
    }
}