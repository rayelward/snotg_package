package edu.depaul.se491.snotg.manager.impl;


/**
 * class for handling user profile json.
 */
public class UserProfileJson {
	String userId;
	String profileDescr;
	String profileName;
	String shout;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the profileDescr
	 */
	public String getProfileDescr() {
		return profileDescr;
	}

	/**
	 * @param profileDescr the profileDescr to set
	 */
	public void setProfileDescr(String profileDescr) {
		this.profileDescr = profileDescr;
	}

	/**
	 * @return the profileName
	 */
	public String getProfileName() {
		return profileName;
	}

	/**
	 * @param profileName the profileName to set
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	/**
	 * @return the shout
	 */
	public String getShout() {
		return shout;
	}

	/**
	 * @param shout the shout to set
	 */
	public void setShout(String shout) {
		this.shout = shout;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserProfileJson [userId=" + userId + ", profileDescr="
				+ profileDescr + ", profileName=" + profileName + ", shout="
				+ shout + "]";
	}


	
}
