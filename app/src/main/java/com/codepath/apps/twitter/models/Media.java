package com.codepath.apps.twitter.models;

import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("media_url")
    String mediaUrl;

    public Media() {
    }

    public Media(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}
