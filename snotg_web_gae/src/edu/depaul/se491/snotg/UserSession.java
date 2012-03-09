package edu.depaul.se491.snotg;

/**
 * This class represents the user's session when using the app. It contains the
 * usr, their selected profile for the session and their current location.
 * 
 * @author mmichalak
 * 
 */
public class UserSession {
	User user;
	UserProfile userProfile;
	Location userLoc;

	public UserSession() {
		super();
	}

	public UserSession(User usr, UserProfile usrProfile) {
		this.user = usr;
		this.userProfile = usrProfile;
	}

	public UserSession(User usr, UserProfile usrProfile, Location usrLoc) {
		this.user = usr;
		this.userProfile = usrProfile;
		this.userLoc = usrLoc;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User usr) {
		this.user = usr;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile usrProfile) {
		this.userProfile = usrProfile;
	}

	public Location getUserLoc() {
		return userLoc;
	}

	public void setUserLoc(Location usrLoc) {
		this.userLoc = usrLoc;
	}

}