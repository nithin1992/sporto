package com.sportodemoapp.library;
 
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MainDatabaseHandler {
 
    public static final String KEY_ROWID = "_id";
    public static final String KEY_PLACEID = "placeid";
    public static final String KEY_PRIMKEY = "PrimaryKey";
    public static final String KEY_NAME = "Name";
    public static final String KEY_TIMING = "Timing";
    public static final String KEY_LOCALITY = "Locality";
    public static final String KEY_EDITOR = "Editor";
    public static final String KEY_WEBSITE = "Website";
    public static final String KEY_CONTACT = "Contact";
    public static final String KEY_CONTACT1 = "Contact1";
    public static final String KEY_LATITUDE = "Lat";
    public static final String KEY_LONGITUDE = "Lon";
    public static final String KEY_ADDRESS = "Address";
    public static final String KEY_CATEGORY = "Category";
    public static final String KEY_RATING = "Rating";
    public static final String KEY_DISTANCE = "Distance";
 
 
 private static final String TAG = "MainDatabaseHandler";
 private DatabaseHelper mDbHelper;
 private SQLiteDatabase mDb;
 
 private static final String DATABASE_NAME = "results";
 private static final String searchResults = "search_results";
 private static final int DATABASE_VERSION = 1;
 
 private final Context mCtx;
 
 private static final String DATABASE_CREATE =
		 "CREATE TABLE " + searchResults + "("
	                + KEY_ROWID + " INTEGER PRIMARY KEY autoincrement,"
	                + KEY_PLACEID + " TEXT,"
	                + KEY_PRIMKEY + " TEXT,"
	                + KEY_NAME + " TEXT,"
	                + KEY_TIMING + " TEXT,"
	                + KEY_LOCALITY + " TEXT,"
	                + KEY_EDITOR + " TEXT,"
	                + KEY_WEBSITE + " TEXT,"
	                + KEY_CONTACT + " TEXT,"
	                + KEY_CONTACT1 + " TEXT,"
	                + KEY_LATITUDE + " TEXT,"
	                + KEY_LONGITUDE + " TEXT,"
	                + KEY_ADDRESS + " TEXT,"
	                + KEY_CATEGORY + " TEXT,"
	                + KEY_RATING + " FLOAT," 
	                + KEY_DISTANCE + " TEXT"+ ")";
 
 
 
 public static class DatabaseHelper extends SQLiteOpenHelper {
 
  DatabaseHelper(Context context) {
   super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
 
 
  @Override
  public void onCreate(SQLiteDatabase db) {
   Log.w(TAG, DATABASE_CREATE);
   db.execSQL(DATABASE_CREATE);
  }
 
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
   Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
     + newVersion + ", which will destroy all old data");
   db.execSQL("DROP TABLE IF EXISTS " + searchResults);
   onCreate(db);
  }
 
 }

 
 public MainDatabaseHandler(Context ctx) {
  this.mCtx = ctx;
 }
 
 public MainDatabaseHandler open() throws SQLException {
  mDbHelper = new DatabaseHelper(mCtx);
  mDb = mDbHelper.getWritableDatabase();
  return this;
 }
 
 public void close() {
  if (mDbHelper != null) {
   mDbHelper.close();
  }
 }
 
 
 
 public long addResults(String PrimaryKey,String PlaceId, String Name, String Timing, String Locality, String Editor, String Website, String Contact, String Contact1, Double Lat, Double Lon, String Address, String Category, String Rating, Double Distance) {
	   ContentValues values = new ContentValues();
	   values.put(KEY_PRIMKEY, PrimaryKey);
	   values.put(KEY_PLACEID, PlaceId);
       values.put(KEY_NAME, Name);
       values.put(KEY_TIMING, Timing); 
       values.put(KEY_LOCALITY, Locality);
       values.put(KEY_EDITOR, Editor);
       values.put(KEY_WEBSITE, Website);
       values.put(KEY_CONTACT, Contact);
       values.put(KEY_CONTACT1, Contact1);
       values.put(KEY_LATITUDE, Lat);
       values.put(KEY_LONGITUDE, Lon);
       values.put(KEY_ADDRESS, Address);
       values.put(KEY_CATEGORY, Category);
       values.put(KEY_RATING, Rating);
       values.put(KEY_DISTANCE, Distance);
       return mDb.insert(searchResults, null, values);
    } 
 
 public boolean deleteAllResults() {
	 
	  int doneDelete = 0;
	  doneDelete = mDb.delete(searchResults, null , null);
	  Log.w(TAG, Integer.toString(doneDelete));
	  return doneDelete > 0;
	 
	 }
 
 public Cursor fetchAllResults() {
 
  Cursor mCursor = mDb.query(searchResults, new String[] {KEY_ROWID, KEY_NAME,
    KEY_LOCALITY, KEY_CATEGORY, KEY_RATING, KEY_DISTANCE}, 
    null, null, null, null, null);
 
  if (mCursor != null) {
   mCursor.moveToFirst();
  }
  return mCursor;
 }
 
 public HashMap<String, String> getDetailedInfo(String compositeKey){
     HashMap<String,String> detailedInfo = new HashMap<String,String>();
     String selectQuery = "SELECT * FROM " + searchResults +" WHERE " + KEY_PRIMKEY + "='"+ compositeKey +"';";
     Cursor cursor = mDb.rawQuery(selectQuery, null);
      cursor.moveToFirst();
         detailedInfo.put("name", cursor.getString(3));
         detailedInfo.put("timing", cursor.getString(4));
         detailedInfo.put("locality", cursor.getString(5));
         detailedInfo.put("editor", cursor.getString(6));
         detailedInfo.put("website", cursor.getString(7));
         detailedInfo.put("contact", cursor.getString(8));
			detailedInfo.put("contact1", cursor.getString(9));
			detailedInfo.put("latitude", cursor.getString(10));
			detailedInfo.put("longitude", cursor.getString(11));
			detailedInfo.put("address", cursor.getString(12));
			detailedInfo.put("category", cursor.getString(13));
			detailedInfo.put("rating", cursor.getString(14));
			detailedInfo.put("distance", cursor.getString(15));
     cursor.close();
     return detailedInfo;
 }
 
 public String fetchLocationId(String compositeKey) {
	 String selectQuery = "SELECT " +KEY_PLACEID+" FROM "+searchResults+" WHERE "+KEY_PRIMKEY+ "='"+compositeKey+"';";
     Cursor cursor = mDb.rawQuery(selectQuery, null);
     cursor.moveToFirst();
     String placeid = cursor.getString(cursor.getColumnIndex("placeid"));
     Log.e("queryresult is", placeid);
	  return placeid;
	 } 
 
 
 public double distanceToDest(double lat1,double lon1, double lat2, double lon2) {
     
     
     double dLat = Math.toRadians(lat2-lat1);

     double dLon = Math.toRadians(lon2-lon1);

     double a = Math.sin(dLat/2) * Math.sin(dLat/2) +

     Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *

     Math.sin(dLon/2) * Math.sin(dLon/2);

     double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

//6378.1 is gravity of earth
     return  c*6378.1;

  }
 
 }