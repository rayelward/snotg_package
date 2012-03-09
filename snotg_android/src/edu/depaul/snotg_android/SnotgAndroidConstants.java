package edu.depaul.snotg_android;


public class SnotgAndroidConstants {

	public static final long HEARTBEAT_INTERVAL = 30 * 1000; // sec * 1000
	
	public static final String URI_PROTOCOL = "http";
	// Host and Port are initialized in TabBarWidget from res/string.xml
	public static String URI_BACKEND_HOSTNAME = null; 
	public static int URI_BACKEND_PORT = 9999;
	public static final String URL_PATH_HEARTBEAT = "/user_locations";
	public static final String URL_PATH_USER = "/user";
	
	public static String URL_AUTH_CONNECTION() {
		return "http://" + URI_BACKEND_HOSTNAME + ":" + URI_BACKEND_PORT +
					"/_ah/login?continue=http://" + URI_BACKEND_HOSTNAME + ":" + URI_BACKEND_PORT + "/&auth=";
	}
												
	
	public static final String EMPTY_JSON_STRING = "[]";

	// State Variables used be the app to hold user state
	public static String STATE_USERID = null;
	public static String STATE_CHAT_OTHER_USERID = null;
	
	//Chat info
	public static String CHAT_PASSWORD = null;
	public static String CHAT_USER_NAME_OTHER = "";
	
	// google server info "talk.google.com",5222, "gmail.com"
	public static String CHAT_HOST = "talk.google.com";
	public static int CHAT_PORT_NUMBER = 5222;
	public static String CHAT_SERVICE_NAME = "gmail.com";
	
}