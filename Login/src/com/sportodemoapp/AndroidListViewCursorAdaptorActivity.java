package com.sportodemoapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.sportodemoapp.library.MainDatabaseHandler;

//import android.view.View;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AndroidListViewCursorAdaptorActivity extends Activity {

	private MainDatabaseHandler dbHelper;
	private SimpleCursorAdapter dataAdapter;
	public final static String COMPOSITE_KEY = "composite_key";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultslayout);
		TextView emptySearch = (TextView) findViewById(R.id.emptySearch);
		Bundle bundle = getIntent().getExtras();
		Boolean flag = bundle.getBoolean("flag");
		dbHelper = new MainDatabaseHandler(this);
		dbHelper.open();
		if(flag){
		// Generate ListView from SQLite Database
		displayListView();
		}
		else
		{
			emptySearch.setText("Sorry, we have no results for you!");			
		}
	}

	private void displayListView() {

		Cursor cursor = dbHelper.fetchAllResults();

		// The desired columns to be bound
		String[] columns = new String[] {
			    MainDatabaseHandler.KEY_NAME,
			    MainDatabaseHandler.KEY_LOCALITY,
			    MainDatabaseHandler.KEY_RATING,
			    MainDatabaseHandler.KEY_DISTANCE };

		// the XML defined views which the data will be bound to
		int[] to = new int[] {
				R.id.name,
			    R.id.locality,  
			    R.id.rating,
			    R.id.distance, };

		// create the adapter using the cursor pointing to the desired data
		// as well as the layout information
		dataAdapter = new SimpleCursorAdapter(this, R.layout.search_result,
				cursor, columns, to, 0);

		ListView listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				Cursor cursor = (Cursor)listView.getItemAtPosition(position);
				String compositeKey = cursor.getString(cursor.getColumnIndexOrThrow("Name")).concat(cursor.getString(cursor.getColumnIndexOrThrow("Locality")));
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			    intent.putExtra(COMPOSITE_KEY, compositeKey);
			    startActivity(intent);
			}
		});

	}
}