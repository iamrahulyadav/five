package com.five.shubhamagarwal.five.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import com.five.shubhamagarwal.five.activities.LoginActivity;
import com.five.shubhamagarwal.five.activities.RatingsActivity;

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
            totalHeight += TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, MyApplication.getAppContext().getResources().getDisplayMetrics());
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
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.USER_ID, userId);
        editor.commit();
    }

    public static String getUserIdFromLocalStorage(){
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(Constants.PREFS_NAME, 0);
        return settings.getString(Constants.USER_ID, "");
    }

    public static void saveSessionIdToLocalStorage(String userId) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.SESSION_ID, userId);
        editor.commit();
    }

    public static String getSessionIdFromLocalStorage(){
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(Constants.PREFS_NAME, 0);
        return settings.getString(Constants.SESSION_ID, "");
    }

    public static void saveFiltersToLocalStorage(boolean filters) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Constants.FILTERS, filters);
        editor.commit();
    }

    public static Boolean getFiltersFromLocalStorage(){
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(Constants.PREFS_NAME, 0);
        return settings.getBoolean(Constants.FILTERS, false);
    }

    private static void startActivity(Intent intent, boolean clearStack){
        if(clearStack){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        MyApplication.getAppContext().startActivity(intent);
    }

    public static void startLoginActivity(boolean clearStack) {
        Intent intent = new Intent(MyApplication.getAppContext(), LoginActivity.class);
        startActivity(intent, clearStack);
    }

    public static void startLoginActivity(boolean clearStack, String key, String value) {
        Intent intent = new Intent(MyApplication.getAppContext(), LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        intent.putExtras(bundle);
        startActivity(intent, clearStack);
    }

    public static void startFiltersActivity(boolean clearStack) {
        Intent intent = new Intent(MyApplication.getAppContext(), FiltersActivity.class);
        startActivity(intent, clearStack);
    }

    public static void startFiltersActivity(boolean clearStack, String key, String value) {
        Intent intent = new Intent(MyApplication.getAppContext(), FiltersActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        intent.putExtras(bundle);
        startActivity(intent, clearStack);
    }

    public static void startCallStatusActivity(boolean clearStack) {
        Intent intent = new Intent(MyApplication.getAppContext(), CallStatusActivity.class);
        startActivity(intent, clearStack);
    }


    public static void startCallStatusActivity(boolean clearStack, String key, String value) {
        Intent intent = new Intent(MyApplication.getAppContext(), CallStatusActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        intent.putExtras(bundle);
        startActivity(intent, clearStack);
    }

    public static void startCallActivity(boolean clearStack, String key, String value) {
        Intent intent = new Intent(MyApplication.getAppContext(), CallActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        intent.putExtras(bundle);
        startActivity(intent, clearStack);
    }

    public static void startCallActivity(boolean clearStack) {
        Intent intent = new Intent(MyApplication.getAppContext(), CallActivity.class);
        startActivity(intent, clearStack);
    }

    public static void startRatingsActivity(boolean clearStack) {
        Intent intent = new Intent(MyApplication.getAppContext(), RatingsActivity.class);
        startActivity(intent, clearStack);
    }

    public static void startRatingsActivity(boolean clearStack, String key, String value) {
        Intent intent = new Intent(MyApplication.getAppContext(), RatingsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        intent.putExtras(bundle);
        startActivity(intent, clearStack);
    }
}
