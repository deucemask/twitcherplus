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
public class UserTimelineFragment extends FeedFragment {

    private String userHandle;

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    @Override
    public void retrieveTweets(final boolean clearOld) {
        getTwitterClient().getUserTimeline(this.userHandle, lastTweetId, TWEETS_PER_PAGE, new TwitterClient.Callback() {
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
                Log.e(UserTimelineFragment.class.getSimpleName(), "Failed to get feed. " + error, t);
                Toast.makeText(UserTimelineFragment.this.getContext(), "Network error during feed load", Toast.LENGTH_LONG).show();
                swipeContainer.setRefreshing(false);
            }

        });
    }

}
