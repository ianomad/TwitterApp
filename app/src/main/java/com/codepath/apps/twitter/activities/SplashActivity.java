package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.oauth.OAuthLoginActionBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    @BindView(R.id.connectTwitterAccount)
    Button connectButton;
    @BindView(R.id.errorLabel)
    TextView errorLabel;

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
            fadeIn(connectButton);
            connectButton.setOnClickListener((v) -> {
                getClient().connect();
                fadeOut(connectButton);
                fadeOut(errorLabel);
            });
        }
    }

    @Override
    public void onLoginSuccess() {
        openHome();
    }

    private void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();

        fadeIn(connectButton);
        fadeIn(errorLabel);
    }

    void fadeIn(View view) {
        view.setAlpha(0);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1).setDuration(300).start();
    }

    void fadeOut(View view) {
        view.setAlpha(1);
        view.animate().alpha(0).setDuration(300).withEndAction(() -> view.setVisibility(View.GONE)).start();
    }
}
