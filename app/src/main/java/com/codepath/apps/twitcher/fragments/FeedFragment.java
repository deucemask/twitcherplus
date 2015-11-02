package com.codepath.apps.twitcher.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitcher.R;
import com.codepath.apps.twitcher.activities.CreateTweetActivity;
import com.codepath.apps.twitcher.activities.ProfileActivity;
import com.codepath.apps.twitcher.adapters.FeedListAdapter;
import com.codepath.apps.twitcher.helpers.EndlessScrollListener;
import com.codepath.apps.twitcher.models.Tweet;
import com.codepath.apps.twitcher.models.User;
import com.codepath.apps.twitcher.net.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.color.holo_blue_bright;
import static android.R.color.holo_green_light;
import static android.R.color.holo_orange_light;
import static android.R.color.holo_red_light;
import static com.codepath.apps.twitcher.TwitcherApp.getTwitterClient;

public abstract class FeedFragment extends Fragment {
    public static final int TWEETS_PER_PAGE = 25;
    private static final int SCROLL_MAX_PAGES = 14;
    private static final int SCROLL_BUFFER = 12;

    private ListView lvTweets;
    private List<Tweet> tweets;
    protected FeedListAdapter aTweets;
    protected SwipeRefreshLayout swipeContainer;


    protected long lastTweetId = 0L;
    protected int nextPage = 0;

    public abstract void retrieveTweets(boolean cleanOld);

    public void addTweet(Tweet tweet) {
        if(this.aTweets != null && tweet != null) {
            this.aTweets.insert(tweet, 0);
        }
    }

    public void resetTweets(List<Tweet> tweets) {
        if(this.aTweets != null && tweets != null && tweets.size() > 0) {
            this.aTweets.clear();
            this.aTweets.addAll(tweets);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new FeedListAdapter(getContext(), tweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener(SCROLL_BUFFER) {
            @Override
            public boolean onLoadMore() {
                if (nextPage > SCROLL_MAX_PAGES) {
                    return false;
                }

                retrieveTweets(false);

                return true;
            }
        });
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tweet tweet = tweets.get(position);
                if(tweet != null && tweet.user != null) {
                    viewProfile(tweet.user);
                }
            }
        });

        this.swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadTweets();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(holo_blue_bright, holo_green_light, holo_orange_light, holo_red_light);

        return view;

//        retrieveTweets();
//        retrieveProfile();
    }

    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void reloadTweets() {
        this.nextPage = 0;
        this.lastTweetId = 0L;
        retrieveTweets(true);
    }

    public void viewProfile(User profile) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("profile", profile);
        startActivity(intent);
    }




}
