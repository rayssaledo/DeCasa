package projeto1.ufcg.edu.decasa.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class MySharedPreferences {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "Pref";
    private static final String KEY_SERVICE_ACTIVE = "service_active";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String USER_LOCATION = "user_location";

    public static final String KEY_NAME_USER = "name_user";
    public static final String KEY_USERNAME_USER = "username_user";


    public MySharedPreferences(Context context){
        this.mContext = context;
        mPref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
    }


    public void saveService(String service){
        mEditor.putString(KEY_SERVICE_ACTIVE, service);
        mEditor.commit();
    }


    public String getService(){
        String service = mPref.getString(KEY_SERVICE_ACTIVE, null);
        return service;
    }

    public void saveUserLogged(String login){
        mEditor.putBoolean(IS_USER_LOGIN, true);
        mEditor.putString(KEY_USERNAME_USER, login);
        mEditor.commit();
    }

    public String getUserLogged(){
        String login = mPref.getString(KEY_USERNAME_USER, null);
        return login;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> userDetails = new HashMap<String, String>();
        userDetails.put(KEY_NAME_USER, mPref.getString(KEY_NAME_USER, null));
        userDetails.put(KEY_USERNAME_USER, mPref.getString(KEY_USERNAME_USER, null));
        return userDetails;
    }

    public boolean isUserLoggedIn(){
        return mPref.getBoolean(IS_USER_LOGIN, false);
    }


    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
//        Intent i = new Intent(mContext, LoginActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(i);
    }

    public String getUserLocation() {
        String location = mPref.getString(USER_LOCATION, null);
        return location;
    }

    public void saveUserLocation (String location){
        mEditor.putString(USER_LOCATION, location);
        mEditor.commit();
    }
}
