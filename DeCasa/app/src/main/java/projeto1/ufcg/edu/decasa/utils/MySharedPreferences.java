package projeto1.ufcg.edu.decasa.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import projeto1.ufcg.edu.decasa.models.Professional;

public class MySharedPreferences {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    int PRIVATE_MODE = 0;
    private List<Professional> listProfessionals;

    private static final String PREFER_NAME = "Pref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_NAME_USER = "name_user";
    public static final String KEY_CPF_USER = "cpf_user";
    public static final String KEY_PHONE_USER = "phone_user";
    public static final String KEY_NEIGHBORHOOD_USER = "neighborhood_user";
    public static final String KEY_STREET_USER = "street_user";
    public static final String KEY_NUMBER_USER = "number_user";
    public static final String KEY_SITE_USER = "site_user";
    public static final String KEY_SOCIAL_NETWORK_USER = "social_network_user";
    public static final String KEY_PICTURY_USER = "pictury_user";
    public static final String KEY_EMAIL_USER = "email_user";
    public static final String KEY_PASSWORD_USER = "password_user";
    public static final String KEY_SERVICES_USER = "list_services";
    public static final String KEY_LIST_PROFESSIONALS = "list_professionals";

    public MySharedPreferences(Context context){
        this.mContext = context;
        mPref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        mEditor = mPref.edit();
    }

    /*public void saveProfessional(String name, String cpf, String phone, String neighborhood, String street,
                                 String number, String site, String socialNetwork, String pictury, String email,
                                 String password, String services){
        mEditor.putBoolean(IS_USER_LOGIN, true);
        mEditor.putString(KEY_NAME_USER, name);
        mEditor.putString(KEY_CPF_USER, cpf);
        mEditor.putString(KEY_PHONE_USER, phone);
        mEditor.putString(KEY_NEIGHBORHOOD_USER, neighborhood);
        mEditor.putString(KEY_STREET_USER, street);
        mEditor.putString(KEY_NUMBER_USER, number);
        mEditor.putString(KEY_SITE_USER, site);
        mEditor.putString(KEY_SOCIAL_NETWORK_USER, socialNetwork);
        mEditor.putString(KEY_PICTURY_USER, pictury);
        mEditor.putString(KEY_EMAIL_USER, email);
        mEditor.putString(KEY_PASSWORD_USER, password);
        mEditor.putString(KEY_SERVICES_USER, services);
        mEditor.commit();
    }

    public Professional getProfessional(){
        String name = mPref.getString(KEY_NAME_USER, null);
        String cpf = mPref.getString(KEY_CPF_USER, null);
        String phone = mPref.getString(KEY_PHONE_USER, null);
        String neighborhood =  mPref.getString(KEY_NEIGHBORHOOD_USER, null);
        String street = mPref.getString(KEY_STREET_USER, null);
        String number = mPref.getString(KEY_NUMBER_USER, null);
        String site = mPref.getString(KEY_SITE_USER, null);
        String socialNetwork =  mPref.getString(KEY_SOCIAL_NETWORK_USER, null);
        String pictury = mPref.getString(KEY_PICTURY_USER, null);
        String email = mPref.getString(KEY_EMAIL_USER, null);
        String password = mPref.getString(KEY_PASSWORD_USER, null);
        String services = mPref.getString(KEY_SERVICES_USER, null);

        Professional professional = null;
        try {
            professional = new Professional(name, cpf, phone, neighborhood, street, number, site, socialNetwork, pictury, email, password, services);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return professional;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> userDetails = new HashMap<String, String>();
        userDetails.put(KEY_NAME_USER, mPref.getString(KEY_NAME_USER, null));
        userDetails.put(KEY_CPF_USER, mPref.getString(KEY_CPF_USER, null));
        userDetails.put(KEY_PHONE_USER, mPref.getString(KEY_PHONE_USER, null));
        userDetails.put(KEY_NEIGHBORHOOD_USER, mPref.getString(KEY_NEIGHBORHOOD_USER, null));
        userDetails.put(KEY_STREET_USER, mPref.getString(KEY_STREET_USER, null));
        userDetails.put(KEY_NUMBER_USER, mPref.getString(KEY_NUMBER_USER, null));
        userDetails.put(KEY_SITE_USER, mPref.getString(KEY_SITE_USER, null));
        userDetails.put(KEY_SOCIAL_NETWORK_USER, mPref.getString(KEY_SOCIAL_NETWORK_USER, null));
        userDetails.put(KEY_PICTURY_USER, mPref.getString(KEY_PICTURY_USER, null));
        userDetails.put(KEY_EMAIL_USER, mPref.getString(KEY_EMAIL_USER, null));
        userDetails.put(KEY_PASSWORD_USER, mPref.getString(KEY_PASSWORD_USER, null));
        userDetails.put(KEY_SERVICES_USER, mPref.getString(KEY_SERVICES_USER, null));

        return userDetails;
    }*/

    public void saveLisProfessionals (String listProfessionals) {
        mEditor.putString(KEY_LIST_PROFESSIONALS, listProfessionals);
        mEditor.commit();
    }

    public List<Professional> getListProfessionals() {
        String jsonArrayString = mPref.getString(KEY_LIST_PROFESSIONALS, "");
        try {
            JSONArray jsonArray = new JSONArray(jsonArrayString);
            loadsList(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        } return listProfessionals;
    }

   private void loadsList(JSONArray jsonArray) throws JSONException {
        List<Professional> listProfessionals = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonProfessional = jsonArray.getJSONObject(i);
            String name = jsonProfessional.getString("name");
            String cpf = jsonProfessional.getString("cpf");
            String phone = jsonProfessional.getString("phone");
            String neighborhood = jsonProfessional.getString("neighborhood");
            String street = jsonProfessional.getString("street");
            String number = jsonProfessional.getString("number");
            String site = jsonProfessional.getString("site");
            String socialNetwork = jsonProfessional.getString("socialNetwork");
            String pictury = jsonProfessional.getString("pictury");
            String email = jsonProfessional.getString("email");
            String password = jsonProfessional.getString("password");
            String services = jsonProfessional.getString("services");
            services = services.replace("[", "");
            services = services.replace("]", "");
            services = services.replaceAll("\"", "");
            String[] listServices = services.split(",");

            Professional professional = null;
            try {
                professional = new Professional(name, cpf, phone, neighborhood, street, number, site, socialNetwork, pictury, email, password, listServices);
                listProfessionals.add(professional);
                professional.setLocation(new Location(street+ " "+ neighborhood +" "+ "Campina Grande-PB"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
