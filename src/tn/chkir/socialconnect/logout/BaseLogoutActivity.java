package tn.chkir.socialconnect.logout;

import tn.chkir.socialconnect.BaseActivity;
import tn.chkir.socialconnect.R;
import android.os.Bundle;
import android.view.Menu;

public class BaseLogoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
    }
    
}
