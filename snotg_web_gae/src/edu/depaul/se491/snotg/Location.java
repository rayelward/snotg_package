package edu.depaul.se491.snotg;

import java.sql.Timestamp;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/*
 * Class for ORM in GAE of users.
 */
@PersistenceCapable
public class Location {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private double latitude;

	@Persistent
	private double longitude;

	@Persistent
	private Timestamp lastUpdated;

	public Location() {
		super();
	}

	public Location(Double lat, Double longit) {
		this.latitude = lat;
		this.longitude = longit;
	}

	public Key getKey() {
		return key;
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

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}