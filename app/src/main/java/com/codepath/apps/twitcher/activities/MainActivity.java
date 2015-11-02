package com.codepath.apps.twitcher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitcher.R;
import com.codepath.apps.twitcher.adapters.TabAdapter;
import com.codepath.apps.twitcher.fragments.FeedFragment;
import com.codepath.apps.twitcher.fragments.HomeTimelineFragment;
import com.codepath.apps.twitcher.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitcher.models.Tweet;
import com.codepath.apps.twitcher.models.User;
import com.codepath.apps.twitcher.net.TwitterClient;
import com.google.common.collect.Maps;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

import static com.codepath.apps.twitcher.TwitcherApp.getTwitterClient;

/**
 * Created by dmaskev on 11/1/15.
 */
public class MainActivity extends AppCompatActivity {
    private static final int ACTIVITY_CODE_CREATE_TWEET = 999;
    private User profile = new User("", "");
    private HomeTimelineFragment homeTimelineFragment = new HomeTimelineFragment();
    private MentionsTimelineFragment mentionsTimelineFragment = new MentionsTimelineFragment();
    private LinkedHashMap<String, FeedFragment> tabFragments = new LinkedHashMap<>();
    private String query = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabFragments.put("Home", homeTimelineFragment);
        tabFragments.put("Mentions", mentionsTimelineFragment);

        ViewPager vpTabs = (ViewPager) findViewById(R.id.vpTabs);
        vpTabs.setAdapter(new TabAdapter(getSupportFragmentManager(), tabFragments));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(vpTabs);

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flFeed, mentionsTimelineFragment);
//        ft.addToBackStack(null);
//        ft.commit();

        retrieveProfile();
        retrieveTwitterRateLimit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newQuery) {
                if (newQuery == null || newQuery.isEmpty()) {
                    return false;
                }

                return homeTimelineFragment.onSearch(newQuery);
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                return false;
//                if (newQuery == null || newQuery.length() < 2) {
//                    return false;
//                }
//
//                return homeTimelineFragment.onSearch(newQuery);
            }
        });

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.miWrite) {
            createWriteTweetIntent();
        } else if (id == R.id.miProfile) {
            createViewProfileIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createWriteTweetIntent() {
        Intent intent = new Intent(this, CreateTweetActivity.class);
        if(profile != null) {
            intent.putExtra("avatar", profile.avatarUrl);
        }

        startActivityForResult(intent, ACTIVITY_CODE_CREATE_TWEET);
    }

    private void createViewProfileIntent() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("profile", profile);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ACTIVITY_CODE_CREATE_TWEET) {
            Tweet tweet = data.getParcelableExtra("tweet");
            if(tweet != null) {
                homeTimelineFragment.addTweet(tweet);
                if(tweet.text != null && tweet.text.contains('@' + this.profile.handle)) {
                    mentionsTimelineFragment.addTweet(tweet);
                }
            }
        }
    }

    private void retrieveProfile() {
        getTwitterClient().getProfile(new TwitterClient.Callback() {
            @Override
            public void onSuccess(User profile) {
                if (profile != null) {
                    MainActivity.this.profile = profile;
                }

            }

            @Override
            public void onFailure(int statusCode, String error, Throwable t) {
                Log.e(FeedFragment.class.getSimpleName(), "Failed to get feed. " + error, t);
                Toast.makeText(MainActivity.this, "Network error during profile load", Toast.LENGTH_LONG).show();
            }

        });
    }


    private void retrieveTwitterRateLimit() {
//        https://api.twitter.com/1.1/application/rate_limit_status.json
        getTwitterClient().getRateLimit(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(FeedFragment.class.getSimpleName(), "Twitter rate limit " + response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(FeedFragment.class.getSimpleName(), "Failed to get rate limit " + responseString);
            }
        });

    }

}
