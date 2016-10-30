package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.oauth.OAuthLoginActionBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    @BindView(R.id.connectTwitterAccount)
    Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume(); //this is where client gets initialized in the super class
        initHandlers();
    }

    private void initHandlers() {
        if (getClient().isAuthenticated()) {
            openHome();
        } else {
            connectButton.setOnClickListener((v) -> getClient().connect());
        }
    }

    @Override
    public void onLoginSuccess() {
        openHome();
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }
}
