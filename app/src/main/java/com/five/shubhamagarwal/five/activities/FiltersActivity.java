package com.five.shubhamagarwal.five.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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


public class FiltersActivity extends AppCompatActivity {

    private static final String TAG = FiltersActivity.class.getSimpleName();
    CheckBox mMale, mFemale, mCasual, mRelationship, mLove, mFriendship, mAction;
    RangeSeekBar mAgeBar;
    Button mSubmit;

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
        mSubmit = (Button) findViewById(R.id.submit_filters);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFilters();
            }
        });
    }

    private boolean validateData() {
        if(!mMale.isChecked() && !mFemale.isChecked()){
            Gen.toast("At least select one gender to talk to!");
            return false;
        } else if(!mCasual.isChecked() && !mRelationship.isChecked() && !mLove.isChecked()&& !mFriendship.isChecked()&& !mAction.isChecked()){
            Gen.toast("Select something to talk about");
            return false;
        }
        return true;
    }

    private void saveFilters(){
        if(!validateData())
            return;

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
                Gen.saveFiltersToLocalStorage(true); // this indicates that the filter screen is done by the user
                Gen.startCallStatusActivity(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Gen.showVolleyError(error);
            }
        });
        requestQueue.add(request);
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
