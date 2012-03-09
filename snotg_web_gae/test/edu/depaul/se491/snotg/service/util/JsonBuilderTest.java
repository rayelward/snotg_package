package edu.depaul.se491.snotg.service.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.depaul.se491.snotg.User;
import edu.depaul.se491.snotg.UserProfile;
import static org.junit.Assert.*;

/**
 * Test class for building json objects.
 * 
 */
public class JsonBuilderTest {

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
	 * Tests getting a json object of the user profile.
	 *
	 */
	@Test
	public void getUserProfileJsonTest() {
		UserProfile userProf = new UserProfile();
		userProf.setKey(KeyFactory.createKey("UserProfile", 456L));
		userProf.setProfileName("Test Profile 1");
		userProf.setShout("Yo");
		User usr = new User();
		usr.setUserId("mmichalak");
		usr.setUserName("Michael Michalak");
		userProf.setUser(usr);

		String retJson = JsonBuilder.buildProfileReturnJson(userProf,
				null);
		System.out.println(retJson);
		assertNotNull(retJson);
		assertEquals("{\"user_name\":\"Michael Michalak\",\"profile_name\":\"Test Profile 1\",\"userid\":\"mmichalak\",\"shout\":\"Yo\",\"key\":456}",
				retJson);
	}

}
