package com.sportodemoapp;

import org.json.JSONObject;

import com.sportodemoapp.library.DatabaseHandler;
import com.sportodemoapp.library.MainDatabaseHandler;
import com.sportodemoapp.library.SessionManager;
import com.sportodemoapp.library.UserFunctions;
import com.sportodemoapp.library.MainDatabaseHandler.DatabaseHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class Rating extends Activity {
	RatingBar rating;
	EditText review;
	public String compositeKey;
	private MainDatabaseHandler dbHelper;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        rating = (RatingBar) findViewById(R.id.ratingBar1);
        review = (EditText) findViewById(R.id.review);
        Button button = (Button) findViewById(R.id.button1);
        dbHelper = new MainDatabaseHandler(this);
        Intent intent = getIntent();
        compositeKey = intent.getStringExtra(InfoFragment.COMPOSITE_KEY); 
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Float ratingValue = rating.getRating();
            	if (  ( ratingValue!=0) && ( !review.getText().toString().equals("")) )
                {
                   new PostReview().execute();
                }
            	else{
            		Toast.makeText(getApplicationContext(),
                            "Your submission is incomplete, please recheck!", Toast.LENGTH_SHORT).show();
            	}
            	}});
	}
	
    private class PostReview extends AsyncTask<String, String, JSONObject> {
	String placeid,reviewValue,uid,ratingValueString;
	Float ratingValue;
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        rating = (RatingBar) findViewById(R.id.ratingBar1);
        review = (EditText) findViewById(R.id.review);
        reviewValue = review.getText().toString();
        ratingValue = rating.getRating();
        ratingValueString = Float.toString(ratingValue);
        dbHelper.open();
		placeid = dbHelper.fetchLocationId(compositeKey);
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		uid = db.getUserId();

	}
	
	 @Override
     protected JSONObject doInBackground(String... args) {
		 UserFunctions userFunction = new UserFunctions();
         JSONObject json = userFunction.insertrating(uid,placeid,ratingValueString, reviewValue);
         return json;
     }
	 
	 @Override
     protected void onPostExecute(JSONObject json) {
		 Toast.makeText(getApplicationContext(),
                 "Values are:"+uid+placeid+ratingValueString+reviewValue, Toast.LENGTH_SHORT).show();
		 Toast.makeText(getApplicationContext(),
                 "Success!", Toast.LENGTH_SHORT).show();
         }
	 }

}
