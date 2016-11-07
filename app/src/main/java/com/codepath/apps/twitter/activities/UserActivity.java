/*
 * Created by Iliiazbek Akhmedov
 * Copyright (c) 2016.
 */

package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;
import com.codepath.apps.twitter.models.User;

import org.parceler.Parcels;

public class UserActivity extends AppCompatActivity {

    public static final String USER = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User user = Parcels.unwrap(getIntent().getExtras().getParcelable(USER));

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(user.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        UserTimelineFragment fragment = UserTimelineFragment.newInstance(user.getId(), user.getScreenName());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
