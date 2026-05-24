package me.narlove.chatbot.utilities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import me.narlove.chatbot.R;

public class GenericUtils {
    public static void switchFragment(Fragment current, Fragment next, boolean isAllowedBack)
    {
        FragmentManager manager = current.getParentFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction()
                .replace(R.id.fragContainer, next)
                .setReorderingAllowed(true);

        if (isAllowedBack)
        {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public static void switchFragment(FragmentActivity current, Fragment next, boolean isAllowedBack)
    {
        FragmentManager manager = current.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction()
                .replace(R.id.fragContainer, next)
                .setReorderingAllowed(true);

        if (isAllowedBack)
        {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
