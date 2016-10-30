package com.codepath.apps.twitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class TwitterClient extends OAuthBaseClient {
    private static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    private static final String REST_URL = "https://api.twitter.com/1.1/";
    private static final String REST_CONSUMER_KEY = "UgjbAGiEiMZZ45TW3GtdM4UE1";
    private static final String REST_CONSUMER_SECRET = "fQTLMX9A7xv1pHIwxtdaSvHsp7JJs7AIKZIJcbMxzaiXUoEq06";
    private static final String REST_CALLBACK_URL = "oauth://twitter-android";

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    /**
     * Get twitter home timeline
     *
     * @param handler
     */
    public void getHomeTimeline(Long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/statuses/home_timeline.json");

        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("count", 25);
        params.put("since_id", 1);

        if (null != maxId) {
            params.put("max_id", maxId - 1);
        }

        client.get(apiUrl, params, handler);
    }
}