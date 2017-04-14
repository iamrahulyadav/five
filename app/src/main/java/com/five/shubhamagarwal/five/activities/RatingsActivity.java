package com.five.shubhamagarwal.five.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.five.shubhamagarwal.five.Adapters.RatingListViewAdapter;
import com.five.shubhamagarwal.five.Models.RatingParameter;
import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.utils.Gen;
import com.five.shubhamagarwal.five.utils.JsonObjectRequestWithAuth;
import com.five.shubhamagarwal.five.utils.VolleySingelton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by shubhamagrawal on 01/04/17.
 */

public class RatingsActivity extends AppCompatActivity {
    private ListView mlistView;
    private CheckBox mshareCheckBox;
    private EditText mfeedback, mshareMessage;

    private static final String TAG = CallActivity.class.getSimpleName();

    private ArrayAdapter<RatingParameter> adapter;
    private ArrayList<RatingParameter> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        mlistView = (ListView) findViewById(R.id.list_view);
        mshareCheckBox = (CheckBox) findViewById(R.id.share_checkbox);
        mfeedback = (EditText) findViewById(R.id.feedback);
        mshareMessage = (EditText) findViewById(R.id.share_message);
        setListData();

        adapter = new RatingListViewAdapter(this, R.layout.rating_icon_text, arrayList);
        mlistView.setAdapter(adapter);
        Gen.setListViewHeightBasedOnChildren(mlistView); // Hack to show the listview in expanded form.

        final Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submitRating();
            }
        });
        mshareCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(isChecked)
                  mshareMessage.setVisibility(View.VISIBLE);
              else
                  mshareMessage.setVisibility(View.INVISIBLE);
              }
          }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            Boolean callEndedByUser = getIntent().getExtras().getBoolean("call_ended_by_user", false);
            if (callEndedByUser) {
                Gen.toast("Other user has disconnected the call");
            }
        }
    }

    private JSONObject getPostData() throws JSONException {
        JSONObject js = new JSONObject();
        JSONObject ratings = new JSONObject();
        JSONObject ratingParams = new JSONObject();
        for(RatingParameter ratingparameter: arrayList){
            ratingParams.put(ratingparameter.getName(), ratingparameter.getRatingStar());
        }

        ratings.put("share_profile", mshareCheckBox.isChecked());
        ratings.put("feedback", mfeedback.getText().toString());
        ratings.put("share_message", mshareMessage.getText().toString());
        ratings.put("rating_params", ratingParams);

        js.put("opentok_session_id", Gen.getSessionIdFromLocalStorage());
        js.put("ratings", ratings);
        return js;
    }

    private void submitRating(){
        final Activity activity = this;
        RequestQueue requestQueue = VolleySingelton.getInstance().getRequestQueue();
        JSONObject postData = null;
        try {
            postData = getPostData();
        } catch (JSONException e) {
            Gen.showError(e);
        }
        JsonObjectRequest request = new JsonObjectRequestWithAuth(Request.Method.POST, Gen.SERVER_URL + "/ratings", postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gen.hideLoader(activity);
                Gen.toastLong("Thanks for your rating! We will notify you when we schedule your next call.");
                Gen.startActivity(activity, true, CallStatusActivity.class);
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

    private void setListData() {
        arrayList = new ArrayList<>();
        arrayList.add(new RatingParameter(1.0, "Looks"));
        arrayList.add(new RatingParameter(1.0, "Communication"));
        arrayList.add(new RatingParameter(1, "Originality"));
        arrayList.add(new RatingParameter(1, "Confidence"));
        arrayList.add(new RatingParameter(1, "Smartness"));
        arrayList.add(new RatingParameter(1, "Behaviour"));
        arrayList.add(new RatingParameter(1, "Overall"));
        Gen.setListViewHeightBasedOnChildren(mlistView);
    }
}
