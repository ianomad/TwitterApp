package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.adapters.TweetsAdapter;
import com.codepath.apps.twitter.dialogs.CreateTweetDialog;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements CreateTweetDialog.TweetHandler {

    @BindView(R.id.tweetRV)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private List<Tweet> tweetList;
    private TweetsAdapter tweetsAdapter;
    private TwitterClient twitterClient;
    private LinearLayoutManager layoutManager;

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
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                twitterClient.getHomeTimeline(null, tweetList.get(tweetList.size() - 1).getId(), new TwitterClient.TweetsCallback() {
                    @Override
                    public void onSuccess(List<Tweet> res) {
                        tweetsAdapter.addTweets(res);
                        tweetsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
            }
        });

        twitterClient.getHomeTimeline(null, null, new TwitterClient.TweetsCallback() {
            @Override
            public void onSuccess(List<Tweet> res) {
                tweetsAdapter.addTweets(res);
                tweetsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

        fab.setOnClickListener(v -> new CreateTweetDialog().show(getSupportFragmentManager(), "CreateTweet"));
    }

    private void getNewestTweets() {
        Long newestId = !tweetList.isEmpty() ? tweetList.get(0).getId() : 1L;

        twitterClient.getHomeTimeline(newestId + 1, null, new TwitterClient.TweetsCallback() {
            @Override
            public void onSuccess(List<Tweet> tweetList) {
                tweetsAdapter.addTweets(0, tweetList);
                tweetsAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(0);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace(); //show snackbar
            }
        });
    }

    @Override
    public void onNewTweetSave(CreateTweetDialog dialog, String tweet) {
        dialog.dismiss();

        twitterClient.postTweet(tweet, new TwitterClient.CreateTweetCallback() {
            @Override
            public void onSuccess() {
                getNewestTweets();
            }

            @Override
            public void onError(Throwable e) {
                new AlertDialog.Builder(HomeActivity.this)
                        .setMessage("Could not create a tweet.")
                        .setPositiveButton("OK", null)
                        .show();
                e.printStackTrace();
            }
        });
    }
}
