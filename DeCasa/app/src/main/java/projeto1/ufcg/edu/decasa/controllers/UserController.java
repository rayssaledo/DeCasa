package projeto1.ufcg.edu.decasa.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;


import org.json.JSONException;
import org.json.JSONObject;

import projeto1.ufcg.edu.decasa.models.User;
import projeto1.ufcg.edu.decasa.utils.HttpListener;
import projeto1.ufcg.edu.decasa.utils.HttpUtils;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class UserController {

    private HttpUtils mHttp;
    private Activity mActivity;
    private String url;
    private MySharedPreferences mySharedPreferences;


    public UserController(Activity activity) {
        mActivity = activity;
        mHttp = new HttpUtils(mActivity);
        url = "http://decasa-decasa.rhcloud.com/";
        mySharedPreferences = new MySharedPreferences(mActivity.getApplicationContext());
    }


    public void login(final String login, final String password, final Class classDest) {
       // mActivity.mLoadingLogin.setVisibility(View.VISIBLE);
        String rout_check_login = url + "checklogin";
        final JSONObject json = new JSONObject();
        try {
            json.put("login", login);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(rout_check_login, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) {
                try {
                    if (result.getInt("ok") == 0) {
                        new AlertDialog.Builder(mActivity)
                                .setTitle("Erro")
                                .setMessage("Usuário não cadastrado") //TODO internacionalizar
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                      //mActivity.mLoadingLogin.setVisibility(View.GONE);
                                    }
                                })
                                .create()
                                .show();

                    } else {
                        mySharedPreferences.saveUserLogged(login);
                        setView(mActivity, classDest);
                        mActivity.finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(mActivity)
                        .setTitle("Erro")
                        .setMessage("Conexão não disponível.") //TODO internacionalizar
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              //  mActivity.mLoadingLogin.setVisibility(View.GONE);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    public User getUser(final String login){
            User user = null;
            String urlGetUser = "http://decasa-decasa.rhcloud.com/getUser?login=" + login ;
            mHttp.get(urlGetUser, new HttpListener() {
                @Override
                public void onSucess(JSONObject response) throws JSONException {
                    if (response.getInt("ok") == 1) {
                        String name = response.getString("name");
                        String date_of_birth = response.getString("birth");
                        String gender = response.getString("gender");
                        String street = response.getString("street");
                        String number = response.getString("number");
                        String neighborhood = response.getString("neighborhood");
                        String city = response.getString("city");
                        String state = response.getString("state");
                        String username = response.getString("username");
                        String password = response.getString("password");

                        try {
                            User user = new User(name, date_of_birth, gender, street, number,
                                    neighborhood, city, state, username, password);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                            new AlertDialog.Builder(mActivity)
                                    .setTitle("Erro")
                                    .setMessage("") //TODO internacionalizar com mensagem certa
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //mActivity.mLoadingLogin.setVisibility(View.GONE);
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
                            .setMessage("Conexão não disponível.") //TODO internacionalizar
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //  mActivity.mLoadingLogin.setVisibility(View.GONE);
                                }
                            })
                            .create()
                            .show();
                }
            });

            return user;
    }

    public void setView(Context context, Class classe) {
        Intent it = new Intent();
        it.setClass(context, classe);
        mActivity.startActivity(it);
    }


}
