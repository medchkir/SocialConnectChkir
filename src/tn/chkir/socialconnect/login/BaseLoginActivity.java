package tn.chkir.socialconnect.login;

import tn.chkir.socialconnect.BaseActivity;
import tn.chkir.socialconnect.R;
import tn.chkir.socialconnect.R.layout;
import tn.chkir.socialconnect.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class BaseLoginActivity extends BaseActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    
}
