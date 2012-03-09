package edu.depaul.snotg_android.Activity;

import edu.depaul.snotg_android.R;
import edu.depaul.snotg_android.SnotgAndroidConstants;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private EditText chatPwd;	
	private EditText chatHost;
	private EditText chatPort;
	private EditText chatServiceName;
	private TextView SpecialThanksText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
	    setContentView(R.layout.settingscreen);
	    chatPwd = (EditText) this.findViewById(R.id.password);
	    chatPwd.setText(SnotgAndroidConstants.CHAT_PASSWORD);
	    chatHost = (EditText) this.findViewById(R.id.host);
	    chatHost.setText(SnotgAndroidConstants.CHAT_HOST);
	    chatPort = (EditText) this.findViewById(R.id.port);
	    chatPort.setText( Integer.toString(SnotgAndroidConstants.CHAT_PORT_NUMBER) );
	    chatServiceName = (EditText) this.findViewById(R.id.service);
	    chatServiceName.setText(SnotgAndroidConstants.CHAT_SERVICE_NAME);
	    
	    //Set Special Thanks Information
	    SpecialThanksText = (TextView) this.findViewById(R.id.specialThanks);
	    SpecialThanksText.setText("\nSpecial Thanks To: \nKunal D \nGarry T \nMichael M \nRay E \nMilad S");
	    
	    
	    
	    Button savePassword = (Button) this.findViewById(R.id.savechatinfo);
	    savePassword.setOnClickListener(
	    	new View.OnClickListener() {
				public void onClick(View view) {
					//TODO - NEEDS VALIDATION:  not null, empty Port is a number
					String text = chatPwd.getText().toString();
					SnotgAndroidConstants.CHAT_PASSWORD = text;
					
					SnotgAndroidConstants.CHAT_HOST = chatHost.getText().toString();
					SnotgAndroidConstants.CHAT_PORT_NUMBER = Integer.parseInt(chatPort.getText().toString());
					SnotgAndroidConstants.CHAT_SERVICE_NAME = chatServiceName.getText().toString();
					
					Toast toast = Toast.makeText(getApplicationContext(), "Chat setup saved!", Toast.LENGTH_LONG);
					toast.show();
				}
	    	}
	    	);
	}
}
