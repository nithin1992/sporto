package com.sportodemoapp.library;


import android.content.Context;
import com.sportodemoapp.*;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
 
public class SearchResultsDisplay extends CursorAdapter implements OnRatingBarChangeListener{
 
	 public SearchResultsDisplay(Context context, Cursor c) {
	        super(context, c);
	    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.search_result, parent, false);
        
        return retView;
    }
 
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views
    	TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(cursor.getString(cursor.getColumnIndex(MainDatabaseHandler.KEY_NAME)));
        TextView locality = (TextView)view.findViewById(R.id.locality);
        locality.setText(cursor.getString(cursor.getColumnIndex(MainDatabaseHandler.KEY_LOCALITY)));
        RatingBar rating = (RatingBar)view.findViewById(R.id.star);
        rating.setOnRatingBarChangeListener(this);
        rating.setRating(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MainDatabaseHandler.KEY_RATING))));
        TextView distance = (TextView)view.findViewById(R.id.distance);
        distance.setText(cursor.getString(cursor.getColumnIndex(MainDatabaseHandler.KEY_DISTANCE)));
    }
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        // TODO Auto-generated method stub

   }
   
}