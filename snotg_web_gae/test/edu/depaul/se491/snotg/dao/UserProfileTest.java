package edu.depaul.se491.snotg.dao;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.User;
import edu.depaul.se491.snotg.UserProfile;
import edu.depaul.se491.snotg.dao.jdo.UserProfileJdoDao;
import edu.depaul.se491.snotg.factory.DaoFactory;
/**
 * Junit class for testing the user profile in the datastore.
 *
 */
public class UserProfileTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	private PersistenceManager pm = PMF.getFactory()
					.getPersistenceManager();
	
	@Before
	public void setUp() {
		PMF.getFactory().getPersistenceManager();
		helper.setUp();
	}

	@After
	public void tearDown() {
		pm.close();
		helper.tearDown();
	}
	
	/**
	 * Testing creating and getting from the user profile in the data store.
	 *
	 */
	@Test
	public void createAndGetTest() {

		Key key = KeyFactory.createKey("UserProfile", 456L);

		// prove entity doesn't yet exist in DB
		UserProfile foundUsrProf = find(key);
		assertNull(foundUsrProf);

		UserProfile profile = new UserProfile();
		profile.setKey(key);
		profile.setProfileName("Michael at DePaul");
		profile.setDescription("This is my DePaul profile.  I love DP!");
		profile.setShout("Go Blue Demons!");
		profile.setUserId("mmichalak");
		Key usrKey = KeyFactory.createKey("User", 1L);
		User usr = new User();
		usr.setKey(usrKey);
		usr.setUserId("mmichalak");
		usr.setUserName("Michael Michalak");
		profile.setUser(usr);
		save(profile);

		// Get the entity to prove exists in DB
		foundUsrProf = find(key);
		assertNotNull("UserLocation for mike was not found.",
				foundUsrProf);

		assertEquals("Michael at DePaul", profile.getProfileName());
		assertEquals("This is my DePaul profile.  I love DP!",
				profile.getDescription());
		assertEquals("Go Blue Demons!", profile.getShout());
		assertEquals("mmichalak", profile.getUserId());

	}

	/**
	 * Testing queries for a user's profile in the datastore
	 *
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void queryTest() {
		Key key = KeyFactory.createKey("UserProfile", 456L);
		Key usrKey = KeyFactory.createKey("User", 1L);

		// prove entity doesn't yet exist in DB
		UserProfile foundUsrProf = find(key);
		assertNull(foundUsrProf);

		UserProfile profile = new UserProfile();
		profile.setKey(key);
		profile.setProfileName("Michael at DePaul");
		profile.setDescription("This is my DePaul profile.  I love DP!");
		profile.setShout("Go Blue Demons!");
		profile.setUserId("mmichalak");
		User usr = new User();
		usr.setKey(usrKey);
		usr.setUserId("mmichalak");
		usr.setUserName("Michael Michalak");
		profile.setUser(usr);
		save(profile);

		profile = new UserProfile();
		key = KeyFactory.createKey("UserProfile", 7777L);
		profile.setKey(key);
		profile.setProfileName("Free Willie at DePaul");
		profile.setUserId("fwillie");
		usr = new User();
		usrKey = KeyFactory.createKey("User", 2L);
		usr.setKey(usrKey);
		usr.setUserId("fwillie");
		usr.setUserName("Free Willie Harms");
		profile.setUser(usr);
		save(profile);

		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();
		foundUsrProf = null;
		@SuppressWarnings("unused")
		boolean notFound = false;
		try {
			foundUsrProf = pm.getObjectById(UserProfile.class, key);

		} catch (JDOObjectNotFoundException e) {
			notFound = true;
		} finally {
		}

		// Test via query
		pm = PMF.getFactory().getPersistenceManager();
		List<UserProfile> results = null;
		try {
			javax.jdo.Query query = pm.newQuery(UserProfile.class,
					"userId == userIdParam");
			query.declareParameters("String userIdParam");

			results = (List<UserProfile>) query.execute("fwillie");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(results);
		assertEquals(1, results.size());
		assertTrue(results.get(0).getUserId()
				.equalsIgnoreCase("fwillie"));
		assertNotNull(results.get(0).getUser());
		assertTrue(results.get(0).getUser().getUserName()
				.equalsIgnoreCase("Free Willie Harms"));
	}


	/**
	 * Queries the user table for testing.
	 *
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void queryUserTest() {
		Key key = KeyFactory.createKey("User", 2L);

		@SuppressWarnings("unused")
		UserProfile foundUsrProf;

		User usr = new User();
		usr.setKey(key);
		usr.setUserId("mmichalak");
		usr.setUserName("Michael Michalak");
		// Separate test for avatar image below

		UserProfile profile = new UserProfile();
		Key keyProf = KeyFactory.createKey("UserProfile", 456L);
		profile.setKey(keyProf);
		profile.setProfileName("Michael at DePaul");
		profile.setDescription("This is my DePaul profile.  I love DP!");
		profile.setShout("Go Blue Demons!");
		profile.setUserId("mmichalak");
		profile.setUser(usr);
		save(profile);

		// Test via Key lookup
		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();
		foundUsrProf = null;
		boolean notFound = false;
		List<User> results = null;
		try {
			javax.jdo.Query query = pm.newQuery(User.class,
			"userId == userIdParam");
			query.declareParameters("String userIdParam");

			results = (List<User>) query
					.execute("mmichalak");
		} catch (Exception e) {
			notFound = true;
			e.printStackTrace();
		}
		
		assertFalse(notFound);
		assertNotNull(results);
		assertEquals(1, results.size());
		User u = results.get(0);
		assertNotNull(u);
		assertTrue(u.getUserId().equalsIgnoreCase(
				"mmichalak"));
		assertTrue(u.getUserName().equalsIgnoreCase(
		"Michael Michalak"));

	}
	
		
	/**
	 * This is not using detachable option to enable updating once PM is
	 * closed.
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void updateProfileTest() {
	
		Key key = KeyFactory.createKey("UserProfile", 456L);
		UserProfile profile = new UserProfile();
		profile.setKey(key);
		profile.setProfileName("Michael at DePaul");
		profile.setDescription("This is my DePaul profile.  I love DP!");
		profile.setShout("Go Blue Demons!");
		profile.setUserId("mmichalak");
		User usr = new User();
		Key usrKey = KeyFactory.createKey("User", 1L);
		usr.setKey(usrKey);
		usr.setUserId("mmichalak");
		usr.setUserName("Michael Michalak");
		profile.setUser(usr);
		save(profile);

		// Get the entity to prove exists in DB
		UserProfile foundUsrProf = find(key);
		assertNotNull("UserLocation for mike was not found.",
				foundUsrProf);

		assertEquals("Michael at DePaul", profile.getProfileName());
		assertEquals("This is my DePaul profile.  I love DP!",
				profile.getDescription());
		assertEquals("Go Blue Demons!", profile.getShout());
		assertEquals("mmichalak", profile.getUserId());

		// Get out the profile for updating
		UserProfile savedUsrProf = null;
		boolean notFound = false;
		List<UserProfile> results = null;
		try {
			javax.jdo.Query query = pm.newQuery(UserProfile.class,
							"userId == userIdParam");
			query.declareParameters("String userIdParam");

			results = (List<UserProfile>) query
					.execute("mmichalak");
		} catch (Exception e) {
			notFound = true;
			e.printStackTrace();
		}
		assertFalse(notFound);
		assertNotNull(results);
		assertEquals(1, results.size());
		savedUsrProf = results.get(0);

		// Now update profile
		savedUsrProf.setProfileName("Michaels new profile");
		savedUsrProf.setDescription("This is now my work profile!");
		savedUsrProf.setShout("Go Hawks!");
		save(savedUsrProf);
		
		foundUsrProf = null;
		notFound = false;
		results = null;
		try {
			javax.jdo.Query query = pm.newQuery(UserProfile.class,
							"userId == userIdParam");
			query.declareParameters("String userIdParam");

			results = (List<UserProfile>) query
					.execute("mmichalak");
		} catch (Exception e) {
			notFound = true;
			e.printStackTrace();
		}
		assertFalse(notFound);
		assertNotNull(results);
		assertEquals(1, results.size());
		foundUsrProf = results.get(0);
		assertEquals(savedUsrProf.getProfileName(), foundUsrProf.getProfileName());
		assertEquals(savedUsrProf.getDescription(),
				foundUsrProf.getDescription());
		assertEquals(savedUsrProf.getShout(), foundUsrProf.getShout());
		assertEquals("mmichalak", foundUsrProf.getUserId());
	}

	/**
	 * detachable update test
	 * 
	 */
	public void detachableUpdateTest() {

	}
	/**
	 * Testing to find a user in the system.
	 *
	 */
	@Test
	public void findUserProfileDaoTest() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		insertTestProfile();
		// Test JdoFactory
		UserProfileDao dao = DaoFactory.createUserProfileDao();
		assertTrue( dao instanceof UserProfileJdoDao );
		
		UserProfile userProf = dao.findUserProfile("se491snotg");
		assertNotNull(userProf);
		assertEquals("This is my DePaul profile.  I love DP!", userProf.getDescription());
		
		//Test when not found
		userProf = null;
		userProf = dao.findUserProfile("xxxxx");
		assertNull(userProf);
		
		// Test if mult profiles exist
		Key key = KeyFactory.createKey("UserProfile", 678L);
		UserProfile profile = new UserProfile();
		profile.setKey(key);
		profile.setProfileName("SNotG default profile");
		profile.setDescription("This is my DePaul profile");
		profile.setShout("Hello!");
		profile.setUserId("se491snotg");   	
		save(profile);
		
		assertEquals(2, ds.prepare(new Query("UserProfile")).countEntities(withLimit(10)));
		userProf = null;
		userProf = dao.findUserProfile("se491snotg");
		assertNotNull(userProf);
		assertEquals("This is my DePaul profile.  I love DP!", userProf.getDescription());
		// NOTE this does not guarantee which one is found.  Order is based on key value
	}
	

	/**
	 * Testing an insert in the data store.
	 *
	 */
	private void insertTestProfile() {
		Key key = KeyFactory.createKey("UserProfile", 456L);
		UserProfile profile = new UserProfile();
		profile.setKey(key);
		profile.setProfileName("SNotG default profile");
		profile.setDescription("This is my DePaul profile.  I love DP!");
		profile.setShout("Go Blue Demons!");
		profile.setUserId("se491snotg");   	
		save(profile);
	}

	/**
	 * Testing to find a user profile in the data store.
	 *
	 */
	private UserProfile find(Key usrProfile) {

		UserProfile foundUsrProf = null;
		try {
			foundUsrProf = pm.getObjectById(UserProfile.class,
					usrProfile);

		} catch (JDOObjectNotFoundException e) {
		} finally {

		}
		return foundUsrProf;
	}

	/**
	 * Easy way to make a users profile persistent.
	 *
	 */
	private void save(UserProfile prof) {

		try {
			pm.makePersistent(prof);
		} finally {

		}
	}
	/**
	 * Easy way to make a user entry persistent.
	 *
	 */
	@SuppressWarnings("unused")
	private void save(User usr) {

		try {
			pm.makePersistent(usr);
		} finally {

		}
	}
}