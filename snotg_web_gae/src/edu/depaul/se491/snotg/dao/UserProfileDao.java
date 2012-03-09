package edu.depaul.se491.snotg.dao;

import edu.depaul.se491.snotg.User;
import edu.depaul.se491.snotg.UserProfile;

/*
 * Interface for communicating with user profile.
 */
public interface UserProfileDao {

	/*
 	  * Finds a user in the user table
 	  */
	public User findUser(String userId);

	/*
 	  * Finds a user profile in the user profile table
 	  */
	public UserProfile findUserProfile(String userId);

	/*
 	  * Updates a user profile in the user profile table
 	  */
	public UserProfile updateUserProfile(UserProfile userProf);

	/*
 	  * Inserts a new user profile into the profile table
 	  */
	public void insertUserProfile(UserProfile userProf);

	/*
 	  * Deletes a user profile from the user profile table.
 	  */
	public void deleteUserProfile(String userId);
}
