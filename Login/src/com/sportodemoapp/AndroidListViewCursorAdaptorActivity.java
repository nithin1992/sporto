package com.sportodemoapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.sportodemoapp.library.CustomCursorAdapter;
import com.sportodemoapp.library.MainDatabaseHandler;
import com.sportodemoapp.library.SearchResultsDisplay;

//import android.view.View;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AndroidListViewCursorAdaptorActivity extends Activity {

	private MainDatabaseHandler dbHelper;
	//private SimpleCursorAdapter dataAdapter;
	private SearchResultsDisplay customAdapter;
	public final static String COMPOSITE_KEY = "composite_key";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultslayout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

	private void displayListView() {

    	ListView listView = (ListView) findViewById(R.id.listView1);
    	customAdapter = new SearchResultsDisplay(AndroidListViewCursorAdaptorActivity.this, dbHelper.fetchAllResults());
        listView.setAdapter(customAdapter);
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