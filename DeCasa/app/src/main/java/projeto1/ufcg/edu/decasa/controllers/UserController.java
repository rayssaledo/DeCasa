package projeto1.ufcg.edu.decasa.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.models.User;
import projeto1.ufcg.edu.decasa.utils.HttpListener;
import projeto1.ufcg.edu.decasa.utils.HttpUtils;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;
import projeto1.ufcg.edu.decasa.views.LoginActivity;
import projeto1.ufcg.edu.decasa.views.UserCadastreActivity;

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

    public void cadastre(final String name, final String birthDate, final String gender,
                         final String street, final String number, final String neighborhood,
                         final String city, final String state, final String photo,
                         final String username, final String password, final Class classDest,
                         final Professional professional) {

        UserCadastreActivity.mLoadingCadastre.setVisibility(View.VISIBLE);
        String urlCadastre = url + "add-user";
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("birthDate", birthDate);
            json.put("gender", gender);
            json.put("street", street);
            json.put("number", number);
            json.put("neighborhood", neighborhood);
            json.put("city", city);
            json.put("state", state);
            json.put("photo", photo);
            json.put("username", username);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(urlCadastre, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) throws JSONException{
                if (result.getInt("ok") == 0) {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("Erro")
                            .setMessage(result.getString("msg")) //TODO internacionalizar
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    UserCadastreActivity.mLoadingCadastre.setVisibility(View.GONE);
                                }
                            })
                            .create()
                            .show();
                } else {
                    new AlertDialog.Builder(mActivity)
                            .setMessage("Cadastro realizado com sucesso") //TODO internacionalizar
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mySharedPreferences.saveUserLogged(username);
                                    Intent intent = new Intent(mActivity,
                                            classDest);
                                    intent.putExtra("PROFESSIONAL", professional);
                                    mActivity.startActivity(intent);
                                    mActivity.finish();
                                    mActivity.finish();

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
                        .setMessage("Conexão não disponível") //TODO intercionalizar
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UserCadastreActivity.mLoadingCadastre.setVisibility(View.GONE);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }


    public void login(final String login, final String password, final Class classDest,
                      final Professional professional) {
        LoginActivity.loading.setVisibility(View.VISIBLE);
        String rout_check_login = url + "check-login";
        final JSONObject json = new JSONObject();
        try {
            json.put("username", login);
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
                                        LoginActivity.loading.setVisibility(View.GONE);
                                    }
                                })
                                .create()
                                .show();

                    } else {
                        mySharedPreferences.saveUserLogged(login);
                        Intent intent = new Intent(mActivity,
                                classDest);
                        intent.putExtra("PROFESSIONAL", professional);
                        mActivity.startActivity(intent);
                        mActivity.finish();
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
                                LoginActivity.loading.setVisibility(View.GONE);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    public List<User> getUser(final String login, final Handler handler){
        final List<User> userList = new ArrayList<>();
        String urlGetUser = "http://decasa-decasa.rhcloud.com/get-user?username=" + login ;
        mHttp.get(urlGetUser, new HttpListener() {
            @Override
            public void onSucess(JSONObject result) throws JSONException {
                if (result.getInt("ok") == 1) {
                    JSONObject jsonResult = result.getJSONObject("result");
                    String name = jsonResult.getString("name");
                    String date_of_birth = jsonResult.getString("birthDate");
                    String gender = jsonResult.getString("gender");
                    String street = jsonResult.getString("street");
                    String number = jsonResult.getString("number");
                    String neighborhood = jsonResult.getString("neighborhood");
                    String city = jsonResult.getString("city");
                    String state = jsonResult.getString("state");
                    String username = jsonResult.getString("username");
                    String password = jsonResult.getString("password");
                    String photo = jsonResult.getString("photo");
                    try {
                        User user = new User(name, date_of_birth, gender, street, number,
                                neighborhood, city, state, username, password, photo);
                        userList.add(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 103;
                    handler.sendMessage(message);
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

        return userList;
    }

}