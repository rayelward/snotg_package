package edu.depaul.se491.snotg;

import java.util.Date;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Embedded;

import com.google.appengine.api.datastore.Key;

/*
 * Class for ORM in GAE of users.
 */
@PersistenceCapable(detachable = "true")
public class UserLocation {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String userName;

	@Persistent
	private Date lastUpdated;

	@Persistent
	@Embedded
	private Loc loc;

	public UserLocation() {
		super();
	}

	public UserLocation(Key key, String usrName) {
		this.key = key;
		this.userName = usrName;
	}

	public UserLocation(String usrName) {
		this.userName = usrName;
	}

	public UserLocation(Key key, String usrName, Loc loc) {
		this.key = key;
		this.userName = usrName;
		this.loc = loc;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public void setUserName(String user) {
		this.userName = user;
	}

	public String getUserName() {
		return userName;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date date) {
		this.lastUpdated = date;
	}

	public Loc getLoc() {
		return loc;
	}

	public void setLoc(Loc loc) {
		this.loc = loc;
	}
	/*
	 * inline Class for ORM in GAE of users.
	 */
	@PersistenceCapable(detachable = "true")
	@EmbeddedOnly
	public static class Loc {
		@Persistent
		private double latitude;

		@Persistent
		private double longitude;

		public Loc(double lat, double longit) {
			this.latitude = lat;
			this.longitude = longit;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double xCoordinate) {
			this.latitude = xCoordinate;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double yCoordinate) {
			this.longitude = yCoordinate;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserLocation [key=" + key + ", userName=" + userName
				+ ", lastUpdated=" + lastUpdated + ", loc=" + loc + "]";
	}

}