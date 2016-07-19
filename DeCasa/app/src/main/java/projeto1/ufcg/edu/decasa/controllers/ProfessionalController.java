package projeto1.ufcg.edu.decasa.controllers;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.HttpListener;
import projeto1.ufcg.edu.decasa.utils.HttpUtils;

public class ProfessionalController {

    private HttpUtils mHttp;
    private Activity mActivity;
    private String url;

    public ProfessionalController(Activity activity) {
        mActivity = activity;
        mHttp = new HttpUtils(mActivity);
        url = "http://decasa-decasa.rhcloud.com/";
    }

    public List<Professional> getProfessionalsByService(String service) {
        String urlGetByService = "http://decasa-decasa.rhcloud.com/get-by-service?service=" + service;
        final List<Professional> listProfessional = new ArrayList<Professional>();
        mHttp.get(urlGetByService, new HttpListener() {
            @Override
            public void onSucess(JSONObject response) throws JSONException {
                if (response.getInt("ok") == 1) {
                    JSONArray jsonArray = response.getJSONArray("result");
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
                        String photo = jsonProfessional.getString("photo");
                        ArrayList<String> services = (ArrayList) jsonProfessional.get("services");
                        String email = jsonProfessional.getString("email");
                        String password = jsonProfessional.getString("password");

                        try {
                            Professional professional = new Professional(name, services);
                            listProfessional.add(professional);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("Erro")
                            .setMessage(response.getString("msg"))
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // mLoading.setVisibility(View.GONE);
                                }
                            })
                            .create()
                            .show();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(mActivity)
                        .setTitle("Erro")
                        .setMessage("Conexão não disponível.")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create().show();
            }
        });
        return listProfessional;
    }
}
