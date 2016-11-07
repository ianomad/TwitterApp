package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.adapters.HomePageTabsAdapter;
import com.codepath.apps.twitter.dialogs.CreateTweetDialog;
import com.codepath.apps.twitter.fragments.HomeFeedFragment;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;
import com.codepath.apps.twitter.models.UserProfile;
import com.codepath.apps.twitter.views.NavHeaderViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MainActivity extends AppCompatActivity implements CreateTweetDialog.TweetHandler {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.slidingTabs)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navView)
    NavigationView navView;

    NavHeaderViewHolder navHeaderViewHolder;

    private ActionBarDrawerToggle drawerToggle;
    private TwitterClient twitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        navView.setItemIconTintList(null);

        setSupportActionBar(toolbar);

        twitterClient = TwitterApplication.getTwitterClient();

        HomePageTabsAdapter adapter = new HomePageTabsAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //not to reload my profile over and over
        viewPager.setOffscreenPageLimit(2);

        setupNavView();

        fab.setOnClickListener(v -> new CreateTweetDialog().show(getSupportFragmentManager(), "CreateTweet"));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        navHeaderViewHolder = new NavHeaderViewHolder(navView.getHeaderView(0));
        loadProfileData();
    }

    void setupNavView() {
        navView.setNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
    }

    private void loadProfileData() {
        twitterClient.getMyUserProfile(new TwitterClient.UserProfileCallback() {
            @Override
            public void onSuccess(UserProfile userProfile) {
                Glide.with(MainActivity.this)
                        .load(userProfile.getProfileImageUrl())
                        .placeholder(R.drawable.empty)
                        .bitmapTransform(new RoundedCornersTransformation(MainActivity.this, 3, 3))
                        .into(navHeaderViewHolder.profileImage);

                String screenName = "@" + userProfile.getScreenName();

                navHeaderViewHolder.profileNameTV.setText(userProfile.getName());
                navHeaderViewHolder.screenNameTV.setText(screenName);
                navHeaderViewHolder.descriptionTV.setText(userProfile.getDescription());

                String following = userProfile.getFriendsCount() + " Following";
                String followers = userProfile.getFollowersCount() + " Followers";

                navHeaderViewHolder.followingTV.setText(following);
                navHeaderViewHolder.followersTV.setText(followers);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.sign_out: {
                signOut();
                return;
            }
            case R.id.favorites: {
                startActivity(new Intent(this, FavoritesActivity.class));
            }
            default: {

            }
        }
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        drawerToggle.onConfigurationChanged(newConfig);
    }
}
