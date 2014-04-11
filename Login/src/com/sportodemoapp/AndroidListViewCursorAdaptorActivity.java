package com.sportodemoapp;

 
import com.sportodemoapp.library.MainDatabaseHandler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
 
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AndroidListViewCursorAdaptorActivity extends Activity {
 
 private MainDatabaseHandler dbHelper;
 private SimpleCursorAdapter dataAdapter;
 
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.resultslayout);
 
  dbHelper = new MainDatabaseHandler(this);
  dbHelper.open();
  
  //Generate ListView from SQLite Database
  displayListView();
 
 }
 
 private void displayListView() {
 
 
  Cursor cursor = dbHelper.fetchAllResults();
 
  // The desired columns to be bound
  String[] columns = new String[] {
    MainDatabaseHandler.KEY_NAME,
    MainDatabaseHandler.KEY_LOCALITY,
    MainDatabaseHandler.KEY_CATEGORY,
    MainDatabaseHandler.KEY_RATING,
    MainDatabaseHandler.KEY_DISTANCE
  };
 
  // the XML defined views which the data will be bound to
  int[] to = new int[] { 
    R.id.name,
    R.id.locality,
    R.id.category,
    R.id.rating,
    R.id.distance,
  };
 
  // create the adapter using the cursor pointing to the desired data 
  //as well as the layout information
  dataAdapter = new SimpleCursorAdapter(
    this, R.layout.search_result, 
    cursor, 
    columns, 
    to,
    0);
 
  ListView listView = (ListView) findViewById(R.id.listView1);
  // Assign adapter to ListView
  listView.setAdapter(dataAdapter);
 
 
  /*listView.setOnItemClickListener(new OnItemClickListener() {
   @Override
   public void onItemClick(AdapterView<?> listView, View view, 
     int position, long id) {
   // Get the cursor, positioned to the corresponding row in the result set
   Cursor cursor = (Cursor) listView.getItemAtPosition(position);
 
   // Get the state's capital from this row in the database.
   String countryCode = 
    cursor.getString(cursor.getColumnIndexOrThrow("code"));
   Toast.makeText(getApplicationContext(),
     countryCode, Toast.LENGTH_SHORT).show();
 
   }
  });*/
 
  EditText myFilter = (EditText) findViewById(R.id.myFilter);
  myFilter.addTextChangedListener(new TextWatcher() {
 
   public void afterTextChanged(Editable s) {
   }
 
   public void beforeTextChanged(CharSequence s, int start, 
     int count, int after) {
   }
 
   public void onTextChanged(CharSequence s, int start, 
     int before, int count) {
    dataAdapter.getFilter().filter(s.toString());
   }
  });
   
  dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
         public Cursor runQuery(CharSequence constraint) {
             return dbHelper.fetchResultsByName(constraint.toString());
         }
     });
 
 }
}