package com.five.shubhamagarwal.five.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.five.shubhamagarwal.five.BuildConfig;
import com.five.shubhamagarwal.five.R;
import com.five.shubhamagarwal.five.utils.Constants;
import com.five.shubhamagarwal.five.utils.Gen;
import com.five.shubhamagarwal.five.utils.JsonObjectRequestWithAuth;
import com.five.shubhamagarwal.five.utils.VolleySingelton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Filter;

public class LoginActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private String TAG = "LoginActivity";
    CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private SliderLayout mDemoSlider;
    AuthCredential credential;
    AccessToken accessToken;
    private static final String SERVER_URL = BuildConfig.SERVER_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Bundle extras = getIntent().getExtras();

        if(extras!=null && extras.getString(Constants.SHOW_LOGOUT_SCREEN, null) != null){

        } else if(Gen.getUserIdFromLocalStorage() != "") {
            if(Gen.getFiltersFromLocalStorage() == true) {
                Gen.startActivity(this, true, CallStatusActivity.class);
            } else {
                Gen.startActivity(this, true, FiltersActivity.class);
            }
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        configImageSlider();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Gen.showError(error);
            }
        });
    }

    private void configImageSlider() {
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("screen1",R.mipmap.screen_1);
        file_maps.put("screen2",R.mipmap.screen_2);
        file_maps.put("screen3",R.mipmap.screen_3);

        for (String name : file_maps.keySet()) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            mDemoSlider.addSlider(sliderView);
        }

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(2000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        final Activity activity = this;
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        accessToken = token;
        credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            RequestQueue requestQueue = VolleySingelton.getInstance().getRequestQueue();
                            Gen.showLoader(activity);
                            JSONObject postData = null;
                            try {
                                postData = getPostData();
                            } catch (JSONException e) {
                                Gen.showError(e);
                            }
                            JsonObjectRequest request = new JsonObjectRequestWithAuth(Request.Method.POST, SERVER_URL + "/user", postData, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Gen.hideLoader(activity);
                                    try {
                                        Gen.saveUserIdToLocalStorage(response.getString("user_uuid"));
                                        Log.d(TAG, response.getString("new_signup"));
                                    } catch (Exception e) {
                                        Gen.showError(e);
                                    }
                                    Gen.startActivity(activity, false, FiltersActivity.class);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Gen.hideLoader(activity);
                                    Gen.showVolleyError(error);
                                }
                            });
                            requestQueue.add(request);
                        }
                    }
                });
    }

    private JSONObject getPostData() throws JSONException {
        JSONObject js = new JSONObject();
        js.put("firebase_user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        js.put("fb_data", new JSONObject(Gen.getJSONString(accessToken)));
        String token = FirebaseInstanceId.getInstance().getToken();
        if(token != null){
            Gen.saveFCMTokenToLocalStorage(token);
        }
        js.put("fcm_token", Gen.getFCMTokenFromLocalStorage());
        return js;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
