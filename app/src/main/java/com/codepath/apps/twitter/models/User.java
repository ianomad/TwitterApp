package com.codepath.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by akhmedovi on 10/30/16.
 * Copyright - 2015
 */

@Parcel
public class User {

    String id;

    String name;

    @SerializedName("screen_name")
    String screenName;

    @SerializedName("profile_image_url")
    String profileImageUrl;

    @SerializedName("profile_background_image_url")
    String profileBackgroundImageUrl;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
