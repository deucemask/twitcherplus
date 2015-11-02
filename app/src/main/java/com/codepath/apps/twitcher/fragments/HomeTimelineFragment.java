package com.codepath.apps.twitcher.fragments;

import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.twitcher.models.Tweet;
import com.codepath.apps.twitcher.net.TwitterClient;

import java.util.List;

import static com.codepath.apps.twitcher.TwitcherApp.getTwitterClient;

/**
 * Created by dmaskev on 11/1/15.
 */
public class HomeTimelineFragment extends FeedFragment {
    private String query = "";
    boolean isSearchMode = false;

    @Override
    public void retrieveTweets(final boolean clearOld) {
        if(query == null || query.isEmpty()) {
            isSearchMode = false;
        }

        if(!isSearchMode) {
            getTimelineFeed(clearOld);
        } else {
            onSearch(query, clearOld);
        }

    }

    private void getTimelineFeed(final boolean clearOld) {
        Log.d(FeedFragment.class.getSimpleName(), "getting twitter feed");
        getTwitterClient().getFeed(lastTweetId, TWEETS_PER_PAGE, new TwitterClient.Callback() {
            @Override
            public void onSuccess(List<Tweet> tweets) {
                if (tweets != null && !tweets.isEmpty()) {
                    if (clearOld) {
                        aTweets.clear();
                    }
                    aTweets.addAll(tweets);
                    lastTweetId = tweets.get(tweets.size() - 1).tweetId;
                }
                nextPage++;
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, String error, Throwable t) {
                Log.e(HomeTimelineFragment.class.getSimpleName(), "Failed to get feed. " + error, t);
                Toast.makeText(HomeTimelineFragment.this.getContext(), "Network error during feed load", Toast.LENGTH_LONG).show();
                swipeContainer.setRefreshing(false);
            }

        });
    }

    private boolean onSearch(String newQuery, final boolean clearOld) {
        this.query = newQuery;
        

        getTwitterClient().search(query, lastTweetId, TWEETS_PER_PAGE, new TwitterClient.Callback() {
            @Override
            public void onSuccess(List<Tweet> tweets) {
                if (tweets != null && !tweets.isEmpty()) {
                    if (clearOld) {
                        aTweets.clear();
                    }
                    aTweets.addAll(tweets);
                    lastTweetId = tweets.get(tweets.size() - 1).tweetId;
                }
                nextPage++;
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, String error, Throwable t) {
                Log.e(HomeTimelineFragment.class.getSimpleName(), "Failed to get feed. " + error, t);
                Toast.makeText(HomeTimelineFragment.this.getContext(), "Network error during feed load", Toast.LENGTH_LONG).show();
                swipeContainer.setRefreshing(false);
            }

        });

        return true;
    }

    public boolean onSearch(String newQuery) {
        if (newQuery == null || newQuery.trim().equals(query.trim())) {
            return false;
        }

        isSearchMode = true;
        return onSearch(newQuery, true);
    }


}
