/*
 * Created by Iliiazbek Akhmedov
 * Copyright (c) 2016.
 */

package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.adapters.TweetsAdapter;
import com.codepath.apps.twitter.dialogs.CreateTweetDialog;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.models.UserProfile;
import com.codepath.apps.twitter.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class UserTimelineFragment extends Fragment implements CreateTweetDialog.TweetHandler {

    private static final String USER_ID = "USER_ID";
    private static final String SCREEN_NAME = "SCREEN_NAME";

    @BindView(R.id.profileImage)
    ImageView profileIV;
    @BindView(R.id.profileName)
    TextView profileNameTV;
    @BindView(R.id.screenName)
    TextView screenNameTV;
    @BindView(R.id.description)
    TextView descriptionTV;
    @BindView(R.id.followingTV)
    TextView followingTV;
    @BindView(R.id.followersTV)
    TextView followersTV;
    @BindView(R.id.tweetRV)
    RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private List<Tweet> tweetList;
    private TweetsAdapter tweetsAdapter;
    private TwitterClient twitterClient;
    private UserProfile userProfile;

    public UserTimelineFragment() {

    }

    public static UserTimelineFragment newInstance(String userId, String screenName) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString(USER_ID, userId);
        args.putString(SCREEN_NAME, screenName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        twitterClient = TwitterApplication.getTwitterClient();
        this.twitterClient = TwitterApplication.getTwitterClient();
        tweetList = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(getActivity(), tweetList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_timeline_fragment, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(tweetsAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchTweets(null, tweetList.get(tweetList.size() - 1).getId(), false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this::getNewestTweets);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TwitterClient.UserProfileCallback callback = new TwitterClient.UserProfileCallback() {
            @Override
            public void onSuccess(UserProfile userProfile) {
                UserTimelineFragment.this.userProfile = userProfile;
                setData();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };

        String userId = getArguments().getString(USER_ID);
        String screenName = getArguments().getString(SCREEN_NAME);
        swipeContainer.setRefreshing(true);

        if (null == screenName || null == userId) {
            twitterClient.getMyUserProfile(callback);
        } else {
            twitterClient.getUserProfile(userId, screenName, callback);
        }
    }

    private void setData() {
        Glide.with(this)
                .load(userProfile.getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(getContext(), 3, 3))
                .into(profileIV);

        String screenName = "@" + userProfile.getScreenName();

        profileNameTV.setText(userProfile.getName());
        screenNameTV.setText(screenName);
        descriptionTV.setText(userProfile.getDescription());

        String following = userProfile.getFriendsCount() + " Following";
        String followers = userProfile.getFollowersCount() + " Followers";

        followingTV.setText(following);
        followersTV.setText(followers);
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

        twitterClient.getUserTimeline(userProfile.getId(), sinceId, maxId, callback);
    }

    @Override
    public void onNewTweetSave(CreateTweetDialog dialog, String tweet) {
        getNewestTweets();
    }
}
