package com.five.shubhamagarwal.five;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.five.shubhamagarwal.five.activities.CallStatusActivity;
import com.five.shubhamagarwal.five.activities.FiltersActivity;
import com.five.shubhamagarwal.five.activities.LoginActivity;
import com.five.shubhamagarwal.five.utils.Constants;
import com.five.shubhamagarwal.five.utils.Gen;
import com.five.shubhamagarwal.five.utils.JsonObjectRequestWithAuth;
import com.five.shubhamagarwal.five.utils.VolleySingelton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubhamagrawal on 04/04/17.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RequestQueue requestQueue = VolleySingelton.getInstance().getRequestQueue();
        JSONObject postData = null;
        try {
            postData = getPostData();
        } catch (JSONException e) {
            Gen.showError(e);
        }
        JsonObjectRequest request = new JsonObjectRequestWithAuth(Request.Method.POST, Constants.SERVER_URL + "/user", postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MyApplication", "success");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Gen.showVolleyError(error);
            }
        });
        requestQueue.add(request);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    private JSONObject getPostData() throws JSONException {
        JSONObject js = new JSONObject();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            js.put("firebase_user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());

        String token = FirebaseInstanceId.getInstance().getToken();
        if(token != null){
            Gen.saveFCMTokenToLocalStorage(token);
        }
        js.put("fcm_token", Gen.getFCMTokenFromLocalStorage());
        return js;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
