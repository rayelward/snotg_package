package edu.depaul.snotg_android.Layout;


import edu.depaul.snotg_android.R;
import edu.depaul.snotg_android.Activity.AndroidMapActivity;
import edu.depaul.snotg_android.Activity.HeartbeatTask;
import edu.depaul.snotg_android.Activity.MyProfileActivity;
import edu.depaul.snotg_android.Activity.NearbyListActivity;
import edu.depaul.snotg_android.Activity.SettingsActivity;
import edu.depaul.snotg_android.Chat.XMPPClient;
import edu.depaul.snotg_android.Map.MapMe;
import edu.depaul.snotg_android.R.drawable;
import edu.depaul.snotg_android.R.layout;
import edu.depaul.snotg_android.SnotgAndroidConstants;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class TabBarWidget extends TabActivity {
	
	static String TAG = "TabBarWidget";
	private static TabHost tabHost;
	public static TabBarWidget self;
	public int startingTab =1;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    self = this;
	    setContentView(R.layout.tab_bar_layout);

	    Resources res = getResources(); // Resource object to get Drawables
	     tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	    	    
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, MyProfileActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    
	    spec = tabHost.newTabSpec("profile").setIndicator("Profile",
	                      res.getDrawable(R.drawable.ic_tab_profile))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    //Map
	    intent = new Intent().setClass(this, MapMe.class);
	    spec = tabHost.newTabSpec("map").setIndicator("Map",
	                      res.getDrawable(R.drawable.ic_tab_map))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    //chat
	    intent = new Intent().setClass(this, XMPPClient.class);
	    spec = tabHost.newTabSpec("chat").setIndicator("Chat",
	                      res.getDrawable(R.drawable.ic_tab_list))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    //Settings
	    intent = new Intent().setClass(this, SettingsActivity.class);
	    spec = tabHost.newTabSpec("settings").setIndicator("Settings",
	                      res.getDrawable(R.drawable.ic_tab_settings))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(1);
	    
	    // Start the Heartbeat thread
	    Log.i("TabBarWidget", "Starting the Heartbeat....");
		new HeartbeatTask().execute(null);
	}
	public static void switchTab(int tab){
        tabHost.setCurrentTab(tab);
	}
        public static void setStartingTab(int tab){
            tabHost.setCurrentTab(tab);
}
}
