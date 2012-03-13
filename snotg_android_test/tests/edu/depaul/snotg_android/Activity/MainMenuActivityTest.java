package edu.depaul.snotg_android.Activity;

import android.test.ActivityInstrumentationTestCase2;

/**
 *  JUST A STUB to validate the test project
 *
 */
public class MainMenuActivityTest extends
		ActivityInstrumentationTestCase2<edu.depaul.snotg_android.Activity.MainMenuActivity> {

    private MainMenuActivity activity; 
    
		    
	public MainMenuActivityTest() {
		super("edu.depaul.snotg_android.Activity", MainMenuActivity.class);
	}
	
    protected void setUp() throws Exception {
        super.setUp();
        activity = this.getActivity();	
      
    }
    
    public void testJunk() {
    	assertTrue(1==1);
    }
    
    public void testPreconditions() {

    }
	
	public void testPre() {
		assertNotNull(activity);
	}
	


}
