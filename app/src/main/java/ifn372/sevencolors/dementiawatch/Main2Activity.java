package ifn372.sevencolors.dementiawatch;

import android.app.AlertDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.List;
import java.util.Locale;

import ifn372.sevencolors.backend.entities.fenceApi.model.Fence;
import ifn372.sevencolors.dementiawatch.webservices.FenceService;
import ifn372.sevencolors.dementiawatch.webservices.IFenceService;

public class Main2Activity extends AppCompatActivity implements IFenceService
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void btnCreateEvent(View view)
    {
        if(!validation())
        {
            this.showMessage("Error", "Please fill out all text boxes.");
            return;
        }

        //location[0] = Lat(Horizontal(X))
        //location[1] = Lon(Vertical(Y))
        double[] location = this.getLocation();
        if(location[0] == -1 || location[1] == -1)
        {
            this.showMessage("Error", "Location could not be found.\nPlease re-enter address.");
            return;
        }

        //this.showMessage("Info", "Lat:" + location[0] + ", Lon:" + location[1]);
        String radius = "";
        try
        {
            //Check the radius can be converted to float
            float temp = 0;
            temp = Float.parseFloat(this.getValue(R.id.numRadius));
            radius = this.getValue(R.id.numRadius);
        }
        catch (Exception e)
        {
            this.showMessage("Error", "Radius could not be converted to appropriate number.");
            return;
        }
        String fenceName = this.getValue(R.id.txtFenceName);
        String address = this.getValue(R.id.txtAddress);
        address += ", " + this.getValue(R.id.txtCity) + ", " + this.getValue(R.id.txtState);

        /*
            Pass dummy user id until user login function is developed.
            TODO: Pass actual user id.
         */
        String userId = "1";

        FenceService fence = new FenceService(this);
        try
        {
            Fence info = fence.execute(
                    userId, fenceName,
                    Double.toString(location[0]), Double.toString(location[1]),
                    radius, address).get();
            if(info == null || !info.getSuccess())
            {
                this.showMessage("Error", "An error has occurred. Please try again.");
            }
            else
            {
                this.showMessage("Info", "Fence has been created.");
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getStackTrace());
        }

    }

    /**
     * Get a location by address using Geocoding.
     * @return double[] double[0] = Latitude(Horizontal), double[1] = Longitude(Vertical)
     */
    private double[] getLocation()
    {
        double[] ret = new double[2];
        ret[0] = -1;
        ret[1] = -1;
        try
        {
            String address = this.getValue(R.id.txtAddress);
            String city = this.getValue(R.id.txtCity);
            String state = this.getValue(R.id.txtState);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            //The second param represents the maximum number of record to be returned.
            List<Address> addList = geocoder.getFromLocationName(address + "," + city + "," + state, 1);
            if(addList == null || addList.size() == 0)
            {
                System.err.println("No location found. addList.size() is 0");
                return ret;
            }
            Address addr = addList.get(0);
            // Horizontal(X)
            ret[0] = addr.getLatitude();
            //Vertical(Y)
            ret[1] = addr.getLongitude();

        }
        catch (Exception e)
        {
            System.err.println(e.getStackTrace());
            ret[0] = -1;
            ret[1] = -1;
            return ret;
        }
        return ret;
    }

    /***
     * Validation for values
     * @return Returns true when all text boxes are not empty.Otherwise returns false.
     */
    private boolean validation()
    {
        String fenceName = this.getValue(R.id.txtFenceName);
        String address = this.getValue(R.id.txtAddress);
        String city = this.getValue(R.id.txtCity);
        String state = this.getValue(R.id.txtState);
        String radius = this.getValue(R.id.numRadius);
        if(fenceName.equals("") || address.equals("") || city.equals("") || state.equals("") || radius.equals(""))
        {
            return false;
        }

        return true;
    }

    private String getValue(int control)
    {
        return ((EditText)findViewById(control)).getText().toString();
    }

    private void showMessage(String title, String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.show();
    }

    @Override
    public void processData(boolean isSuccess) {
        //Toast.makeText(this, greeting, Toast.LENGTH_SHORT).show();
    }
}
