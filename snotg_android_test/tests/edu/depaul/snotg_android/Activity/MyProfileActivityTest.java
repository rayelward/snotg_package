package edu.depaul.snotg_android.Activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import edu.depaul.snotg_android.R;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

/**
 *  JUST A STUB to validate the test project
 *
 */
public class MyProfileActivityTest extends
		ActivityInstrumentationTestCase2<edu.depaul.snotg_android.Activity.MyProfileActivity> {

	private static final String TAG = "MyProfileActivityTest";
    private MyProfileActivity activity; 
    
    EditText shoutText;
    
		    
	public MyProfileActivityTest() {
		super("edu.depaul.snotg_android.Activity", MyProfileActivity.class);
	}
	
    protected void setUp() throws Exception {
        super.setUp();
        activity = this.getActivity();	
        shoutText = (EditText) activity.findViewById( R.id.profile_shoutText );
        								// com.marakana.R.id.editKilos);
    }
    
    public void testGetQueryParamsForGet() {
    	ArrayList<ArrayList<String>>  paramsList = activity.getQueryParamsForGet();
    	assertEquals(paramsList.size(), 2);
    	assertEquals(paramsList.get(0).get(0), "get_user_profile");
    }
    
    public void testBuildJsonForGet() {
    	String json = activity.buildJsonForGet();
    	//{"userName":"mmich8266"}  
    	String [] params = json.split(":");
    	assertEquals(params.length, 2);
    	assertTrue(params[0].indexOf("userId") > 0);
    	assertTrue(params[1].length() > 3);
    }
    
    public void testReturnProfileJson() {
    	String jString = "{\"ts\":\"Sat Jan 28 00:28:33 UTC 2012\",\"profile_name\":\"SNotgs Test Profile\",\"profile_descr\":\"SNotGs First Profile\",\"userid\":\"se491snotg\",\"profile_key\":\"UserProfile(7778)\",\"msg\":\"Response from GAE User Service - \",\"shout\":\"The SNotG Master of DePaul\"}";
    	try {
    		
			JSONObject jObject = new JSONObject(jString); 
			assertEquals("SNotgs Test Profile", jObject.getString("profile_name") );
			assertEquals("UserProfile(7778)", jObject.getString("profile_key") );
			assertEquals("SNotGs First Profile", jObject.getString("profile_descr") );
			assertEquals("The SNotG Master of DePaul", jObject.getString("shout") );
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void testScreenText() {
    	assertNotNull(this.getActivity());
    	assertTrue("Shout field is empty", "".equals(shoutText.getText().toString()));
    }
    
}
