package com.codepath.apps.twitter.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 10/30/16.
 * Copyright - 2015
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rootView)
    public View rootView;
    @BindView(R.id.content)
    public TextView content;
    @BindView(R.id.screenName)
    public TextView screenName;
    @BindView(R.id.author)
    public TextView author;
    @BindView(R.id.timePosted)
    public TextView timePosted;
    @BindView(R.id.profileImage)
    public ImageView profileImage;
    @BindView(R.id.retweetCount)
    public TextView retweetCount;
    @BindView(R.id.favCount)
    public TextView favCount;

    public TweetViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }
}
