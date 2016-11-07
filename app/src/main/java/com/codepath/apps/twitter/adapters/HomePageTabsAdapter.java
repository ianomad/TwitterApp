/*
 * Created by Iliiazbek Akhmedov
 * Copyright (c) 2016.
 */

package com.codepath.apps.twitter.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.fragments.HomeFeedFragment;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;

public class HomePageTabsAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[]{"Timeline", "Mentions", "Me"};
    private Integer tabImages[] = new Integer[]{R.drawable.home, R.drawable.notifications, R.drawable.person};
    private Context context;

    public HomePageTabsAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
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

        Drawable image = ContextCompat.getDrawable(context, tabImages[position]);
        image.setBounds(0, 0, (int) (image.getIntrinsicWidth() * 0.8), (int) (image.getIntrinsicHeight() * 0.8));

        SpannableString sb = new SpannableString(" " + tabTitles[position]);

        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
}
