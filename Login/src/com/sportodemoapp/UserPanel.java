package com.sportodemoapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sportodemoapp.library.SessionManager;
import com.sportodemoapp.library.UserFunctions;
import com.sportodemoapp.library.DatabaseHandler;

import java.util.HashMap;

public class UserPanel extends Fragment {
    Button btnLogout;
    Button changepas;
    /**
     * Called when the activity is first created.
     */
    @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        
        View view = inflater.inflate(R.layout.user_panel, container, false);
        return view;
    }
    
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState); 
        changepas = (Button) getView().findViewById(R.id.btchangepass);
        btnLogout = (Button) getView().findViewById(R.id.logout);

        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
         HashMap<String,String> user = new HashMap<String, String>();
         user = db.getUserDetails();


        /**
         * Change Password Activity Started
         **/
        changepas.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent chgpass = new Intent(getActivity().getApplicationContext(), ChangePassword.class);

                startActivity(chgpass);
            }

        });

       /**
        *Logout from the User Panel which clears the data in Sqlite database
        **/
        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                UserFunctions logout = new UserFunctions();
                logout.logoutUser(getActivity().getApplicationContext());
                SessionManager session = new SessionManager(getActivity().getApplicationContext());
                session.logoutUser();
                Intent login = new Intent(getActivity().getApplicationContext(), Main.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                //finish();
            }
        });
/**
 * Sets user first name and last name in text view.
 **/
        final TextView login = (TextView) getView().findViewById(R.id.textwelcome);
        login.setText("Welcome  "+user.get("fname"));
        final TextView lname = (TextView) getView().findViewById(R.id.lname);
        lname.setText(user.get("lname"));


    }}