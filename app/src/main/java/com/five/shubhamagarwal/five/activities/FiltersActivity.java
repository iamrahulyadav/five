package com.five.shubhamagarwal.five.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.utils.Gen;
import com.five.shubhamagarwal.five.utils.JsonObjectRequestWithAuth;
import com.five.shubhamagarwal.five.utils.VolleySingelton;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class FiltersActivity extends AppCompatActivity {

    private static final String TAG = FiltersActivity.class.getSimpleName();
    CheckBox mMale, mFemale, mCasual, mRelationship, mLove, mFriendship, mAction;
    RangeSeekBar mAgeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        mMale = (CheckBox) findViewById(R.id.male_checkbox);
        mFemale = (CheckBox) findViewById(R.id.female_checkbox);
        mCasual = (CheckBox) findViewById(R.id.casual_checkbox);
        mRelationship = (CheckBox) findViewById(R.id.relationship_checkbox);
        mLove = (CheckBox) findViewById(R.id.love_checkbox);
        mFriendship = (CheckBox) findViewById(R.id.friendship_checkbox);
        mAction = (CheckBox) findViewById(R.id.action_checkbox);
        mAgeBar = (RangeSeekBar) findViewById(R.id.age_seekbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filters_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.filters_save){
            RequestQueue requestQueue = VolleySingelton.getInstance().getRequestQueue();
            JSONObject postData = null;
            try {
                postData = getPostData();
            } catch (JSONException e) {
                Gen.showError(e);
            }
            JsonObjectRequest request = new JsonObjectRequestWithAuth(Request.Method.POST, Gen.SERVER_URL + "/update_user_details", postData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    startHomeActivity();
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
        return super.onOptionsItemSelected(item);
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, CallStatusActivity.class);
        startActivity(intent);
    }

    private JSONObject getPostData() throws JSONException {
        JSONObject js = new JSONObject();
        js.put("male", mMale.isChecked());
        js.put("female", mFemale.isChecked());
        js.put("casual", mCasual.isChecked());
        js.put("relationship", mRelationship.isChecked());
        js.put("love", mLove.isChecked());
        js.put("friendship", mFriendship.isChecked());
        js.put("action", mAction.isChecked());
        js.put("minAge", mAgeBar.getSelectedMinValue());
        js.put("maxAge", mAgeBar.getSelectedMaxValue());
        JSONObject filters = new JSONObject();
        filters.put("filters", js);
        return filters;
    }
}
