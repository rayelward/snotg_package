package edu.depaul.snotg_android.Activity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import edu.depaul.snotg_android.Map.MapMe;
import edu.depaul.snotg_android.Map.ProfileOverlay;

/**
 *  
 *
 */
public class MapMeTest extends
		ActivityInstrumentationTestCase2<edu.depaul.snotg_android.Map.MapMe> {

	private static final String TAG = "MapMeTest";
    private MapMe activity; 
    
    EditText shoutText;
    
		    
	public MapMeTest() {
		super("edu.depaul.snotg_android.Map", MapMe.class);
	}
	
    protected void setUp() throws Exception {
        super.setUp();
        activity = this.getActivity();	
    }
    
 
    
/*    public void testScreenText() {
    	assertNotNull(this.getActivity());
    	assertTrue("Shout field is empty", "".equals(shoutText.getText().toString()));
    }*/
    
    public void testProfileOverlay() {
    	//ProfileOverlay profOverlay = this.getInstrumentation().getContext();
    	activity.displayNearbyUsers();
    	ProfileOverlay nearbyUsersOverlay = activity.getNearbyUsersOverlay();
    	assertNotNull(nearbyUsersOverlay);
    }
    
}
