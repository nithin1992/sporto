package com.sportodemoapp.library;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ReviewDatabaseHandler {
	public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "Name";
    public static final String KEY_RATING = "Rating";
    public static final String KEY_REVIEW  = "Review";
    
    
    private static final String TAG = "ReviewDatabaseHandler";
    private DatabaseHelper rDbHelper;
    private SQLiteDatabase rDb;
    
    private static final String DATABASE_NAME = "reviewresults";
    private static final String reviewResults = "review_results";
    private static final int DATABASE_VERSION = 1;
    
    private final Context mCtx;
    
  //for Fat table 
    private static final String DATABASE_CREATEREVIEW =
   		 "CREATE TABLE " + reviewResults + "("
   	                + KEY_ROWID + " INTEGER PRIMARY KEY autoincrement,"
   	                + KEY_NAME + " TEXT,"
   	                + KEY_RATING + " TEXT,"
   	                + KEY_REVIEW + " TEXT"
   	                + ")";
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
   	 
  	  DatabaseHelper(Context context) {
  	   super(context, DATABASE_NAME, null, DATABASE_VERSION);
  	  }
  	 
  	 
  	  @Override
  	  public void onCreate(SQLiteDatabase db) {
  	   Log.w(TAG, DATABASE_CREATEREVIEW);
  	   db.execSQL(DATABASE_CREATEREVIEW);
  	  }
  	 
  	  @Override
  	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  	   Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
  	     + newVersion + ", which will destroy all old data");
  	   db.execSQL("DROP TABLE IF EXISTS " + reviewResults);
  	   onCreate(db);
  	  }
  	 
  	 }

  	 
  	 public ReviewDatabaseHandler(Context ctx) {
  	  this.mCtx = ctx;
  	 }
  	 
  	 public ReviewDatabaseHandler open() throws SQLException {
  	  rDbHelper = new DatabaseHelper(mCtx);
  	  rDb = rDbHelper.getWritableDatabase();
  	  return this;
  	 }
  	 
  	 public void close() {
  	  if (rDbHelper != null) {
  	   rDbHelper.close();
  	  }
  	 }
  	 
  	 public long addResults(String Name, String Rating, String Review) {
  		   ContentValues values = new ContentValues();
  		   values.put(KEY_NAME, Name);
  	       values.put(KEY_RATING, Rating);
  	       values.put(KEY_REVIEW, Review); 
  	       return rDb.insert(reviewResults, null, values);
  	    }
  	 
  	public boolean deleteAllResults() {
  		 
  	  int doneDelete = 0;
  	  doneDelete = rDb.delete(reviewResults, null , null);
  	  Log.w(TAG, Integer.toString(doneDelete));
  	  return doneDelete > 0;
  	 
  	 }
  	 
  	 
  	 
  	 public Cursor fetchAllResults() {
  		 
  		  Cursor mCursor = rDb.query(reviewResults, new String[] {KEY_ROWID, KEY_NAME,
  				  KEY_RATING, KEY_REVIEW}, 
  		    null, null, null, null, null);
  		 
  		  if (mCursor != null) {
  		   mCursor.moveToFirst();
  		  }
  		  return mCursor;
  		 }
  	 
  
}

