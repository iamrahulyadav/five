package com.five.shubhamagarwal.five.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.five.shubhamagarwal.five.Adapters.RatingListViewAdapter;
import com.five.shubhamagarwal.five.Models.RatingParameter;
import com.five.shubhamagarwal.five.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by shubhamagrawal on 01/04/17.
 */

public class RatingsActivity extends AppCompatActivity{
    private ListView listView;
    private ArrayAdapter<RatingParameter> adapter;
    private ArrayList<RatingParameter> arrayList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        listView = (ListView)findViewById(R.id.list_view);
        setListData();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        adapter = new RatingListViewAdapter(this, R.layout.item_listview, arrayList);
        listView.setAdapter(adapter);
        final Button button = (Button) findViewById(R.id.submit);
        final EditText feedback = (EditText) findViewById(R.id.feedback);
        // TODO: get userA and userB
        final String userA = "1234";
        final String userB = "2345";
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HashMap<String, Object> rating = new HashMap<>();
                rating.put("giver", userB);
                rating.put("feedback", feedback.getText().toString());
                rating.put("ratingList", arrayList);
                mDatabase.child("ratings").child(userA).setValue(rating);

            }
        });
    }

    private void setListData() {
        // TODO: Use this from database
        arrayList = new ArrayList<>();
        arrayList.add(new RatingParameter(1.0, "Looks"));
        arrayList.add(new RatingParameter(1.0, "Communication"));
        arrayList.add(new RatingParameter(1, "Originality"));
        arrayList.add(new RatingParameter(1, "Confidence"));
        arrayList.add(new RatingParameter(1, "Overall"));
    }
}
