package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

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

    private TwitterClient twitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twitterClient = TwitterApplication.getTwitterClient();

        ButterKnife.bind(this);

        HomePageTabsAdapter adapter = new HomePageTabsAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(v -> new CreateTweetDialog().show(getSupportFragmentManager(), "CreateTweet"));
    }

    @Override
    public void onNewTweetSave(CreateTweetDialog dialog, String tweet) {
        dialog.dismiss();

        twitterClient.postTweet(tweet, new TwitterClient.CreateTweetCallback() {
            @Override
            public void onSuccess() {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();

                for(Fragment fr : fragments) {
                    if(fr instanceof CreateTweetDialog.TweetHandler) {
                        ((CreateTweetDialog.TweetHandler)fr).onNewTweetSave(dialog, tweet);
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
}
