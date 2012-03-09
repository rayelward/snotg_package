package edu.depaul.se491.snotg.manager.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.depaul.se491.snotg.UserProfile;
import edu.depaul.se491.snotg.manager.UserManager;

/**
 * Test class for the user manager implementation.
 *
 */
public class UserManagerImplTest {

	UserManager userMgr = new UserManagerImpl();

	/**
	 * Tests finding a user profile with the manager.
	 *
	 */
	@Test
	public void findUserProfileTest() {
		// NOTE:  this test will fail if running against localhost GAE.  Need to first seed db for this.
		UserProfile userProf = userMgr.findUserProfile("se491snotg");
		assertNotNull(userProf);
		assertNotNull(userProf.getDescription());
		assertNotNull(userProf.getShout());
	}
}
 