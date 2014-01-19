package tn.chkir.socialconnect.login;

import tn.chkir.socialconnect.R;
import tn.chkir.socialconnect.logout.LogoutActivity;
import tn.chkir.socialconnect.model.Connected;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

public class GoogleConnect extends BaseLoginActivity implements
	ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {
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
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        
        // Progress bar to be displayed if the connection failure is not resolved.
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
        // The user clicked the sign-in button already. Start to resolve
        // connection errors. Wait until onConnected() to dismiss the
        // connection dialog.
        if (result.hasResolution()) {
          try {
                   result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
           } catch (SendIntentException e) {
                   mPlusClient.connect();
           }
        }
      }
      // Save the result and resolve the connection failure upon a user click.
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
    	onSignedIn();
    }
   
    @Override
    public void onDisconnected() {
        Log.d(TAG, "disconnected");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
            if (mConnectionResult == null) {
                mConnectionProgressDialog.show();
                mPlusClient.connect();
            } else {
                try {
                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (SendIntentException e) {
                    // Try connecting again.
                    mConnectionResult = null;
                    mPlusClient.connect();
                }
            }
        }
    }
    
    public void onSignedIn() {
    	if (mPlusClient.isConnected()) {
    	    new AsyncTask<Object, Void, Person>() {
    	      @Override
    	      protected Person doInBackground(Object... o) {
    	        return mPlusClient.getCurrentPerson();
    	      }

    	      @Override
    	      protected void onPostExecute(Person person) {
    	        if (person != null) {
    	        	SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	        	sharedpreferences.edit().putInt(KEY_NETWORK_ID, NETWORK_ID_GOOGLE).commit();
    	        	Connected connected = new Connected(person.getId(), person.getName().getGivenName(), person.getName().getFamilyName(), person.getImage().getUrl());
    	        	Intent intent = new Intent(getApplicationContext(), LogoutActivity.class);
    	        	intent.putExtra(KEY_CONNECTED, connected);
    	        	mConnectionProgressDialog.dismiss();
        	        startActivity(intent);
    	        	finish();
    	        }
    	    };
    	  }.execute();
    	}
    }
}
