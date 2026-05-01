package me.narlove.sportsnewsfeed.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.narlove.sportsnewsfeed.utilities.Category;

public class FiltersViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();

    public FiltersViewModel(@NonNull Application application) {
        super(application);

        // set as all categories to begin with
        categories.setValue(new ArrayList<>(Arrays.asList(Category.values())));
    }

    public LiveData<List<Category>> getCurrentCategories()
    {
        return categories;
    }

    public void updateCurrentCategories(List<Category> updates)
    {
        categories.setValue(updates);
    }
}
