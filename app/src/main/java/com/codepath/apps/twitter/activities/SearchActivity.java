/*
 * Created by Iliiazbek Akhmedov
 * Copyright (c) 2016.
 */

package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

public class SearchActivity extends AppCompatActivity {

    public static final String SEARCH = "SEARCH";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tweetRV)
    RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private SearchView searchView;
    private EndlessRecyclerViewScrollListener scrollListener;

    private List<Tweet> tweetList;
    private TweetsAdapter tweetsAdapter;
    private TwitterClient twitterClient;
    private String query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(R.string.search);
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
        this.query = getIntent().getStringExtra(SEARCH);
        fetchTweets(null, null, false);
    }

    private void getNewestTweets() {
        Long newestId = !tweetList.isEmpty() ? tweetList.get(0).getId() : 1L;
        fetchTweets(newestId, null, true);
    }

    private void fetchTweets(Long sinceId, Long maxId, boolean appendTop) {
        TwitterClient.TweetsCallback callback = new TwitterClient.TweetsCallback() {
            @Override
            public void onSuccess(List<Tweet> res) {
                if (appendTop) {
                    tweetList.addAll(0, res);
                } else {
                    tweetList.addAll(res);
                }

                tweetsAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

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
        twitterClient.getBySearchKey(query, sinceId, maxId, callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                tweetList.clear();
                tweetsAdapter.notifyDataSetChanged();
                scrollListener.resetState();

                SearchActivity.this.query = query;
                fetchTweets(null, null, false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
