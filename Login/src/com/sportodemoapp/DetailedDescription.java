package com.sportodemoapp;

import java.util.HashMap;

import com.sportodemoapp.library.MainDatabaseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailedDescription extends Activity {
	private MainDatabaseHandler dbHelper;
	public String compositeKey;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_description);
		dbHelper = new MainDatabaseHandler(this);
		dbHelper.open();
	    Intent intent = getIntent();
	    compositeKey = intent.getStringExtra(AndroidListViewCursorAdaptorActivity.COMPOSITE_KEY);
	    displayView();
	}
	
	private void displayView(){
		TextView sampleOutput = (TextView) findViewById(R.id.sampleOutput);
		HashMap<String,String> getDetailedInfo = new HashMap<String, String>();
		getDetailedInfo = dbHelper.getDetailedInfo(compositeKey);
		String name=getDetailedInfo.get("name");
		String timing=getDetailedInfo.get("timing");
		String locality=getDetailedInfo.get("locality");
		String editor=getDetailedInfo.get("editor");
		String website=getDetailedInfo.get("website");
		String contact=getDetailedInfo.get("contact");
		String contact1=getDetailedInfo.get("contact1");
		String latitude=getDetailedInfo.get("latitude");
		String longitude=getDetailedInfo.get("longitude");
		String address=getDetailedInfo.get("address");
		String category=getDetailedInfo.get("category");
		String rating=getDetailedInfo.get("rating");
		String distance=getDetailedInfo.get("distance");
		String OutputData = "";
		OutputData += " Name 		    : "+ name +" \n "
	            + "Timing 		: "+ timing +" \n "
	            + "Locality 				: "+ locality +" \n " 
	            + "Editor's Note 		: "+ editor +" \n "
	            + "Website 		: "+ website +" \n "
	            + "Contact 		: "+ contact +" \n "
	            + "Contact1 		: "+ contact1 +" \n "
	            + "Latitude 		: "+ latitude +" \n "
	            + "Longtitude 		: "+ longitude +" \n "
	            + "Address 		: "+ address +" \n "
	            + "Category 		: "+ category +" \n "
	            + "Rating 		: "+ rating +" \n "
	            + "Distance 		: "+ distance +" \n "
	                        +"--------------------------------------------------\n";
		sampleOutput.setText( OutputData );	
	}
	
	}