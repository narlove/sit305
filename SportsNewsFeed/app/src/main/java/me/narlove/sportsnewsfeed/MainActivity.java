package me.narlove.sportsnewsfeed;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.narlove.sportsnewsfeed.fragments.BookmarksFragment;
import me.narlove.sportsnewsfeed.fragments.FeedFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        switchFragment(new FeedFragment());

        navView = findViewById(R.id.navView);

        navView.setOnItemSelectedListener(item ->
        {
            int itemId = item.getItemId();

            if (itemId == R.id.feedItem) {
                switchFragment(new FeedFragment());
            } else if (itemId == R.id.bookmarksItem) {
                switchFragment(new BookmarksFragment());
            } else {
                throw new IllegalStateException("item id is impossible");
            }

            return true;
        });
    }

    private void switchFragment(Fragment fragment)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}