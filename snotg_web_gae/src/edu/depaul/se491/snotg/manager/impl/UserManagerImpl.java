package edu.depaul.se491.snotg.manager.impl;

import edu.depaul.se491.snotg.User;
import edu.depaul.se491.snotg.UserProfile;
import edu.depaul.se491.snotg.dao.UserProfileDao;
import edu.depaul.se491.snotg.factory.DaoFactory;
import edu.depaul.se491.snotg.manager.UserManager;
/*
 * Implements user management.
 */
public class UserManagerImpl implements UserManager {

	UserProfileDao userProfileDao;
	/*
	  * constructor.
	  */
	public UserManagerImpl() {
		userProfileDao = DaoFactory.createUserProfileDao();
	}
	/*
	  * finds a user in the datastore.
	  */
	public User findUser(String userId) {
		return userProfileDao.findUser(userId);
	}
	
	/*
	  * finds a user profile by their userid.
	  */
	public UserProfile findUserProfile(String userId) {
		UserProfile userProf = null;
		userProf = userProfileDao.findUserProfile(userId);
		// NOTE: the jdo mapping is currently not working and loading the User.
		// THis direct call is a temp work-around to get the User
		User usr = userProfileDao.findUser(userId);
		userProf.setUser(usr);
		
		return userProf;
	}
	/*
	  * Interface for user location management.
	  */
	@SuppressWarnings("unused")
	public boolean saveUserProfile(UserProfile userProf) {
		boolean isSuccess;
		try {
			UserProfile updatedUserProf = userProfileDao.updateUserProfile(userProf);
			isSuccess = true;
		}catch (Exception e) {
			isSuccess = false;
		}

		return isSuccess;
	}
}
