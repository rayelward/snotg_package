package edu.depaul.se491.snotg.service.impl;

import static edu.depaul.se491.snotg.SnotgConstants.INVALID_REQUEST_PARAM_MSG;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import edu.depaul.se491.snotg.SnotgConstants;
import edu.depaul.se491.snotg.UserProfile;
import edu.depaul.se491.snotg.manager.UserManager;
import edu.depaul.se491.snotg.manager.impl.UserManagerImpl;
import edu.depaul.se491.snotg.manager.impl.UserProfileJson;
import edu.depaul.se491.snotg.service.util.JsonBuilder;
/**
 * Servlet for handling changes to the user information.
 */
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = -5990109549319782289L;

	UserManager userMgr = new UserManagerImpl();

	/**
	 * handles the get request.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String jsonResponse = null;

		if (req.getParameter("get_user_profile") != null)
			jsonResponse = getUserProfile(req);
		else if (req.getParameter("save_user_profile") != null)
			jsonResponse = saveUserProfile(req);
		else
			jsonResponse = INVALID_REQUEST_PARAM_MSG;

		resp.getWriter().println(jsonResponse);
	}

	/**
	 * saves a user profile.
	 */
	private String saveUserProfile(HttpServletRequest req) {

		String reqJson = req.getParameter("request_json");

		UserProfile userProf = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserProfileJson userJson = mapper.readValue(reqJson.getBytes(), UserProfileJson.class);			
			
			// Translate values from Json obj to backend object
			userProf = new UserProfile();
			userProf.setUserId(userJson.getUserId());
			userProf.setProfileName(userJson.getProfileName());
			userProf.setDescription(userJson.getProfileDescr());
			userProf.setShout(userJson.getShout());

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		userMgr.saveUserProfile(userProf);

		String jsonText = null;
		try {
			jsonText = JsonBuilder.buildProfileReturnJson(userProf, SnotgConstants.SUCCESS);
			//System.out.print(jsonText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonText;
	}

	/**
	 * gets a users profile.
	 */
	String getUserProfile(HttpServletRequest req) {

		String reqJson = req.getParameter("request_json");
		
		String userId = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserProfileJson userJson = mapper.readValue(reqJson.getBytes(), UserProfileJson.class);			
			//mapper.writeValue(System.out, user); // where 'dst' can be File, OutputStream or Writer
			// Translate values from Json obj to backend object
			userId = userJson.getUserId();

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		UserProfile userProf = userMgr.findUserProfile(userId);

		String jsonText = null;
		try {
			jsonText = JsonBuilder.buildProfileReturnJson(userProf, SnotgConstants.SUCCESS);
			//System.out.print(jsonText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonText;
	}
}
