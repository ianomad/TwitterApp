package com.codepath.apps.twitter.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.twitter.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 10/30/16.
 * Copyright - 2015
 */

public class CreateTweetDialog extends DialogFragment {

    @BindView(R.id.tweetEt)
    EditText tweetEt;
    @BindView(R.id.tweetBtn)
    Button tweetBtn;
    @BindView(R.id.leftCharCount)
    TextView leftCharCount;

    public CreateTweetDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.view_create_tweet, (ViewGroup) getView(), false);

        ButterKnife.bind(this, dialogView);
        dialogBuilder.setView(dialogView);

        tweetEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                leftCharCount.setText(String.valueOf(140 - tweetEt.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tweetBtn.setOnClickListener((v) -> {
            ((TweetHandler)getActivity()).onNewTweetSave(this, tweetEt.getText().toString());
        });

        return dialogBuilder.create();
    }

    public interface TweetHandler {
        public void onNewTweetSave(CreateTweetDialog dialog, String tweet);
    }
}
