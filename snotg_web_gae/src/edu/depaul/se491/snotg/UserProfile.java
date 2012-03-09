package edu.depaul.se491.snotg;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

/*
 * Class for ORM in GAE of users.
 */
@PersistenceCapable(detachable="true")
public class UserProfile {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	// FK on User key
	@Persistent
	private User user;
	
	@Persistent
	private String userId;

	@Persistent
	private Blob avatar;

	@Persistent
	private String profileName;

	@Persistent
	private String description;

	@Persistent
	private String shout;

	
	public UserProfile() {
		super();
	}

	public UserProfile(String profName) {
		this.profileName = profName;
	}

	public UserProfile(Key key, String profName) {
		this.key = key;
		this.profileName = profName;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Blob getAvatar() {
		return avatar;
	}

	public void setAvatar(Blob avatar) {
		this.avatar = avatar;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShout() {
		return shout;
	}

	public void setShout(String status) {
		this.shout = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userName) {
		this.userId = userName;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *                the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserProfile [key=" + key + ", user=" + user + ", userId="
				+ userId + ", avatar=" + avatar + ", profileName="
				+ profileName + ", description=" + description + ", shout="
				+ shout + "]";
	}

}