package edu.depaul.se491.snotg;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
 * Servlet for checking status of phone.
 */
@SuppressWarnings("serial")
public class CheckStatus extends HttpServlet {
	@SuppressWarnings("unused")
	private static final String groupKey = "57d373be095b26ccc6b6cb1d910d631d";

	//handles request to servlet
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException {
		String user = request.getHeader("user");
		String time = request.getHeader("time");
		double x = Double.parseDouble(request.getHeader("x"));
		double y = Double.parseDouble(request.getHeader("y"));

		// if no phone is in the db and this is the first poll
		if (isFirstTime(user)) {
			// create a new record of the phone
			User phone = new User(user);
			PersistenceManager pm = PMF.getPM();
			try {
				pm.makePersistent(phone);
			} finally {
				pm.close();
			}
		}// end if

		// get phone info from backing store
		String result = buildGeneralResponse(user);
		// store the new time and coordinates
		updateData(user, time, x, y);

		// forward it away
		response.setContentType("text/plain");
		response.getWriter().println(result);
	}// doPost

	//forwards get response as a post response.
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException {
		doPost(request, response);
	}

	// method to check if the phone already exists in the db of if the db is
	// empty
	@SuppressWarnings("unchecked")
	public boolean isFirstTime(String _user) {
		PersistenceManager pm = PMF.getPM();
		String query = "select from " + User.class.getName()
				+ " where user == '" + _user + "'";
		List<User> phones = (List<User>) pm.newQuery(query).execute();
		if (phones.isEmpty())
			return true;
		else
			return false;
	}

	//builds general response.
	//
	@SuppressWarnings("unchecked")
	private String buildGeneralResponse(String _user) {
		PersistenceManager pm = PMF.getPM();
		String query = "select from " + User.class.getName()
				+ " where user == '" + _user + "'";
		List<User> phones = (List<User>) pm.newQuery(query).execute();
		StringBuilder sb = new StringBuilder();
		for (@SuppressWarnings("unused") User phone : phones) {
			sb.append("<Result>\n");
			sb.append("\t<Destination>" + _user
					+ "</Destination>\n");
			sb.append("</Result>\n");

		}
		pm.close();

		String plainText = sb.toString();

		// return encryptText( plainText );
		return plainText;
	}

	// delete the old record and store the new phone data to the db
	@SuppressWarnings("unchecked")
	private void updateData(String _user, String _time, double _x, double _y) {
		// update the new time and location send from the headers
		PersistenceManager pm = PMF.getPM();
		User phone = null;
		String query = "select from " + User.class.getName()
				+ " where user == '" + _user + "'";
		List<User> phones = (List<User>) pm.newQuery(query).execute();
		for (User p : phones) {
			phone = new User(p.getUserId());

		}

		PersistenceManager pmm = PMF.getPM();
		Query query2 = pmm.newQuery(User.class);
		query2.deletePersistentAll();

		try {
			pm.makePersistent(phone);
		} finally {
			pm.close();
		}
	}

}// CheckStatus