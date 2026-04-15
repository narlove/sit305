package me.narlove.calendar.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
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
        if (newItems != null) {
            this.internalList.addAll(newItems);
        }

        this.notifyDataSetChanged();
    }

    public void setSelectionTracker(SelectionTracker<Long> tracker)
    {
        this.tracker = tracker;
    }

    // required override
    // https://proandroiddev.com/a-guide-to-recyclerview-selection-3ed9f2381504
    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= internalList.size()) {
            return RecyclerView.NO_ID;
        }

        return internalList.get(position).getId();
    }

    public ItemKeyProvider<Long> getItemKeyProvider() {
        return new ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_MAPPED) {
            @Nullable
            @Override
            public Long getKey(int position) {
                if (position < 0 || position >= internalList.size()) {
                    return null;
                }

                return internalList.get(position).getId();
            }

            @Override
            public int getPosition(@NonNull Long key) {
                for (int i = 0; i < internalList.size(); i++) {
                    if (internalList.get(i).getId() == key) {
                        return i;
                    }
                }

                return RecyclerView.NO_POSITION;
            }
        };
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

        SingleItem item = internalList.get(position);

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getItemTitle().setText(item.getTitle());
        viewHolder.getItemCategory().setText(item.getCategory());
        viewHolder.getItemLocation().setText(item.getLocation());
        viewHolder.getItemDate().setText(item.getDateString());

        if (tracker != null)
        {
            viewHolder.bind(item, tracker.isSelected(item.getId()));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return internalList.size();
    }
}
