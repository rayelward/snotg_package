package edu.depaul.snotg_android.Map;

import static edu.depaul.snotg_android.SnotgAndroidConstants.EMPTY_JSON_STRING;
import static edu.depaul.snotg_android.SnotgAndroidConstants.URL_PATH_USER;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import edu.depaul.snotg_android.SnotgAndroidConstants;
import edu.depaul.snotg_android.Activity.OtherProfileActivity;
import edu.depaul.snotg_android.Profile.UserProfile;
import edu.depaul.snotg_android.util.JsonUtility;


public class ProfileOverlay extends BalloonItemizedOverlay<OverlayItem>{
	private static final String TAG = "ProfileOverlay";
	
	private ArrayList<OverlayItem> profileList = new ArrayList<OverlayItem>();
	private Context c;
	

	//Constructor for the ProfileOverlay class
	public ProfileOverlay(Drawable image, MapView mapView) {
		super(boundCenter(image), mapView);
		populate();
		c = mapView.getContext();
	}
	
	public void drawTop(Drawable image){
		return;
	}
	
	public void addOverlay(OverlayItem overlay){
		profileList.add(overlay);
		populate();
	}

	//Creates an item into the profileList Overlay array
	@Override
	protected OverlayItem createItem(int i) {
		return profileList.get(i);
	}

	//This returns the size of the profileList Overlays
	@Override
	public int size() {
		return profileList.size();
	}
	
	//remove an item if necessary
	public void removeItem(int i){
		profileList.remove(i);
		populate();
	}
	
	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		Toast.makeText(c, "Retrieving profile for " + item.getTitle(),
				Toast.LENGTH_LONG).show();
		Intent other = new Intent( c, OtherProfileActivity.class);
		UserProfile userProf = fetchProfile( item.getTitle() );
		SnotgAndroidConstants.STATE_CHAT_OTHER_USERID = userProf.getUserId(); // State for chat recipient
		
		Bundle b = new Bundle();
		b.putSerializable("profile", userProf);
		other.putExtras(b);
		c.startActivity(other);
		
		return true;
	}

	private UserProfile fetchProfile(String selectedUserId) {
		
		String retJson = JsonUtility.sendRequest(URL_PATH_USER,
				getQueryParamsForGet(selectedUserId)); 
		
		UserProfile userProf = new UserProfile();
		if (retJson != null) {
	    	try {
	    		
				JSONObject jObject = new JSONObject(retJson); 
				userProf.setProfileName( jObject.getString("profileName") );
				userProf.setDescription( jObject.getString("profileDescr") );
				userProf.setShout( jObject.getString("shout") );
				userProf.setUserId( jObject.getString("userId") );
				userProf.setUserName( jObject.getString("userName") );
				
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
		
		return userProf;		
	}

	ArrayList<ArrayList<String>> getQueryParamsForGet(String selectedUserId) {
		ArrayList<ArrayList<String>> pairs = new ArrayList<ArrayList<String>>();

		ArrayList<String> pair = new ArrayList<String>();
		pair.add(0, "get_user_profile");
		pair.add(1, "true");
		pairs.add(pair);

		pair = new ArrayList<String>();
		pair.add(0, "request_json");
		String jsonText = buildJsonForGet(selectedUserId);
		pair.add(1, jsonText);
		pairs.add(pair);

		return pairs;
	}
	
	String buildJsonForGet(String selectedUserId) {
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("userId", selectedUserId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i(TAG, jObj.toString());

		if (jObj != null)
			return jObj.toString();
		else
			return EMPTY_JSON_STRING;
	}

}
