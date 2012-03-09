package edu.depaul.se491.snotg.manager;

import java.util.List;

import edu.depaul.se491.snotg.UserLocation;
/*
  * Interface for user location management.
  */
public interface UserLocationManager {

	/*
	  * Updates user location.
	  */
	public void updateUserLocation(UserLocation userLoc);

	/*
	  * Returns all user locations.
	  */
	public List<UserLocation> getUserLocations();

	/*
	  * returns user locations near a current user.
	  */
	public List<UserLocation> getCloseUserLocations(UserLocation userLoc, String currentUser);
}
