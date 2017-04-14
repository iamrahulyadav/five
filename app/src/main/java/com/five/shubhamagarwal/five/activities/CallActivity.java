package com.five.shubhamagarwal.five.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.utils.Constants;
import com.five.shubhamagarwal.five.utils.Gen;
import com.five.shubhamagarwal.five.utils.PermissionUtil;
import com.five.shubhamagarwal.five.utils.WebServiceCoordinator;
import com.five.shubhamagarwal.five.utils.VideoCallHandlers;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.skyfishjy.library.RippleBackground;
import java.text.ParseException;
import java.util.Date;


// TODO: if the subscriber is changing the camera(switching it on or off), we are not getting any notification at any handlers.
// Change ripplebackground to not appear if the call is already made in the session

public class CallActivity extends AppCompatActivity implements WebServiceCoordinator.Listener,
        Session.SessionListener, PublisherKit.PublisherListener, Publisher.CameraListener, SubscriberKit.SubscriberListener, Session.StreamPropertiesListener{

    private static final String LOG_TAG = CallActivity.class.getSimpleName();
    private WebServiceCoordinator webServiceCoordinator;
    private VideoCallHandlers videoCallHandlers;

    public String apiKey, sessionId, token;
    public Session session;
    public Publisher publisher;
    public Subscriber subscriber;
    public CountDownTimer callTimer;
    public Long secondsLeft = 0L;

    public FrameLayout mPublisherViewContainer, mSubscriberViewContainer;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.CAMERA_AUDIO_WAKE_LOCK:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Gen.toast("Permission Granted!");
                } else {
                    Gen.toast("Permission Denied!");
                    // re-request permission
                    PermissionUtil.requestPermission(Manifest.permission.CAMERA, Constants.CAMERA_AUDIO_WAKE_LOCK, this);
                }
                break;
        }
    }

    public ImageButton mCallDisconnectButton, mCameraCycleButton, mCameraOnOffButton, mMicOnOffButton;
    public TextView mCallTimerView;
    public ImageView mGenderPlaceholder, mCircleImage;
    public RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mPublisherViewContainer = (FrameLayout) findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout) findViewById(R.id.subscriber_container);
        mCallDisconnectButton = (ImageButton) findViewById(R.id.disconnect_call);
        mCameraCycleButton = (ImageButton) findViewById(R.id.camera_cycle_button);
        mCameraOnOffButton = (ImageButton) findViewById(R.id.camera_onoff_button);
        mMicOnOffButton = (ImageButton) findViewById(R.id.mic_onoff_button);
        mCallTimerView = (TextView) findViewById(R.id.call_timer_view);
        mGenderPlaceholder = (ImageView) findViewById(R.id.placeholder_image);
        mCircleImage = (ImageView) findViewById(R.id.circle_placeholder);

        // attach call handler
        videoCallHandlers = new VideoCallHandlers(this);

        mCallDisconnectButton.setOnClickListener(videoCallHandlers);
        mCameraCycleButton.setOnClickListener(videoCallHandlers);
        mCameraOnOffButton.setOnClickListener(videoCallHandlers);
        mMicOnOffButton.setOnClickListener(videoCallHandlers);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WAKE_LOCK, Manifest.permission.RECORD_AUDIO}, Constants.CAMERA_AUDIO_WAKE_LOCK);
        // init call handler and web service coordinator
        webServiceCoordinator = new WebServiceCoordinator(this, this);
        Gen.showLoader(this);
        webServiceCoordinator.fetchSessionConnectionData();

        Intent intent = getIntent();
        String chat_end_time = intent.getStringExtra(CallStatusActivity.CHAT_END_TIME_KEY);
        String current_time = intent.getStringExtra(CallStatusActivity.CURRENT_TIME);
        String gender = intent.getStringExtra(CallStatusActivity.GENDER);
        if(gender.equals("female")){
            mGenderPlaceholder.setImageResource(R.mipmap.female);
            mCircleImage.setImageResource(R.mipmap.female);
        } else {
            mGenderPlaceholder.setImageResource(R.mipmap.male);
            mCircleImage.setImageResource(R.mipmap.male);
        }

        rippleBackground = (RippleBackground)findViewById(R.id.calling_animated);
        rippleBackground.startRippleAnimation();

        ISO8601DateFormat df = new ISO8601DateFormat();
        try {
            Date currentTime = df.parse(current_time);
            Date endTime = df.parse(chat_end_time);
            secondsLeft = (endTime.getTime() - currentTime.getTime())/1000;
            // Log.d(LOG_TAG, "Seconds Left = "+secondsLeft);
            startCounter(secondsLeft);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void startCounter(final long inSeconds){
        if(callTimer != null) {
            callTimer.cancel();
            callTimer = null;
        }
        callTimer = new CountDownTimer(inSeconds*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsLeft--;
                String text = String.format("%02d:%02d", secondsLeft/60, secondsLeft%60);
                mCallTimerView.setText(text);
            }

            @Override
            public void onFinish() {
                mCallDisconnectButton.performClick();
            }
        };
        callTimer.start();
    }

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {
        Gen.hideLoader(this);

        this.apiKey = apiKey;
        this.sessionId = sessionId;
        this.token = token;
        initializeSession();
        initializePublisher();
    }

    private void initializePublisher() {
        publisher = new Publisher(this);
        publisher.setPublisherListener(this);
        publisher.setCameraListener(this);
        publisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        mPublisherViewContainer.addView(publisher.getView());
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {
        Gen.hideLoader(this);
        Gen.showError(error);
        // Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
    }

    @Override
    public void onConnected(Session session) {
        // Log.i(LOG_TAG, "Session Connected");
        if(publisher != null){
            this.session.publish(publisher);
        }
    }

    @Override
    public void onDisconnected(Session session) {
        // Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        rippleBackground.setVisibility(View.GONE);
        adjustUIOnVideoChange(stream);
        // Log.i(LOG_TAG, "Stream Received");
        if (subscriber == null) {
            subscriber = new Subscriber(this, stream);
            subscriber.setSubscriberListener(this);
            subscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                    BaseVideoRenderer.STYLE_VIDEO_FILL);
            this.session.subscribe(subscriber);
        }
    }

    private void adjustUIOnVideoChange(Stream stream) {
//        if(stream.hasVideo()){
//            mGenderPlaceholder.setVisibility(View.GONE);
//        } else {
//            mGenderPlaceholder.setVisibility(View.VISIBLE);
//        }
        // adding temporarily because subscriber video change event wasn't triggering.
        mGenderPlaceholder.setVisibility(View.GONE);
    }


    @Override
    public void onStreamDropped(Session session, Stream stream) {
        // Log.i(LOG_TAG, "Stream Dropped");

        if (subscriber != null) {
            subscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        logOpenTokError(opentokError);
    }


    private void logOpenTokError(OpentokError opentokError) {
        // Log.e(LOG_TAG, "Error Domain: " + opentokError.getErrorDomain().name());
        // Log.e(LOG_TAG, "Error Code: " + opentokError.getErrorCode().name());
    }

    private void initializeSession() {
        session = new Session(this, apiKey, sessionId);
        session.setSessionListener(this);
        session.connect(token);
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        // Log.i(LOG_TAG, "Publisher Stream Created");
        publisher.setPublishVideo(true);
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        // Log.i(LOG_TAG, "Publisher Stream Destroyed");
    }


    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onCameraChanged(Publisher publisher, int i) {
        // Log.d(LOG_TAG, "stream video dimension changed");
    }

    @Override
    public void onCameraError(Publisher publisher, OpentokError opentokError) {

    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {
        // Log.i(LOG_TAG, "Subscriber Connected");
        mSubscriberViewContainer.addView(subscriber.getView());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
        // Log.i(LOG_TAG, "Subscriber Disconnected");
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
        logOpenTokError(opentokError);
    }

    @Override
    protected void onDestroy() {
        // Log.i(LOG_TAG, "On Stop Called");
        if(publisher != null)
            publisher.destroy();
        if(subscriber != null)
            subscriber.destroy();
        if(session != null)
            session.disconnect();
        if(callTimer!=null){         // invalidateds timer
            callTimer.cancel();
            callTimer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onStreamHasAudioChanged(Session session, Stream stream, boolean b) {
        // Log.d(LOG_TAG, "stream video dimension changed");
    }

    @Override
    public void onStreamHasVideoChanged(Session session, Stream stream, boolean b) {
        adjustUIOnVideoChange(stream);
    }

    @Override
    public void onStreamVideoDimensionsChanged(Session session, Stream stream, int i, int i1) {
        // Log.d(LOG_TAG, "stream video dimension changed");
    }

    @Override
    public void onStreamVideoTypeChanged(Session session, Stream stream, Stream.StreamVideoType streamVideoType) {
        // Log.d(LOG_TAG, "stream video dimension changed");
    }
}
