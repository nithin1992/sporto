package com.sportodemoapp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.sportodemoapp.library.LocationTracker;
import com.sportodemoapp.library.MainDatabaseHandler;
import com.sportodemoapp.library.UserFunctions;

public class RestFulWebservice extends Fragment{
	private static String KEY_NAME = "Name";
    private static String KEY_TIMING = "Timing";
    private static String KEY_LOCALITY = "Locality";
    private static String KEY_EDITOR = "Editor";
    private static String KEY_WEBSITE = "Website";
    private static String KEY_CONTACT = "Contact";
    private static String KEY_CONTACT1 = "Contact1";
    private static String KEY_LATITUDE = "Lat";
    private static String KEY_LONGITUDE = "Lon";
    private static String KEY_ADDRESS = "Address";
    private static String KEY_CATEGORY = "Category";
    private static String KEY_RATING = "Rating";
    private MainDatabaseHandler dbHelper;
    double deviceLatitude;
    double deviceLongitude;

	
    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
     
        View view = inflater.inflate(R.layout.fragment_featuredplaces, container, false);
        return view;
    }
    
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
        dbHelper = new MainDatabaseHandler(getActivity());
        final Button GetServerData = (Button) getView().findViewById(R.id.GetServerData);
         
        GetServerData.setOnClickListener(new OnClickListener() {
            
        	public void onClick(View view) {
        		getDeviceLocation();
        		new ProcessSearch().execute();
        	}
        });    
         
    }
    
    public void getDeviceLocation(){
        LocationTracker gps = new LocationTracker(getActivity());
        if(gps.canGetLocation()){ 
 deviceLatitude = gps.getLatitude();
 deviceLongitude = gps.getLongitude();
         }
        else
        {
        	gps.showSettingsAlert();
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessSearch extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            EditText inputSearch = (EditText) getView().findViewById(R.id.serverText);
            data = inputSearch.getText().toString();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Contacting Servers");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.search(data);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
               
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Getting Data");
                        JSONArray jsonMainNode = json.optJSONArray("Android");
                        int lengthJsonArr = jsonMainNode.length();  
                        
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        dbHelper.open();
                        dbHelper.deleteAllResults();

                        for(int i=0; i < lengthJsonArr; i++) 
                        {
                        	JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                            double distanceBetween = dbHelper.distanceToDest(deviceLatitude, deviceLongitude, jsonChildNode.getDouble(KEY_LATITUDE),jsonChildNode.getDouble(KEY_LONGITUDE));
                            distanceBetween = Math.round(distanceBetween*10.0)/10.0;
                            dbHelper.addResults(jsonChildNode.getString(KEY_NAME),jsonChildNode.getString(KEY_TIMING),jsonChildNode.getString(KEY_LOCALITY),jsonChildNode.getString(KEY_EDITOR),jsonChildNode.getString(KEY_WEBSITE),jsonChildNode.getString(KEY_CONTACT),jsonChildNode.getString(KEY_CONTACT1),jsonChildNode.getDouble(KEY_LATITUDE),jsonChildNode.getDouble(KEY_LONGITUDE),jsonChildNode.getString(KEY_ADDRESS),jsonChildNode.getString(KEY_CATEGORY),jsonChildNode.getString(KEY_RATING),distanceBetween);
                            }
                        /**
                        *If JSON array details are stored in SQlite it launches the User Panel.
                        **/
                        Intent intent = new Intent(getActivity(), AndroidListViewCursorAdaptorActivity.class);
                        pDialog.dismiss();
                        startActivity(intent);
                        
                        /**
                         * Close Login Screen
                         **/
                        //finish();
                                           
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
       }
    }
    
}
    
    
    
    
    
    
    
    
     
   