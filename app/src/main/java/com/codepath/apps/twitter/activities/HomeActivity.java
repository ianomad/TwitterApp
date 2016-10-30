package com.codepath.apps.twitter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.adapters.TweetsAdapter;
import com.codepath.apps.twitter.models.Tweet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.tweetRV)
    RecyclerView recyclerView;

    private List<Tweet> tweetList;
    private TweetsAdapter tweetsAdapter;
    private TwitterClient twitterClient;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.twitterClient = TwitterApplication.getTwitterClient();
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {

        tweetList = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweetList);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(tweetsAdapter);

        twitterClient.getHomeTimeline(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .setDateFormat("EEE MMM dd HH:mm:ss Z yyyy").create();

                Tweet[] tweets = gson.fromJson(new String(responseBody), Tweet[].class);
                tweetsAdapter.addTweets(tweets);
                tweetsAdapter.notifyDataSetChanged();

                System.out.println(gson.toJson(gson.fromJson(new String(responseBody), JsonArray.class)));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
