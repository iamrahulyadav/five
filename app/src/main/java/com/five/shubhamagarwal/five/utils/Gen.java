package com.five.shubhamagarwal.five.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.five.shubhamagarwal.five.BuildConfig;
import com.five.shubhamagarwal.five.MyApplication;
import com.five.shubhamagarwal.five.activities.CallActivity;
import com.five.shubhamagarwal.five.activities.CallStatusActivity;
import com.five.shubhamagarwal.five.activities.FiltersActivity;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shubhamagrawal on 03/04/17.
 */

public class Gen {
    public static final String TAG = Gen.class.getSimpleName();
    public static final String SERVER_URL = BuildConfig.SERVER_URL;
    public static void toast(String text){
        Toast.makeText(MyApplication.getAppContext(), text, Toast.LENGTH_SHORT).show();
    }
    private static ObjectMapper objectMapper;
    private static String USER_ID = "user_id";
    private static String SESSION_ID = "user_id";
    private static String FILTERS = "filters";

    private static String PREFS_NAME = "PreferencesFile";


    public static ObjectMapper getObjectMapper() {
        if(objectMapper == null){
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public static String getTimeDiffFromCurrentTime(long seconds) {
        long hour = seconds/3600;
        seconds = seconds%3600;
        long min = seconds/60;
        seconds = seconds%60;
        return String.format("%02d:%02d:%02d", hour, min, seconds);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String getJSONString(Object obj){
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showVolleyError(VolleyError error){
        Gen.showError(error);
        if(error.networkResponse != null){
            if (error.networkResponse.data != null) {
                try {
                    String body = new String(error.networkResponse.data, "UTF-8");
                    Log.e(TAG, body);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
    }

    public static void showError(Exception e){
        e.printStackTrace();
        toast("Some error occurred!!");
    }

    public static void saveUserIdToLocalStorage(String userId) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public static String getUserIdFromLocalStorage(){
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(USER_ID, "");
    }

    public static void saveSessionIdToLocalStorage(String userId) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SESSION_ID, userId);
        editor.commit();
    }

    public static String getSessionIdFromLocalStorage(){
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(SESSION_ID, "");
    }

    public static void saveFiltersToLocalStorage(boolean filters) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(FILTERS, filters);
        editor.commit();
    }

    public static Boolean getFiltersFromLocalStorage(){
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(FILTERS, false);
    }

    public static void startFiltersActivity(boolean clearStack) {
        Intent intent = new Intent(MyApplication.getAppContext(), FiltersActivity.class);
        if(clearStack){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        MyApplication.getAppContext().startActivity(intent);
    }

    public static void startCallStatusActivity(boolean clearStack) {
        Intent intent = new Intent(MyApplication.getAppContext(), CallStatusActivity.class);
        if(clearStack){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        MyApplication.getAppContext().startActivity(intent);
    }

    public static void startCallActivity(boolean clearStack) {
        Intent intent = new Intent(MyApplication.getAppContext(), CallActivity.class);
        if(clearStack){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        MyApplication.getAppContext().startActivity(intent);
    }
}
