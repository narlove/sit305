package me.narlove.lostandfoundjava.utilities;

import java.util.List;

// records not supported here
public class PostSelection {
    private List<PostCategory> categories;
    private List<PostType> types;

    public PostSelection(List<PostCategory> categories, List<PostType> types) {
        this.categories = categories;
        this.types = types;
    }

    public List<PostCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<PostCategory> categories) {
        this.categories = categories;
    }

    public List<PostType> getTypes() {
        return types;
    }

    public void setTypes(List<PostType> types) {
        this.types = types;
    }
}
