package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.adapters.HomePageTabsAdapter;
import com.codepath.apps.twitter.dialogs.CreateTweetDialog;
import com.codepath.apps.twitter.fragments.HomeFeedFragment;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements CreateTweetDialog.TweetHandler, HomeFeedFragment.TweetTabListener,
        UserTimelineFragment.Listener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.slidingTabs)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private TwitterClient twitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        twitterClient = TwitterApplication.getTwitterClient();

        HomePageTabsAdapter adapter = new HomePageTabsAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(v -> new CreateTweetDialog().show(getSupportFragmentManager(), "CreateTweet"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                performSearch(query);
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
            case R.id.sign_out: {
                signOut();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void performSearch(String query) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(SearchActivity.SEARCH, query);
        startActivity(intent);
    }

    @Override
    public void onNewTweetSave(CreateTweetDialog dialog, String tweet) {
        dialog.dismiss();

        twitterClient.postTweet(tweet, new TwitterClient.CreateTweetCallback() {
            @Override
            public void onSuccess() {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();

                for (Fragment fr : fragments) {
                    if (fr instanceof CreateTweetDialog.TweetHandler) {
                        ((CreateTweetDialog.TweetHandler) fr).onNewTweetSave(dialog, tweet);
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Could not create a tweet.")
                        .setPositiveButton("OK", null)
                        .show();
                e.printStackTrace();
            }
        });
    }

    @Override
    public void tweetTabInteract() {

    }

    @Override
    public void userTimelineInteract() {

    }

    private void signOut() {
        new AlertDialog.Builder(this)
                .setMessage("Do you really want to sign out?")
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.signout, (dialog, which) -> {
                    twitterClient.clearAccessToken();
                    startActivity(new Intent(MainActivity.this, SplashActivity.class));
                    finish();
                })
                .show();
    }
}
