package edu.depaul.snotg_android.Map;

public class UserLocationObj {
	
	private String username;
	private Double lon;
	private Double lat;
	private String lastUpdated;
	private String shout;
	
	
	public void setUsername(String inUsername){
		username = inUsername;
	}
	
	public void setLon(Double inLon){
		lon = inLon;
	}
	
	public void setLat(Double inLat){
		lat = inLat;
	}
	
	public void setLastUpdated(String inLastUpdated){
		lastUpdated = inLastUpdated;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public Double getLon(){
		return this.lon;
	}
	
	public Double getLat(){
		return this.lat;
	}
	
	public String getLastUpdated(){
		return this.lastUpdated;
	}

	public String getShout() {
		return shout;
	}

	public void setShout(String shout) {
		this.shout = shout;
	}
}
