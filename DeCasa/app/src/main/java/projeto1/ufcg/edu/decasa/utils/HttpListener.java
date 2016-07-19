package projeto1.ufcg.edu.decasa.utils;

import org.json.JSONException;
import org.json.JSONObject;

public interface HttpListener {
    void onSucess(JSONObject response) throws JSONException;
    void onTimeout();
}