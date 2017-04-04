package com.five.shubhamagarwal.five.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.utils.Gen;
import com.five.shubhamagarwal.five.utils.JsonObjectRequestWithAuth;
import com.five.shubhamagarwal.five.utils.VolleySingelton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;


public class CallStatusActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CallActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_status);
    }
    private static TextView mkeepCalm1, mkeepCalm2, mTimerView;
    private static Button mCallButton;

    public interface CallStatus{
        String NO_CALL = "Till we schedule your next call.";
        String CALL_IN = "Your next call in ...";
    }
    @Override
    protected void onStart() {
        super.onStart();

        mCallButton = (Button) findViewById(R.id.call_button);
        mkeepCalm1 = (TextView) findViewById(R.id.keep_calm_text1);
        mkeepCalm2 = (TextView) findViewById(R.id.keep_calm_text2);
        mTimerView = (TextView) findViewById(R.id.timer_id);

        mCallButton.setVisibility(View.INVISIBLE);
        mCallButton.setOnClickListener(this);

        RequestQueue requestQueue = VolleySingelton.getInstance().getRequestQueue();
        JSONObject postData = null;
        final JsonObjectRequest request = new JsonObjectRequestWithAuth(Request.Method.POST, Gen.SERVER_URL + "/next_chat", postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    if(response.isNull("chat")){
                        // there is no chat scheduled in future
                        mkeepCalm2.setText(CallStatus.NO_CALL);
                        return;
                    }
                    JSONObject chatJSON = response.getJSONObject("chat");
                    int timeLeftForChatStart = chatJSON.getInt("seconds_left_for_chat_start");
                    if(timeLeftForChatStart == 0){
                        // Chat is started or has already started
                        canCallNow();
                    } else {
                        mkeepCalm2.setText(CallStatus.CALL_IN);
                        startCounter(timeLeftForChatStart);
                    }
                } catch (Exception e){
                    Gen.showError(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.data!=null) {
                    try {
                        String body = new String(error.networkResponse.data,"UTF-8");
                        Log.e(TAG, body);
                    } catch (UnsupportedEncodingException e) {
                        Gen.showError(e);
                    }
                }
            }
        });
        requestQueue.add(request);
    }

    public void startCounter(int inSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3);
        final Date scheduledTime = calendar.getTime();

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while ((scheduledTime.getTime() - Calendar.getInstance().getTime().getTime()) >= 0) {
                        final Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Date currentTime = Calendar.getInstance().getTime();
                                String time = Gen.getTimeDiffFromCurrentTime(scheduledTime);
                                mTimerView.setText(time);
                                if ((scheduledTime.getTime() - currentTime.getTime())/1000 <= 0){
                                    canCallNow();
                                }
                            }
                        });
                        runOnUiThread(tr);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    public void canCallNow() {
        mCallButton.setVisibility(View.VISIBLE);

        mTimerView.setVisibility(View.INVISIBLE);
        mkeepCalm1.setVisibility(View.INVISIBLE);
        mkeepCalm2.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call_button: {
                Gen.toast("We are connecting you now ...");
                Intent intent =  new Intent(this, CallActivity.class);
                startActivity(intent);
            }
        }
    }
}
