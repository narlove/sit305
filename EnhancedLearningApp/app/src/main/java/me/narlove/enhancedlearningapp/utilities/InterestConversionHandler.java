package me.narlove.enhancedlearningapp.utilities;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.narlove.enhancedlearningapp.Interest;

public class InterestConversionHandler {
    private static final String SEPARATOR = ",";

    @TypeConverter
    public static String interestToString(Interest interest)
    {
        if (interest == null) throw new IllegalArgumentException("interest should not be null");

        return interest.toString();
    }

    @TypeConverter
    public static Interest stringToInterest(String str)
    {
        if (str == null)
        {
            throw new IllegalArgumentException("integer is not valid");
        }

        return Interest.valueOf(str);
    }

    @TypeConverter
    public static String interestListToString(List<Interest> list)
    {
        if (list == null) throw new IllegalArgumentException("list cannot be null");
        if (list.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++)
        {
            builder.append(list.get(i));
            builder.append(SEPARATOR);
        }

        builder.append(list.get(list.size() - 1));

        return builder.toString();
    }

    @TypeConverter
    public static List<Interest> stringToInterestList(String str)
    {
        if (str.isEmpty()) return new ArrayList<>();
        if (str.length() == 1) return Arrays.asList(Interest.valueOf(str));

        String[] splits = str.split(SEPARATOR);
        List<Interest> interests = new ArrayList<>();

        for (String split : splits) {
            interests.add(Interest.valueOf(split));
        }

        return interests;
    }
}
