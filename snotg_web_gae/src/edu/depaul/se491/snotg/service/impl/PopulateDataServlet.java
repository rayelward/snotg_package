package edu.depaul.se491.snotg.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.UserLocation.Loc;
import edu.depaul.se491.snotg.dao.PopulateData;
import edu.depaul.se491.snotg.dao.PopulateUserData;
import edu.depaul.se491.snotg.manager.UserLocationManager;
import edu.depaul.se491.snotg.manager.impl.UserLocationManagerImpl;

/**
 *Class that populates example data in the data store mostly for testing.
 */
@SuppressWarnings("serial")
public class PopulateDataServlet extends HttpServlet {

	private final static Logger LOGGER = Logger
			.getLogger("PopulateDataServlet");

	/**
	 * Populates data into datastore.
	 * 
	 * URL: /populateData No parameters.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		LOGGER.info("PopulateDataServlet invoked");

		if (req.getParameter("populateUserLocation") != null) {
			PopulateData dataSeeder = new PopulateData();
			dataSeeder.populateUserLocation();
			String jsonText = null;

			UserLocationManager userLocMgr = new UserLocationManagerImpl();
			List<UserLocation> userLocs = userLocMgr.getUserLocations();

			if (userLocs == null || userLocs.size() == 0) {
				resp.getWriter().println("Data now in the datastore: []");
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
			resp.getWriter().println("Data now in the datastore: "+jsonText);
			
		} else if (req.getParameter("populateUserProfile") != null) {
			PopulateUserData dataSeeder = new PopulateUserData();
			dataSeeder.populateUser();
			dataSeeder.populateUserData();
		}
		
		resp.getWriter().println(new Date().toString());
		
	}
}
