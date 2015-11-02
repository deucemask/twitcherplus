package com.codepath.apps.twitcher.models;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.codepath.apps.twitcher.helpers.JsonHelper.parseJsonArray;
import static com.codepath.apps.twitcher.helpers.JsonHelper.parseJsonObject;

/**
 * Created by dmaskev on 11/2/15.
 */
public class SearchResult {

    @SerializedName("statuses")
    public List<Tweet> tweets;

    public static SearchResult fromJson(byte[] data) {
        return parseJsonObject(data, SearchResult.class);
    }
}
