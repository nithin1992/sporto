package com.sportodemoapp;

import java.util.HashMap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sportodemoapp.library.MainDatabaseHandler;

public class InfoFragment extends Fragment {
	private MainDatabaseHandler dbHelper;
	public String compositeKey;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        return view;
    }
	
	@Override
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
		dbHelper = new MainDatabaseHandler(getActivity());
		dbHelper.open();
        Bundle bundle = this.getArguments();
        compositeKey = bundle.getString("compositekey");
	    displayView();
	}
	
	private void displayView(){
		TextView sampleOutput = (TextView) getView().findViewById(R.id.sampleOutput);
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
	