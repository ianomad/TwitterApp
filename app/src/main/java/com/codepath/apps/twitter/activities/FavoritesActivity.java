/*
 * Created by Iliiazbek Akhmedov
 * Copyright (c) 2016.
 */

package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.adapters.TweetsAdapter;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tweetRV)
    RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private List<Tweet> tweetList;
    private TweetsAdapter tweetsAdapter;
    private TwitterClient twitterClient;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(R.string.favorites);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.twitterClient = TwitterApplication.getTwitterClient();
        tweetList = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweetList);
        swipeContainer.setRefreshing(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(tweetsAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchTweets(null, tweetList.get(tweetList.size() - 1).getId(), false);
            }
        };

        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(scrollListener);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this::getNewestTweets);
        fetchTweets(null, null, false);
    }

    private void getNewestTweets() {
        tweetList.clear();
        tweetsAdapter.notifyDataSetChanged();
        scrollListener.resetState();

        fetchTweets(null, null, true);
    }

    private void fetchTweets(Long sinceId, Long maxId, boolean appendTop) {
        TwitterClient.TweetsCallback callback = new TwitterClient.TweetsCallback() {
            @Override
            public void onSuccess(List<Tweet> res) {
                swipeContainer.setRefreshing(false);

                if (appendTop) {
                    tweetList.addAll(0, res);
                } else {
                    tweetList.addAll(res);
                }

                tweetsAdapter.notifyDataSetChanged();

                if (appendTop) {
                    recyclerView.smoothScrollToPosition(0);
                }
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        };

        swipeContainer.setRefreshing(true);
        twitterClient.getFavorites(sinceId, maxId, callback);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
