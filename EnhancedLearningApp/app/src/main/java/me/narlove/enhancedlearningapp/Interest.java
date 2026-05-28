package me.narlove.enhancedlearningapp;

import me.narlove.enhancedlearningapp.utilities.GenericUtils;
import me.narlove.enhancedlearningapp.utilities.InterestConversionHandler;

public enum Interest {
    ALGORITHMS,
    DATA_STRUCTURES,
    WEB_DEVELOPMENT,
    TESTING;

    public static String getFrontendCompatible(Interest interest)
    {
        return GenericUtils.toTitleCase(
                InterestConversionHandler.interestToString(interest)
        );
    }

    public static Interest getInterestFromCompatible(String str)
    {
        if (str == null) throw new IllegalArgumentException("string cannot be null");

        return InterestConversionHandler.stringToInterest(
                str.toUpperCase().replace(" ", "_")
        );
    }
}
