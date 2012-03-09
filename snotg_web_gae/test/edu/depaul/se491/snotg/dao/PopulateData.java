package edu.depaul.se491.snotg.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.UserLocation.Loc;
/**
 * Class used to populate data into the datastore for testing.
 *
 */
public class PopulateData {

	private final static Logger LOGGER = Logger.getLogger("PopulateData");
	/**
	 * Populating fake user location data.
	 *
	 */
	public void populateUserLocation() {
		DatastoreServiceFactory
				.getDatastoreService();

		long t = new Date().getTime();
		long m = 2 * 60 * 1000;
		UserLocation usrLoc = null;

		Loc loc = new Loc(41.878341, -87.62739);
		Key usrLocKey = KeyFactory.createKey("UserLocation", "WillieNelson");
		usrLoc = new UserLocation(usrLocKey, "WillieNelson", loc);
		usrLoc.setLastUpdated(new Date(t + m));
		save(usrLoc);

		Loc loc1 = new Loc(41.7430976, -88.0089023);
		Key usrLocKey1 = KeyFactory.createKey("UserLocation", "daveyjones");
		usrLoc = new UserLocation(usrLocKey1, "daveyjones", loc1);
		usrLoc.setLastUpdated(new Date(t + m * 2));
		save(usrLoc);

		Loc loc2 = new Loc(41.879341, -87.62639);
		Key usrLocKey2 = KeyFactory.createKey("UserLocation", "freddieMerc");
		usrLoc = new UserLocation(usrLocKey2, "freddieMerc", loc2);
		usrLoc.setLastUpdated(new Date(t + m * 3));
		save(usrLoc);

		Loc loc3 = new Loc(41.877141, -87.62639);
		Key usrLocKey3 = KeyFactory.createKey("UserLocation", "se491snotg");
		usrLoc = new UserLocation(usrLocKey3, "se491snotg", loc3);	 
		usrLoc.setLastUpdated( new Date(t+m*4));
		save(usrLoc);

		Loc loc4 = new Loc(41.878341, -87.62439);
		Key usrLocKey4 = KeyFactory.createKey("UserLocation", "se491snotg2");
		usrLoc = new UserLocation(usrLocKey4, "se491snotg2", loc4);	 
		usrLoc.setLastUpdated( new Date(t+m*4));
		save(usrLoc);

		LOGGER.info("populateUserLocation data executed.");

	}

	/**
	 * Second attempt to load junk into the database.
	 *
	 */
	@SuppressWarnings("unused")
	public void junk() {
		long t = new Timestamp(new Date().getTime()).getTime();
		long m = 2 * 60 * 1000;

		ArrayList<UserLocation> usrLocList = new ArrayList<UserLocation>();
		Loc loc = new Loc(41.7430976, -88.0089023);
		Key usrLocKey = KeyFactory.createKey("UserLocation", "se491snotg");
		UserLocation usrLoc = new UserLocation(usrLocKey, "se491snotg", loc);
		usrLoc.setLastUpdated(new Timestamp(t + m));
		usrLocList.add(usrLoc);

		Loc loc1 = new Loc(41.878341, -87.62439);
		Key usrLocKey1 = KeyFactory.createKey("UserLocation", "daveyjones");
		UserLocation usrLoc1 = new UserLocation(usrLocKey1,
				"daveyjones", loc1);
		usrLoc1.setLastUpdated(new Timestamp(t + m * 2));
		usrLocList.add(usrLoc1);

		Loc loc2 = new Loc(41.879341, -87.62639);
		Key usrLocKey2 = KeyFactory.createKey("UserLocation", "freddieMerc");
		UserLocation usrLoc2 = new UserLocation(usrLocKey2,
				"freddieMerc", loc2);
		usrLoc2.setLastUpdated(new Timestamp(t + m * 3));
		usrLocList.add(usrLoc2);

		Loc loc3 = new Loc(41.877141, -87.62639);
		Key usrLocKey3 = KeyFactory.createKey("UserLocation", "WillieNelson");
		UserLocation usrLoc3 = new UserLocation(usrLocKey3,
				"WillieNelson", loc3);
		usrLoc3.setLastUpdated(new Timestamp(t + m * 4));
		usrLocList.add(usrLoc3);

		String jsonText = null;
		try {
			JSONObject obj;
			JSONArray jList = new JSONArray();
			/*
			 * obj.put("msg","Response from GAE User Service - ");
			 * obj.put("ts",new Date().toString()); StringWriter out
			 * = new StringWriter(); obj.write(out); jsonText =
			 * out.toString()usrLocList
			 */
			for (UserLocation l : usrLocList) {
				obj = new JSONObject();
				obj.put("key", l.getKey().toString());
				obj.put("username", l.getUserName());
				obj.put("lastupdated", l.getLastUpdated());
				loc = l.getLoc();
				if (loc != null) {
					obj.put("latitude", l.getLoc()
							.getLatitude());
					obj.put("longitude", l.getLoc()
							.getLongitude());
				}
				jList.put(obj);
			}
			// System.out.println(usrLocList.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}

	}

	/**
	 * Finds a user location from their key in the datastore.
	 *
	 */
	@SuppressWarnings("unused")
	private UserLocation find(Key usrLocKey) {
		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();
		UserLocation foundUsrLoc = null;
		boolean notFound = false;
		try {
			foundUsrLoc = pm.getObjectById(UserLocation.class,
					usrLocKey);
			foundUsrLoc.getLoc(); 

		} catch (JDOObjectNotFoundException e) {
			notFound = true;
		} finally {
			pm.close();
		}
		return foundUsrLoc;
	}

	private void save(UserLocation userLocat) {
		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();
		try {
			pm.makePersistent(userLocat);
		} finally {
			pm.close();
		}
	}
}