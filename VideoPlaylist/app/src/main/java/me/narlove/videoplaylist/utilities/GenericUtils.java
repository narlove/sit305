package me.narlove.videoplaylist.utilities;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.narlove.videoplaylist.R;

public class GenericUtils {
    private static String rexPattern =
            "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube(-nocookie)?\\.com|youtu\\.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|live\\/|v\\/)?)([\\w\\-]{11})((?:\\?|\\&)\\S+)?$";

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

    // regex borrowed from: https://stackoverflow.com/questions/19377262/regex-for-youtube-url
    public static boolean isValidYoutubeUrl(String url)
    {
        Pattern pattern = Pattern.compile(rexPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(url);

        return matcher.find();
    }

    @Nullable
    public static String extractVideoId(@NotNull String url)
    {
        // same regex pattern as above copied to here
        Pattern pattern = Pattern.compile(rexPattern);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find())
        {
            return matcher.group(6);
        }

        return null;
    }
}
