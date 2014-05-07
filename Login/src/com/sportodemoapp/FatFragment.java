package com.sportodemoapp;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sportodemoapp.library.DatabaseHandler;
import com.sportodemoapp.library.MainDatabaseHandler;
import com.sportodemoapp.library.SessionManager;
import com.sportodemoapp.library.UserFunctions;
 
public class FatFragment extends Fragment{
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
        inputAddText = (EditText) getView().findViewById(R.id.addinfo);
        btnFatRegister = (Button) getView().findViewById(R.id.submit1); 
        btnFatFetch = (Button) getView().findViewById(R.id.fat);
        inputDate.setClickable(true);
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(inputDate);
            }
        });
        inputTime.setClickable(true);
        inputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(inputTime);
            }
        });
        btnFatRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	SessionManager session = new SessionManager(getActivity().getApplicationContext());
            	if(!session.isLoggedIn()){
            		Toast.makeText(getActivity().getApplicationContext(),
                            "This feature requires you to login/signup! Please do so and try again!", Toast.LENGTH_SHORT).show();
            	}
            	else{
            	if (( !inputDate.getText().toString().equals(""))&& ( !inputTime.getText().toString().equals(""))&& ( !inputGame.getText().toString().equals(""))&& ( !inputNumberOfPlayers.getText().toString().equals(""))&& ( !inputAddText.getText().toString().equals("")) )
                {
                   new InsertFat().execute();
                }
            	else{
            		Toast.makeText(getActivity().getApplicationContext(),
                            "Your submission is incomplete, please recheck!", Toast.LENGTH_SHORT).show();
            	}
            	}}});  	
        
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
    
    public void selectDate(View view) {
    	DialogFragment newFragment = new SelectDateFragment();
    	newFragment.show(getFragmentManager(), "Date Picker");
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


public void selectTime(View view) {
	DialogFragment newFragment = new SelectTimeFragment();
	newFragment.show(getFragmentManager(), "Time Picker");
	}
	public void populateSetTime(int hour, int minute) {
	inputTime.setText(hour+":"+minute);
	}
	public class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	final Calendar calendar = Calendar.getInstance();
	int hr = calendar.get(Calendar.HOUR_OF_DAY);
    int min = calendar.get(Calendar.MINUTE);
	return new TimePickerDialog(getActivity(),this,hr,min, false);
	}
	 
	public void onTimeSet(TimePicker view, int hr, int min) {
	populateSetTime(hr, min);
	}
	}

}

 