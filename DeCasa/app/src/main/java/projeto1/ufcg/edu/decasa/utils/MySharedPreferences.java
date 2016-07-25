package projeto1.ufcg.edu.decasa.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "Pref";
    private static final String KEY_SERVICE_ACTIVE = "service_active";

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

}
