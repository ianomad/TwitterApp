package com.codepath.apps.twitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.models.TweetSearchResponse;
import com.codepath.apps.twitter.models.UserProfile;
import com.codepath.oauth.OAuthBaseClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TwitterClient extends OAuthBaseClient {
    private static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;

    private final static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("EEE MMM dd HH:mm:ss Z yyyy").create();

    private static final String REST_URL = "https://api.twitter.com/1.1/";
    private static final String REST_CALLBACK_URL = "oauth://twitter-android";
    private static final String REST_CONSUMER_KEY = "UgjbAGiEiMZZ45TW3GtdM4UE1";
    private static final String REST_CONSUMER_SECRET = "fQTLMX9A7xv1pHIwxtdaSvHsp7JJs7AIKZIJcbMxzaiXUoEq06";


    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    /**
     * Get home feed
     *
     * @param callback
     */
    public void getHomeTimeline(Long sinceId, Long maxId, TweetsCallback callback) {
        String apiUrl = getApiUrl("/statuses/home_timeline.json");

        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("count", 25);

        if (null != sinceId) {
            params.put("since_id", sinceId);
        } else {
            params.put("since_id", 1L);
        }

        if (null != maxId) {
            params.put("max_id", maxId - 1);
        }

        client.get(apiUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Tweet[] data = gson.fromJson(new String(responseBody), Tweet[].class);
                ArrayList<Tweet> tweetArrayList = new ArrayList<>();
                Collections.addAll(tweetArrayList, data);

                callback.onSuccess(tweetArrayList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onError(error);
                Log.e("Debug", new String(responseBody));
            }
        });
    }


    /**
     * Get mentions feed
     *
     * @param callback
     */
    public void getMentionsTimeline(Long sinceId, Long maxId, TweetsCallback callback) {
        String apiUrl = getApiUrl("/statuses/mentions_timeline.json");

        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("count", 25);

        if (null != sinceId) {
            params.put("since_id", sinceId);
        } else {
            params.put("since_id", 1L);
        }

        if (null != maxId) {
            params.put("max_id", maxId - 1);
        }

        client.get(apiUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Tweet[] data = gson.fromJson(new String(responseBody), Tweet[].class);
                ArrayList<Tweet> tweetArrayList = new ArrayList<>();
                Collections.addAll(tweetArrayList, data);

                callback.onSuccess(tweetArrayList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onError(error);
                Log.e("Debug", new String(responseBody));
            }
        });
    }

    public void getUserTimeline(String userId, Long sinceId, Long maxId, TweetsCallback callback) {
        String apiUrl = getApiUrl("/statuses/user_timeline.json");

        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("count", 25);

        if (null != sinceId) {
            params.put("since_id", sinceId);
        } else {
            params.put("since_id", 1L);
        }

        if (null != maxId) {
            params.put("max_id", maxId - 1);
        }

        if (null != userId) {
            params.put("user_id", userId);
        }

        client.get(apiUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Tweet[] data = gson.fromJson(new String(responseBody), Tweet[].class);
                ArrayList<Tweet> tweetArrayList = new ArrayList<>();
                Collections.addAll(tweetArrayList, data);

                callback.onSuccess(tweetArrayList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onError(error);
                Log.e("Debug", new String(responseBody));
            }
        });
    }


    public void getBySearchKey(String query, Long sinceId, Long maxId, TweetsCallback callback) {
        String apiUrl = getApiUrl("/search/tweets.json");

        RequestParams params = new RequestParams();
        params.put("q", query);
        params.put("count", 25);

        if (null != sinceId) {
            params.put("since_id", sinceId);
        } else {
            params.put("since_id", 1L);
        }

        if (null != maxId) {
            params.put("max_id", maxId - 1);
        }

        client.get(apiUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                TweetSearchResponse data = gson.fromJson(new String(responseBody), TweetSearchResponse.class);
                callback.onSuccess(data.getStatuses());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onError(error);
                Log.e("Debug", new String(responseBody));
            }
        });
    }

    public void getUserProfile(String userId, String screenName, UserProfileCallback callback) {
        String apiUrl = getApiUrl("/users/show.json");

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("screen_name", screenName);

        client.get(apiUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                UserProfile userCredentials = gson.fromJson(new String(responseBody), UserProfile.class);
                callback.onSuccess(userCredentials);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onError(error);
                Log.e("Debug", new String(responseBody));
            }
        });
    }

    public void getMyUserProfile(UserProfileCallback callback) {
        String apiUrl = getApiUrl("/account/verify_credentials.json");

        RequestParams params = new RequestParams();

        client.get(apiUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                UserProfile userCredentials = gson.fromJson(new String(responseBody), UserProfile.class);
                callback.onSuccess(userCredentials);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onError(error);
                Log.e("Debug", new String(responseBody));
            }
        });
    }

    public void addFavorite(Long id) {
        String apiUrl = getApiUrl("/favorites/create.json");

        RequestParams params = new RequestParams();
        params.put("id", id);

        client.post(apiUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Debug", "Fav added for " + id);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Log.e("Debug", new String(responseBody));
            }
        });
    }

    public void removeFavorite(Long id) {
        String apiUrl = getApiUrl("/favorites/destroy.json");

        RequestParams params = new RequestParams();
        params.put("id", id);

        client.post(apiUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Debug", "Fav removed for " + id);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
                Log.e("Debug", new String(responseBody));
            }
        });
    }

    public void postTweet(String tweet, CreateTweetCallback callback) {
        RequestParams params = new RequestParams();
        params.put("status", tweet);

        String apiUrl = getApiUrl("statuses/update.json");
        client.post(apiUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                callback.onSuccess();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onError(error);
                Log.e("Debug", new String(responseBody));
            }
        });
    }

    public interface TweetsCallback {
        public void onSuccess(List<Tweet> tweetList);

        public void onError(Throwable e);
    }

    public interface CreateTweetCallback {
        public void onSuccess();

        public void onError(Throwable e);
    }

    public interface UserProfileCallback {
        public void onSuccess(UserProfile userProfile);

        public void onError(Throwable e);
    }
}