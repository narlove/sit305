package me.narlove.calendar.helpers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import me.narlove.calendar.custom.SingleItem;

public class EventsViewModel extends ViewModel {
    // now we're storing the data here so that it can be accessed between fragments
    private final MutableLiveData<List<SingleItem>> items = new MutableLiveData<>();

    public void addItem(SingleItem item)
    {
        List<SingleItem> cur = items.getValue();

        if (cur == null)
        {
            throw new IllegalStateException("the list should never be null, this behaviour is not handled");
        }

        cur.add(item);
        items.setValue(cur);
    }

    public void removeItemByPosition(int position)
    {
        List<SingleItem> cur = items.getValue();

        if (cur == null)
        {
            throw new IllegalStateException("the list should never be null, this behaviour is not handled");
        }

        cur.remove(position);
        items.setValue(cur);
    }

    public SingleItem getCurrentItemAtPosition(int position)
    {
        List<SingleItem> cur = items.getValue();

        if (cur == null)
        {
            throw new IllegalStateException("the list should never be null, this behaviour is not handled");
        }

        return cur.get(position);
    }

    public int getCurrentDatasetLength()
    {
        List<SingleItem> cur = items.getValue();

        if (cur == null)
        {
            throw new IllegalStateException("the list should never be null, this behaviour is not handled");
        }

        return cur.size();
    }

    /**
     * Will override all current values in the list. Intended for use to initially populate list in debug.
     * @param overrideItems Items to override the current data with.
     */
    public void setItems(List<SingleItem> overrideItems)
    {
        items.setValue(overrideItems);
    }

    public LiveData<List<SingleItem>> getItems()
    {
        return items;
    }

    public void removeItemById(long position)
    {
        removeItemByPosition((int) position);
    }
}
