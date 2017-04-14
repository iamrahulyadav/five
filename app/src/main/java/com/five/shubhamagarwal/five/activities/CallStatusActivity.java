package com.five.shubhamagarwal.five.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.login.LoginManager;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.utils.Constants;
import com.five.shubhamagarwal.five.utils.Gen;
import com.five.shubhamagarwal.five.utils.JsonObjectRequestWithAuth;
import com.five.shubhamagarwal.five.utils.VolleySingelton;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;


public class CallStatusActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CallActivity.class.getSimpleName();
    public static final String SECONDS_LEFT = "seconds_left_for_chat_start";
    public static final String CHAT_END_TIME_KEY = "chat_end_time";
    public static final String USER = "user";
    public static final String GENDER = "gender";

    private CountDownTimer waitTimer;
    private String chat_end;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_status);
    }

    private static TextView mkeepCalm1, mkeepCalm2, mTimerView;
    private static Button mCallButton;

    public interface CallStatus {
        String NO_CALL = "till we schedule your next call within next 24 hours";
        String CALL_IN = "Your next call is in ...";
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

        Gen.showLoader(this);
        final Activity activity = this;
        final JsonObjectRequest request = new JsonObjectRequestWithAuth(Request.Method.POST, Gen.SERVER_URL + "/next_chat", postData,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gen.hideLoader(activity);
                try {
                    if (response.isNull("chat")) {
                        // there is no chat scheduled in future
                        mkeepCalm2.setText(CallStatus.NO_CALL);
                        return;
                    }
                    JSONObject chatJSON = response.getJSONObject("chat");
                    chat_end = chatJSON.getString(CHAT_END_TIME_KEY);
                    int timeLeftForChatStart = chatJSON.getInt(SECONDS_LEFT);
                    gender = chatJSON.getJSONObject(USER).getString(GENDER);

                    if (timeLeftForChatStart == 0) {
                        // Chat is started or has already started
                        canCallNow();
                    } else {
                        mkeepCalm2.setText(CallStatus.CALL_IN);
                        startCounter(timeLeftForChatStart);
                    }
                } catch (Exception e) {
                    Gen.showError(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Gen.hideLoader(activity);
                Gen.showVolleyError(error);
            }
        });
        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, R.id.menu_action_filter, Menu.NONE, R.string.menu_action_filter);
        menu.add(Menu.NONE, R.id.menu_action_logout, Menu.NONE, R.string.menu_action_logout);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_filter:{
                Gen.startActivity(this, false, FiltersActivity.class);
                return true;
            }
            case R.id.menu_action_logout:{
                LoginManager.getInstance().logOut();
                Gen.startActivity(this, false, LoginActivity.class, Constants.SHOW_LOGOUT_SCREEN, "true");
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startCounter(final int inSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, inSeconds);

        if(waitTimer != null) {
            waitTimer.cancel();
            waitTimer = null;
        }

        waitTimer = new CountDownTimer(inSeconds*1000, 1000) {

            long sec = inSeconds;

            public void onTick(long millisUntilFinished) {
                String time = Gen.getTimeDiffFromCurrentTime(sec);
                mTimerView.setText(time);
                sec--;
            }

            public void onFinish() {
                canCallNow();
            }
        }.start();
    }

    public void canCallNow() {
        mCallButton.setVisibility(View.VISIBLE);
        mTimerView.setVisibility(View.INVISIBLE);
        mkeepCalm1.setVisibility(View.INVISIBLE);
        mkeepCalm2.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_button: {
                Gen.toast("We are connecting you now ...");
                Intent intent = new Intent(this, CallActivity.class);
                intent.putExtra(CHAT_END_TIME_KEY, chat_end);
                intent.putExtra(GENDER, gender);
                startActivity(intent);
            }
        }
    }
}
