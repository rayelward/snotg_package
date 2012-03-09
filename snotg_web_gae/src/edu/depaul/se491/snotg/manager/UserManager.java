package edu.depaul.se491.snotg.manager;

import edu.depaul.se491.snotg.User;
import edu.depaul.se491.snotg.UserProfile;
/*
 * Interface for user management.
 */
public interface UserManager {

	/*
	  * finds a users entry in the user table.
	  */
	public User findUser(String userId);
	/*
	  * finds a user profile in user profile table.
	  */
	public UserProfile findUserProfile(String userId);
	/*
	  * saves a user profile in the user profile table.
	  */
	public boolean saveUserProfile(UserProfile userProf);

}
