package projeto1.ufcg.edu.decasa.controllers;

import android.app.Activity;
import android.content.DialogInterface;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.HttpListener;
import projeto1.ufcg.edu.decasa.utils.HttpUtils;
import projeto1.ufcg.edu.decasa.views.ProfessionalsActivity;

import android.os.Handler;
import android.os.Message;
import android.view.View;

public class ProfessionalController {

    private HttpUtils mHttp;
    private Activity mActivity;
    private String url;


    public ProfessionalController(Activity activity) {
        mActivity = activity;
        mHttp = new HttpUtils(mActivity);
        url = "http://decasa-decasa.rhcloud.com/";
    }

    public List<Professional> getProfessionalsByService(final String service, final Handler handler) {

        //ProfessionalsActivity.mLoading.setVisibility(View.VISIBLE);
        final List<Professional> professionals = new ArrayList<>();
        String urlGetByService = url + "get-by-service?service=" + service;
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
                        String pictury = jsonProfessional.getString("pictury");
                        String email = jsonProfessional.getString("email");
                        String password = jsonProfessional.getString("password");
                        String services = jsonProfessional.getString("services");
                        services = services.replace("[", "");
                        services = services.replace("]", "");
                        services = services.replaceAll("\"", "");
                        String[] listServices = services.split(",");
                        try {
                            Professional professional = new Professional(name, cpf, phone,
                                    neighborhood, street, number, site, socialNetwork, pictury,
                                    email, password, listServices);
                            professional.setLocation(new Location(street+ " "+ neighborhood +" "+ "Campina Grande-PB"));
                            professionals.add(professional);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    Message message = new Message();
                    message.what = 100;
                    handler.sendMessage(message);

                } else {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("Erro")
                            .setMessage(response.getString("msg"))
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                     //ProfessionalsActivity.mLoading.setVisibility(View.GONE);
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
                                //ProfessionalsActivity.mLoading.setVisibility(View.GONE);
                            }
                        })
                        .create().show();
            }
        });

      return professionals;
    }

}
