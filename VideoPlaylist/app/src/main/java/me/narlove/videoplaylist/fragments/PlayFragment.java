package me.narlove.videoplaylist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import me.narlove.videoplaylist.R;
import me.narlove.videoplaylist.utilities.GenericUtils;

public class PlayFragment extends Fragment {

    private static final String ARG_URL = "url";
    private String url;

    public PlayFragment() {
        // Required empty public constructor
    }

    public static PlayFragment newInstance(String url) {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_play, container, false);

        WebView youtubeWebView = v.findViewById(R.id.webView);

        youtubeWebView.getSettings().setJavaScriptEnabled(true);
        youtubeWebView.setWebChromeClient(new WebChromeClient());

        String videoId = GenericUtils.extractVideoId(url);

        if (videoId != null)
        {
            String iframe = "<iframe width='100%' height='100%' src='https://www.youtube.com/embed/" + videoId + "' frameborder='0' allowfullscreen></iframe>";

            youtubeWebView.loadDataWithBaseURL(
                "https://com.example.istream/", // no clue why this works and nothing else does
                iframe,
                "text/html",
                "utf-8",
                null
            );
        }

        return v;
    }
}