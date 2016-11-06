package com.codepath.apps.twitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class HomeFeedFragment extends Fragment implements CreateTweetDialog.TweetHandler {

    public enum Feed {
        HOME, MENTIONS
    }

    private TweetTabListener listener;

    @BindView(R.id.tweetRV)
    RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private List<Tweet> tweetList;
    private TweetsAdapter tweetsAdapter;
    private TwitterClient twitterClient;
    private Feed feed;

    public HomeFeedFragment() {
    }

    public static HomeFeedFragment newInstance(@NonNull Feed feed) {
        HomeFeedFragment fragment = new HomeFeedFragment();
        Bundle args = new Bundle();
        args.putString("FEED", String.valueOf(feed));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.twitterClient = TwitterApplication.getTwitterClient();
        tweetList = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(getActivity(), tweetList);


        feed = Feed.valueOf(getArguments().getString("FEED"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tweet_timeline_fragment, container, false);
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

        if (feed == Feed.HOME) {
            twitterClient.getHomeTimeline(sinceId, maxId, callback);
        } else {
            twitterClient.getMentionsTimeline(sinceId, maxId, callback);
        }
    }

    @Override
    public void onNewTweetSave(CreateTweetDialog dialog, String tweet) {
        getNewestTweets();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof TweetTabListener) {
            listener = (TweetTabListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TweetTabListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface TweetTabListener {
        void tweetTabInteract();
    }
}
