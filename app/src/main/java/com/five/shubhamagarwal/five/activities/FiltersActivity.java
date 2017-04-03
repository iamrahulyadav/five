package com.five.shubhamagarwal.five.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.five.shubhamagarwal.five.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.HashMap;


public class FiltersActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    CheckBox mMale, mFemale, mCasual, mRelationship, mLove, mFriendship, mAction;
    RangeSeekBar mAgeBar;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mMale = (CheckBox) findViewById(R.id.male_checkbox);
        mFemale = (CheckBox) findViewById(R.id.female_checkbox);
        mCasual = (CheckBox) findViewById(R.id.casual_checkbox);
        mRelationship = (CheckBox) findViewById(R.id.relationship_checkbox);
        mLove = (CheckBox) findViewById(R.id.love_checkbox);
        mFriendship = (CheckBox) findViewById(R.id.friendship_checkbox);
        mAction = (CheckBox) findViewById(R.id.action_checkbox);
        mAgeBar = (RangeSeekBar) findViewById(R.id.age_seekbar);

        // TODO: get user id from somewhere
        user = "1234";
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
//            HashMap<String, Object> interests = new HashMap<>();
//            int minAge = (int) mAgeBar.getSelectedMinValue();
//            int maxAge = (int) mAgeBar.getSelectedMaxValue();
//
//            interests.put("ageMin", minAge);
//            interests.put("ageMax", maxAge);
//            interests.put("lookingFor", lookingFor.getSelectedItem().toString());
//            interests.put("interestedIn", interestedIn.getSelectedItem().toString());
//            mDatabase.child("interests").child(user).setValue(interests)
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(FiltersActivity.this, "Error!! Your preferences are not saved.", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(FiltersActivity.this, "Your Filters are Saved Successfully", Toast.LENGTH_SHORT).show();
//                            startHomeActivity();
//                        }
//                    });
        }
        startHomeActivity();
        return super.onOptionsItemSelected(item);
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
