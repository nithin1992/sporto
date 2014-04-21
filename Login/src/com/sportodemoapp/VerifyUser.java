package com.sportodemoapp;

import org.json.JSONException;
import org.json.JSONObject;
import com.sportodemoapp.library.SessionManager;
import com.sportodemoapp.library.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerifyUser extends Activity{
	  EditText verifyusertext;
	  Button verifybutton;
	  String uid,firstname,email;
	  private static String KEY_SUCCESS = "success";
	  private static String KEY_ERROR = "error";
	  private static String KEY_UID = "uid";
	    private static String KEY_FIRSTNAME = "fname";
	    private static String KEY_EMAIL = "email";
	    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_user);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent myIntent = getIntent();
        uid = myIntent.getStringExtra("KEY_UID");
        firstname=myIntent.getStringExtra("KEY_FIRSTNAME");
        email=myIntent.getStringExtra("KEY_EMAIL");
        verifyusertext = (EditText) findViewById(R.id.verificationcodetext);
        verifybutton = (Button) findViewById(R.id.verificationcodebutton);
        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	new ProcessVerify().execute();
            	

            }



        });
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
    
	
	private class ProcessVerify extends AsyncTask<String, String, JSONObject> {
		 String verificationcode;
		@Override
        protected void onPreExecute() {
            super.onPreExecute();                  
            verificationcode = verifyusertext.getText().toString();
		}
		
		
		@Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.verificationsend(verificationcode);
            return json;


        }
		
		

        @Override
        protected void onPostExecute(JSONObject json) {
      /**
       * Checks if the Password Change Process is sucesss
       **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                	String res = json.getString(KEY_SUCCESS);
                	String red = json.getString(KEY_ERROR);
                    if(res=="1")
                    {                    
                    SessionManager sessionEntry = new SessionManager(getApplicationContext());
                    sessionEntry.createLoginSession(KEY_UID,KEY_FIRSTNAME, KEY_EMAIL);
                    Toast.makeText(getApplicationContext(),
                            "Successfully Verified and logged in", Toast.LENGTH_SHORT).show();
                    Intent verificationintent = new Intent(getApplicationContext(), Main.class);
                   /**
                     * Close all views before launching Registered screen
                    **/
                    verificationintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(verificationintent);
                    
                    }
                    else
                    {
                    	Toast.makeText(getApplicationContext(),
                                "Why can't you even enter a small code correctly?", Toast.LENGTH_SHORT).show();
                    }
                }
            }
                       catch (JSONException e) {
                        e.printStackTrace();


                    }
        }
	


}
}