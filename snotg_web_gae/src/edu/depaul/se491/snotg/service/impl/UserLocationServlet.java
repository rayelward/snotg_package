package edu.depaul.se491.snotg.service.impl;

import static edu.depaul.se491.snotg.SnotgConstants.INVALID_REQUEST_PARAM_MSG;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.User;
import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.UserLocation.Loc;
import edu.depaul.se491.snotg.UserLocationJson;
import edu.depaul.se491.snotg.UserProfile;
import edu.depaul.se491.snotg.manager.UserLocationManager;
import edu.depaul.se491.snotg.manager.UserManager;
import edu.depaul.se491.snotg.manager.impl.UserLocationManagerImpl;
import edu.depaul.se491.snotg.manager.impl.UserManagerImpl;

/**
 * This class represents a restful service. The service contracts are:
 * 
 * Requests are returned as json strings
 * 
 */
@SuppressWarnings("serial")
public class UserLocationServlet extends HttpServlet {

	
	UserLocationManager userLocMgr = new UserLocationManagerImpl();
	UserManager userMgr = new UserManagerImpl();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String jsonResponse = null;

		if (req.getParameter("get_user_locs") != null)
			jsonResponse = getUserLocations(req);
		else if (req.getParameter("user_heartbeat") != null)
			jsonResponse = handleHeartbeat(req);
		else
			jsonResponse = INVALID_REQUEST_PARAM_MSG;

		resp.getWriter().println(jsonResponse);
	}

	/**
	 * URL: user_locations?get_user_locs
	 * 
	 * Retrieves list of active user geo locations
	 * 
	 * @return String
	 */
	private String getUserLocations(HttpServletRequest req) {
		/*
		 * FOR AUTH com.google.appengine.api.users.UserService
		 * userService = UserServiceFactory.getUserService(); User user
		 * = userService.getCurrentUser();
		 */

		String jsonText = null;

		List<UserLocation> userLocs = userLocMgr.getUserLocations();

		if (userLocs == null || userLocs.size() == 0) {
			return "[]";
		}

		JSONArray jList = new JSONArray();
		try {
			JSONObject obj;
			for (UserLocation l : userLocs) {
				obj = new JSONObject();
				obj.put("key", l.getKey().toString());
				obj.put("username", l.getUserName());
				obj.put("lastupdated", l.getLastUpdated());
				Loc loc = l.getLoc();
				if (loc != null) {
					obj.put("latitude", l.getLoc()
							.getLatitude());
					obj.put("longitude", l.getLoc()
							.getLongitude());
				}
				jList.put(obj);
			}
			StringWriter out = new StringWriter();
			jList.write(out);
			jsonText = out.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonText;
	}

	/*
	 * URL: user_locations?user_heartbeat
	 * 
	 * This takes and save the current users location and returns the list
	 * of active user geo locations (as with get_user_locs call).
	 */
	private String handleHeartbeat(HttpServletRequest req) {
		UserLocation userLoc = new UserLocation();

		String reqJson = req.getParameter("request_json");

		String currentUser = new String();
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserLocationJson userJson = mapper.readValue(
					reqJson.getBytes(),
					UserLocationJson.class);
			
			//check if in user table if not, makes default table and user.
			if(userMgr.findUser(userJson.getUserName()) == null){
				addToUserTable(userJson.getUserName());
			}
			userLoc.setUserName(userJson.getUserName());
			currentUser = userJson.getUserName();
			UserLocation.Loc loc = new UserLocation.Loc(userJson
					.getLatit().doubleValue(), userJson
					.getLongit().doubleValue());
			userLoc.setLoc(loc);
			userLoc.setLastUpdated(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		userLocMgr.updateUserLocation(userLoc);
		
		String jsonText = null;

		List<UserLocation> userLocs = userLocMgr.getCloseUserLocations(userLoc, currentUser);
		
		if (userLocs == null || userLocs.size() == 0) {
			return "[]";
		}

		JSONArray jList = new JSONArray();
		try {
			JSONObject obj;
			for (UserLocation userLocation : userLocs) {
				obj = new JSONObject();
				obj.put("key", userLocation.getKey().toString());
				obj.put("username", userLocation.getUserName());
				obj.put("lastupdated", userLocation.getLastUpdated());
				Loc loc = userLocation.getLoc();
				if (loc != null) {
					obj.put("latitude", userLocation.getLoc()
							.getLatitude());
					obj.put("longitude", userLocation.getLoc()
							.getLongitude());
				}
				//Gets profile from the user profile table and attaches the current shout.
				obj.put("shout", userMgr.findUserProfile(userLocation.getUserName()).getShout());
				
				jList.put(obj);
			}
			StringWriter out = new StringWriter();
			jList.write(out);
			jsonText = out.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonText;
	}



	/**
	 * Adds a user and user profile to new users that use the heartbeat.
	 */
	private void addToUserTable(String userName) {
		Key key1 = KeyFactory.createKey("User", userName);
		User usr = new User();
		usr.setKey(key1);
		usr.setUserId(userName);
		usr.setUserName(userName);
		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();
		
		UserProfile profile = new UserProfile();
		Key key2 = KeyFactory.createKey("UserProfile", userName);
		profile.setKey(key2);
		profile.setProfileName("");
		profile.setShout("");
		profile.setUserId(userName);
		profile.setDescription("");
		
		
		try {
			pm.makePersistent(usr);
			pm.makePersistent(profile);
		} finally {
			pm.close();
		}
	}
}
