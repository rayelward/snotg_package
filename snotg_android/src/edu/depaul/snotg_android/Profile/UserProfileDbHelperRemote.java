package edu.depaul.snotg_android.Profile;

import static edu.depaul.snotg_android.SnotgAndroidConstants.EMPTY_JSON_STRING;
import static edu.depaul.snotg_android.SnotgAndroidConstants.URL_PATH_USER;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import edu.depaul.snotg_android.SnotgAndroidConstants;
import edu.depaul.snotg_android.util.JsonUtility;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserProfileDbHelperRemote extends SQLiteOpenHelper implements UserProfileDbHelper {
	
	private static final String TAG = UserProfileDbHelperRemote.class.getSimpleName();
	private static final int VERSION = 2;
	
	public UserProfileDbHelperRemote( Context context ) {
		super( context, null, null, VERSION );
	}
	
	/**
	 * NOT sure if we will really need this.  From orig implem
	 */
	@Deprecated
	public UserProfile[] getUserProfiles() {		
		return null;
	}
	
	
	/**
	 * 
	 */
	public UserProfile findUserProfile(String userId) {
		
		String retJson = JsonUtility.sendRequest(URL_PATH_USER,
				getQueryParamsForGet()); 
		
		UserProfile userProf = new UserProfile();
		if (retJson != null) {
	    	try {
	    		
				JSONObject jObject = new JSONObject(retJson); 
				//userProf.setProfileName( jObject.getString("profile_key") );
				userProf.setProfileName( jObject.getString("profileName") );
				userProf.setDescription( jObject.getString("profileDescr") );
				userProf.setShout( jObject.getString("shout") );	
				// retJson contains username and userid, but it is not needed.  Ignore
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		return userProf;
	}
	
	/**
	 * 
	 */
	public UserProfile saveUserProfile(UserProfile userProf) {
		Log.i( TAG, "Saving User Profile" );
		
		String retJson = JsonUtility.sendRequest(URL_PATH_USER,
				getQueryParamsForSave(userProf)); 
		
		if (retJson != null) {
	    	try {
	    		//User userProfRet = new UserProfile();
				JSONObject jObject = new JSONObject(retJson); 
				//userProf.setProfileName( jObject.getString("profile_key") );
				userProf.setProfileName( jObject.getString("profileName") );
				userProf.setDescription( jObject.getString("profileDescr") );
				userProf.setShout( jObject.getString("shout") );	
				// retJson contains username and userid, but it is not needed.  Ignore
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

		return userProf;
	}

	ArrayList<ArrayList<String>> getQueryParamsForSave(UserProfile userProf) {
		ArrayList<ArrayList<String>> pairs = new ArrayList<ArrayList<String>>();

		ArrayList<String> pair = new ArrayList<String>();
		pair.add(0, "save_user_profile");
		pair.add(1, "true");
		pairs.add(pair);

		pair = new ArrayList<String>();
		pair.add(0, "request_json");
		String jsonText = buildJsonForSave(userProf);
		pair.add(1, jsonText);
		pairs.add(pair);

		return pairs;
	}
	
	String buildJsonForSave(UserProfile userProf) {
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("userId", SnotgAndroidConstants.STATE_USERID);
			jObj.put("profileName", userProf.getProfileName());
			jObj.put("profileDescr", userProf.getDescription());
			jObj.put("shout", userProf.getShout());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i(TAG, jObj.toString());

		if (jObj != null)
			return jObj.toString();
		else
			return EMPTY_JSON_STRING;
	}


	ArrayList<ArrayList<String>> getQueryParamsForGet() {
		ArrayList<ArrayList<String>> pairs = new ArrayList<ArrayList<String>>();

		ArrayList<String> pair = new ArrayList<String>();
		pair.add(0, "get_user_profile");
		pair.add(1, "true");
		pairs.add(pair);

		pair = new ArrayList<String>();
		pair.add(0, "request_json");
		String jsonText = buildJsonForGet();
		pair.add(1, jsonText);
		pairs.add(pair);

		return pairs;
	}
	
	String buildJsonForGet() {
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("userId", SnotgAndroidConstants.STATE_USERID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i(TAG, jObj.toString());

		if (jObj != null)
			return jObj.toString();
		else
			return EMPTY_JSON_STRING;
	}
	
	/* (non-Javadoc)
	 * @see edu.depaul.snotg_android.Profile.UserProfileDbHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		return;
	}
	
	/* (non-Javadoc)
	 * @see edu.depaul.snotg_android.Profile.UserProfileDbHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		return;

	}

	/* (non-Javadoc)
	 * @see edu.depaul.snotg_android.Profile.UserProfileDbHelper#closeItUp()
	 */
	@Override
	public void closeItUp() {
		return;
	}

}
