package me.narlove.chatbot.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.ChatFutures;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.GenerativeBackend;
import com.google.firebase.ai.type.RequestOptions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.narlove.chatbot.R;
import me.narlove.chatbot.persistence.MessagesViewModel;
import me.narlove.chatbot.recycler.CustomAdapter;
import me.narlove.chatbot.utilities.Author;
import me.narlove.chatbot.persistence.MessageObject;

public class MessagesFragment extends Fragment
{
    private static final String ARG_USERNAME = "username";
    private String username;

    private Button sendButton;
    private EditText textEntry;
    private RecyclerView recyclerView;
    private View aiLoadingIndicator;

    private GenerativeModelFutures model;
    private ChatFutures chatSession;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public MessagesFragment()
    {
    }

    public static MessagesFragment newInstance(String username) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }

        // define a preamble that tells the ai the user's name from their authentication.
        Content systemInstruction = new Content.Builder()
                .addText("The user's name is " + username + ". " +
                        "Use this name to address them, unless they ask you to use a different name.")
                .build();

        GenerativeModel firebaseAI = FirebaseAI.getInstance(GenerativeBackend.googleAI())
                .generativeModel(
                        "gemini-2.5-flash",
                        null,
                        null,
                        null,
                        null,
                        systemInstruction // request options
                );

        model = GenerativeModelFutures.from(firebaseAI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_messages, container, false);
        MessagesViewModel dbvm = new ViewModelProvider(requireActivity()).get(MessagesViewModel.class);

        sendButton = v.findViewById(R.id.sendButton);
        textEntry = v.findViewById(R.id.sendMessage);
        recyclerView = v.findViewById(R.id.messages);
        aiLoadingIndicator = v.findViewById(R.id.aiLoadingIndicator);

        CustomAdapter adapter = new CustomAdapter(requireContext());
        adapter.setHasStableIds(true);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount)
            {
            recyclerView.post(() ->
            {
                // program errors trying to load messages if db is empty if this clause
                // is not here
                if (adapter.getItemCount() != 0)
                {
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            });
            }
        });

        LinearLayoutManager layout = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);

        dbvm.getAllItems().observe(getViewLifecycleOwner(), newList ->
        {
            adapter.submitList(newList);

            // if this is the first run thru, we create a chat session
            // BUT only if this is the first session and PREVIOUS MESSAGES have been sent
            if (chatSession == null && newList != null && !newList.isEmpty()) {
                // https://firebase.google.com/docs/ai-logic/chat?authuser=0&api=dev#prereqs
                List<Content> history = new ArrayList<>();
                for (MessageObject msg : newList) {
                    String role = (msg.getAuthor() == Author.USER) ? "user" : "model";
                    history.add(new Content.Builder()
                            .setRole(role)
                            .addText(msg.getContent())
                            .build());
                }
                chatSession = model.startChat(history);
            }
        });

        sendButton.setOnClickListener(clicked ->
        {
            String messageContents = textEntry.getText().toString();

            if (messageContents.isEmpty())
            {
                // don't want to waste a db call if not necessary
                return;
            }

            // Ensure chat session is initialized (e.g. if DB was empty)
            if (chatSession == null) {
                chatSession = model.startChat();
            }

            aiLoadingIndicator.setVisibility(View.VISIBLE);

            dbvm.insert(new MessageObject(messageContents, Author.USER));
            textEntry.getText().clear();

            Content userMessage = new Content.Builder()
                    .addText(messageContents)
                    .build();

            // use sendMessage to maintain a conversation
            ListenableFuture<GenerateContentResponse> response = chatSession.sendMessage(userMessage);
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText();
                    dbvm.insert(new MessageObject(resultText, Author.BOT));

                    // if added checks if the fragment is attached to the activity.
                    // this ensures if teh user has swapped screens (and fragment is no longer
                    // visible on their screen), that "requireActivity" won't throw.
                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> aiLoadingIndicator.setVisibility(View.GONE));
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();

                    if (isAdded()) {
                        requireActivity().runOnUiThread(() -> aiLoadingIndicator.setVisibility(View.GONE));
                    }
                }
            }, executor);
        });

        return v;
    }
}
