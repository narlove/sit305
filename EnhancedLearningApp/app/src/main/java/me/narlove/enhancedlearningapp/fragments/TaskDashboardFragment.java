package me.narlove.enhancedlearningapp.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.GenerationConfig;
import com.google.firebase.ai.type.GenerativeBackend;
import com.google.firebase.ai.type.Schema;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.narlove.enhancedlearningapp.R;
import me.narlove.enhancedlearningapp.datatypes.Interest;
import me.narlove.enhancedlearningapp.datatypes.Question;
import me.narlove.enhancedlearningapp.datatypes.Task;
import me.narlove.enhancedlearningapp.datatypes.User;
import me.narlove.enhancedlearningapp.persistence.DatabaseViewModel;
import me.narlove.enhancedlearningapp.recycler.CustomAdapter;
import me.narlove.enhancedlearningapp.utilities.GenericUtils;
import me.narlove.enhancedlearningapp.utilities.InterestConversionHandler;
import me.narlove.enhancedlearningapp.utilities.UserViewModel;

public class TaskDashboardFragment extends Fragment {

    private TextView titleText;
    private MaterialCardView taskBannerCard;
    private TextView taskBannerText;
    private RecyclerView recyclerView;
    private Button generateTaskButton;
    private Button goBackButton;

    private DatabaseViewModel dbvm;
    private UserViewModel userVm;

    private User currentUser;

    private GenerativeModelFutures model;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbvm = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
        userVm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        currentUser = userVm.getCurrentUser().getValue();

        // if the user has become signed out, move them to the login fragment again
        if (currentUser == null)
        {
            GenericUtils.switchFragment(this, new LoginFragment(), false);
            return;
        }

        // define format for response to be in so that we shoudn't get any errors with it
        Schema jsonSchema = Schema.array(
                Schema.obj(
                        Map.of(
                                "questionText", Schema.str(),
                                "optionA", Schema.str(),
                                "optionB", Schema.str(),
                                "optionC", Schema.str(),
                                "correctOption", Schema.numInt(),
                                "hint", Schema.str()
                        )
                )
        );

        // temperature to try and get different questions without having to store a history of
        // previously prompted questions
        GenerationConfig generationConfig = new GenerationConfig.Builder()
                .setTemperature(0.7f)
                .setResponseMimeType("application/json")
                .setResponseSchema(jsonSchema)
                .build();

        GenerativeModel firebaseAI = FirebaseAI.getInstance(GenerativeBackend.googleAI())
                .generativeModel(
                        "gemini-2.5-flash",
                        generationConfig
                );

        model = GenerativeModelFutures.from(firebaseAI);

        Log.i("narloveapp", "generated models?");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_task_dashboard, container, false);

        titleText = v.findViewById(R.id.titleText);
        taskBannerCard = v.findViewById(R.id.taskBannerCard);
        taskBannerText = v.findViewById(R.id.taskBannerText);
        recyclerView = v.findViewById(R.id.taskRecyclerView);
        generateTaskButton = v.findViewById(R.id.generateTaskButton);
        goBackButton = v.findViewById(R.id.logoutButton);

        titleText.setText(String.format("Hello, %s", currentUser.getName()));

        CustomAdapter adapter = new CustomAdapter(requireContext(), taskId ->
        {
            GenericUtils.switchFragment(TaskDashboardFragment.this, TaskFragment.newInstance(taskId), true);
        });
        adapter.setHasStableIds(true);

        LinearLayoutManager layout = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);

        dbvm.getTasksByUserId(currentUser.getUserId()).observe(getViewLifecycleOwner(), tasks ->
        {
            adapter.submitList(tasks);
        });

        dbvm.getNumberOfTasksByUserId(currentUser.getUserId()).observe(getViewLifecycleOwner(), number ->
        {
            // prim for primitive
            int prim = number;
            if (prim == 0)
            {
                taskBannerCard.setVisibility(GONE);
            }
            else if (prim == 1)
            {
                taskBannerCard.setVisibility(VISIBLE);
                taskBannerText.setText("You have 1 task due!");
            }
            else
            {
                taskBannerCard.setVisibility(VISIBLE);
                taskBannerText.setText(String.format("You have %d tasks due!", prim));
            }
        });

        goBackButton.setOnClickListener(clicked ->
        {
            userVm.setCurrentUser(null);
            GenericUtils.switchFragment(TaskDashboardFragment.this,
                    new LoginFragment(), false);
        });

        // make use an llm to generate a task based on a random user interest
        generateTaskButton.setOnClickListener(clicked ->
        {
            List<Interest> userInterests = currentUser.getInterests();
            Interest selectedInterest = userInterests.get(
                    new Random().nextInt(userInterests.size())
            );

            Gson gson = new Gson();

            String prompt = String.format("Generate exactly three secondary-school level trivia questions about the following topic: %s. " +
                            "The plaintext question String that I can show to a user should be stored in the 'questionText' field. " +
                            "The questions should be multiple choice, and have three possible answers. Ensure that one of the answers is correct, " +
                            "and that the other two answers are incorrect. The correct answer should be in a random position " +
                            "(either optionA, optionB, or optionC) so that it is difficult for the user to guess the answer. " +
                            "I need you to confirm which answer is the correct one by providing a field titled 'correctOption' " +
                            "with a value of either 0, 1, or 2 depending on whether optionA, optionB, or optionC is correct, respectively. " +
                            "You also need to generate a single hint for the question, that should help prompt the user towards the correct answer." +
                            "The hint should be stored in a field called 'hint'",
                    selectedInterest.toString());

            Content content = new Content.Builder()
                    .addText(prompt)
                    .build();

            Log.i("narloveapp", "onCreateView: created prompt, set response callbackk");

            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
            Futures.addCallback(
                    response,
                    new FutureCallback<GenerateContentResponse>() {
                        @Override
                        public void onSuccess(GenerateContentResponse result) {
                            Log.i("narloveapp", "success callback");
                            String response = result.getText();
                            Question[] questions = gson.fromJson(response, Question[].class);

                            // could probably put this in a loop with a dictionary or something,
                            // can't really be bothered lowk.
                            Question genQuestion1 = questions[0];
                            genQuestion1.setOrderIndex(0);
                            Question genQuestion2 = questions[1];
                            genQuestion1.setOrderIndex(1);
                            Question genQuestion3 = questions[2];
                            genQuestion1.setOrderIndex(2);

                            Task generatedTask = new Task(
                                    currentUser.getUserId(),
                                    selectedInterest,
                                    prompt,
                                    response,
                                    String.format("This task focuses on improving your %s skills.",
                                            Interest.getFrontendCompatible(selectedInterest))
                            );

                            dbvm.insertTaskWithQuestions(generatedTask, Arrays.asList(
                                    genQuestion1,
                                    genQuestion2,
                                    genQuestion3
                                    ));
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            t.printStackTrace();
                        }
                    },
                    executor);
        });

        return v;
    }
}