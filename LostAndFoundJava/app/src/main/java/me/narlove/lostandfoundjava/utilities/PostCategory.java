package me.narlove.lostandfoundjava.utilities;

public enum PostCategory {
    WALLETS,
    PETS,
    ELECTRONICS;

    public static PostCategory fromSpinnerVal(String val)
    {
        for (PostCategory cat : PostCategory.values())
        {
            if (val.equalsIgnoreCase(cat.toString()))
            {
                return cat;
            }
        }

        return null;
    }
}
