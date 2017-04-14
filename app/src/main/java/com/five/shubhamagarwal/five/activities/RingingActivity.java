package com.five.shubhamagarwal.five.activities;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.utils.Gen;
import com.skyfishjy.library.RippleBackground;

public class RingingActivity extends AppCompatActivity implements View.OnClickListener {

    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringing);

        Button acceptCallButton = (Button) findViewById(R.id.accept_call_button);
        Button silentCallButton = (Button) findViewById(R.id.silent_call_button);
        Button rejectCallButton = (Button) findViewById(R.id.reject_call_button);

        acceptCallButton.setOnClickListener(this);
        silentCallButton.setOnClickListener(this);
        rejectCallButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.ringingImageBackground);
        rippleBackground.startRippleAnimation();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ringtone.play();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.accept_call_button){
            Gen.startActivity(this, true, CallStatusActivity.class);
        }else if(v.getId() == R.id.silent_call_button){
            ringtone.stop();
        }else if (v.getId() == R.id.reject_call_button){
            Gen.startActivity(this, true, CallStatusActivity.class);
        }
    }
}
