package me.narlove.calendar.helpers;

import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import org.jspecify.annotations.NonNull;

public class CustomItemDetailsLookup extends ItemDetailsLookup<Long> {

    private final RecyclerView recycler;

    public CustomItemDetailsLookup(RecyclerView recycler)
    {
        this.recycler = recycler;
    }

    @Override
    public ItemDetailsLookup.ItemDetails<Long> getItemDetails(@NonNull MotionEvent event) {
        View view = recycler.findChildViewUnder(event.getX(), event.getY());

        if (view != null)
        {
            return ((CustomAdapter.ViewHolder) recycler.getChildViewHolder(view)).getItemDetails();
        }

        return null;
    }
}
