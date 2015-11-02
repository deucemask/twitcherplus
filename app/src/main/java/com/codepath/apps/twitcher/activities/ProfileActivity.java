package com.codepath.apps.twitcher.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitcher.R;
import com.codepath.apps.twitcher.fragments.UserTimelineFragment;
import com.codepath.apps.twitcher.models.User;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import static com.google.common.base.Strings.isNullOrEmpty;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        User profile = (User)getIntent().getParcelableExtra("profile");

        if(profile == null) {
            return;
        }

        TextView tvProfileName = (TextView) findViewById(R.id.tvProfileName);
        tvProfileName.setText(profile.name);

        TextView tvProfileHandle = (TextView) findViewById(R.id.tvProfileHandle);
        tvProfileHandle.setText('@' + profile.handle);

        if(!isNullOrEmpty(profile.avatarUrl)) {
            ImageView ivAvatar = (ImageView) findViewById(R.id.ivProfilePicture);
            Picasso.with(this).load(profile.avatarUrl).into(ivAvatar);
        }

        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        tvFollowing.setText(NumberFormat.getInstance().format(profile.following));

        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowers.setText(NumberFormat.getInstance().format(profile.followers));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UserTimelineFragment fragment = new UserTimelineFragment();
        fragment.setUserHandle(profile.handle);
        ft.replace(R.id.flUserTimeline, fragment);
//        ft.addToBackStack(null);
        ft.commit();



//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
