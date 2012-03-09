package edu.depaul.se491.snotg.dao.jdo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.User;
import edu.depaul.se491.snotg.UserProfile;
import edu.depaul.se491.snotg.dao.UserProfileDao;

/*
 * Implementation for communicating with user profile.
 */
public class UserProfileJdoDao implements UserProfileDao{

	private final static Logger LOGGER = Logger.getLogger("UserProfileDao");
	

	/*
 	  * Finds a user profile in the user profile table
 	  */
	@SuppressWarnings("unchecked")
	@Override
	public UserProfile findUserProfile(String userId) {
		PersistenceManager pm = PMF.getFactory().getPersistenceManager();
		List<UserProfile> results = null;
		
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(UserProfile.class,
					"userId == userIdParam");
			query.declareParameters("String userIdParam");
			
			results = (List<UserProfile>) query.execute(userId);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error getting Profile for user=" + userId);
			e.printStackTrace();
		}
		finally {
			if (query != null)
				query.closeAll();
		}
		
		UserProfile returnProf = null;
		if (results != null && results.size()>0)
			returnProf = results.get(0);
		
		return returnProf;
	}

	/*
 	  * Updates a user in the user profile table
 	  */
	@SuppressWarnings("null")
	@Override
	public UserProfile updateUserProfile(UserProfile userProf) {
		PersistenceManager pm = PMF.getFactory().getPersistenceManager();
		
		UserProfile foundUserProf = null;
		
		javax.jdo.Query query = null;
		try {
			query = pm.newQuery(UserProfile.class,
					"userId == userIdParam");
			query.declareParameters("String userIdParam");
			
			@SuppressWarnings("unchecked")
			List<UserProfile> results = (List<UserProfile>) query.execute(userProf.getUserId());
			if (results == null && results.size() != 1) {
				LOGGER.log(Level.WARNING, "Error saving Profile = " + userProf.toString());
				return null;
			}
			
			foundUserProf = results.get(0);
			if (userProf.getProfileName() != null && !userProf.getProfileName().isEmpty())
				foundUserProf.setProfileName(userProf.getProfileName());
			if (userProf.getDescription() != null && !userProf.getDescription().isEmpty())
				foundUserProf.setDescription(userProf.getDescription());
			if (userProf.getShout() != null && !userProf.getShout().isEmpty())
				foundUserProf.setShout(userProf.getShout());
			
			pm.makePersistent(foundUserProf);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error saving Profile = " + userProf.toString());
			e.printStackTrace();
			return null;
		}
		finally {
			pm.close();
		}

		return foundUserProf;		
	}

	/*
	  * Inserts a user profile from the user profile table.
	  */
	@Override
	public void insertUserProfile(UserProfile userProf) {
		// TODO Auto-generated method stub
	}

	/*
	  * Deletes a user profile from the user profile table.
	  */
	@Override
	public void deleteUserProfile(String userId) {
		// TODO Auto-generated method stub
	}
	

	/**
	 * Get User obj for a specific user by userId
	 */
	@SuppressWarnings("unchecked")
	public User findUser(String userId) {
		PersistenceManager pm = PMF.getFactory()
		.getPersistenceManager();

		Query query = pm.newQuery(User.class);
		
		List<User> results = null;
		User usr = null;
		try {
			query = pm.newQuery(User.class,
			"userId == userIdParam");
			query.declareParameters("String userIdParam");

			results = (List<User>) query
					.execute(userId);
			
			if (results != null) {
				if (results.size() == 1) {
					usr = results.get(0);
				}
				else if (results.size() == 0) {
					LOGGER.log(Level.WARNING, "User not found for userId=" + userId);
					usr = null;
				}
				else if (results.size() > 1) {
					usr = results.get(0);
					LOGGER.log(Level.WARNING, "Found more than one user when expecting one for userId=" + userId);
				}
			} else {
				usr = null;
			}
		} finally {
			query.closeAll();
		}
		
		return usr;
	}
}
