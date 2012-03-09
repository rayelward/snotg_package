package edu.depaul.se491.snotg;

import java.math.BigDecimal;
import java.util.Date;
/**
 * Json test object.  Testing what could possible be passed in a heartbeat.
 *
 */
class JsonTestObject {
	String userName;
	BigDecimal latit;
	BigDecimal longit;
	Date dt;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getLatit() {
		return latit;
	}

	public void setLatit(BigDecimal latit) {
		this.latit = latit;
	}

	public BigDecimal getLongit() {
		return longit;
	}

	public void setLongit(BigDecimal longit) {
		this.longit = longit;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

}
