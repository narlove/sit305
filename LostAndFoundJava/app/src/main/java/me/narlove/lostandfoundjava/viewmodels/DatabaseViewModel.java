package me.narlove.lostandfoundjava.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import me.narlove.lostandfoundjava.persistence.Post;
import me.narlove.lostandfoundjava.persistence.Repository;
import me.narlove.lostandfoundjava.utilities.PostCategory;
import me.narlove.lostandfoundjava.utilities.PostType;

public class DatabaseViewModel extends AndroidViewModel {
    private final Repository repo;
    // shortened the name down from allUsersWithPlaylistEntries but if it becomes too hard to read
    // or to comprehend i will change it back
    private final LiveData<List<Post>> posts;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);

        repo = new Repository(application);
        posts = repo.getAllPosts();
    }

    public LiveData<List<Post>> getAllPosts()
    {
        return this.posts;
    }

    public LiveData<List<Post>> getPostsByCategory(PostCategory category)
    {
        return this.repo.getPostsByCategory(category);
    }

    public LiveData<List<Post>> getPostsByCategoryOfType(List<PostCategory> category, List<PostType> type)
    {
        return this.repo.getPostsByCategoryOfType(category, type);
    }

    public void insert(Post post)
    {
        repo.insert(post);
    }

    public void delete(Post post)
    {
        repo.delete(post);
    }
}
