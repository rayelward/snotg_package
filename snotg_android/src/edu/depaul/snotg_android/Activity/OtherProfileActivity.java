package edu.depaul.snotg_android.Activity;

import edu.depaul.snotg_android.R;
import edu.depaul.snotg_android.SnotgAndroidConstants;
import edu.depaul.snotg_android.Chat.XMPPClient;
import edu.depaul.snotg_android.Layout.TabBarWidget;
import edu.depaul.snotg_android.Profile.UserProfile;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.content.BroadcastReceiver;

public class OtherProfileActivity extends Activity {
	
	private static final String TAG = "OtherProfileActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_profile);
		
		Bundle b = getIntent().getExtras();
		UserProfile userProf = (UserProfile) b.get("profile");
		
		TextView otherUser = (TextView) findViewById( R.id.profile_otherUserid );
		otherUser.setText(userProf.getUserId());
		
		TextView shoutText = (TextView) findViewById( R.id.profile_otherShoutText );
		shoutText.setText(userProf.getShout());
		
		TextView descriptionText = (TextView) findViewById( R.id.profile_otherDescriptionText );
		descriptionText.setText(userProf.getDescription());		
		
		View chatButton = findViewById( R.id.profile_otherChatButton );
		chatButton.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i( TAG, "clicked chat button" );
				Intent tabs = new Intent( getBaseContext(), TabBarWidget.class);
	            TabBarWidget.switchTab(2);
	            XMPPClient.ChangeUser();
				finish();
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
}
