package com.five.shubhamagarwal.five.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.utils.Gen;
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


public class CallActivity extends AppCompatActivity implements WebServiceCoordinator.Listener,
        Session.SessionListener, PublisherKit.PublisherListener, Publisher.CameraListener, SubscriberKit.SubscriberListener{

    private static final String LOG_TAG = CallActivity.class.getSimpleName();
    private WebServiceCoordinator webServiceCoordinator;
    private VideoCallHandlers videoCallHandlers;

    public String apiKey, sessionId, token;
    public Session session;
    public Publisher publisher;
    public Subscriber subscriber;

    public FrameLayout mPublisherViewContainer, mSubscriberViewContainer;
    public ImageButton mCallDisconnectButton, mCameraCycleButton, mCameraOnOffButton, mMicOnOffButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
        mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);
        mCallDisconnectButton = (ImageButton) findViewById(R.id.disconnect_call);
        mCameraCycleButton = (ImageButton) findViewById(R.id.camera_cycle_button);
        mCameraOnOffButton = (ImageButton) findViewById(R.id.camera_onoff_button);
        mMicOnOffButton = (ImageButton) findViewById(R.id.mic_onoff_button);

        // init call handler and web service coordinator
        videoCallHandlers = new VideoCallHandlers(this);
        webServiceCoordinator = new WebServiceCoordinator(this, this);

        Gen.showLoader(this);
        webServiceCoordinator.fetchSessionConnectionData();

        // attach call handler
        mCallDisconnectButton.setOnClickListener(videoCallHandlers);
        mCameraCycleButton.setOnClickListener(videoCallHandlers);
        mCameraOnOffButton.setOnClickListener(videoCallHandlers);
        mMicOnOffButton.setOnClickListener(videoCallHandlers);
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
        Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
    }

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        if(publisher != null){
            this.session.publish(publisher);
        }
    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");

        if (subscriber == null) {
            subscriber = new Subscriber(this, stream);
            subscriber.setSubscriberListener(this);
            subscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE,
                    BaseVideoRenderer.STYLE_VIDEO_FILL);
            this.session.subscribe(subscriber);
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");

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
        Log.e(LOG_TAG, "Error Domain: " + opentokError.getErrorDomain().name());
        Log.e(LOG_TAG, "Error Code: " + opentokError.getErrorCode().name());
    }

    private void initializeSession() {
        session = new Session(this, apiKey, sessionId);
        session.setSessionListener(this);
        session.connect(token);
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher Stream Created");
        publisher.setPublishVideo(false);
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
        mSubscriberViewContainer.addView(subscriber.getView());
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
        if(publisher != null)
            publisher.destroy();
        if(subscriber != null)
            subscriber.destroy();
        if(session != null)
            session.disconnect();

        super.onStop();
    }
}
