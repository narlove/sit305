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

    public void replaceItem(Long id, SingleItem newItem)
    {
        this.removeItemById(id);
        this.addItem(newItem);

        // dont need to set items because calling our helper methods will do that for us
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

    public SingleItem getItemCurrentlyAtPosition(int position)
    {
        List<SingleItem> cur = items.getValue();

        if (cur == null)
        {
            throw new IllegalStateException("the list should never be null, this behaviour is not handled");
        }

        return cur.get(position);
    }

    public SingleItem getItemWithIdAtCurrentTime(long id)
    {
        return getItemCurrentlyAtPosition((int) id);
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
