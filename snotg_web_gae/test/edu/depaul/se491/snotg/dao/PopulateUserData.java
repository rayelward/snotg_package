package edu.depaul.se491.snotg.dao;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.User;
import edu.depaul.se491.snotg.UserProfile;
/**
 * Populates the datastore with fake profile and user information.
 *
 */
public class PopulateUserData {

	private final static Logger LOGGER = Logger.getLogger("PopulateData");
	
	/**
	 * Populates the User table.
	 *
	 */
	public void populateUserData() {
		DatastoreServiceFactory.getDatastoreService();
		
		

		Key key2 = KeyFactory.createKey("User", "WillieNelson");
		User usr2 = new User();
		usr2.setKey(key2);
		usr2.setUserId("WillieNelson");
		usr2.setUserName("Willie Nelson");

		Key key3 = KeyFactory.createKey("User", "se491snotg");
		User usr3 = new User();
		usr3.setKey(key3);
		usr3.setUserId("se491snotg");
		usr3.setUserName("SNotG Shared User");
		
		Key key6 = KeyFactory.createKey("User", "se491snotg2");
		User usr6 = new User();
		usr6.setKey(key6);
		usr6.setUserId("se491snotg2");
		usr6.setUserName("SNotG Shared User #2");
		
		Key key4 = KeyFactory.createKey("User", "daveyjones");
		User usr4 = new User();
		usr4.setKey(key4);
		usr4.setUserId("daveyjones");
		usr4.setUserName("Davey J Jones");
		
		Key key5 = KeyFactory.createKey("User",  "freddieMerc");
		User usr5 = new User();
		usr5.setKey(key5);
		usr5.setUserId( "freddieMerc");
		usr5.setUserName("freddieMerc");

		UserProfile profile;

		profile = new UserProfile();
		Key key = KeyFactory.createKey("UserProfile","freddieMerc");
		profile.setKey(key);
		profile.setProfileName("FreddieMerc");
		profile.setDescription("another");
		profile.setShout("HIHIHIHIHHI!");
		profile.setUserId("freddieMerc");
		profile.setUser(usr5);
		save(profile);

		profile = new UserProfile();
		key = KeyFactory.createKey("UserProfile", "WillieNelson");
		profile.setKey(key);
		profile.setProfileName("Free Willie at DePaul");
		profile.setDescription("This is my third different profile, blah, blah, blah");
		profile.setShout("I am here!");
		profile.setUserId("WillieNelson");
		profile.setUser(usr2);
		save(profile);

		profile = new UserProfile();
		key = KeyFactory.createKey("UserProfile", "se491snotg");
		profile.setKey(key);
		profile.setProfileName("SNotgs Test Profile");
		profile.setDescription("This is my SNotG profile for Mr. DePaul.  I am just a robot, blah, blah");
		profile.setShout("The SNotG Master of DePaul");
		profile.setUserId("se491snotg");
		profile.setUser(usr3);
		save(profile);

		profile = new UserProfile();
		key = KeyFactory.createKey("UserProfile", "se491snotg2");
		profile.setKey(key);
		profile.setProfileName("SNotg2s Test Profile");
		profile.setDescription("This is an alternate profile for SNotG.");
		profile.setShout("SNotG of the Loop");
		profile.setUserId("se491snotg2");
		profile.setUser(usr6);
		save(profile);
		
		profile = new UserProfile();
		key = KeyFactory.createKey("UserProfile", "daveyjones");
		profile.setKey(key);
		profile.setProfileName("Daveys Test Profile!");
		profile.setDescription("This is my one and only profile.");
		profile.setShout("Hey Hey we're the monkeys!");
		profile.setUserId("daveyjones");
		profile.setUser(usr4);
		save(profile);
		
		LOGGER.info("populateUserProfile data executed.");

	}

	/**
	 * Deprecated.  All User data is filled in with profile because of dependencies now.
	 *
	 */
	public void populateUser() {
		DatastoreServiceFactory.getDatastoreService();
	}

	/**
	 * Easy way to make a users profile persistent.
	 *
	 */
	private void save(UserProfile prof) {
		PersistenceManager pm = PMF.getFactory().getPersistenceManager();
		try {
			pm.makePersistent(prof);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unused")
	/**
	 * Easy way to make a user persistent.
	 *
	 */
	private void save(User usr) {
		PersistenceManager pm = PMF.getFactory().getPersistenceManager();
		try {
			pm.makePersistent(usr);
		} finally {
			pm.close();
		}
	}

}