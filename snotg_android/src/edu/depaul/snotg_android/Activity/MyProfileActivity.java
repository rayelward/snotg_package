package edu.depaul.snotg_android.Activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import edu.depaul.snotg_android.R;
import edu.depaul.snotg_android.SnotgAndroidConstants;
import edu.depaul.snotg_android.Profile.UserProfile;
import edu.depaul.snotg_android.Profile.UserProfileDbHelper;
import edu.depaul.snotg_android.Profile.UserProfileDbHelperRemote;

public class MyProfileActivity  extends Activity {

	private static final String TAG = MyProfileActivity.class.getSimpleName();
	private static final int TAKE_PIC = 7453912;
	
	private UserProfileDbHelper dbHelper;
	private UserProfile currentProfile;
	
	private EditText description;
	private EditText shout;
	private TextView name;
	private ImageView avatarImage;
	private Uri picFileUri;
	
	private Button editButton;
	private Button submitButton;
	private Button cancelButton;
	//private Button otherButton;



	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
        setContentView( R.layout.my_profile );  
		dbHelper = new UserProfileDbHelperRemote( getBaseContext() );
        setFieldLabels();        
        loadProfile();        
        setListeners();

	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.profile_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.save:
	        	Log.i( TAG, "clicked submit button" );
				setEditMode( false );	
				boolean profileUpdateSuccessful = saveProfile();
				Log.i( TAG, "profile update successful? " + profileUpdateSuccessful);
	            return true;
	        case R.id.edit:
	        	setEditMode( true );
	            return true;
	        case R.id.cancel:
	        	Log.i( TAG, "clicked cancel button" );
	        	setEditMode( false );
				revertAnyChanges();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbHelper.closeItUp();
	} 
	
	private void setFieldLabels() {
		description = (EditText) findViewById( R.id.profile_descText );
		shout = (EditText) findViewById( R.id.profile_shoutText );
		name = (TextView) findViewById( R.id.profile_nameText );
		avatarImage = (ImageView) findViewById( R.id.profile_avatarImage );
		editButton = (Button) findViewById( R.id.profile_editButton );
		cancelButton = (Button) findViewById( R.id.profile_cancelButton );
		submitButton = (Button) findViewById( R.id.profile_submitButton );
		//otherButton = (Button) findViewById( R.id.profile_otherProfileButton );
	}
	
	private void setListeners() {
        //avatarImage.setOnClickListener(new AvatarImageOnClickListener() );      			
		editButton.setOnClickListener( new EditButtonOnClickListener() );			
		cancelButton.setOnClickListener( new CancelButtonOnClickListener() );			
		submitButton.setOnClickListener( new SubmitButtonOnClickListener() );
		//otherButton.setOnClickListener( new OtherButtonOnClickListener() );
	}
	
	private void loadProfile() {
		Log.i( TAG, "Loading user profile" );
		currentProfile = dbHelper.findUserProfile( SnotgAndroidConstants.STATE_USERID );
		setHints();
		description.setText(currentProfile.getDescription());
		name.setText(currentProfile.getProfileName());
		shout.setText(currentProfile.getShout());
	}

	private void setHints() {
		name.setHint( R.string.profile_nameHint );
		description.setHint( R.string.profile_descriptionHint );
		shout.setHint( R.string.profile_shoutHint );
	}
	
	private void revertAnyChanges() {
		description.setText(currentProfile.getDescription());
		name.setText(currentProfile.getProfileName());
		shout.setText(currentProfile.getShout());
	}
	
	private void setEditMode( boolean editable ) {
		avatarImage.setEnabled( editable );
		name.setEnabled( editable );
		shout.setEnabled( editable );
		description.setEnabled( editable );	
		editButton.setEnabled( !editable );
		cancelButton.setEnabled( editable );
		submitButton.setEnabled( editable );
	}
		
	private boolean saveProfile() {
		currentProfile.setDescription( description.getText().toString() );
		currentProfile.setProfileName( name.getText().toString() );
		currentProfile.setShout( shout.getText().toString() );
		//currentProfile.setAvatar( getAvatarImageAsByteArray() );
		Log.i(TAG, currentProfile.toString());
		dbHelper.saveUserProfile( currentProfile );

		return true;
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "requestCode: " + requestCode + " --- resultCode: " + resultCode);
		Log.i(TAG, "picFileUri: " + picFileUri );
		if( data != null ) {
			Log.i(TAG, " --- Intent: " + data.getDataString() );
		} else {
			Log.i(TAG, "Intent is null");
		}
		if( requestCode==TAKE_PIC ) {
			if( resultCode==RESULT_OK ) {
				//Log.i(TAG, "data.toURI(): " + data.toURI());
				//Log.i(TAG, "data.getData(): " + data.getData());
				avatarImage.setImageURI( picFileUri );
			} else if( resultCode==RESULT_CANCELED ) {
				Log.i( TAG, "user cancelled request to capture image" );
			}
		}
	}

	private static Uri getOutputImageFileUri() {
	      return Uri.fromFile( getOutputImageFile() );
	}
	
	private static File getOutputImageFile() {
		String mediaState = Environment.getExternalStorageState();
		if( mediaState.equals(Environment.MEDIA_MOUNTED) ) {
			Log.i(TAG, "media mounted");
		} else if( mediaState.equals(Environment.MEDIA_SHARED) ) {
			Log.i(TAG, "media shared");
		} else {
			Log.i(TAG, "media state: " + mediaState );
		}
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES ), "snotg");
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs() ){
	            Log.d( TAG, "failed to create directory: " + mediaStorageDir);
	            return null;
	        }
	    }
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format( new Date() );
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
	    return mediaFile;
	}
	
	public byte[] getAvatarImageAsByteArray() {
		Bitmap bitmap = ((BitmapDrawable) avatarImage.getDrawable()).getBitmap();
		return getByteArray( bitmap );	
	}
	
	public byte[] getByteArray(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 0, bos);
		return bos.toByteArray();
	}
	
	public Bitmap getBitmap(byte[] bitmap) {
	    return BitmapFactory.decodeByteArray(bitmap , 0, bitmap.length);
	}

	private class AvatarImageOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.i(TAG, "clicked avatar image");
			if( avatarImage.isEnabled() ) {
				picFileUri = getOutputImageFileUri();
				Intent captureImage = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
				captureImage.putExtra( MediaStore.EXTRA_OUTPUT, picFileUri );
				startActivityForResult( captureImage, TAKE_PIC );
			}
		} 
	}
	
	private class EditButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.i( TAG, "clicked edit button" );
			setEditMode( true );				
		}
	}
	
	private class CancelButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.i( TAG, "clicked cancel button" );
			setEditMode( false );
			revertAnyChanges();
		}
	}
	
	
	private class SubmitButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.i( TAG, "clicked submit button" );
			setEditMode( false );	
			boolean profileUpdateSuccessful = saveProfile();
			Log.i( TAG, "profile update successful? " + profileUpdateSuccessful);			
		}
	}
	
	/**
	 * @deprecated
	 * THIS IS CURRENTLY NOT USED.  It was initially used to load and view
	 * another user's profile.  It was accessed from the User Profile page.
	 * It is currently not in scope.
	 *
	 */
	private class OtherButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.i( TAG, "clicked other button - loadProfile() invoked" );
			/*Intent other = new Intent( getBaseContext(), OtherProfileActivity.class );
			UserProfile up = new UserProfile();
			up.setShout( "Help me with my SE450 homework!" );
			up.setDescription( "I'm a grad student at DePaul in the school of Computing and Digital Media" );
			Bundle b = new Bundle();
			b.putSerializable( "profile", up );
			other.putExtras( b );
			startActivity( other );	*/		
	        loadProfile();   
		}
	}

}
