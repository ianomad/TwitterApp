package com.codepath.apps.twitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.views.TweetViewHolder;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by akhmedovi on 10/30/16.
 * Copyright - 2015
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    static final int WITHOUTIMAGE = 1;
    static final int WITHIMAGE = 2;

    Context context;
    List<Tweet> tweetList;
    PrettyTime prettyTime;

    public TweetsAdapter(Context context, List<Tweet> tweetList) {
        this.tweetList = tweetList;
        this.context = context;
        this.prettyTime = new PrettyTime(Locale.US);
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == WITHOUTIMAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.tweet_item, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.tweet_item_with_image, parent, false);
        }

        return new TweetViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (null == tweetList.get(position).getImageUrl()) {
            return WITHOUTIMAGE;
        }

        return WITHIMAGE;
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
        h.favCount.setText(String.valueOf(tweet.getFavCount()));
        h.retweetCount.setText(String.valueOf(tweet.getRetweetCount()));

        Glide.with(context)
                .load(tweet.getUser().getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, 3, 3))
                .into(h.profileImage);

        h.favCount.setOnClickListener((v) -> {
            Toast.makeText(context, "You have liked this...", Toast.LENGTH_SHORT).show();
        });

        h.more.setOnClickListener((v) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ + " + tweet.getId()));
            context.startActivity(browserIntent);
        });

        if (null != tweet.getImageUrl() && null != h.imageView) {
            Glide.with(context)
                    .load(tweet.getImageUrl())
                    .bitmapTransform(new RoundedCornersTransformation(context, 3, 3))
                    .into(h.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public void addTweets(int index, List<Tweet> tweets) {
        tweetList.addAll(index, tweets);
    }

    public void addTweets(List<Tweet> tweets) {
        tweetList.addAll(tweets);
    }
}
