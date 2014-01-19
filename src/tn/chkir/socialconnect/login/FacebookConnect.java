package tn.chkir.socialconnect.login;

import tn.chkir.socialconnect.R;
import tn.chkir.socialconnect.logout.LogoutActivity;
import tn.chkir.socialconnect.model.Connected;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;

public class FacebookConnect extends GoogleConnect {

	private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.i("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.i("HelloFacebook", "Success!");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        findViewById(R.id.fb_login).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickLogin();
			}
		});
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    private void onClickLogin() {
    	Session.openActiveSession(this, true, new Session.StatusCallback() {
    		@SuppressWarnings("deprecation")
			@Override
            public void call(final Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                	Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
	                    @Override
	                    public void onCompleted(GraphUser user, Response response) {
	                    	OnFBConnected(user);
	                    }
	                });
                }
            }
        });
    }
    
    private void OnFBConnected(GraphUser user) {
    	SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    	editor.putInt(KEY_NETWORK_ID, NETWORK_ID_FB);
    	editor.commit();
    	
    	Connected connected = new Connected(user.getId(), user.getFirstName(), user.getLastName(), user.getId());
    	Intent intent = new Intent(getApplicationContext(), LogoutActivity.class);
    	intent.putExtra(KEY_CONNECTED, connected);
    	Log.i("start","start");
    	startActivity(intent);
    	finish();
    	Log.i("",user.getFirstName());
    }
 }

