package me.narlove.enhancedlearningapp.utilities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import me.narlove.enhancedlearningapp.R;

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
        else
        {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    public static String toTitleCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder titleCase = new StringBuilder(str.length());
        boolean nextTitleCase = true;

        for (char c : str.toCharArray()) {
            if (Character.isSpaceChar(c) || c == '_') {
                nextTitleCase = true;
                titleCase.append(' ');
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
                titleCase.append(c);
            } else {
                c = Character.toLowerCase(c);
                titleCase.append(c);
            }
        }

        return titleCase.toString().trim();
    }
}
