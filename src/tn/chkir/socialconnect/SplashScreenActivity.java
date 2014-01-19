package tn.chkir.socialconnect;

import tn.chkir.socialconnect.login.LoginActivity;
import tn.chkir.socialconnect.logout.LogoutActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SplashScreenActivity extends BaseActivity {
	private static final int STOPSPLASH = 0;

	private static final long SPLASHTIME = 500;

	public static SharedPreferences sharedpreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spalshscreen);
		
		sharedpreference = PreferenceManager.getDefaultSharedPreferences(this);
    	final Message msg = new Message();
		msg.what = STOPSPLASH;
		splashHandler.sendMessageDelayed(msg, SPLASHTIME);
		
	}
	
	private final transient Handler splashHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Intent intent = null;
			if (msg.what == STOPSPLASH){
				if(sharedpreference.getInt(KEY_NETWORK_ID, -1) == -1) {
					intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
				}else {
					intent = new Intent(SplashScreenActivity.this, LogoutActivity.class);
				}
				startActivity(intent);
				finish();
			}
			super.handleMessage(msg);
		}
	};
}