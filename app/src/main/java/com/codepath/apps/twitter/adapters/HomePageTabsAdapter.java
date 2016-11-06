/*
 * Created by Iliiazbek Akhmedov
 * Copyright (c) 2016.
 */

package com.codepath.apps.twitter.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.fragments.HomeFeedFragment;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;

public class HomePageTabsAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[]{"Timeline", "Mentions", "Me"};
    private Context context;
    private TwitterClient twitterClient;

    public HomePageTabsAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
        this.twitterClient = TwitterApplication.getTwitterClient();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch (position) {
            case 0: {
                fragment = HomeFeedFragment.newInstance(HomeFeedFragment.Feed.HOME);
                break;
            }
            case 1: {
                fragment = HomeFeedFragment.newInstance(HomeFeedFragment.Feed.MENTIONS);
                break;
            }
            default: {
                fragment = UserTimelineFragment.newInstance(null, null);
            }
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
