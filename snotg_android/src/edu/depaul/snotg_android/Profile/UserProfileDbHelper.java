package edu.depaul.snotg_android.Profile;

import android.database.sqlite.SQLiteDatabase;

public interface UserProfileDbHelper {

	public abstract UserProfile[] getUserProfiles();
	
	public UserProfile findUserProfile(String userId);
	public UserProfile saveUserProfile(UserProfile p);

	public void onCreate(SQLiteDatabase db);
	public void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion);

	public void closeItUp();

}