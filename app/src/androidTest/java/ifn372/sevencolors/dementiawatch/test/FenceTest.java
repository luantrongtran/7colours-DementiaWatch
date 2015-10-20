package ifn372.sevencolors.dementiawatch.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;

import ifn372.sevencolors.dementiawatch.activities.MapsActivity;


public class FenceTest extends ActivityInstrumentationTestCase2<MapsActivity> {
  	private Solo solo;
  	
  	public FenceTest() {
		super(MapsActivity.class);
  	}

  	public void setUp() throws Exception {
        super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}
  
	public void testRun() {
        //Wait for activity: 'ifn372.sevencolors.dementiawatch.activities.MapsActivity'
		solo.waitForActivity(MapsActivity.class, 2000);
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Assert that: 'patient1' is shown
		assertTrue("'patient1' is not shown!", solo.waitForView(solo.getView(ifn372.sevencolors.dementiawatch.R.id.rowText)));
        //Wait for activity: 'ifn372.sevencolors.dementiawatch.activities.MapsActivity'
		assertTrue("ifn372.sevencolors.dementiawatch.activities.MapsActivity is not found!", solo.waitForActivity(MapsActivity.class));
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Wait for activity: 'ifn372.sevencolors.dementiawatch.activities.PatientSettingActivity'
		assertTrue("ifn372.sevencolors.dementiawatch.activities.PatientSettingActivity is not found!", solo.waitForActivity(ifn372.sevencolors.dementiawatch.activities.PatientSettingActivity.class));
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageView.class, 1));
        //Wait for activity: 'ifn372.sevencolors.dementiawatch.CreateFenceActivity'
		assertTrue("ifn372.sevencolors.dementiawatch.CreateFenceActivity is not found!", solo.waitForActivity(ifn372.sevencolors.dementiawatch.CreateFenceActivity.class));
        //Enter the text: 'patient1'
		solo.clearEditText((android.widget.EditText) solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtFenceName));
		solo.enterText((android.widget.EditText) solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtFenceName), "patient1");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtAddress));
        //Enter the text: 'QUT Gardens Point'
		solo.clearEditText((android.widget.EditText) solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtAddress));
		solo.enterText((android.widget.EditText) solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtAddress), "QUT Gardens Point");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtCity));
        //Enter the text: 'Brisbane'
		solo.clearEditText((android.widget.EditText) solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtCity));
		solo.enterText((android.widget.EditText) solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtCity), "Brisbane");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtState));
        //Enter the text: 'QLD'
		solo.clearEditText((android.widget.EditText) solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtState));
		solo.enterText((android.widget.EditText) solo.getView(ifn372.sevencolors.dementiawatch.R.id.txtState), "QLD");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(ifn372.sevencolors.dementiawatch.R.id.numRadius));
        //Enter the text: '200'
		solo.clearEditText((android.widget.EditText) solo.getView(ifn372.sevencolors.dementiawatch.R.id.numRadius));
		solo.enterText((android.widget.EditText) solo.getView(ifn372.sevencolors.dementiawatch.R.id.numRadius), "200");
        //Click on Create Fence
		solo.clickOnView(solo.getView(ifn372.sevencolors.dementiawatch.R.id.btnCreate));
        //Wait for dialog
		solo.waitForDialogToOpen(5000);
        //Wait for dialog
		solo.waitForDialogToOpen(5000);
        //Press menu back key
		solo.goBack();
        //Set default small timeout to 13721 milliseconds
		Timeout.setSmallTimeout(13721);
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Assert that: 'patient2' is shown
		assertTrue("'patient2' is not shown!", solo.waitForView(solo.getView(ifn372.sevencolors.dementiawatch.R.id.rowText, 1)));
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Wait for activity: 'ifn372.sevencolors.dementiawatch.activities.PatientSettingActivity'
		assertTrue("ifn372.sevencolors.dementiawatch.activities.PatientSettingActivity is not found!", solo.waitForActivity(ifn372.sevencolors.dementiawatch.activities.PatientSettingActivity.class));
        //Click on OFF
		solo.clickOnView(solo.getView(ifn372.sevencolors.dementiawatch.R.id.pickedUpModeToggleButton));
        //Wait for activity: 'ifn372.sevencolors.dementiawatch.activities.MapsActivity'
		assertTrue("ifn372.sevencolors.dementiawatch.activities.MapsActivity is not found!", solo.waitForActivity(MapsActivity.class));
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Wait for activity: 'ifn372.sevencolors.dementiawatch.activities.PatientSettingActivity'
		assertTrue("ifn372.sevencolors.dementiawatch.activities.PatientSettingActivity is not found!", solo.waitForActivity(ifn372.sevencolors.dementiawatch.activities.PatientSettingActivity.class));
        //Click on ON
		solo.clickOnView(solo.getView(ifn372.sevencolors.dementiawatch.R.id.pickedUpModeToggleButton));
        //Press menu back key
		solo.goBack();
	}
}
