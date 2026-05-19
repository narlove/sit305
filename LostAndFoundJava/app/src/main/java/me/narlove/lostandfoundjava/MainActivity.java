package me.narlove.lostandfoundjava;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.libraries.places.api.Places;

import me.narlove.lostandfoundjava.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

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

        if (!Places.isInitialized())
        {
            String apiKey = BuildConfig.PLACES_API_KEY;

            // Log an error if apiKey is not set.
            if (apiKey.isEmpty() || apiKey.equals("placeholder")) {
                finish();
                return;
            }

            Places.initializeWithNewPlacesApiEnabled(getApplicationContext(), apiKey);
        }

        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
            .replace(R.id.fragmentContainer, new HomeFragment())
            .commit();
    }
}