package com.sportodemoapp;



 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sportodemoapp.library.CustomCursorAdapter;
import com.sportodemoapp.library.DatabaseHandler;
import com.sportodemoapp.library.MainDatabaseHandler;
import com.sportodemoapp.library.ReviewDatabaseHandler;
import com.sportodemoapp.library.UserFunctions;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
 
public class RatingFragment extends Fragment {
	private static final String KEY_NAME = "FirstName";
    private static final String KEY_RATING = "Rating";
    private static final String KEY_REVIEW  = "Review";
	public String compositeKey;
	Boolean flag;
	private SimpleCursorAdapter dataAdapter;
	private CustomCursorAdapter customAdapter;
	private MainDatabaseHandler mDbHelper;
	private ReviewDatabaseHandler rDbHelper;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_rating, container, false);
         
        return rootView;
    }
    
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
        Bundle bundle = this.getArguments();
        compositeKey = bundle.getString("compositekey");
        mDbHelper = new MainDatabaseHandler(getActivity());
        rDbHelper = new ReviewDatabaseHandler(getActivity());
        new FetchReview().execute();
    	
    }
    
    
    private class FetchReview extends AsyncTask<String, String, JSONObject> {
	String placeid;	
	 private ProgressDialog pDialog;
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDbHelper.open();
		placeid = mDbHelper.fetchLocationId(compositeKey);
		 pDialog = new ProgressDialog(getActivity());
         pDialog.setTitle("Contacting Servers");
         pDialog.setIndeterminate(false);
         pDialog.setCancelable(true);
         pDialog.show();

	}
	
	 @Override
     protected JSONObject doInBackground(String... args) {
		 UserFunctions userFunction = new UserFunctions();
         JSONObject json = userFunction.fetchreview(placeid);
         return json;
     }
	 
	 @Override
     protected void onPostExecute(JSONObject json) {
		 TextView emptyFetch = (TextView) getView().findViewById(R.id.emptySearch);
		 try {
		 JSONArray jsonMainNode = json.optJSONArray("reviews");
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
         rDbHelper.open();
         rDbHelper.deleteAllResults();

         for(int i=0; i < lengthJsonArr; i++) 
         {
         	JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
         	rDbHelper.addResults(jsonChildNode.getString(KEY_NAME), jsonChildNode.getString(KEY_RATING), jsonChildNode.getString(KEY_REVIEW));
         }if(flag){
     		// Generate ListView from SQLite Database
        	 DisplayListView();
        	 pDialog.dismiss();
     		}
     		else
     		{
     			emptyFetch.setText("No reviews yet! Be the first one to review this place!");			
     			pDialog.dismiss();
     		}
         
         }
		 catch (JSONException e) {
             e.printStackTrace();
		 }
	 }
    }
    
    private void DisplayListView()
    {
    	ListView listView = (ListView) getView().findViewById(R.id.reviewlistView);
    	customAdapter = new CustomCursorAdapter(getActivity(), rDbHelper.fetchAllResults());
        listView.setAdapter(customAdapter);
   
    }
     
 
}