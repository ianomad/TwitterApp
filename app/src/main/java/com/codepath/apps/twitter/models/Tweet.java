package com.codepath.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class Tweet {

    private Long id;

    private String text;

    @SerializedName("created_at")
    private Date createdAt;

    private User user;

    @SerializedName("favorite_count")
    private int favCount;

    @SerializedName("retweeted_status")
    private Tweet retweetedStatus;

    @SerializedName("retweet_count")
    private int retweetCount;

    private Entities entities;

    private boolean favorited;

    public Tweet() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getFavCount() {
        if(null != retweetedStatus) {
            return retweetedStatus.getFavCount();
        }

        return favCount;
    }

    public void setFavCount(int favCount) {
        if(null != retweetedStatus) {
            retweetedStatus.setFavCount(favCount);
            return;
        }

        this.favCount = favCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {

        if (null == entities || null == entities.getMedia() || entities.getMedia().isEmpty()) {
            return null;
        }

        return entities.getMedia().get(0).getMediaUrl();
    }

    public Tweet getRetweetedStatus() {
        return retweetedStatus;
    }

    public void setRetweetedStatus(Tweet retweetedStatus) {
        this.retweetedStatus = retweetedStatus;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
