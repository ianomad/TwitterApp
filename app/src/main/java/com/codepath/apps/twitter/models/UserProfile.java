/*
 * Created by Iliiazbek Akhmedov
 * Copyright (c) 2016.
 */

package com.codepath.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class UserProfile {
    private String id;

    private String name;

    private String description;

    @SerializedName("screen_name")
    private String screenName;

    @SerializedName("followers_count")
    private Long followersCount;

    @SerializedName("profile_image_url")
    private String profileImageUrl;

    @SerializedName("friends_count")
    private Long friendsCount;

    public UserProfile() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Long followersCount) {
        this.followersCount = followersCount;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Long getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Long friendsCount) {
        this.friendsCount = friendsCount;
    }
}
