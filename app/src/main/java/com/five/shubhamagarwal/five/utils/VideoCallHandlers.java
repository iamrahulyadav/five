package com.five.shubhamagarwal.five.utils;

import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.five.shubhamagarwal.five.activities.CallActivity;
import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.activities.RatingsActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubhamagrawal on 03/04/17.
 */

public class VideoCallHandlers implements View.OnClickListener{

    private CallActivity callActivity;
    private static String LOG_TAG = VideoCallHandlers.class.getSimpleName();

    public VideoCallHandlers(CallActivity callActivity) {
        this.callActivity = callActivity;
    }

    @Override
    public void onClick(View v) {
        // Log.i(LOG_TAG, "Button Pressed for "+v.getId());
        switch (v.getId()){
            case R.id.disconnect_call: {
                Gen.toast("Disconnecting the call.....");
                Gen.startActivity(callActivity, true, RatingsActivity.class);
                RequestQueue requestQueue = VolleySingelton.getInstance().getRequestQueue();
                JSONObject postData = new JSONObject();
                try {
                    postData.put(Gen.NOTIFICATION_TYPE, Gen.CALL_ENDED_NOTIFICATION );
                    postData.put(Gen.FCM_TOKEN_KEY, Gen.getFCMTokenFromLocalStorage() )
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final JsonObjectRequest request = new JsonObjectRequestWithAuth(Request.Method.POST, Gen.SERVER_URL + "/notification", postData,)

                break;
            }

            case R.id.camera_cycle_button: {
                Gen.toast("Flipping the camera.....");
                if(callActivity.publisher != null)
                    callActivity.publisher.cycleCamera();
                break;
            }

            case R.id.camera_onoff_button: {
                if(callActivity.publisher.getPublishVideo()) {  // Camera is On
                    Gen.toast("Deactivating Camera");
                    callActivity.mCameraOnOffButton.setImageResource(R.mipmap.camera_on);
                    callActivity.publisher.setPublishVideo(false);
                }else{
                    Gen.toast("Activating Camera");
                    callActivity.mCameraOnOffButton.setImageResource(R.mipmap.camera_off);
                    callActivity.publisher.setPublishVideo(true);
                }
                break;
            }

            case R.id.mic_onoff_button: {
                if(callActivity.publisher.getPublishAudio()){ // Mic is Onn
                    Gen.toast("Deactivating Mic");
                    callActivity.mMicOnOffButton.setImageResource(R.mipmap.mic_onn);
                    callActivity.publisher.setPublishAudio(false);
                }else{
                    Gen.toast("Activating Mic");
                    callActivity.mMicOnOffButton.setImageResource(R.mipmap.mic_off);
                    callActivity.publisher.setPublishAudio(true);
                }
                break;
            }
        }
    }
}
