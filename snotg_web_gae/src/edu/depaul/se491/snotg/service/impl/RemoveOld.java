package edu.depaul.se491.snotg.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.depaul.se491.snotg.PMF;
import edu.depaul.se491.snotg.UserLocation;
/**
 * cron job servlet. Is called every 5 minutes to make sure no stale 
 *  date is in the database.
 */
@SuppressWarnings("serial")
public class RemoveOld extends HttpServlet {

	/*
	 * handles cron request
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// get the date and time of 5 minutes ago.
		long t = new Date().getTime();
		long m = 5 * 60 * 1000;
		Date fiveMinAgo = new Date(t - m);

		// grab everything from the database with lastupdate < 5 minutes
		// ago
		PersistenceManager pm = PMF.getFactory()
				.getPersistenceManager();
		Query query = pm.newQuery(UserLocation.class);
		query.setFilter("lastUpdated < :lastUpdatedParam");
		try {
			@SuppressWarnings("rawtypes")
			Collection results = (Collection) query
					.execute(fiveMinAgo);
			if (!results.isEmpty()) {
				// delete all the results.
				for (Object o : results) {
					pm.deletePersistent(o);
				}
			} else {
				// ... no results do nothing.
			}
		} finally {
			query.closeAll();
		}
	}
}
