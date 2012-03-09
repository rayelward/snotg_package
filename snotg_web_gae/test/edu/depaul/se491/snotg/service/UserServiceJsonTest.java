package edu.depaul.se491.snotg.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.depaul.se491.snotg.User;
import edu.depaul.se491.snotg.UserProfile;
import edu.depaul.se491.snotg.manager.impl.UserProfileJson;

/**
 * Test class for json services.
 * 
 */
public class UserServiceJsonTest {

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
	 * Testing responses from the data store in json data structure.  
	 * 
	 */
	@Test
	public void getUserProfileJsonResponseTest() {
		UserProfile userProf = new UserProfile();
		userProf.setKey(KeyFactory.createKey("UserProfile", 456L));
		userProf.setProfileName("Test Profile 1");
		userProf.setShout("Yo");
		User usr = new User();
		usr.setUserId("mmichalak");
		usr.setUserName("Michael Michalak");
		userProf.setUser(usr);

		fail();
	}

	/**
	 * Testing the user profile get request.
	 *
	 */
	@Test
	public void getUserProfileJsonRequestTest() {
		String reqJson = "{\"userId\":\"se491snotg\"}";
		ObjectMapper mapper = new ObjectMapper();
		UserProfileJson userJson = null;
		try {
			userJson = mapper.readValue(reqJson.getBytes(),
					UserProfileJson.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals("se491snotg", userJson.getUserId());
	}
}
