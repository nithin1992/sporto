package com.sportodemoapp.library;
import java.util.HashMap;

import com.sportodemoapp.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Sporto";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_UID = "uid";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
     
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
     //Create login session

    public void createLoginSession(String uid, String name, String email){
         editor.putBoolean(IS_LOGIN, true);
         editor.putString(KEY_UID, uid);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }  
    
     /* Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything*/
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, Main.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
            _context.startActivity(i);
        }
         
    }
         
     //* Get stored session data

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_UID, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    } 
     
      //* Clear session details

    public void logoutUser(){
        // Clearing all data from Shared Preferences
    	
        editor.clear();
        editor.commit();
         
        /*// After logout redirect user to Login Activity
        Intent i = new Intent(_context, Main.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        _context.startActivity(i);*/
    }
     
   
     
     // Quick check for login. Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}