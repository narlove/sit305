package me.narlove.lostandfoundjava.utilities;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.narlove.lostandfoundjava.R;

public class GenericUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static void switchFragment(Fragment current, Fragment next, boolean shouldAllowBack)
    {
        FragmentManager manager = current.getParentFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction()
                .setReorderingAllowed(false)
                .replace(R.id.fragmentContainer, next);

        if (shouldAllowBack)
        {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    // will have to create a date formatting function and vice versa here i think
    @NotNull
    public static String formatDateAsString(@NotNull Date date)
    {
        return DATE_FORMAT.format(date);
    }

    // returns null on failure
    @Nullable
    public static Date parseStringToDate(@NotNull String str)
    {
        try
        {
            return DATE_FORMAT.parse(str);
        }
        catch (ParseException err)
        {
            return null;
        }
    }

    // for specifically handling upload Timestamp.
    // this should be it's own Timestamp class. it's not.
    @NotNull
    public static String formatTimestampAsString(@NotNull Date date)
    {
        return TIMESTAMP_FORMAT.format(date);
    }

    // returns null on failure
    @Nullable
    public static Date parseStringToTimestamp(@NotNull String str)
    {
        try
        {
            return TIMESTAMP_FORMAT.parse(str);
        }
        catch (ParseException err)
        {
            return null;
        }
    }
}
