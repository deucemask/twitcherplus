package com.codepath.apps.twitcher.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.twitcher.fragments.FeedFragment;
import com.codepath.apps.twitcher.fragments.HomeTimelineFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dmaskev on 11/1/15.
 */
public class TabAdapter extends FragmentPagerAdapter {
    private static final int TAB_COUNT = 2;
    private List<FeedFragment> tabs = new ArrayList<>();
    private List<String> titles = new ArrayList<>();


    public TabAdapter(FragmentManager fm, LinkedHashMap<String, FeedFragment> titleTabMap) {
        super(fm);
        if(titleTabMap != null) {
            for(Map.Entry<String, FeedFragment> entry : titleTabMap.entrySet()) {
                this.tabs.add(entry.getValue());
                this.titles.add(entry.getKey());
            }
        }
    }



    @Override
    public Fragment getItem(int position) {
        return this.tabs.get(position);
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
