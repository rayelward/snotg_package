package edu.depaul.se491.snotg.factory;

import static edu.depaul.se491.snotg.SnotgConstants.JDO;
import static edu.depaul.se491.snotg.SnotgConstants.PERSISTENCE;
import edu.depaul.se491.snotg.dao.UserLocationDao;
import edu.depaul.se491.snotg.dao.UserProfileDao;
import edu.depaul.se491.snotg.dao.jdo.UserLocationJdoDao;
import edu.depaul.se491.snotg.dao.jdo.UserProfileJdoDao;

/**
 * Creates Daos for JDO or JPA
 * NOTE:  this class is unit tested via each respect dao's unit
 * tests
 * 
 *
 */
public class DaoFactory {

	private DaoFactory() {
		;
	}

	public static UserLocationDao createUserLocationDao() {
		if (PERSISTENCE.equals(JDO))
			return new UserLocationJdoDao();
		return null;
	}

	public static UserProfileDao createUserProfileDao() {
		if (PERSISTENCE.equals(JDO))
			return new UserProfileJdoDao();
		return null;
	}
}
