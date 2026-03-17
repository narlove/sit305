package me.narlove.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FinishActivity extends AppCompatActivity {

    TextView congratsLabel;
    TextView score;

    Button restartButton;
    Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finish);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent passedIntent = getIntent();
        String username = passedIntent.getStringExtra("username");

        int wrongQuestions = passedIntent.getIntExtra("wrongQuestions", -1);
        int totalQuestions = passedIntent.getIntExtra("totalQuestions", -1);

        if (wrongQuestions == -1 || totalQuestions == -1 || wrongQuestions > totalQuestions)
        {
            throw new IllegalArgumentException("identifying the content of the finish activity failed");
        }

        congratsLabel = findViewById(R.id.congratsLabel);
        score = findViewById(R.id.score);

        restartButton = findViewById(R.id.restartButton);
        finishButton = findViewById(R.id.finishButton);

        finishButton.setOnClickListener(v -> finishCallback(v));
        restartButton.setOnClickListener(v -> restartCallback(v, username));

        congratsLabel.setText("Congratulations, " + username + "!");
        score.setText(String.valueOf(totalQuestions - wrongQuestions) + "/" + String.valueOf(totalQuestions));
    }

    private void finishCallback(View v)
    {
        finishAffinity();
    }

    private void restartCallback(View v, String oldUser)
    {
        Intent empty = new Intent(this, LandingActivity.class);

        empty.putExtra("username", oldUser);

        startActivity(empty);
        finish();
    }
}