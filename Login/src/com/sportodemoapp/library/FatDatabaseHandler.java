package com.sportodemoapp.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sportodemoapp.library.MainDatabaseHandler.DatabaseHelper;

public class FatDatabaseHandler {
	   
    //for fat table
    public static final String KEY_ROWID = "_id";
    public static final String KEY_FNAME = "Fname";
    public static final String KEY_LNAME = "Lname";
    public static final String KEY_EMAIL  = "Email";
    public static final String KEY_MOB= "Mob";
    public static final String KEY_DATEOFPLAY = "Dateofplay";
    public static final String KEY_STARTTIME = "Starttime";
    public static final String KEY_NOOFPLAYERS = "Noofplayers";
    public static final String KEY_GAME = "Game";
    public static final String KEY_ADDITIONALINFO = "Addinfo";
    
    private static final String TAG = "FatDatabaseHandler";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_NAME = "fatresults";
    private static final String fatResults = "fat_results";
    private static final int DATABASE_VERSION = 1;
    
    private final Context mCtx;
    
  //for Fat table 
    private static final String DATABASE_CREATEFAT =
   		 "CREATE TABLE " + fatResults + "("
   	                + KEY_ROWID + " INTEGER PRIMARY KEY autoincrement,"
   	                + KEY_FNAME + " TEXT,"
   	                + KEY_LNAME + " TEXT,"
   	                + KEY_EMAIL + " TEXT,"
   	                + KEY_MOB + " TEXT,"
   	                + KEY_DATEOFPLAY + " TEXT,"
   	                + KEY_STARTTIME + " TEXT,"
   	                + KEY_NOOFPLAYERS + " TEXT,"
   	                + KEY_GAME + " TEXT,"
   	                + KEY_ADDITIONALINFO + " TEXT,"+ ")";
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
    	 
    	  DatabaseHelper(Context context) {
    	   super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	  }
    	 
    	 
    	  @Override
    	  public void onCreate(SQLiteDatabase db) {
    	   Log.w(TAG, DATABASE_CREATEFAT);
    	   db.execSQL(DATABASE_CREATEFAT);
    	  }
    	 
    	  @Override
    	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	   Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
    	     + newVersion + ", which will destroy all old data");
    	   db.execSQL("DROP TABLE IF EXISTS " + fatResults);
    	   onCreate(db);
    	  }
    	 
    	 }

    	 
    	 public FatDatabaseHandler(Context ctx) {
    	  this.mCtx = ctx;
    	 }
    	 
    	 public FatDatabaseHandler open() throws SQLException {
    	  mDbHelper = new DatabaseHelper(mCtx);
    	  mDb = mDbHelper.getWritableDatabase();
    	  return this;
    	 }
    	 
    	 public void close() {
    	  if (mDbHelper != null) {
    	   mDbHelper.close();
    	  }
    	 }
    	 
    	 public long addResults(String Fname, String Lname, String Email, String Mob, String Dateofplay, String Starttime, String Noofplayers, String Game, String Addinfo) {
    		   ContentValues values = new ContentValues();
    		   values.put(KEY_FNAME, Fname);
    	       values.put(KEY_LNAME, Lname);
    	       values.put(KEY_EMAIL, Email); 
    	       values.put(KEY_MOB, Mob);
    	       values.put(KEY_DATEOFPLAY, Dateofplay);
    	       values.put(KEY_STARTTIME, Starttime);
    	       values.put(KEY_NOOFPLAYERS, Noofplayers);
    	       values.put(KEY_GAME, Game);
    	       values.put(KEY_ADDITIONALINFO, Addinfo);
    	      
    	       return mDb.insert(fatResults, null, values);
    	    }
    	 
    	 public Cursor fetchAllResults() {
    		 
    		  Cursor mCursor = mDb.query(fatResults, new String[] {KEY_ROWID, KEY_FNAME,
    				  KEY_LNAME, KEY_EMAIL, KEY_MOB, KEY_DATEOFPLAY, KEY_STARTTIME,KEY_NOOFPLAYERS, KEY_GAME, KEY_ADDITIONALINFO}, 
    		    null, null, null, null, null);
    		 
    		  if (mCursor != null) {
    		   mCursor.moveToFirst();
    		  }
    		  return mCursor;
    		 }
    	 
    
}
