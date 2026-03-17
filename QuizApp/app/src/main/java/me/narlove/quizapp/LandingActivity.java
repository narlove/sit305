package me.narlove.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LandingActivity extends AppCompatActivity {

    EditText username;
    Button submit;

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

        Intent intent = getIntent();

        username = findViewById(R.id.usernameEntry);
        submit = findViewById(R.id.submit);

        // if we were passed the username in the restart intent, then we can show that
        // otherwise we'll let the user enter their own username.
        String usernameOrNull = intent.getStringExtra("username");
        if (usernameOrNull != null)
        {
            username.setText(usernameOrNull);
        }

        submit.setOnClickListener(v -> onSubmit(v));
    }

    private void onSubmit(View v)
    {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("username", username.getText().toString());

        Intent intentQuiz = setupQuiz(intent);

        startActivity(intentQuiz);
        finish();
    }

    // IF YOU WANT TO CHANGE THE QUIZ, EDIT THIS METHOD RIGHT HERE
    private Intent setupQuiz(Intent intent)
    {
        Bundle question1 = createQuestion(
                0,
                "What is the only food that never spoils?",
                "Peanut Butter",
                "Honey",
                "Jelly",
                'B'
        );

        Bundle question2 = createQuestion(
                1,
                "What is an eight sided shape called?",
                "Hexagon",
                "Pentagon",
                "Octagon",
                'C'
        );

        Bundle question3 = createQuestion(
                2,
                "What is the capital city of Australia?",
                "Canberra",
                "Sydney",
                "Melbourne",
                'A'
        );

        Bundle question4 = createQuestion(
                3,
                "What is the planet closest to the Sun?",
                "Mercury",
                "Venus",
                "Earth",
                'A'
        );

        Bundle question5 = createQuestion(
                4,
                "What is the largest organ in the human body?",
                "Heart",
                "Skin",
                "Lungs",
                'B'
        );

        // TO CREATE A QUESTION:
        // CREATE A NEW BUNDLE ABOVE AND CALL CREATEQUESTION
        // ADD THE BUNDLE TO THE INTENT USING PUTEXTRA BELOW
        // UPDATE THE TOTALQUESTIONS NUMBER UNDER THE METADATA SECTION.

        intent.putExtra("question0", question1);
        intent.putExtra("question1", question2);
        intent.putExtra("question2", question3);
        intent.putExtra("question3", question4);
        intent.putExtra("question4", question5);

        // metadata for keeping track of quiz game
        intent.putExtra("currentQuestion", 0);
        intent.putExtra("totalQuestions", 5);
        intent.putExtra("wrongQuestions", 0);

        return intent;
    }

    // DO NOT EDIT THIS METHOD UNLESS CHANGING THE NUMBER OF OPTIONS IN A QUESTION
    private Bundle createQuestion(int id, String question, String optionA, String optionB, String optionC, char answer)
    {
        Bundle q = new Bundle();

        q.putInt("id", id);
        q.putString("question", question);
        q.putString("optionA", optionA);
        q.putString("optionB", optionB);
        q.putString("optionC", optionC);
        q.putChar("answer", answer);

        return q;
    }
}