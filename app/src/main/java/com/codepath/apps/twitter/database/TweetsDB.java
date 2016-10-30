package com.codepath.apps.twitter.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = TweetsDB.NAME, version = TweetsDB.VERSION)
public class TweetsDB {
    static final String NAME = "TwitterDataBase";
    static final int VERSION = 1;
}
