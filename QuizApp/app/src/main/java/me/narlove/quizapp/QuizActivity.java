package me.narlove.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuizActivity extends AppCompatActivity {

    TextView questionText;
    Button buttonA;
    Button buttonB;
    Button buttonC;

    Button submitOrNext;
    boolean submitted = false;
    Button targetButton = null;

    // progress info
    ProgressBar progressBar;
    TextView progressFraction;

    // not primitive as we need to know whether wasWrong was set or not
    Boolean wasWrong = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // assign instance variables to elements
        // these assignments need to be run before calling populatetext
        questionText = findViewById(R.id.question);
        buttonA = findViewById(R.id.buttonA);
        buttonB = findViewById(R.id.buttonB);
        buttonC = findViewById(R.id.buttonC);

        submitOrNext = findViewById(R.id.submitOrNext);

        progressBar = findViewById(R.id.progressBar);
        progressFraction = findViewById(R.id.progressFraction);

        Intent passedIntent = getIntent();

        // pull basic question identifying information
        int currentQuestionIndex = passedIntent.getIntExtra("currentQuestion", -1);
        int totalQuestions = passedIntent.getIntExtra("totalQuestions", -1);

        // lol added on this last one cause it kinda seemed like the place to do it, tho
        // not as "urgent" as the previous two grabs
        int wrongQuestions = passedIntent.getIntExtra("wrongQuestions", -1);

        if (currentQuestionIndex == -1 || totalQuestions == -1 ||
                currentQuestionIndex > totalQuestions - 1 || wrongQuestions == -1)
        {
            throw new IllegalArgumentException("a problem occurred while identifying the current question context");
        }

        // string concatenation to identify which bundle to pull
        String nameToExtract = "question".concat(String.valueOf(currentQuestionIndex));
        Bundle questionBundle = passedIntent.getBundleExtra(nameToExtract);

        if (questionBundle == null)
        {
            throw new IllegalArgumentException("a problem occurred with extracting current question details");
        }

        char correctChar = questionBundle.getChar("answer");

        populateText(questionBundle);
        setProgress(currentQuestionIndex, totalQuestions);

        // assign onclicks
        // has to be after the intent is pulled because i intend on passing the intent
        submitOrNext.setOnClickListener(v -> {
            // if they have not yet submitted their guess, they are clicking the submit button to
            // submit their guess
            // if they HAVE already submitted their guess, they are clicking the next button
            if (!submitted)
            {
                onSelect(correctChar);
            }
            else
            {
                onNext(passedIntent, currentQuestionIndex, totalQuestions, wrongQuestions);
            }
        });

        buttonA.setOnClickListener(v -> buttonCallback(v));
        buttonB.setOnClickListener(v -> buttonCallback(v));
        buttonC.setOnClickListener(v -> buttonCallback(v));
    }

    private void setAllButtonColor(int colorId)
    {
        buttonA.setBackgroundTintList(ColorStateList.valueOf(colorId));
        buttonB.setBackgroundTintList(ColorStateList.valueOf(colorId));
        buttonC.setBackgroundTintList(ColorStateList.valueOf(colorId));
    }

    private void buttonCallback(View v)
    {
        // if the user has already submitted a guess, do not accept changing the target any further
        // also generic make sure we have a button
        if (!(v instanceof Button) || submitted)
        {
            return;
        }

        targetButton = (Button) v;

        int purpleColor = ContextCompat.getColor(this, R.color.purple);
        int yellowColor = ContextCompat.getColor(this, R.color.yellow);

        setAllButtonColor(purpleColor);
        targetButton.setBackgroundTintList(ColorStateList.valueOf(yellowColor));
    }

    private void populateText(Bundle questionBundle)
    {
        String question = questionBundle.getString("question");
        String optionA = questionBundle.getString("optionA");
        String optionB = questionBundle.getString("optionB");
        String optionC = questionBundle.getString("optionC");

        questionText.setText(question);
        buttonA.setText(optionA);
        buttonB.setText(optionB);
        buttonC.setText(optionC);
    }

    private void setProgress(int currentQuestion, int totalQuestions)
    {
        // cast to turn to floating point division instead of integer
        float actualProgress = ((float) currentQuestion + 1) / totalQuestions;
        int percentageProgress = Math.round(actualProgress * 100);
        progressBar.setProgress(percentageProgress);

        progressFraction.setText(String.valueOf(currentQuestion + 1) + "/" + String.valueOf(totalQuestions));
    }

    // move the intent to the next activity
    private void onNext(Intent old, int currentGuess, int totalQuestions, int wrongGuesses)
    {
        // update number of wrong guesses - implicitly overwrites
        if (wasWrong != null && wasWrong)
        {
            old.putExtra("wrongQuestions", wrongGuesses + 1);
        } // otherwise we don't have to update

        // update current guess number
        // in the case of the final question, this currentQuestion should get set equal to
        // totalQuestions, despite the fact that that is impossible due to zero indexing.
        // however, if that is the case, it will get caught in the following if block.
        old.putExtra("currentQuestion", currentGuess + 1);

        // start next activity -> if applicable, run the finish screen
        Intent current = chooseIntent(old, currentGuess, totalQuestions);

        startActivity(current);
        finish();
    }

    @NonNull
    private Intent chooseIntent(Intent old, int currentGuess, int totalQuestions) {
        Intent current;
        // currentGuess here uses whatever guess this activity was instantiated to represent
        // NOT the current guess that has been updated in the "old extras" a few lines previous.
        if (currentGuess == totalQuestions - 1)
        {
            current = new Intent(this, FinishActivity.class);
        }
        else
        {
            current = new Intent(this, QuizActivity.class);
        }

        Bundle oldExtras = old.getExtras();
        if (oldExtras != null)
        {
            current.putExtras(oldExtras);
        }

        return current;
    }

    private void onSelect(char correctGuess)
    {
        // check whether a guess was selected or not
        if (targetButton == null)
        {
            // no guess selected, return -> not giving the liberty of a Toast
            return;
        }

        // first identify correct guess
        int selectedId = targetButton.getId();
        if (selectedId == R.id.buttonA)
        {
            wasWrong = correctGuess != 'A';
        }
        else if (selectedId == R.id.buttonB)
        {
            wasWrong = correctGuess != 'B';
        }
        else // selected C
        {
            wasWrong = correctGuess != 'C';
        }

        // get the id of the view that was supposed to be correct so we can apply visual feedback
        int correctId;
        switch (correctGuess)
        {
            case 'A':
                correctId = R.id.buttonA;
                break;
            case 'B':
                correctId = R.id.buttonB;
                break;
            case 'C':
            default:
                correctId = R.id.buttonC;
                break;
        }

        int greenColor = ContextCompat.getColor(this, R.color.green);
        int redColor = ContextCompat.getColor(this, R.color.red);

        // provide visual feedback of success
        if (wasWrong)
        {
            // then set colour of expected
            targetButton.setBackgroundTintList(ColorStateList.valueOf(redColor));
        }

        // regardless setting correct to green
        Button correct = findViewById(correctId);
        correct.setBackgroundTintList(ColorStateList.valueOf(greenColor));

        // adjust the content of the submit button to say "Next"
        submitOrNext.setText("Next");

        // set submitted as true
        submitted = true;
    }
}