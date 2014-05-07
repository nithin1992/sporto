package com.sportodemoapp;

import org.json.JSONException;
import org.json.JSONObject;

import com.sportodemoapp.library.DatabaseHandler;
import com.sportodemoapp.library.MainDatabaseHandler;
import com.sportodemoapp.library.UserFunctions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RatingBar;
 
public class FatFragment extends Fragment {
    private static String KEY_SUCCESS = "success";
	public final static String COMPOSITE_KEY = "composite_key";
	public String compositeKey;	
	//String fatDate,fatTime,fatGame,fatNumber,fatAddText;
	EditText inputDate;
    EditText inputTime;
    EditText inputGame;
    EditText inputNumberOfPlayers;
    EditText inputAddText;
    Button btnFatRegister;
    Button btnFatFetch;
	private MainDatabaseHandler mDbHelper;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_fat, container, false);
         
        return rootView;
    }
    //why not pushed
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
    	Bundle bundle = this.getArguments();
    	compositeKey = bundle.getString("compositekey");    
    	mDbHelper = new MainDatabaseHandler(getActivity());
        inputDate = (EditText) getView().findViewById(R.id.date);
        inputTime = (EditText) getView().findViewById(R.id.time);
        inputGame = (EditText) getView().findViewById(R.id.game);
        inputNumberOfPlayers = (EditText) getView().findViewById(R.id.number);
        inputAddText = (EditText) getView().findViewById(R.id.addtext);
        btnFatRegister = (Button) getView().findViewById(R.id.submit1); 
        btnFatFetch = (Button) getView().findViewById(R.id.fat); 
        btnFatRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if (( !inputDate.getText().toString().equals(""))&& ( !inputTime.getText().toString().equals(""))&& ( !inputGame.getText().toString().equals(""))&& ( !inputNumberOfPlayers.getText().toString().equals(""))&& ( !inputAddText.getText().toString().equals("")) )
                {
                   new InsertFat().execute();
                }
            	else{
            		Toast.makeText(getActivity().getApplicationContext(),
                            "Your submission is incomplete, please recheck!", Toast.LENGTH_SHORT).show();
            	}
            	}});  	
        
        btnFatFetch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent intent = new Intent(getActivity().getApplicationContext(),FatResultDisplay.class);
            	intent.putExtra(COMPOSITE_KEY, compositeKey);	
            	startActivity(intent);
            }});  	
    	
    }
    
    private class InsertFat extends AsyncTask<String, String, JSONObject> {
    	String placeid,uid;	
    	 private ProgressDialog pDialog;
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDbHelper.open();
    		placeid = mDbHelper.fetchLocationId(compositeKey);
    		DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
    		uid = db.getUserId();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Updating");
            pDialog.setMessage("Broadcasting your request!");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
    	}
    	
    	 @Override
         protected JSONObject doInBackground(String... args) {
    		 UserFunctions userFunction = new UserFunctions();
             JSONObject json = userFunction.insertfat(uid, placeid, inputNumberOfPlayers.getText().toString(), inputGame.getText().toString(),inputTime.getText().toString(),inputDate.getText().toString(),inputAddText.getText().toString());
             return json;
         }
    	 
    	 @Override
         protected void onPostExecute(JSONObject json) {
    		 
    		 try {
    			 String res = json.getString(KEY_SUCCESS);
    			 if(Integer.parseInt(res) == 1) {
    				 pDialog.dismiss();
    		 		 Toast.makeText(getActivity().getApplicationContext(),
                     "Success!", Toast.LENGTH_SHORT).show();
             }
                 else{
                	 pDialog.dismiss();
                	 Toast.makeText(getActivity().getApplicationContext(),                			 
                             "Something went wrong!", Toast.LENGTH_SHORT).show();
                 }
                 
    		 }
    		 catch (JSONException e) {
                 e.printStackTrace();


             }
    	 
    }
    }
    
}