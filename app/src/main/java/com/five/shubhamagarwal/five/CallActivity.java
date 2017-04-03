package com.five.shubhamagarwal.five;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;


public class CallActivity extends AppCompatActivity implements WebServiceCoordinator.Listener,
        Session.SessionListener, PublisherKit.PublisherListener, Publisher.CameraListener, SubscriberKit.SubscriberListener,
        View.OnClickListener {

    private static final String LOG_TAG = CallActivity.class.getSimpleName();
    private WebServiceCoordinator mWebServiceCoordinator;
    private String mApiKey;
    private String mSessionId;
    private String mToken;
    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private ImageButton mCallDisconnectButton;
    private ImageButton mCameraCycleButton;
    private ImageButton mCameraOnOffButton;
    private ImageButton mMicOnOffButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mWebServiceCoordinator = new WebServiceCoordinator(this, this);
        mWebServiceCoordinator.fetchSessionConnectionData();

        mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);
        mCallDisconnectButton = (ImageButton) findViewById(R.id.disconnect_call);
        mCallDisconnectButton.setOnClickListener(this);
        mCameraCycleButton = (ImageButton) findViewById(R.id.camera_cycle_button);
        mCameraCycleButton.setOnClickListener(this);
        mCameraOnOffButton = (ImageButton) findViewById(R.id.camera_onoff_button);
        mCameraOnOffButton.setOnClickListener(this);
        mMicOnOffButton = (ImageButton) findViewById(R.id.mic_onoff_button);
        mMicOnOffButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSessionConnectionDataReady(String apiKey, String sessionId, String token) {
        mApiKey = apiKey;
        mSessionId = sessionId;
        mToken = token;
        initializeSession();
        initializePublisher();
    }

    private void initializePublisher() {
        mPublisher = new Publisher(this);
        mPublisher.setPublisherListener(this);
        mPublisher.setCameraListener(this);
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                BaseVideoRenderer.STYLE_VIDEO_FILL);
        mPublisherViewContainer.addView(mPublisher.getView());
    }

    @Override
    public void onWebServiceCoordinatorError(Exception error) {
        Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
    }

    /* Session Listener methods */

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        if(mPublisher != null){
            mSession.publish(mPublisher);
        }
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (mSubscriber == null) {
            mSubscriber = new Subscriber(this, stream);
            mSubscriber.setSubscriberListener(this);
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                    BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSession.subscribe(mSubscriber);
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        logOpenTokError(opentokError);
    }


    private void logOpenTokError(OpentokError opentokError) {
        Log.e(LOG_TAG, "Error Domain: " + opentokError.getErrorDomain().name());
        Log.e(LOG_TAG, "Error Code: " + opentokError.getErrorCode().name());
    }

    private void initializeSession() {
        mSession = new Session(this, mApiKey, mSessionId);
        mSession.setSessionListener(this);
        mSession.connect(mToken);
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher Stream Created");
        mPublisher.setPublishVideo(false);

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher Stream Destroyed");

    }


    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onCameraChanged(Publisher publisher, int i) {

    }

    @Override
    public void onCameraError(Publisher publisher, OpentokError opentokError) {

    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {
        Log.i(LOG_TAG, "Subscriber Connected");

        mSubscriberViewContainer.addView(mSubscriber.getView());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
        Log.i(LOG_TAG, "Subscriber Disconnected");

    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
        logOpenTokError(opentokError);
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "On Stop Called");
        if(mPublisher != null)
            mPublisher.destroy();
        if(mSubscriber != null)
            mSubscriber.destroy();
        if(mSession != null)
            mSession.disconnect();

        super.onStop();
    }

    @Override
    public void onClick(View v) {
        Log.i(LOG_TAG, "Button Pressed for "+v.getId());
        switch (v.getId()){
            case R.id.disconnect_call: {
                Toast.makeText(this, "Disconnecting the call.....", Toast.LENGTH_LONG).show();
                NavUtils.navigateUpFromSameTask(this);
            }

            case R.id.camera_cycle_button: {
                Toast.makeText(this, "Flipping the camera.....", Toast.LENGTH_LONG).show();
                if(mPublisher != null)
                    mPublisher.cycleCamera();
            }

            case R.id.camera_onoff_button: {
                if(mPublisher.getPublishVideo()) {  // Camera is On
                    Toast.makeText(this, "Deactivating Camera", Toast.LENGTH_LONG).show();
                    mCameraOnOffButton.setBackgroundResource(R.mipmap.camera_off);
                    mPublisher.setPublishVideo(false);
                }else{
                    Toast.makeText(this, "Activating Camera", Toast.LENGTH_LONG).show();
                    mCameraOnOffButton.setBackgroundResource(R.mipmap.camera_on);
                    mPublisher.setPublishVideo(true);
                }
            }

            case R.id.mic_onoff_button: {
                if(mPublisher.getPublishAudio()){ // Mic is Onn
                    Toast.makeText(this, "Deactivating Mic", Toast.LENGTH_LONG).show();
                    mMicOnOffButton.setBackgroundResource(R.mipmap.mic_off);
                    mPublisher.setPublishAudio(false);
                }else{
                    Toast.makeText(this, "Activating Mic", Toast.LENGTH_LONG).show();
                    mMicOnOffButton.setBackgroundResource(R.mipmap.mic_onn);
                    mPublisher.setPublishAudio(true);
                }
            }
        }
    }
}
