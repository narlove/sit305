package me.narlove.calendar.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.narlove.calendar.R;
import me.narlove.calendar.custom.SingleItem;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    // store the data here
    private List<SingleItem> items;
    private SelectionTracker<Long> tracker;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemTitle;

        private final TextView itemCategory;
        private final TextView itemLocation;
        private final TextView itemDate;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            itemCategory = (TextView) view.findViewById(R.id.itemCategory);
            itemLocation = (TextView) view.findViewById(R.id.itemLocation);
            itemDate = (TextView) view.findViewById(R.id.itemDate);
        }

        /**
         * Bind a listener to this item so that when it's clicked it returns item.
         * Code inspired by: <a href="https://stackoverflow.com/questions/49969278/recyclerview-item-click-listener-the-right-way">Reaz Masud</a>
         * @param item The item class to return
         */
        public void bind(final SingleItem item, boolean isActivated)
        {
            itemView.setActivated(isActivated);
        }

        public TextView getItemTitle() {
            return itemTitle;
        }

        public TextView getItemLocation() {
            return itemLocation;
        }

        public TextView getItemCategory() {
            return itemCategory;
        }

        public TextView getItemDate() {
            return itemDate;
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails()
        {
            return new ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return getBindingAdapterPosition();
                }

                @Nullable
                @Override
                public Long getSelectionKey() {
                    return getItemId();
                }
            };
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(List<SingleItem> dataSet) {
        items = dataSet;
    }

    public void setSelectionTracker(SelectionTracker<Long> tracker)
    {
        this.tracker = tracker;
    }

    public void addItem(SingleItem item)
    {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void removeItemByPosition(int position)
    {
        items.remove(position);
        this.notifyItemRemoved(position + 1);
    }

    public void removeItemById(long position)
    {
        removeItemByPosition((int) position);
    }

    // required override
    // https://proandroiddev.com/a-guide-to-recyclerview-selection-3ed9f2381504
    // could possibly set to SQLite id once persistence layer set up
    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getItemTitle().setText(items.get(position).getTitle());
        viewHolder.getItemCategory().setText(items.get(position).getCategory());
        viewHolder.getItemLocation().setText(items.get(position).getLocation());
        viewHolder.getItemDate().setText(items.get(position).getDateString());

        if (tracker != null)
        {
            viewHolder.bind(items.get(position), tracker.isSelected((long) position));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}
