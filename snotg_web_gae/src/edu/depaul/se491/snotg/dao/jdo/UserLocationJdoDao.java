package edu.depaul.se491.snotg.dao.jdo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Query;
import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.dao.UserLocationDao;

/*
 * Implementation for communicating with the user location 
 */
public class UserLocationJdoDao implements UserLocationDao {

	private final static Logger LOGGER = Logger
			.getLogger("UserLocationDao");

	/*
 	  * Returns the full list of user location in the data store.
 	  */
	@SuppressWarnings("unchecked")
	public List<UserLocation> getUserLocations() {
		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();

		Query query = pm.newQuery(UserLocation.class);
		query.setOrdering("lastUpdated desc");

		List<UserLocation> results = null;
		try {
			results = (List<UserLocation>) query.execute();
		} finally {
			query.closeAll();
		}
		return results;
	}

	/*
 	  * Updates a user's location in the data store.
 	  */
	public List<UserLocation> getCloseUserLocations(UserLocation userLoc, String currentUser) {
		UserLocation.Loc loc = userLoc.getLoc();
		
		double minLong = loc.getLongitude() - 0.05;
		double maxLong = loc.getLongitude() + 0.05;
		double minLat = loc.getLatitude() - 0.05;
		double maxLat = loc.getLatitude() + 0.05;
		
		List<UserLocation> results = getUserLocations();
		List<UserLocation> filteredResults = new ArrayList<UserLocation>();
		
		for(UserLocation result : results){
			double testLat = result.getLoc().getLatitude();
			double testLong = result.getLoc().getLongitude();
			String userName = result.getUserName();
			if ((testLat > minLat && testLat < maxLat &&
					testLong > minLong && testLong < maxLong) && !(currentUser.equals(userName))){
				filteredResults.add(result);
			}
		}
		
		return filteredResults;
	}

	/*
 	  * Returns a list of user near a current location.
 	  */
	@SuppressWarnings("unchecked")
	public boolean updateUserLocation(UserLocation userLoc) {
		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();

		List<UserLocation> results = null;
		Query query = null;
		try {
			query = pm.newQuery(UserLocation.class,
					"userName == userNameParam");
			query.declareParameters("String userNameParam");

			results = (List<UserLocation>) query.execute(userLoc
					.getUserName());
			if (results != null && results.size() == 1) {
				UserLocation userLocPersist = results.get(0);
				userLocPersist.setLastUpdated(new Date());
				userLocPersist.setLoc(userLoc.getLoc());
			} else {
				//the user wasn't there, we need to make it persist.
				Key profileKey = KeyFactory.createKey("UserLocation", userLoc.getUserName());
				userLoc.setKey(profileKey);
				pm.makePersistent(userLoc);
			}

		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error trying to find user:  "
							+ userLoc.getUserName());
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return false;
	}
}
