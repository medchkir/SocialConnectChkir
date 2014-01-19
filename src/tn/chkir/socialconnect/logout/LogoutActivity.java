package tn.chkir.socialconnect.logout;

import tn.chkir.socialconnect.R;
import tn.chkir.socialconnect.model.Connected;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class LogoutActivity extends FacebookLogout implements OnClickListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		Connected connected = (Connected) getIntent().getSerializableExtra(KEY_CONNECTED);
		if(connected != null)
			Toast.makeText(getApplicationContext(), "firstname : "+connected.firstname+ " lastname: "+ connected.lastname, Toast.LENGTH_SHORT).show();
		findViewById(R.id.deconnexion).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.deconnexion){
			SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			int network_id = sharedpreferences.getInt(KEY_NETWORK_ID, -1);
			
			if(network_id == NETWORK_ID_FB) 
				FacebookLogout();
			else
			if(network_id == NETWORK_ID_GOOGLE)
				GoogleLogout();
		}
		
	}

}
