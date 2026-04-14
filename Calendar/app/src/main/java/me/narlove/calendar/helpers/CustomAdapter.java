package me.narlove.calendar.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.narlove.calendar.R;
import me.narlove.calendar.custom.SingleItem;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private SelectionTracker<Long> tracker;
    private EventsViewModel viewModel;
    private List<SingleItem> internalList = new ArrayList<>();

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

    public CustomAdapter(EventsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void updateData(List<SingleItem> newItems)
    {
        this.internalList.clear();
        this.internalList.addAll(newItems);
        this.notifyDataSetChanged();
    }

    public void setSelectionTracker(SelectionTracker<Long> tracker)
    {
        this.tracker = tracker;
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
        viewHolder.getItemTitle().setText(this.viewModel.getCurrentItemAtPosition(position).getTitle());
        viewHolder.getItemCategory().setText(this.viewModel.getCurrentItemAtPosition(position).getCategory());
        viewHolder.getItemLocation().setText(this.viewModel.getCurrentItemAtPosition(position).getLocation());
        viewHolder.getItemDate().setText(this.viewModel.getCurrentItemAtPosition(position).getDateString());

        if (tracker != null)
        {
            viewHolder.bind(this.viewModel.getCurrentItemAtPosition(position), tracker.isSelected((long) position));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.viewModel.getCurrentDatasetLength();
    }
}
