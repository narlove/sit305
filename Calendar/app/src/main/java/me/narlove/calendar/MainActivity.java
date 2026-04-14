package me.narlove.calendar;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.narlove.calendar.fragments.CreateFragment;
import me.narlove.calendar.fragments.DashboardFragment;
import me.narlove.calendar.helpers.CustomAdapter;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    CustomAdapter adapter;

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

        switchFragment(new DashboardFragment());

        navView = findViewById(R.id.bottomNavigationView);

        navView.setOnItemSelectedListener(item ->
        {
            int itemId = item.getItemId();

            if (itemId == R.id.create) {
                switchFragment(new CreateFragment());
            } else if (itemId == R.id.dashboard) {
                switchFragment(new DashboardFragment());
            } else {
                throw new IllegalStateException("item id should be one of create or dashboard");
            }

            return true;
        });
    }

    private void switchFragment(Fragment fragment)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}