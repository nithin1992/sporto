package com.sportodemoapp;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Fragment;
import android.view.KeyEvent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.sportodemoapp.library.LocationTracker;
import com.sportodemoapp.library.MainDatabaseHandler;
import com.sportodemoapp.library.UserFunctions;

public class RestFulWebservice extends Fragment{
	private static String KEY_PLACEID = "PlaceId";
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
    Boolean flag;
    //private TextView searchErrorMsg;


    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
     
        View view = inflater.inflate(R.layout.fragment_featuredplaces, container, false);
        /*ActionBar actionBar = getActivity().getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(143, 152, 42)));*/
        return view;
    }
    
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
        dbHelper = new MainDatabaseHandler(getActivity());
        //searchErrorMsg = (TextView) getView().findViewById(R.id.searchErrorMsg);
        EditText inputSearch = (EditText) getView().findViewById(R.id.serverText);
        final Button GetServerData = (Button) getView().findViewById(R.id.GetServerData);
         GetServerData.setOnClickListener(new OnClickListener() {
            
        	public void onClick(View view) {
        		NetAsync(view);
        	}
        });   
         inputSearch.setOnEditorActionListener(new OnEditorActionListener() {
             @Override
             public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 boolean handled = false;
                 if (actionId == EditorInfo.IME_ACTION_GO) {
                	 NetAsync(getView());
                     handled = true;
                 }
                 return handled;
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

    
    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(getActivity());
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
        **/
        @Override
        protected Boolean doInBackground(String... args){



        	ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
         		getDeviceLocation();
        		new ProcessSearch().execute();
            }
            else{
                nDialog.dismiss();
                //searchErrorMsg.setText("Error in Network Connection");
                Toast.makeText(getActivity().getApplicationContext(),
                        "Seems like the internet and I aren't communicating. Check your internet connection ! ", Toast.LENGTH_LONG).show();

            }
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
                        if(lengthJsonArr!=0)
                        {
                        flag = true;	
                        }
                        else
                        {
                        flag = false;
                        }
                    
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
                            String compkey = jsonChildNode.getString(KEY_NAME).concat(jsonChildNode.getString(KEY_LOCALITY));
                            dbHelper.addResults(compkey,jsonChildNode.getString(KEY_PLACEID), jsonChildNode.getString(KEY_NAME),jsonChildNode.getString(KEY_TIMING),jsonChildNode.getString(KEY_LOCALITY),jsonChildNode.getString(KEY_EDITOR),jsonChildNode.getString(KEY_WEBSITE),jsonChildNode.getString(KEY_CONTACT),jsonChildNode.getString(KEY_CONTACT1),jsonChildNode.getDouble(KEY_LATITUDE),jsonChildNode.getDouble(KEY_LONGITUDE),jsonChildNode.getString(KEY_ADDRESS),jsonChildNode.getString(KEY_CATEGORY),jsonChildNode.getString(KEY_RATING),distanceBetween);
                            }
                        /**
                        *If JSON array details are stored in SQlite it launches the User Panel.
                        **/
                        Intent intent = new Intent(getActivity(), AndroidListViewCursorAdaptorActivity.class);
                        pDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("flag", flag);
                        intent.putExtras(bundle);
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
    
    public void NetAsync(View view){
        new NetCheck().execute();
    }
    
    
}
    