package edu.depaul.snotg_android.Auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.depaul.snotg_android.R;
import edu.depaul.snotg_android.SnotgAndroidConstants;
import static edu.depaul.snotg_android.SnotgAndroidConstants.*;
import edu.depaul.snotg_android.Layout.TabBarWidget;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * This activity manages the user login logic for the device.  It authenticates
 * against a selected device account.
 * This was a boilerplate example found on the net
 * 
 * The login method flow is as follows:
 * 1.  OnCreate()
 * 2.  GetAuthTokenCallback.run() - Thread executing auth or callback if logged authenticate
 * 3.  onGetAuthToken() - Creates and runs new GetCookieTask.doInBackground().  This is 
 * 		an ansynch task.
 * 4.  GetCookieTask.doInBackground().  Looks for auth token and if present redirects user 
 * 		to requested resource/url.  If not, it redirects through login flow
 * 
 * @author mmichalak
 *
 */
public class AppInfo extends Activity {
	static String TAG = "AppInfo";
	
	DefaultHttpClient http_client = new DefaultHttpClient();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_info);
		
	    // Load the backend host connection info
	    String host = this.getString(R.string.uri_backend_hostname);
	    int iPort = 0;
	    try {
			iPort = Integer.parseInt(this.getString(R.string.uri_backend_port));
		} catch (NumberFormatException e) {	; }
	    if (host == null || "".equalsIgnoreCase(host))
	    	Log.i(AppInfo.TAG, "Unable to load host and/or port from strings.xml");
	    SnotgAndroidConstants.URI_BACKEND_HOSTNAME  = host;
	    SnotgAndroidConstants.URI_BACKEND_PORT = iPort;

	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		AccountManager accountManager = AccountManager
				.get(getApplicationContext());
		Account account = (Account) intent.getExtras().get("account");
		accountManager.getAuthToken(account, "ah", false,
				new GetAuthTokenCallback(), null);

		Intent m = new Intent(this, TabBarWidget.class);
		startActivity(m);
	}

	private class GetAuthTokenCallback implements
			AccountManagerCallback<Bundle> {
		public void run(AccountManagerFuture<Bundle> result) {
			Bundle bundle;
			try {
				bundle = result.getResult();
				Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
				if (intent != null) {
					// User input required
					startActivity(intent);
				} else {
					onGetAuthToken(bundle);
				}
			} catch (OperationCanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	protected void onGetAuthToken(Bundle bundle) {
		String auth_token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
		new GetCookieTask().execute(auth_token);
	}

	private class GetCookieTask extends AsyncTask<String, Void, Boolean> {
		protected Boolean doInBackground(String... tokens) {
			try {
				// Don't follow redirects
				http_client.getParams().setBooleanParameter(
						ClientPNames.HANDLE_REDIRECTS, false);
				HttpGet http_get = new HttpGet(URL_AUTH_CONNECTION() + tokens[0]);
				HttpResponse response;
				response = http_client.execute(http_get);
				if (response.getStatusLine().getStatusCode() != 302)
					// Response should be a redirect
					return false;

				for (Cookie cookie : http_client.getCookieStore().getCookies()) {
					if (cookie.getName().equals("ACSID"))
						return true;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				http_client.getParams().setBooleanParameter(
						ClientPNames.HANDLE_REDIRECTS, true);
			}
			return false;
		}

		protected void onPostExecute(Boolean result) {
			// new
			// AuthenticatedRequestTask().execute("http://yourapp.appspot.com/admin/");
		}
	}

	private class AuthenticatedRequestTask extends
			AsyncTask<String, Void, HttpResponse> {
		@Override
		protected HttpResponse doInBackground(String... urls) {
			try {
				HttpGet http_get = new HttpGet(urls[0]);
				return http_client.execute(http_get);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(HttpResponse result) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(result.getEntity().getContent()));
				String first_line = reader.readLine();
				Toast.makeText(getApplicationContext(), first_line,
						Toast.LENGTH_LONG).show();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
