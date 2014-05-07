package com.sportodemoapp;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sportodemoapp.FatFragment.SelectDateFragment;
import com.sportodemoapp.library.DatabaseHandler;
import com.sportodemoapp.library.FatDatabaseHandler;
import com.sportodemoapp.library.MainDatabaseHandler;
import com.sportodemoapp.library.UserFunctions;

public class FatResultDisplay extends FragmentActivity{
	private static String KEY_FIRSTNAME = "FirstName";
	private static String KEY_LASTNAME = "LastName";
    private static String KEY_EMAIL = "Email";
    private static String KEY_MOBILE = "Mobile";
    private static String KEY_DATEOFPLAY = "DateOfPlay";
    private static String KEY_STARTTIME = "StartTime";
    private static String KEY_NOOFPLAYERS = "NoOfPlayers";
    private static String KEY_GAME = "Game";
    private static String KEY_ADDINFO = "AddInfo";
	Boolean flag;
    public String compositeKey;
	public String selectedCategory;
	public String selectedDate;
	Spinner spinner1;
	EditText inputDate;
	private SimpleCursorAdapter dataAdapter;
	private MainDatabaseHandler dbHelper;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fatlistview);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
        compositeKey = intent.getStringExtra(FatFragment.COMPOSITE_KEY); 
        dbHelper = new MainDatabaseHandler(this);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.fatcategoryitems, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        inputDate = (EditText) findViewById(R.id.date);
        inputDate.setClickable(true);
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(inputDate);
            }
        });
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	selectedCategory = String.valueOf(spinner1.getSelectedItem());
        	selectedDate = inputDate.getText().toString();        	
        	startFetch();
        }});
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

	private void startFetch(){
		new FatFetch().execute();	
	}


	private class FatFetch extends AsyncTask<String, String, JSONObject> {
		String placeid;
		 private ProgressDialog pDialog;
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        dbHelper.open();
			placeid = dbHelper.fetchLocationId(compositeKey);	
            pDialog = new ProgressDialog(FatResultDisplay.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

	}

		 @Override
	     protected JSONObject doInBackground(String... args) {
			 UserFunctions userFunction = new UserFunctions();
			 JSONObject json = userFunction.searchfat(placeid, selectedCategory, selectedDate);
	         return json;
	     }

		 @SuppressWarnings("deprecation")
		@Override
	     protected void onPostExecute(JSONObject json) {
			 try{
    			 pDialog.setMessage("Retreiving");
                 pDialog.setTitle("FAT Data");
			 JSONArray jsonMainNode = json.optJSONArray("fat");
             int lengthJsonArr = jsonMainNode.length();  
             if(lengthJsonArr!=0)
             {
             flag = true;	
             }
             else
             {
             flag = false;
             }
        	 FatDatabaseHandler fDb = new FatDatabaseHandler(getApplicationContext());
        	 fDb.open();
        	 fDb.deleteAllResults();

             for(int i=0; i < lengthJsonArr; i++) 
             {
             	JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
             	String name = jsonChildNode.getString(KEY_FIRSTNAME).concat(jsonChildNode.getString(KEY_LASTNAME));
             	fDb.addResults(name, jsonChildNode.getString(KEY_EMAIL),jsonChildNode.getString(KEY_MOBILE),jsonChildNode.getString(KEY_DATEOFPLAY),jsonChildNode.getString(KEY_STARTTIME),jsonChildNode.getString(KEY_NOOFPLAYERS),jsonChildNode.getString(KEY_GAME),jsonChildNode.getString(KEY_ADDINFO));
             	 pDialog.dismiss();    
             }
             if(flag){
            	 displayListView();
             }
             else{
            	 pDialog.dismiss();
            	 AlertDialog alertDialog = new AlertDialog.Builder(
                         FatResultDisplay.this).create();
         alertDialog.setTitle("Sorry");
         alertDialog.setMessage("No entrants yet! Your chance to be the first one!");

         alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
                                  }
         });
  
         // Showing Alert Message
         alertDialog.show();

             }
			 }
			 catch (JSONException e) {
	                e.printStackTrace();
	            }

	}

	}


	private void displayListView() {
		 FatDatabaseHandler fDb = new FatDatabaseHandler(getApplicationContext());
    	 fDb.open();
		 Cursor cursor = fDb.fetchAllResults();

		// The desired columns to be bound
		String[] columns = new String[] {
			    FatDatabaseHandler.KEY_NAME,
			    FatDatabaseHandler.KEY_EMAIL,
			    FatDatabaseHandler.KEY_MOB,
			    FatDatabaseHandler.KEY_DATEOFPLAY,
			    FatDatabaseHandler.KEY_STARTTIME,
			    FatDatabaseHandler.KEY_NOOFPLAYERS,
			    FatDatabaseHandler.KEY_GAME,
			    FatDatabaseHandler.KEY_ADDITIONALINFO
			    };

		// the XML defined views which the data will be bound to
		int[] to = new int[] {
				R.id.name,
			    R.id.email,  
			    R.id.mobile,
			    R.id.date,
			    R.id.time,
			    R.id.noofplayers,
			    R.id.game,
			    R.id.addinfo,};

		// create the adapter using the cursor pointing to the desired data
		// as well as the layout information
		dataAdapter = new SimpleCursorAdapter(this, R.layout.fatresult,
				cursor, columns, to, 0);

		ListView listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);


	}
	
	public void selectDate(View view) {
    	DialogFragment newFragment = new SelectDateFragment();
    	newFragment.show(getSupportFragmentManager(), "Date Picker");
    	}
    	public void populateSetDate(int year, String month, String day) {  
    	inputDate.setText(year+"-"+month+"-"+day);
    	}
    	public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    		@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
    	final Calendar calendar = Calendar.getInstance();
    	int yy = calendar.get(Calendar.YEAR);
    	int mm = calendar.get(Calendar.MONTH);
    	int dd = calendar.get(Calendar.DAY_OF_MONTH);
    	
    	return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    	}
    	 
    	public void onDateSet(DatePicker view, int yy, int mm, int dd) {
    		int month = mm + 1;
    	    String formattedMonth = "" + month;
    	    String formattedDayOfMonth = "" + dd;

    	    if(month < 10){

    	        formattedMonth = "0" + month;
    	    }
    	    if(dd < 10){

    	        formattedDayOfMonth = "0" + dd;
    	    }
    	populateSetDate(yy, formattedMonth, formattedDayOfMonth);
    	}
    	}

	
}