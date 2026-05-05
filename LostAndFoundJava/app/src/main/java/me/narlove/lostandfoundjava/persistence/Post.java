package me.narlove.lostandfoundjava.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import me.narlove.lostandfoundjava.utilities.PostCategory;
import me.narlove.lostandfoundjava.utilities.PostType;

@Entity(tableName = "posts")
public class Post {
    @PrimaryKey(autoGenerate = true)
    private long postId;
    private PostType postType;
    private PostCategory postCategory;
    private String postName;
    private String postContactPhone;
    private String postDescription;
    private String postDate;
    private String postLocation;
    // https://developer.android.com/develop/ui/compose/components/datepickers
    private String postUploadDate; // use android date picker and format as string using genericutils
    // image picker will provide uri of uploaded image
    private String imageUri;

    public Post(PostType postType,
                PostCategory postCategory,
                String postName,
                String postContactPhone,
                String postDescription,
                String postDate,
                String postLocation,
                String postUploadDate,
                String imageUri) {
        this.postType = postType;
        this.postCategory = postCategory;
        this.postName = postName;
        this.postContactPhone = postContactPhone;
        this.postDescription = postDescription;
        this.postDate = postDate;
        this.postLocation = postLocation;
        this.postUploadDate = postUploadDate;
        this.imageUri = imageUri;
    }


    // boilerplate on overriding the equals method
    // https://www.sitepoint.com/implement-javas-equals-method-correctly/
    // to be used in the customviewadapter diffcallback
    @Override
    @Ignore
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Post other = (Post) obj;

        return this.getPostId() == other.getPostId();
    }

    // code borrowed from official android developer website
    // https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
    @Ignore
    public static final DiffUtil.ItemCallback<Post> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Post>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Post oldPost, @NonNull Post newPost) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldPost.getPostId() == newPost.getPostId();
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull Post oldPost, @NonNull Post newPost) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldPost.equals(newPost);
                }
            };


    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public PostCategory getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(PostCategory postCategory) {
        this.postCategory = postCategory;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostContactPhone() {
        return postContactPhone;
    }

    public void setPostContactPhone(String postContactPhone) {
        this.postContactPhone = postContactPhone;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public String getPostUploadDate() {
        return postUploadDate;
    }

    public void setPostUploadDate(String postUploadDate) {
        this.postUploadDate = postUploadDate;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
