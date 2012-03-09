package edu.depaul.se491.snotg.service.util;

import java.io.StringWriter;
import java.util.Date;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import edu.depaul.se491.snotg.UserProfile;

/**
 * class for building json strings out of the user profile.
 */
public class JsonBuilder {
	
	public static String buildProfileReturnJson(UserProfile userProf, String msg) {
		
		String jsonText = null;

		try {
			JSONObject obj = new JSONObject();
			obj.put("ts", new Date().toString());
			if (userProf == null)
				obj.put("msg", "Error saving or getting profile for user:  " + msg);
			else {
				obj.put("msg", msg);
				if (userProf.getKey() != null)
					obj.put("profileKey", userProf.getKey().toString());
				obj.put("profileName", userProf.getProfileName());
				obj.put("profileDescr", userProf.getDescription());
				obj.put("shout", userProf.getShout());
				obj.put("userId", userProf.getUserId());
				if (userProf.getUser() != null) {
					obj.put("userName", userProf.getUser().getUserName());
				}
			}
			StringWriter out = new StringWriter();
			obj.write(out);
			jsonText = out.toString();
			//System.out.print(jsonText);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonText;
	}
}
