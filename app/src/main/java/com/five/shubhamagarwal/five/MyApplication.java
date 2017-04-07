package com.five.shubhamagarwal.five;

import android.app.Application;
import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by shubhamagrawal on 04/04/17.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
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
