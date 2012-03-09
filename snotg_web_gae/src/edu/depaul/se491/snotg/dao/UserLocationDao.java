package edu.depaul.se491.snotg.dao;

import java.util.List;

import edu.depaul.se491.snotg.UserLocation;

/*
 * Interface for communicating with the user location 
 */
public interface UserLocationDao {

	/*
 	  * Returns the full list of user location in the data store.
 	  */
	public List<UserLocation> getUserLocations();

	/*
 	  * Updates a user's location in the data store.
 	  */
	public boolean updateUserLocation(UserLocation userLoc);

	/*
 	  * Returns a list of user near a current location.
 	  */
	public List<UserLocation> getCloseUserLocations(UserLocation userLoc, String currentUser);
}
