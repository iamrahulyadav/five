package com.five.shubhamagarwal.five.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by shubhamagrawal on 03/04/17.
 */

public class Gen {
    public static void toast(String text, Context activity){
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
    }

    public static String getTimeDiffFromCurrentTime(Date date) {
        Date currentTime = Calendar.getInstance().getTime();
        long seconds = (date.getTime() - currentTime.getTime())/1000;
        long hour = seconds/3600;
        seconds = seconds%3600;
        long min = seconds/60;
        seconds = seconds%60;
        return String.format("%02d:%02d:%02d", hour, min, seconds);
    }
}
