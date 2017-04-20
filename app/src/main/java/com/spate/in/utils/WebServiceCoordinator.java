package com.spate.in.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.spate.in.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubhamagrawal on 02/04/17.
 */

public class WebServiceCoordinator {

    private static final String SERVER_URL = BuildConfig.SERVER_URL;
    private static final String SESSION_INFO_ENDPOINT = SERVER_URL + "/get_session";
    private static final String LOG_TAG = WebServiceCoordinator.class.getSimpleName();

    private final Context context;
    private Listener delegate;

    public WebServiceCoordinator(Context context, Listener delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    public void fetchSessionConnectionData() {
        RequestQueue reqQueue = Volley.newRequestQueue(context);
        JSONObject postData = null;
        reqQueue.add(new JsonObjectRequestWithAuth(Request.Method.POST, SESSION_INFO_ENDPOINT, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject session = response.getJSONObject("session");
                    String apiKey = session.getString("apiKey");
                    String sessionId = session.getString("sessionId");
                    String token = session.getString("token");
                    Gen.saveSessionIdToLocalStorage(sessionId);

                    delegate.onSessionConnectionDataReady(apiKey, sessionId, token);
                } catch (JSONException e) {
                    Gen.showError(e);
                    delegate.onWebServiceCoordinatorError(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                delegate.onWebServiceCoordinatorError(error);
            }
        }));
    }

    public static interface Listener {
        void onSessionConnectionDataReady(String apiKey, String sessionId, String token);
        void onWebServiceCoordinatorError(Exception error);
    }
}

