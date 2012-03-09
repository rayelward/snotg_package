package edu.depaul.snotg_android.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import edu.depaul.snotg_android.R;
import edu.depaul.snotg_android.SnotgAndroidConstants;
import edu.depaul.snotg_android.Auth.AppInfo;

/**
 * Login screen activity displaying list of device user accounts
 * 
 * @author mmichalak
 *
 */
public class AccountListActivity extends Activity {
	protected AccountManager accountManager;
	protected Intent intent;
	private Button login;
	private String username = null;
	private Account account;


	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	//Remove title bar
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
        super.onCreate(savedInstanceState);
        accountManager = AccountManager.get(getApplicationContext());
        //TODO - Move account type ref to constants or somewhere externalizable as a config param
        Account[] accounts = accountManager.getAccountsByType("com.google");  
        setContentView(R.layout.applicationsplash);
        //this.setListAdapter(new ArrayAdapter<Account>(this, R.layout.applicationsplash, accounts));    
        
        final Spinner spinner2 = (Spinner) findViewById(R.id.applicationlogspinner);
        ArrayAdapter<Account> adapter2 = new ArrayAdapter<Account>(this,
                    android.R.layout.simple_spinner_item, accounts);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        
        login = (Button) findViewById(R.id.button1);
        login.setOnClickListener(
        	new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(AccountListActivity.this, AppInfo.class);
					intent.putExtra("account", account);
					SnotgAndroidConstants.STATE_USERID = username;
					
					startActivity(intent);
				}
        	}
        );
    
        /**
         * Listener for account selection from the Account List
         */
        spinner2.setOnItemSelectedListener(
        	new OnItemSelectedListener() {      
				@Override
				public void onItemSelected(AdapterView<?> arg0, View v, int position,
						long id) {
					// TODO Auto-generated method stub
					account = (Account)spinner2.getItemAtPosition(position);
					
					if (account != null)
						username = account.name.substring(0, account.name.indexOf("@"));
								
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
        	}
        );

    }
}