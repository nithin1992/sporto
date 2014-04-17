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
	
		TextView Rname = (TextView) getView().findViewById(R.id.name);
		TextView Rlocality = (TextView) getView().findViewById(R.id.locality);
		TextView Raddress = (TextView) getView().findViewById(R.id.address);
		TextView Rrating = (TextView) getView().findViewById(R.id.rating);
		TextView Rcontact = (TextView) getView().findViewById(R.id.contact);
		TextView Rcontact1 = (TextView) getView().findViewById(R.id.contact1);
		TextView Rcategory = (TextView) getView().findViewById(R.id.category);
		TextView Rtimings = (TextView) getView().findViewById(R.id.timing);
		TextView Rwebsite = (TextView) getView().findViewById(R.id.website);
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

		Rname.setText( name );
		Rlocality.setText( locality );
		Raddress.setText( address );
		Rrating.setText( rating );
		Rcontact.setText( contact );
		Rcontact1.setText( contact1 );
		Rcategory.setText( category );
		Rtimings.setText( timing );
		Rwebsite.setText( website );
	}
}
	