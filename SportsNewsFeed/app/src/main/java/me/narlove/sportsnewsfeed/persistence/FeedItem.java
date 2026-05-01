package me.narlove.sportsnewsfeed.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import me.narlove.sportsnewsfeed.utilities.Category;

@Entity(tableName = "items")
public class FeedItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private boolean isFavourite;
    private String thumbnailImageName;
    private String posterImageName;
    private String title;
    private String description;
    private Category category;

    public FeedItem(int id, boolean isFavourite, String thumbnailImageName, String posterImageName, String title, String description, Category category) {
        this.id = id;
        this.thumbnailImageName = thumbnailImageName;
        this.isFavourite = isFavourite;
        this.posterImageName = posterImageName;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    // boilerplate on overriding the equals method
    // https://www.sitepoint.com/implement-javas-equals-method-correctly/
    // to be used in the customviewadapter diffcallback
    @Override
    @Ignore
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        FeedItem other = (FeedItem) obj;

        return this.id == other.id;
    }

    // code borrowed from official android developer website
    // https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
    @Ignore
    public static final DiffUtil.ItemCallback<FeedItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<FeedItem>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull FeedItem oldUser, @NonNull FeedItem newUser) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldUser.getId() == newUser.getId();
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull FeedItem oldUser, @NonNull FeedItem newUser) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldUser.equals(newUser);
                }
            };

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnailImageName() {
        return thumbnailImageName;
    }

    public void setThumbnailImageName(String thumbnail) {
        this.thumbnailImageName = thumbnail;
    }

    public String getPosterImageName() {
        return posterImageName;
    }

    public void setPosterImageName(String posterImageName) {
        this.posterImageName = posterImageName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
