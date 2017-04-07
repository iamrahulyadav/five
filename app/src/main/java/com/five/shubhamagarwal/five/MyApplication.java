package com.five.shubhamagarwal.five;

import android.app.Application;
import android.content.Context;

import com.five.shubhamagarwal.five.utils.Gen;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by shubhamagrawal on 04/04/17.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
