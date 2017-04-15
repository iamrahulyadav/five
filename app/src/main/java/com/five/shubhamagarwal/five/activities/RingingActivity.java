package com.five.shubhamagarwal.five.activities;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
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
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringing);

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");
        wakeLock.acquire();

        Button acceptCallButton = (Button) findViewById(R.id.accept_call_button);
        Button silentCallButton = (Button) findViewById(R.id.silent_call_button);
        Button rejectCallButton = (Button) findViewById(R.id.reject_call_button);

        acceptCallButton.setOnClickListener(this);
        silentCallButton.setOnClickListener(this);
        rejectCallButton.setOnClickListener(this);

        ImageView imageView = (ImageView) findViewById(R.id.ringing_image_dp);
        String gender = getIntent().getStringExtra("gender");

        if (gender.equals("male"))
            imageView.setImageResource(R.mipmap.male);
        else
            imageView.setImageResource(R.mipmap.female);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.ringing_image_background);
        rippleBackground.startRippleAnimation();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        ringtone.play();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        // Skip pressed back button
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ringtone.stop();
        wakeLock.release();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.accept_call_button){
            Gen.startActivity(this, true, CallStatusActivity.class);
        }else if(v.getId() == R.id.silent_call_button){
            ringtone.stop();
        }else if (v.getId() == R.id.reject_call_button){
            finish();
        }
    }
}
