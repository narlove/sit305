package me.narlove.lostandfoundjava.utilities;

public enum PostType {
    LOST,
    FOUND;

    public static PostType fromSpinnerVal(String val)
    {
        for (PostType type : PostType.values())
        {
            if (val.equalsIgnoreCase(type.toString()))
            {
                return type;
            }
        }

        return null;
    }
}
