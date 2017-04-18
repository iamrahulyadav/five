package com.five.shubhamagarwal.five.utils;

import android.os.Bundle;

import com.five.shubhamagarwal.five.MyApplication;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by shubhamagrawal on 19/04/17.
 */

public class AnalyticsManager {
    interface Event{
        String ERROR = "Error";
        String ACTIVITY_LAUNCHED = "Activity launched";
    }

    interface  Param{
        String STACK_TRACE = "Stack trace";
        String ERROR_BODY = "Error body";
    }

    public static FirebaseAnalytics getInstance(){
        return FirebaseAnalytics.getInstance(MyApplication.getAppContext());
    }

    public static void log(String eventName, Bundle extraInfo){
        getInstance().logEvent(eventName, extraInfo);
    }

    public static void log(String eventName, String key, String val){
        Bundle bundle = new Bundle();
        bundle.putString(key, val);
        log(eventName, bundle);
    }
}
