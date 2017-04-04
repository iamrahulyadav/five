package com.five.shubhamagarwal.five.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.five.shubhamagarwal.five.Adapters.RatingListViewAdapter;
import com.five.shubhamagarwal.five.Models.RatingParameter;
import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.utils.Gen;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shubhamagrawal on 01/04/17.
 */

public class RatingsActivity extends AppCompatActivity {
    private ListView mlistView;
    private CheckBox mshareCheckBox;
    private EditText mfeedback, mshareMessage;

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

        final Button button = (Button) findViewById(R.id.submit);
        // TODO: get userA and userB
        final String userA = "1234";
        final String userB = "2345";
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HashMap<String, Object> rating = new HashMap<>();
                rating.put("giver", userB);
                rating.put("feedback", mfeedback.getText().toString());
                rating.put("ratingList", arrayList);
            }
        });
        mshareCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(isChecked)
                  mshareMessage.setVisibility(View.VISIBLE);
              else
                  mshareMessage.setVisibility(View.GONE);
              }
          }
        );
    }

    private void setListData() {
        // TODO: Use this from database
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
