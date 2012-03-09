package edu.depaul.snotg_android.Chat;

import java.util.ArrayList;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.depaul.snotg_android.R;
import edu.depaul.snotg_android.SnotgAndroidConstants;


/**
 * Chat client implementing the JabberSmackAPI
 * 
 * @author mmichalak
 *
 */
public class XMPPClient extends ListActivity {
	private static final String TAG = "XMPPClient";
	
	protected AccountManager accountManager;
	private JabberSmackAPI chatClient;

	// Screen elements
	private static TextView chattitle;
	private AlertDialog.Builder loginPopup;
	private AlertDialog.Builder failedPopup;
	private EditText sendText;

	private Handler mHandler = new Handler(); //Message List handler
	ArrayAdapter<String> listAdapter;
	
	// Chat state and tracking variables
	private static String chatWithUserAcctId;
	private static ArrayList<String> messages = new ArrayList<String>();
	private boolean chatLoggedIn = false;
	private int loginAttemptCount = 0; // Used to only try login 3 times
	
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		try 
			{
			Log.i(TAG, "onCreate called");
			setContentView(R.layout.chatscreen);
	
			sendText = (EditText) this.findViewById(R.id.sendText);
			chattitle = (TextView)findViewById(R.id.chattitle);
			
			chattitle.setText(SnotgAndroidConstants.STATE_CHAT_OTHER_USERID);  
			chatClient = new JabberSmackAPI(this);
	
			// Set a listener to send a chat text message
			listAdapter = new ArrayAdapter<String>(this, R.layout.multi_line_list_item, messages);
			setListAdapter(listAdapter);
			
			Button send = (Button) this.findViewById(R.id.send);
			send.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						chatWithUserAcctId = SnotgAndroidConstants.STATE_CHAT_OTHER_USERID +"@gmail.com";
						try {
							String text = sendText.getText().toString();
			
							Log.i(TAG, "Sending text [" + text + "] to [" + chatWithUserAcctId
									+ "]");
							try {
								if (text != null) {
									chatClient.sendMessage(text, chatWithUserAcctId);
									Log.i(TAG, "Sent text [" + text + "] to ["
											+ chatWithUserAcctId + "]");
	
									messages.add("me:  " + text);
									listAdapter.notifyDataSetChanged();
									sendText.setText("");							
								} else
									Log.i(TAG, "Message is emtpy");
								
							} catch (IllegalStateException e) {
								// Login probably timed out
								Log.e(TAG, "IllegalStateException.  Login timed-out");
								chatLoggedIn = false;
								loginAttemptCount = 0;
								onResume();
							}
							catch (XMPPException e) {
								Log.e(TAG, "Failed Sending text [" + text
										+ "] to [" + chatWithUserAcctId + "]:" + e.getMessage());
							}
						} catch (Exception e) {
							Log.e(TAG, "Failed to send message to user: " + e.getMessage());
						}
					}
				}
			);

		} 
		catch (Exception e) {
			Log.e(TAG, "Chat startup failed: " + e.getMessage());
		}
	}

	
	/**
	 * Called by the JabberSmackAPI PacketListener when new messages arrive.
	 * This processes the messsage and displays it on message pane.
	 * @param incomingMSG
	 * @param from
	 */
	public void getMessage(String incomingMSG, String from) {
		Log.i(TAG, "Got text [" + incomingMSG + "] from [" + from
				+ "]");
		if (incomingMSG != null) {
			String fromName = StringUtils.parseBareAddress(from);
			// This is necessary when a user extends that chat to me.  App doesn't know who they are at first to reply
			chatWithUserAcctId = fromName;
			SnotgAndroidConstants.STATE_CHAT_OTHER_USERID = fromName.replaceFirst("@gmail.com", "");

			messages.add(SnotgAndroidConstants.STATE_CHAT_OTHER_USERID + ":  "+incomingMSG);
			
			mHandler.post(
				new Runnable() {
					public void run() {
						listAdapter.notifyDataSetChanged();
						// IMPORTANT! Keep chattitle.setText() here in thread.  CAN cause method to stop
						// executing and the loss of incoming messages
						chattitle.setText(SnotgAndroidConstants.STATE_CHAT_OTHER_USERID);
					}
				}
			);
		}
	}

	/**
	 * Logs the app user into the chat service with the provided password
	 * @return true = login was successful
	 */
	private boolean loginChat() {
		try {
			chatClient.login(SnotgAndroidConstants.STATE_USERID, SnotgAndroidConstants.CHAT_PASSWORD);
			return true;
		} catch (XMPPException e) {
			Log.i(TAG,
					"Failed to login as " + SnotgAndroidConstants.STATE_USERID + ": " + e.getMessage());
			SnotgAndroidConstants.CHAT_PASSWORD = null;
			return false;
		}
	}
	
	/**
	 * Loads and shows the pop-up window for user password
	 */
	private void loadPopUpPasswordWindow() {
		loginPopup = new AlertDialog.Builder(this);
		loginPopup.setTitle("Enter Password");
		if (loginAttemptCount == 1)
			loginPopup.setMessage("Password required to log into Google chat"); // First attempt
		else
			loginPopup.setMessage("Login failed for " + SnotgAndroidConstants.STATE_USERID +
					".  Enter password required to log into Google chat");

		final EditText pwdField = new EditText(this);
		pwdField.setTransformationMethod(PasswordTransformationMethod.getInstance());
		loginPopup.setView(pwdField);
		
		loginPopup.setPositiveButton("Login", 
			new DialogInterface.OnClickListener() {	
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = pwdField.getText().toString();
					//TODO verify not null or empty
					SnotgAndroidConstants.CHAT_PASSWORD = value;
					chatLoggedIn = loginChat();
					
					onResume();
				}
			}
		);
		
		loginPopup.setNegativeButton("Cancel", 
			new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    dialog.cancel();
				  }
			}
		);

		loginPopup.show();
	}
	
	/**
	 * Resets the chat state when a new user is selected to chat with
	 * typically coming from the map
	 */
	public static void ChangeUser()
	{
		 chatWithUserAcctId = SnotgAndroidConstants.STATE_CHAT_OTHER_USERID +"@gmail.com";
		 chattitle.setText(SnotgAndroidConstants.STATE_CHAT_OTHER_USERID.replaceFirst("@gmail.com", ""));
		 messages.clear();
	}
	
	/**
	 * This manages login pop-up logic, managing when the user is logged-in
	 * and when to show login pop-up
	 */
	protected void onResume() {
		super.onResume();

		// 3 login attempts
		if (++loginAttemptCount <=3) {
			// Log in to chat if already not logged in
			if(!chatLoggedIn && SnotgAndroidConstants.CHAT_PASSWORD != null)
				chatLoggedIn = loginChat();
			// If login failed, prompt for pwd and try again
			if(!chatLoggedIn)
				loadPopUpPasswordWindow();
		}
		else {
			//Pop alert not logged in.  
			loadPopUpLoginFailed();	
		}
	}

	/**
	 * Loads and shows the Failed Login pop-up when user chat login fails
	 */
	private void loadPopUpLoginFailed() {
		failedPopup = new AlertDialog.Builder(this);
		failedPopup.setTitle("Login Error");
		failedPopup.setMessage("Unable to login to Google Chat.  As a result, the chat functionality will be unavailable."); 
		
		failedPopup.setPositiveButton("Ok", 
			new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    dialog.cancel();
				  }
			}
		);

		failedPopup.show();
	}

	/**
	 * Resets the login attempt counts once user leaves the chat screen.
	 * This is really when the chat login fails and the user goes to the
	 * Settings tab to set the password.  That way, the user gets their 
	 * 3 login attampts again.
	 */
	protected void onPause() {
		super.onPause();
		loginAttemptCount = 0; // Reset login counter
	}


}