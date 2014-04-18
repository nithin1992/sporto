package com.sportodemoapp;

import java.util.HashMap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sportodemoapp.library.MainDatabaseHandler;
import com.sportodemoapp.library.SessionManager;

public class InfoFragment extends Fragment {
	private MainDatabaseHandler dbHelper;
	public String compositeKey;
	public String name;
	public String contact;
	public String contact1;
	public String latitude;
	public String longitude;
	public final static String COMPOSITE_KEY = "composite_key";
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
		Button Rbuttoncontact = (Button) getView().findViewById(R.id.contact);
		Button Rbuttoncontact1 = (Button) getView().findViewById(R.id.contact1);
		Button Rmapimage = (Button) getView().findViewById(R.id.mapimage);
		Button Rbuttonrating = (Button) getView().findViewById(R.id.rating1);
		HashMap<String,String> getDetailedInfo = new HashMap<String, String>();
		getDetailedInfo = dbHelper.getDetailedInfo(compositeKey);
		name=getDetailedInfo.get("name");
		String timing=getDetailedInfo.get("timing");
		String locality=getDetailedInfo.get("locality");
		String editor=getDetailedInfo.get("editor");
		String website=getDetailedInfo.get("website");
	    contact=getDetailedInfo.get("contact");
		contact1=getDetailedInfo.get("contact1");
		latitude=getDetailedInfo.get("latitude");
		longitude=getDetailedInfo.get("longitude");
		String address=getDetailedInfo.get("address");
		String category=getDetailedInfo.get("category");
		String rating=getDetailedInfo.get("rating");
		String distance=getDetailedInfo.get("distance");
		
		Rbuttoncontact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
		Intent call = new Intent(Intent.ACTION_DIAL);
		call.setData(Uri.parse("tel:" + contact));
		startActivity(call); 
            }});
		
		Rbuttoncontact1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
        		Intent call1 = new Intent(Intent.ACTION_DIAL);
        		call1.setData(Uri.parse("tel:" + contact1));
        		startActivity(call1); 
            }});
		
		Rmapimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Uri location = Uri.parse("geo:0,0?q="+latitude+","+longitude+"("+name+")");
            	// Or map point based on latitude/longitude
            	// Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
            	Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        		startActivity(mapIntent); 
            }});
		
		Rbuttonrating.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	SessionManager session = new SessionManager(getActivity().getApplicationContext());
        	if(!session.isLoggedIn()){
        		Toast.makeText(getActivity().getApplicationContext(),
                        "This feature requires you to login/signup! Please do so and try again!", Toast.LENGTH_SHORT).show();
        		
        	}
        	else{            	          
			Intent intent = new Intent(getActivity(), Rating.class);
		    intent.putExtra(COMPOSITE_KEY, compositeKey);
		    startActivity(intent);
        	}
            }});

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
	