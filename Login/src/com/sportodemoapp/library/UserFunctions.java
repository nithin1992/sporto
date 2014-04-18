package com.sportodemoapp.library;



import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.content.Context;


public class UserFunctions {

    private JSONParser jsonParser;

    //URL of the PHP API
    private static String loginURL = "http://betaenggindustries.com/sporto_api/";
    private static String registerURL = "http://betaenggindustries.com/sporto_api/";
    private static String forpassURL = "http://betaenggindustries.com/sporto_api/";
    private static String chgpassURL = "http://betaenggindustries.com/sporto_api/";
    private static String searchURL = "http://betaenggindustries.com/sporto_api/";
    private static String insertFatURL = "http://betaenggindustries.com/sporto_api/";
    private static String searchFatURL = "http://betaenggindustries.com/sporto_api/";
    private static String insertRatingURl = "http://betaenggindustries.com/sporto_api/";
    private static String fetchReviewURL = "http://betaenggindustries.com/sporto_api/";

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String search_tag = "search";
    private static String insertfat_tag = "insertFat";
    private static String searchfat_tag = "searchFat";
    private static String insertrating_tag = "insertRating";
    private static String fetchreview_tag = "fetchReview";
    
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Function to Login
     **/

    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));

        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
        return json;
    }





    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }


     /**
      * Function to  Register
      **/
    public JSONObject registerUser(String fname, String lname, String email, String mobile, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("fname", fname));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    }


    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }


    public JSONObject search(String data){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", search_tag));
        params.add(new BasicNameValuePair("data", data));
        JSONObject json = jsonParser.getJSONFromUrl(searchURL, params);
        return json;
    }
    
    
    public JSONObject insertfat(String userId,String placeId, String noOfPlayers, String game, String time, String date, String addText){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", insertfat_tag));
        params.add(new BasicNameValuePair("fat_userid", userId));
        params.add(new BasicNameValuePair("fat_placeid", placeId));
        params.add(new BasicNameValuePair("fat_noofplayers", noOfPlayers));
        params.add(new BasicNameValuePair("fat_game", game));
        params.add(new BasicNameValuePair("fat_time", time));
        params.add(new BasicNameValuePair("fat_date", date));
        params.add(new BasicNameValuePair("fat_addtext", addText));
        JSONObject json = jsonParser.getJSONFromUrl(insertFatURL, params);
        return json;
    }
    
    public JSONObject searchfat(String placeId, String game, String date){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", searchfat_tag));
        params.add(new BasicNameValuePair("fat_placeid", placeId));
        params.add(new BasicNameValuePair("fat_game", game));
        params.add(new BasicNameValuePair("fat_date", date));
        JSONObject json = jsonParser.getJSONFromUrl(searchFatURL, params);
        return json;
    }
    
    
    public JSONObject insertrating(String uid,String placeid, String rating, String review){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", insertrating_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("placeid", placeid));
        params.add(new BasicNameValuePair("rating", rating));
        params.add(new BasicNameValuePair("review", review));
        JSONObject json = jsonParser.getJSONFromUrl(insertRatingURl, params);
        return json;
    }
    
    public JSONObject fetchreview(String placeid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", fetchreview_tag));
        params.add(new BasicNameValuePair("placeid", placeid));
        JSONObject json = jsonParser.getJSONFromUrl(fetchReviewURL, params);
        return json;
    }
    
    
}

