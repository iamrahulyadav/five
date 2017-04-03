package com.five.shubhamagarwal.five.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.five.shubhamagarwal.five.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;


public class FiltersActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText age;
    Spinner lookingFor;
    Spinner interestedIn;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        age = (EditText) findViewById(R.id.age);
        lookingFor = (Spinner) findViewById(R.id.lookingFor);
        interestedIn = (Spinner) findViewById(R.id.interstedIn);
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
            HashMap<String, Object> interests = new HashMap<>();
            interests.put("age", age.getText().toString());
            interests.put("lookingFor", lookingFor.getSelectedItem().toString());
            interests.put("interestedIn", interestedIn.getSelectedItem().toString());
            mDatabase.child("interests").child(user).setValue(interests)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FiltersActivity.this, "Error!! Your preferences are not saved.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(FiltersActivity.this, "Your Filters are Saved Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }
}
