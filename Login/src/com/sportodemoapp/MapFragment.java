package com.sportodemoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class MapFragment extends Fragment {
	public String latitude;
	public String longitude;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
         
        return rootView;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
        /*Bundle bundle = this.getArguments();
        latitude = bundle.getString("latitude");
        longitude = bundle.getString("longitude");*/
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse("geo:0,0?q=" + ("12.901045, 77.707329")));
    //intent.setData(Uri.parse("geo:0,0?q=" + (latitude+","+ longitude)));
    try {
        startActivity(intent);
    } catch (Exception e) {
        e.printStackTrace();
    }

    }
}
    

