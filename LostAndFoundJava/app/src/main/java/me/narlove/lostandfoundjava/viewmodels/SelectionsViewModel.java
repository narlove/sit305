package me.narlove.lostandfoundjava.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.narlove.lostandfoundjava.utilities.PostCategory;
import me.narlove.lostandfoundjava.utilities.PostSelection;
import me.narlove.lostandfoundjava.utilities.PostType;

public class SelectionsViewModel extends ViewModel {
    private final MutableLiveData<PostSelection> selection = new MutableLiveData<>();

    public SelectionsViewModel() {
        // set as all possible combinations
        updateCurrentSelection(
                new ArrayList<>(Arrays.asList(PostCategory.values())),
                new ArrayList<>(Arrays.asList(PostType.values())))
        ;
    }

    public LiveData<PostSelection> getCurrentSelection()
    {
        return selection;
    }

    public void updateCurrentSelection(List<PostCategory> cats, List<PostType> types)
    {
        selection.setValue(new PostSelection(cats, types));
    }

    public void updateCurrentSelection(PostSelection sel)
    {
        selection.setValue(sel);
    }

}
