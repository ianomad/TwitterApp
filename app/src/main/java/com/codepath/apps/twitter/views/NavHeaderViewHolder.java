/*
 * Created by Iliiazbek Akhmedov
 * Copyright (c) 2016.
 */

package com.codepath.apps.twitter.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavHeaderViewHolder {

    @BindView(R.id.profileImage)
    public ImageView profileImage;
    @BindView(R.id.profileName)
    public TextView profileNameTV;
    @BindView(R.id.screenName)
    public TextView screenNameTV;
    @BindView(R.id.description)
    public TextView descriptionTV;
    @BindView(R.id.followingTV)
    public TextView followingTV;
    @BindView(R.id.followersTV)
    public TextView followersTV;

    public NavHeaderViewHolder(View itemView) {
        ButterKnife.bind(this, itemView);
    }
}
