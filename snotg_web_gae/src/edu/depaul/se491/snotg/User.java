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
@PersistenceCapable
public class User {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	/**
	 * Natural key. E.g. gsmith, dandrews
	 */
	@Persistent
	private String userId;

	/**
	 * E.g. Greg Smith, David Andrews
	 */
	@Persistent
	private String userName;

	/**
	 * Default user picture. Can be overridden with profile's avatar image
	 */
	@Persistent
	private Blob picture;

	public User() {
		super();
	}

	public User(Key key, String usrName) {
		this.key = key;
		this.userId = usrName;
	}

	public User(String usrName) {
		this.userId = usrName;
	}

	public User(Key key, String usrName, Blob usrPic) {
		this.key = key;
		this.userId = usrName;
		this.picture = usrPic;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public void setUserId(String user) {
		this.userId = user;
	}

	public String getUserId() {
		return userId;
	}

	public byte[] getImage() {
		if (picture == null)
			return null;
		return picture.getBytes();
	}

	public void setImage(byte[] _bytes) {
		this.picture = new Blob(_bytes);
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *                the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}