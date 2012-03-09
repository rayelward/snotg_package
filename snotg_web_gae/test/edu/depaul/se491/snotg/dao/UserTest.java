package edu.depaul.se491.snotg.dao;

import static org.junit.Assert.assertEquals;
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
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.User;
/**
 * Tests the user table.
 *
 */
@SuppressWarnings("unused")
public class UserTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	/**
	 * Testing persistence of the datastore
	 *
	 */
	@Test
	public void createAndGetTest() {
		Key key = KeyFactory.createKey("User", 456L);

		// prove entity doesn't yet exist in DB
		User foundUsr = find(key);
		assertNull(foundUsr);

		User usr = new User();
		usr.setKey(key);
		usr.setUserId("mmichalak");
		usr.setUserName("Michael Michalak");
		// Separate test for avatar image below
		save(usr);

		// Get the entity to prove exists in DB
		foundUsr = find(key);
		assertNotNull("UserLocation for mike was not found.", foundUsr);

		assertEquals("mmichalak", usr.getUserId());
		assertEquals("Michael Michalak", usr.getUserName());
		assertTrue(456L == usr.getKey().getId());

	}

	/**
	 * Avatar not currently implemented.
	 *
	 */
	@Test
	public void saveAvatarTest() {
	}

	/**
	 * Tests querying the datastore.
	 *
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void queryTest() {
		Key key = KeyFactory.createKey("User", 1L);

		// prove entity doesn't yet exist in DB
		User foundUsr = find(key);
		assertNull(foundUsr);

		User usr = new User();
		usr.setKey(key);
		usr.setUserId("mmichalak");
		usr.setUserName("Michael Michalak");
		// Separate test for avatar image below
		save(usr);

		usr = new User();
		key = KeyFactory.createKey("User", 2L);
		usr.setKey(key);
		usr.setUserId("fwillie");
		usr.setUserName("Free Willie");
		// Separate test for avatar image below
		save(usr);

		// Get the entity to prove exists in DB
		key = KeyFactory.createKey("User", 2L);
		foundUsr = find(key);
		assertNotNull("UserLocation for Michael Michalak was not found.",
				foundUsr);

		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();
		foundUsr = null;
		boolean notFound = false;
		try {
			foundUsr = pm.getObjectById(User.class, key);

		} catch (JDOObjectNotFoundException e) {
			notFound = true;
		} finally {
			pm.close();
		}

		// Test via query
		pm = PMF.getFactory().getPersistenceManager();
		List<User> results = null;
		List<User> results2 = null;
		try {
			javax.jdo.Query query = pm.newQuery(User.class,
					"userId == userIdParam");
			query.declareParameters("String userIdParam");

			results = (List<User>) query.execute("mmichalak");
			results2 = (List<User>) query.execute("fwillie");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(results);
		assertTrue(results.size() == 1);
		assertTrue(results.get(0).getUserId()
				.equalsIgnoreCase("mmichalak"));

		assertNotNull(results2);
		assertTrue(results2.size() == 1);
		assertTrue(results2.get(0).getUserId()
				.equalsIgnoreCase("fwillie"));

	}

	/**
	 * This is not using detachable option to enable updating once PM is
	 * closed.
	 * 
	 */
	@Test
	public void updateTest() {
		Key key = KeyFactory.createKey("User", 456L);

		// prove entity doesn't yet exist in DB
		User foundUsr = find(key);
		assertNull(foundUsr);

		User usr = new User();
		usr.setKey(key);
		usr.setUserId("mmichalak");
		usr.setUserName("Michael Michalak");
		// Separate test for avatar image below
		save(usr);

		// Get the entity to prove exists in DB
		foundUsr = find(key);
		assertNotNull("UserLocation for mike was not found.", foundUsr);

		assertEquals("mmichalak", usr.getUserId());
		assertEquals("Michael Michalak", usr.getUserName());
		assertTrue(456L == usr.getKey().getId());

		usr.setUserName("Michael Gunzenhausen Michalak");
		// Separate test for avatar image below
		save(usr);

		assertEquals("mmichalak", usr.getUserId());
		assertEquals("Michael Gunzenhausen Michalak", usr.getUserName());
	}

	/**
	 * detachable update test
	 * 
	 */
	@Test
	public void detachableUpdateTest() {
	}

	/**
	 * Easy way to find a user.
	 *
	 */
	private User find(Key usr) {
		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();
		User foundUsr = null;
		boolean notFound = false;
		try {
			foundUsr = pm.getObjectById(User.class, usr);

		} catch (JDOObjectNotFoundException e) {
			notFound = true;
		} finally {
			pm.close();
		}
		return foundUsr;
	}
	
	/**
	 * Easy way to make a user persistent.
	 *
	 */
	private void save(User usr) {
		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();
		try {
			pm.makePersistent(usr);
		} finally {
			pm.close();
		}
	}
}