package com.codepath.apps.twitter.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.activities.UserActivity;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.views.TweetViewHolder;

import org.ocpsoft.prettytime.PrettyTime;
import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by akhmedovi on 10/30/16.
 * Copyright - 2015
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private static final int WITHOUT_IMAGE = 1;
    private static final int WITH_IMAGE = 2;

    private Activity activity;
    private List<Tweet> tweetList;
    private PrettyTime prettyTime;
    private TwitterClient twitterClient;

    public TweetsAdapter(Activity activity, List<Tweet> tweetList) {
        this.tweetList = tweetList;
        this.activity = activity;
        this.prettyTime = new PrettyTime(Locale.US);
        this.twitterClient = TwitterApplication.getTwitterClient();
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == WITHOUT_IMAGE) {
            view = LayoutInflater.from(activity).inflate(R.layout.tweet_item, parent, false);
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.tweet_item_with_image, parent, false);
        }

        return new TweetViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (null == tweetList.get(position).getImageUrl()) {
            return WITHOUT_IMAGE;
        }

        return WITH_IMAGE;
    }

    private Tweet getItem(int position) {
        return tweetList.get(position);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder h, int position) {
        Tweet tweet = getItem(position);
        String screenName = "@" + tweet.getUser().getScreenName();

        h.content.setText(tweet.getText());
        h.timePosted.setText(prettyTime.format(tweet.getCreatedAt()));
        h.author.setText(tweet.getUser().getName());
        h.screenName.setText(screenName);
        h.retweetCount.setText(String.valueOf(tweet.getRetweetCount()));

        Glide.with(activity)
                .load(tweet.getUser().getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(activity, 3, 3))
                .into(h.profileImage);

        setFavoriteData(h, tweet);

        h.favCount.setOnClickListener((v) -> {
            int favCount = tweet.getFavCount();
            if (tweet.isFavorited()) {
                twitterClient.removeFavorite(tweet.getId());
                tweet.setFavorited(false);
                favCount--;
            } else {
                twitterClient.addFavorite(tweet.getId());
                tweet.setFavorited(true);
                favCount++;
            }

            tweet.setFavCount(favCount);
            setFavoriteData(h, tweet);
        });

        h.more.setOnClickListener((v) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ + " + tweet.getId()));
            activity.startActivity(browserIntent);
        });

        h.profileImage.setOnClickListener((v -> {
            Intent intent = new Intent(activity, UserActivity.class);
            intent.putExtra(UserActivity.USER, Parcels.wrap(tweet.getUser()));
            activity.startActivity(intent);
        }));

        if (null != tweet.getImageUrl() && null != h.imageView) {
            Glide.with(activity)
                    .load(tweet.getImageUrl())
                    .bitmapTransform(new RoundedCornersTransformation(activity, 3, 3))
                    .into(h.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    private void setFavoriteData(TweetViewHolder h, Tweet tweet) {
        if (tweet.isFavorited()) {
            DrawableCompat.setTint(h.favCount.getCompoundDrawables()[1],
                    ResourcesCompat.getColor(activity.getResources(), R.color.exit, null));
        } else {
            DrawableCompat.setTint(h.favCount.getCompoundDrawables()[1],
                    ResourcesCompat.getColor(activity.getResources(), R.color.grey, null));
        }

        h.favCount.setText(String.valueOf(tweet.getFavCount()));
    }
}
