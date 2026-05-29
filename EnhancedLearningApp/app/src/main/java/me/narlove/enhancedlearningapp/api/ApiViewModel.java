package me.narlove.enhancedlearningapp.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import me.narlove.enhancedlearningapp.persistence.datatypes.Task;

public class ApiViewModel extends ViewModel {
    // these are unrelated, stored in teh same view model for conciseness
    private MutableLiveData<List<Task>> tasksList = new MutableLiveData<>();
    private MutableLiveData<Integer> tasksCount = new MutableLiveData<>();

    public ApiViewModel()
    {
        tasksList.setValue(new ArrayList<>()); // set to empty arraylist
        tasksCount.setValue(0);
    }

    public MutableLiveData<List<Task>> getTasksList() {
        return tasksList;
    }

    public void setTasksList(List<Task> tasksList) {
        this.tasksList.setValue(tasksList);
    }

    public MutableLiveData<Integer> getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(int tasksCount) {
        this.tasksCount.setValue(tasksCount);
    }
}
