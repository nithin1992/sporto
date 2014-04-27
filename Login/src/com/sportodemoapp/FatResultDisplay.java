package com.sportodemoapp;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.sportodemoapp.library.MainDatabaseHandler;
import com.sportodemoapp.library.UserFunctions;

public class FatResultDisplay extends Activity{
	public String compositeKey;
	private MainDatabaseHandler dbHelper;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fatlistview);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
        //compositeKey = intent.getStringExtra(FatFragment.COMPOSITE_KEY); 
        dbHelper = new MainDatabaseHandler(this);
        displayListView();
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	private void displayListView(){
	
	
	
	}
	
	
	private class FatFetch extends AsyncTask<String, String, JSONObject> {
		String placeid;
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        dbHelper.open();
			placeid = dbHelper.fetchLocationId(compositeKey);			
			
	}
		
		 @Override
	     protected JSONObject doInBackground(String... args) {
			 UserFunctions userFunction = new UserFunctions();
	        // JSONObject json = userFunction.searchfat(placeId, game, date)
			 JSONObject json =null;
	         return json;
	     }
		 
		 @Override
	     protected void onPostExecute(JSONObject json) {
	}
	
	}
}

