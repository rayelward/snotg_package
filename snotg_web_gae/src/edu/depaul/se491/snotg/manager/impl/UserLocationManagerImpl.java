package edu.depaul.se491.snotg.manager.impl;

import java.util.List;

import edu.depaul.se491.snotg.UserLocation;
import edu.depaul.se491.snotg.dao.UserLocationDao;
import edu.depaul.se491.snotg.factory.DaoFactory;
import edu.depaul.se491.snotg.manager.UserLocationManager;
/*
 * Implements the user location manager.
 */
public class UserLocationManagerImpl implements UserLocationManager {

	UserLocationDao userLocDao;
	/*
	  * Constructor.
	  */
	public UserLocationManagerImpl() {
		userLocDao = DaoFactory.createUserLocationDao();
	}
	/*
	  * Updates the user location.
	  */
	@Override
	public void updateUserLocation(UserLocation userLoc) {
		userLocDao.updateUserLocation(userLoc);

	}
	
	/*
	  * gets all users.
	  */
	@Override
	public List<UserLocation> getUserLocations() {
		List<UserLocation> usrLocList;
		usrLocList = userLocDao.getUserLocations();

		return usrLocList;
	}

	/*
	  * gets all users that are close.
	  */
	public List<UserLocation> getCloseUserLocations(UserLocation userLoc, String currentUser) {
		List<UserLocation> usrLocList;
		usrLocList = userLocDao.getCloseUserLocations(userLoc, currentUser);
		return usrLocList;
	}
}
