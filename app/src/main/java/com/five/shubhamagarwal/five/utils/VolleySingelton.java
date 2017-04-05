package com.five.shubhamagarwal.five.utils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.five.shubhamagarwal.five.MyApplication;

/**
 * Created by shubhamagrawal on 04/04/17.
 */

public class VolleySingelton {
    private static VolleySingelton instance = null;
    private RequestQueue requestQueue;

    private VolleySingelton() {
        this.requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public static VolleySingelton getInstance() {
        if (instance == null) {
            instance = new VolleySingelton();
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
