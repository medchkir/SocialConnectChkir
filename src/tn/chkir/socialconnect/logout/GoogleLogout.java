package tn.chkir.socialconnect.logout;

import tn.chkir.socialconnect.login.LoginActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;

public class GoogleLogout extends BaseLogoutActivity implements
	ConnectionCallbacks, OnConnectionFailedListener {
	private static final String TAG = "ExampleActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlusClient = new PlusClient.Builder(this, this, this).
        		setScopes(Scopes.PLUS_LOGIN, Scopes.PLUS_PROFILE ,"https://www.googleapis.com/auth/userinfo.email")
        		.build();

        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mPlusClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
      if (mConnectionProgressDialog.isShowing()) {
        if (result.hasResolution()) {
          try {
                   result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
           } catch (SendIntentException e) {
                   mPlusClient.connect();
           }
        }
      }
      mConnectionResult = result;
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    	
    }
   
    @Override
    public void onDisconnected() {
    	Log.d(TAG, "disconnected");
    }

    
    
    public void GoogleLogout() {
    	if (mPlusClient.isConnected()) {
        	mPlusClient.clearDefaultAccount();
            mPlusClient.disconnect();
            //mPlusClient.connect();
            SharedPreferences sharedpreference = PreferenceManager.getDefaultSharedPreferences(this);
        	sharedpreference.edit().remove(KEY_NETWORK_ID).commit();
        	Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        	startActivity(intent);
        	finish();
        }
    }
}
