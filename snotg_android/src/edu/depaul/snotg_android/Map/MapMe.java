package edu.depaul.snotg_android.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.depaul.snotg_android.R;

public class MapMe extends MapActivity implements LocationListener,
		OnClickListener {

	private static final String TAG = "MapMe";
	
	public ArrayList<OverlayItem> userItem = new ArrayList<OverlayItem>();

	private static double lat;
	private static double lon;

	// Important Map Information and Initialization
	static Context context;
	private MapController MapControl;
	private MapView MapView;
	LocationManager LocMan;
	Location Loc;
	Criteria criteria = new Criteria();
	String GPSprovider = LocationManager.GPS_PROVIDER;
	String agpsProvider = LocationManager.NETWORK_PROVIDER;
	Bundle locBundle;
	
	int numberSats = -1;
	float satAccuracy = 2000;
	
	private String currentProvider;
	private List<Overlay> mapOverlays;
	// How often do we want to update the location?
	long GPSupdateInterval = 5000; // millisec
	float GPSmoveInterval = 1; // In meters

	// Overlay stuff
	private SnotgLocationOverlay myLocationOverlay;

	// Button to trigger the overLay of the other nearby users :)
	private Button nearbyUsersButton;
	private Drawable usermarker;
	private ProfileOverlay nearbyUsersOverlay;
	private boolean usersDisplayed = false;
	
	// Button to trigger centering location 
	private Button centerMeButton;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapme);

		// Get application context for later use
		context = getApplicationContext();
	
		// Add controls to map
		MapView = (MapView) findViewById(R.id.mv2);
		MapView.setSatellite(false);
		MapView.setTraffic(false);
		MapView.setBuiltInZoomControls(true);
		int maxZoom = MapView.getMaxZoomLevel();
		int initZoom = (int) (0.90 * (double) maxZoom);
		MapControl = MapView.getController();
		MapControl.setZoom(initZoom);

		// Set up location manager for determining the present location of the
		// phone
		LocMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		    	centerOnLocation(location, null);
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		  };
		  
		// Register the listener with the location manager to receive location updates from GPS
		LocMan.requestLocationUpdates(GPSprovider, GPSupdateInterval,GPSmoveInterval, this);
				
		// Register the listener with the location manager to receive location updates from CELL TOWERS and WIFI
		LocMan.requestLocationUpdates(agpsProvider, 0, 0, this);
		
		// Set up compass and dot
		List<Overlay> overlays = MapView.getOverlays();
		myLocationOverlay = new SnotgLocationOverlay(this, MapView);
		overlays.add(myLocationOverlay);
		
		// Nearby Users Button and Onclick Event
		nearbyUsersButton = (Button) findViewById(R.id.nearbyUsers);
		nearbyUsersButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				displayNearbyUsers();
			}
		});
		
		// Center Map on Location and Onclick Event
		centerMeButton = (Button) findViewById(R.id.centerMe);
		centerMeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String bestProvider = LocMan.getBestProvider(criteria, true);
				centerOnLocation(null, bestProvider);
			}
		});

	}
	
	public static double getLatitude() {
		return lat;
	}

	public static double getLongitude() {
		return lon;
	}

	/*****************************************************************
	 * getUserLocations():
	 * Method provides call to GAE servlet to obtain nearby user locations.
	 * Adds users to local array. 	 
	 * ***************************************************************/
	public void getUserLocations() {
		String profile = edu.depaul.snotg_android.Activity.HeartbeatTask
				.getJSONuserLocation();

		HashMap<String, String> ret = JSONmap.readJsonReturnObj(profile);

		Set set = ret.entrySet();
		Iterator i = set.iterator();
		
		// Clear out array
		userItem.clear();

		// Loops through received users and places them into map array
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();

			UserLocationObj userLocation = new UserLocationObj();
			userLocation = (UserLocationObj) me.getValue();

			String shout = userLocation.getShout();
			if (shout == null || shout.isEmpty())
				shout = userLocation.getLastUpdated();
				
			userItem.add(
					new OverlayItem(
							new GeoPoint(	(int) (userLocation.getLat() * 1e6),
											(int) (int) (userLocation.getLon() * 1e6) ), 
							userLocation.getUsername(), 
							shout)
					);
		}

		Log.i(TAG, "Size of Array: " + userItem.size());
	}

	/*****************************************************************
	 * displayNearbyUsers():
	 * If nearby users button is clicked, method calls getUserLocations
	 * to obtain users that are in the vicinity using json object call to 
	 * GAE.  Once users are returned, stores users in array and draws location
	 * onto map.
	 * 
	 * If button is pressed additional times, will refresh nearby users with 
	 * updated nearby users.	 
	 * ***************************************************************/
	public void displayNearbyUsers() {
		
		//Get nearByUsers
		getUserLocations();
		
		int userLength = userItem.size();
		// Create itemizedOverlay2 if it doesn't exist and display all three
		// items

		mapOverlays = MapView.getOverlays();
		usermarker = this.getResources().getDrawable(R.drawable.usermarker);
		nearbyUsersOverlay = new ProfileOverlay(usermarker, MapView);
		// Display all three items at once
		for (int i = 0; i < userLength; i++) {
			nearbyUsersOverlay.addOverlay(userItem.get(i));
		}
		
		// Remove prev nearby users overlay.  Number of overlays should not exceed 2
		// The first overlay is for my position
		if (mapOverlays.size() > 1)
			for (int i=1; i < mapOverlays.size(); ++i)
				mapOverlays.remove(i);

		mapOverlays.add(nearbyUsersOverlay);
		usersDisplayed = true;


		// Added symbols will be displayed when map is redrawn so force redraw
		// now
		MapView.postInvalidate();

	}

	/*****************************************************************
	 * isRouteDisplayed():
	 * Returns false	 
	 * ***************************************************************/
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/*****************************************************************
	 * onLocationChanged():
	 * Providers will notify of any location changed due to application 
	 * interval request for location updates.  If location is changed, map
	 * will center on new location.	 
	 * ***************************************************************/
	@Override
	public void onLocationChanged(Location location) {
		centerOnLocation(location, null);
	}

	/*****************************************************************
	 * OnProviderDisabled():
	 * If provider is disabled (ie. network, gps), will be removed from
	 * application requesting location updates 
	 * ***************************************************************/
	@Override
	public void onProviderDisabled(String provider) {
		LocMan.removeUpdates(this);
	}

	/*****************************************************************
	 * onProviderEnabled():
	 * Once GPS is enabled, will request location updates from GPS at provided 
	 * parameter intervals.	 
	 * ***************************************************************/
	@Override
	public void onProviderEnabled(String provider) {
		LocMan.requestLocationUpdates(provider, GPSupdateInterval,GPSmoveInterval, this);
	}

	/*****************************************************************
	 * onStatusChanged():
	 * Allows location provider to notify map of any status changes on GPS or network location,
	 * subsequently calling the center on location function to center map onto new position.	 
	 * ***************************************************************/
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		centerOnLocation(null, provider);
	}

	/*****************************************************************
	 * centerOnLocation():
	 * With in parameters of either the location of type 'Location,' OR
	 * if provider is passed in method, will center map on current location.
	 * 
	 * If Location is passed parameter, will directly center on longitude and 
	 * latitude.
	 * 
	 * If Provider is passed parameter, will find longitude and latitude through
	 * provider get functions, and then center map on location.	 
	 * ***************************************************************/
	private void centerOnLocation(Location location, String inProvider) {
		
			if (inProvider != null){
				Loc = LocMan.getLastKnownLocation(inProvider);
				//Must check to see if there is proper location
					if (Loc != null){
					lat = Loc.getLatitude();			
					lon = Loc.getLongitude();
					}
				}
				else{
					lat = location.getLatitude();
					lon = location.getLongitude();
				}
			GeoPoint newPoint = new GeoPoint((int) (lat * 1e6),
					(int) (lon * 1e6));
			MapControl.animateTo(newPoint);			
	}
	
	 /*****************************************************************
	 * onPause():
	 * On pause of application, (ie. backgrounding application) GPS will 
	 * be notified and turned off to save battery.	 
	 * ***************************************************************/
	public void onPause() {
		super.onPause();
		myLocationOverlay.disableCompass();
		myLocationOverlay.disableMyLocation();
		LocMan.removeUpdates(this);
	}

	 /*****************************************************************
	 * onResume(): 
	 * On resume of application, GPS will be notified and restarted
	 *****************************************************************/
	public void onResume() {
		super.onResume();
		LocMan.requestLocationUpdates(GPSprovider, GPSupdateInterval,
				GPSmoveInterval, this);
		LocMan.requestLocationUpdates(agpsProvider, 0, 0, this);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	/**
	 * These are only used by the unit test in the test project
	 */
	public ProfileOverlay getNearbyUsersOverlay() {
		return nearbyUsersOverlay;
	}
	public void setNearbyUsersOverlay(ProfileOverlay nearbyUsersOverlay) {
		this.nearbyUsersOverlay = nearbyUsersOverlay;
	}
	
	
	
}
