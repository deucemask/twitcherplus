package com.codepath.apps.twitcher.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import static com.codepath.apps.twitcher.helpers.JsonHelper.parseJsonObject;

/**
 * Created by dmaskev on 10/24/15.
 */
@Table(name="users")
public class User extends Model implements Parcelable {
    @Column
    public String name;

    @Column
    @SerializedName("screen_name")
    public String handle;

    @Column
    @SerializedName("profile_image_url")
    public String avatarUrl;

    @Column
    @SerializedName("followers_count")
    public int followers;

    @Column
    @SerializedName("friends_count")
    public int following;


    public User() {

    }

    public User(String name, String avatarUrl) {
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public static User fromJson(byte[] data) {
        return parseJsonObject(data, User.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.handle);
        dest.writeString(this.avatarUrl);
        dest.writeInt(this.followers);
        dest.writeInt(this.following);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.handle = in.readString();
        this.avatarUrl = in.readString();
        this.followers = in.readInt();
        this.following = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
