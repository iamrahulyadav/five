package com.five.shubhamagarwal.five.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.five.shubhamagarwal.five.MyApplication;

/**
 * Created by shubhamagrawal on 07/04/17.
 */

public class PermissionUtil{

    public static void showExplanation(String title, String message, final String permission, final int permissionRequestCode, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getAppContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode, activity);
                    }
                });
        builder.create().show();
    }

    public static void requestPermission(String permissionName, int permissionRequestCode, Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{permissionName}, permissionRequestCode);
    }

    public static void showPermission(String permission, Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                PermissionUtil.showExplanation("Permission Needed", "Rationale", permission, Constants.CAMERA_AUDIO_WAKE_LOCK, activity);
            } else {
                PermissionUtil.requestPermission(permission, Constants.CAMERA_AUDIO_WAKE_LOCK, activity);
            }
        } else {
            Gen.toast("Permission already granted!!");
        }
    }
}
