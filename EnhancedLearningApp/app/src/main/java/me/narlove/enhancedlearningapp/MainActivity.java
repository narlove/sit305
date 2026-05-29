package me.narlove.enhancedlearningapp;

import static me.narlove.enhancedlearningapp.utilities.GenericUtils.switchFragment;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import me.narlove.enhancedlearningapp.api.ApiViewModel;
import me.narlove.enhancedlearningapp.fragments.LoginFragment;
import me.narlove.enhancedlearningapp.persistence.MongoRepo;

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

        ApiViewModel vm = new ViewModelProvider(this).get(ApiViewModel.class);
        MongoRepo.assignViewModel(vm);

        switchFragment(this, new LoginFragment(), false);
    }
}