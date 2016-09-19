package projeto1.ufcg.edu.decasa.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.models.User;
import projeto1.ufcg.edu.decasa.utils.HttpListener;
import projeto1.ufcg.edu.decasa.utils.HttpUtils;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;
import projeto1.ufcg.edu.decasa.views.EditUserProfileActivity;
import projeto1.ufcg.edu.decasa.views.LoginActivity;
import projeto1.ufcg.edu.decasa.views.MainActivity;
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

    public void update(final String name, final String birthDate, final String gender,
                         final String street, final String number, final String neighborhood,
                         final String city, final String state, final String photo,
                         final String username, final String password, final Class classDest) {

        EditUserProfileActivity.loadingEdit.setVisibility(View.VISIBLE);
        String urlUpdate = url + "update-user";
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
        mHttp.post(urlUpdate, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) throws JSONException{
                if (result.getInt("ok") == 0) {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("Erro")
                            .setMessage(result.getString("msg")) //TODO internacionalizar
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditUserProfileActivity.loadingEdit.setVisibility(View.GONE);
                                }
                            })
                            .create()
                            .show();
                } else {
                    new AlertDialog.Builder(mActivity)
                            .setMessage("Atualizado com sucesso!") //TODO internacionalizar
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EditUserProfileActivity.loadingEdit.setVisibility(View.GONE);
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
                                EditUserProfileActivity.loadingEdit.setVisibility(View.GONE);
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
                        MainActivity.mMainActivity.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(mActivity)
                        .setTitle("Erro")
                        .setMessage(mActivity.getString(R.string.err_unavailable_connection))
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


    public List<Integer> addFavorite(final String username,final String email, final String name,
                                     final String cpf, final String phone, final String street,
                                     final String number, final String neighborhood,
                                     final String city, final String state, final String site,
                                     final String socialNetwork, final String avg,
                                     final String service, final Handler handler) {

        final List<Integer> listIsFavorite = new ArrayList<>();
        String urlAddFavorite = url + "add-favorite";
        final JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("emailProfessional", email);
            json.put("nameProfessional", name);
            json.put("cpfProfessional", cpf);
            json.put("phoneProfessional", phone);
            json.put("streetProfessional", street);
            json.put("numberProfessional", number);
            json.put("neighborhoodProfessional", neighborhood);
            json.put("cityProfessional", city);
            json.put("stateProfessional", state);
            json.put("siteProfessional", site);
            json.put("socialNetworkProfessional", socialNetwork);
            json.put("serviceProfessional", service);
            json.put("avgProfessional", avg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(urlAddFavorite, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) {
                try {
                    if (result.getInt("ok") == 0) {
                        new AlertDialog.Builder(mActivity)
                                .setTitle("Erro")
                                .setMessage(result.getString("msg")) //TODO internacionalizar
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .create()
                                .show();

                    } else {
                        listIsFavorite.add(1);
                        Message message = new Message();
                        message.what = 105;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(mActivity)
                        .setTitle("Erro")
                        .setMessage(mActivity.getString(R.string.err_unavailable_connection))
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
        return  listIsFavorite;
    }

    public void removeFavorite(final String username, final String emailProfessional,
                               final String service, final Handler handler) {

        String urlRemoveFavorite = url + "remove-favorite";
        final JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("emailProfessional", emailProfessional);
            json.put("serviceProfessional", service);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(urlRemoveFavorite, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) {
                try {
                    if (result.getInt("ok") == 0) {
                        new AlertDialog.Builder(mActivity)
                                .setTitle("Erro")
                                .setMessage(result.getString("msg")) //TODO internacionalizar
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .show();

                    } else {
                        Message message = new Message();
                        message.what = 107;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(mActivity)
                        .setTitle("Erro")
                        .setMessage(mActivity.getString(R.string.err_unavailable_connection))
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

    public List<Integer> getIsFavorite(final String username, final String email_professional,
                                       final String service, final Handler handler){
        final List<Integer> listIsFavorite = new ArrayList<>();
        String urlGetUser = url + "get-is-favorite?username=" + username + "&emailProfessional=" +
                email_professional + "&serviceProfessional=" + service ;
        mHttp.get(urlGetUser, new HttpListener() {
            @Override
            public void onSucess(JSONObject result) throws JSONException {
                if (result.getInt("ok") == 1) {
                    listIsFavorite.add(1);
                } else {
                    listIsFavorite.add(0);
                }
                Message message = new Message();
                message.what = 106;
                handler.sendMessage(message);
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(mActivity)
                        .setTitle("Erro")
                        .setMessage(mActivity.getString(R.string.err_unavailable_connection))
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }
        });

        return listIsFavorite;
    }

    public List<Professional> getFavoritesUserByService(final String username, final String service,
                                                        final Handler handler) {
        final List<Professional> professionals = new ArrayList<>();
        String urlFavoritesUserByService = url + "get-favorites-user-by-service?username=" +
                username + "&service=" + service ;

        Log.d("FavoritesTest", "Entrou no método");
        mHttp.get(urlFavoritesUserByService, new HttpListener() {
            @Override
            public void onSucess(JSONObject response) throws JSONException {
                if (response.getInt("ok") == 1) {
                    Log.d("FavoritesTest", "Entrou no IF");
                    JSONArray jsonArray = response.getJSONArray("result");
                    Log.d("FavoritesTest", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonFavorites = jsonArray.getJSONObject(i);
                        String favorites = jsonFavorites.getString("list");
                        JSONArray jsonArrayFavorites = new JSONArray(favorites);
                        for (int j = 0; j < jsonArrayFavorites.length(); j++) {
                            JSONObject jsonFavorite = jsonArrayFavorites.getJSONObject(j);
                            Log.d("FavoritesTest", jsonFavorite.toString());
                            String name = jsonFavorite.getString("name");
                            String cpf = jsonFavorite.getString("cpf");
                            String phone = jsonFavorite.getString("phone");
                            String street = jsonFavorite.getString("street");
                            String number = jsonFavorite.getString("number");
                            String neighborhood = jsonFavorite.getString("neighborhood");
                            String city = jsonFavorite.getString("city");
                            String state = jsonFavorite.getString("state");
                            String site = jsonFavorite.getString("site");
                            String socialNetwork = jsonFavorite.getString("socialNetwork");
                            String service = jsonFavorite.getString("service");
                            String email = jsonFavorite.getString("email");
                            float avg = Float.valueOf(jsonFavorite.getString("avg"));
                            try {
                                Professional professional = new Professional(name, cpf, phone,
                                        street, number, neighborhood, city, state, site,
                                        socialNetwork,service, email, avg);
                                professional.setLocation(new Location(street + ", " + number + " " +
                                        city + " " + state));
                                professionals.add(professional);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    Message message = new Message();
                    message.what = 108;
                    handler.sendMessage(message);
                } else {
                    Log.d("FavoritesTest", "Entrou no ELSE");
                    new AlertDialog.Builder(mActivity)
                            .setTitle("Erro")
                            .setMessage(response.getString("msg")) //TODO internacionalizar
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

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
                        .setMessage(mActivity.getString(R.string.err_unavailable_connection))
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }
        });

        return professionals;
    }

//    public List<Professional> getFavoritesPlumbersUser(final String login, final Handler handler){
//        final List<Professional> professionals = new ArrayList<>();
//        String urlGetFavorites = "http://decasa-decasa.rhcloud.com/get-favoritesPlumbers-user?username=" + login;
//        mHttp.get(urlGetFavorites, new HttpListener() {
//            @Override
//            public void onSucess(JSONObject response) throws JSONException {
//                if (response.getInt("ok") == 1) {
//                  JSONArray  jsonArray = response.getJSONArray("result");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonFavorites = jsonArray.getJSONObject(i);
//                        String favorites = jsonFavorites.getString("favoritesPlumbers");
//                        JSONArray jsonArrayFavorites = new JSONArray(favorites);
//                        for (int j = 0; j < jsonArrayFavorites.length(); j++) {
//                            JSONObject jsonFavorite = jsonArrayFavorites.getJSONObject(j);
//                            String name = jsonFavorite.getString("name");
//                            String cpf = jsonFavorite.getString("cpf");
//                            String phone = jsonFavorite.getString("phone");
//                            String street = jsonFavorite.getString("street");
//                            String number = jsonFavorite.getString("number");
//                            String neighborhood = jsonFavorite.getString("neighborhood");
//                            String city = jsonFavorite.getString("city");
//                            String state = jsonFavorite.getString("state");
//                            String site = jsonFavorite.getString("site");
//                            String socialNetwork = jsonFavorite.getString("socialNetwork");
//                            //String pictury = jsonProfessional.getString("pictury");
//                            String email = jsonFavorite.getString("email");
//                            String services = jsonFavorite.getString("services");
//                            services = services.replace("[", "");
//                            services = services.replace("]", "");
//                            services = services.replaceAll("\"", "");
//                            String[] listServices = services.split(",");
//
//                            float evaluationAverage = Float.valueOf(jsonFavorite.getString("avg"));
//                            try {
////                                Professional professional = new Professional(name, cpf, phone,
////                                        street, number, neighborhood, city, state, site, socialNetwork,
////                                        email, listServices);
////                                professional.setEvaluationsAverage(evaluationAverage);
////                                professional.setLocation(new Location(street + ", " + number + " " +
////                                        city + " " + state));
////
////                                professionals.add(professional);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//                    Message message = new Message();
//                    message.what = 108;
//                    handler.sendMessage(message);
//                } else {
//                    new AlertDialog.Builder(mActivity)
//                            .setTitle("Erro")
//                            .setMessage(response.getString("msg")) //TODO internacionalizar
//                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    //  mActivity.mLoadingLogin.setVisibility(View.GONE);
//                                }
//                            })
//                            .create()
//                            .show();
//                }
//
//            }
//
//            @Override
//            public void onTimeout() {
//                new AlertDialog.Builder(mActivity)
//                        .setTitle("Erro")
//                        .setMessage(mActivity.getString(R.string.err_unavailable_connection))
//                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //  mActivity.mLoadingLogin.setVisibility(View.GONE);
//                            }
//                        })
//                        .create()
//                        .show();
//            }
//        });
//
//        return professionals;
//    }
//
//    public List<Professional> getFavoritesFittersUser(final String login, final Handler handler){
//        final List<Professional> professionals = new ArrayList<>();
//        String urlGetFavorites = "http://decasa-decasa.rhcloud.com/get-favoritesFitters-user?username=" + login;
//        mHttp.get(urlGetFavorites, new HttpListener() {
//            @Override
//            public void onSucess(JSONObject response) throws JSONException {
//                if (response.getInt("ok") == 1) {
//                    JSONArray  jsonArray = response.getJSONArray("result");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonFavorites = jsonArray.getJSONObject(i);
//                        String favorites = jsonFavorites.getString("favoritesFitters");
//                        JSONArray jsonArrayFavorites = new JSONArray(favorites);
//                        for (int j = 0; j < jsonArrayFavorites.length(); j++) {
//                            JSONObject jsonFavorite = jsonArrayFavorites.getJSONObject(j);
//                            String name = jsonFavorite.getString("name");
//                            String cpf = jsonFavorite.getString("cpf");
//                            String phone = jsonFavorite.getString("phone");
//                            String street = jsonFavorite.getString("street");
//                            String number = jsonFavorite.getString("number");
//                            String neighborhood = jsonFavorite.getString("neighborhood");
//                            String city = jsonFavorite.getString("city");
//                            String state = jsonFavorite.getString("state");
//                            String site = jsonFavorite.getString("site");
//                            String socialNetwork = jsonFavorite.getString("socialNetwork");
//                            //String pictury = jsonProfessional.getString("pictury");
//                            String email = jsonFavorite.getString("email");
//                            String services = jsonFavorite.getString("services");
//                            services = services.replace("[", "");
//                            services = services.replace("]", "");
//                            services = services.replaceAll("\"", "");
//                            String[] listServices = services.split(",");
//
//                            float evaluationAverage = Float.valueOf(jsonFavorite.getString("avg"));
//                            try {
////                                Professional professional = new Professional(name, cpf, phone,
////                                        street, number, neighborhood, city, state, site, socialNetwork,
////                                        email, listServices);
////                                professional.setEvaluationsAverage(evaluationAverage);
////                                professional.setLocation(new Location(street + ", " + number + " " +
////                                        city + " " + state));
////
////                                professionals.add(professional);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                    Message message = new Message();
//                    message.what = 108;
//                    handler.sendMessage(message);
//
//                } else {
//                    new AlertDialog.Builder(mActivity)
//                            .setTitle("Erro")
//                            .setMessage(response.getString("msg")) //TODO internacionalizar
//                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    //  mActivity.mLoadingLogin.setVisibility(View.GONE);
//                                }
//                            })
//                            .create()
//                            .show();
//                }
//
//            }
//
//            @Override
//            public void onTimeout() {
//                new AlertDialog.Builder(mActivity)
//                        .setTitle("Erro")
//                        .setMessage(mActivity.getString(R.string.err_unavailable_connection))
//                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //  mActivity.mLoadingLogin.setVisibility(View.GONE);
//                            }
//                        })
//                        .create()
//                        .show();
//            }
//        });
//
//        return professionals;
//    }
//
//    public List<Professional> getFavoritesElectriciansUser(final String login, final Handler handler){
//        final List<Professional> professionals = new ArrayList<>();
//        String urlGetFavorites = "http://decasa-decasa.rhcloud.com/get-favoritesElectricians-user?username=" + login;
//        mHttp.get(urlGetFavorites, new HttpListener() {
//            @Override
//            public void onSucess(JSONObject response) throws JSONException {
//                if (response.getInt("ok") == 1) {
//                    JSONArray  jsonArray = response.getJSONArray("result");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonFavorites = jsonArray.getJSONObject(i);
//                        String favorites = jsonFavorites.getString("favoritesElectricians");
//                        JSONArray jsonArrayFavorites = new JSONArray(favorites);
//                        for (int j = 0; j < jsonArrayFavorites.length(); j++) {
//                            JSONObject jsonFavorite = jsonArrayFavorites.getJSONObject(j);
//                            String name = jsonFavorite.getString("name");
//                            String cpf = jsonFavorite.getString("cpf");
//                            String phone = jsonFavorite.getString("phone");
//                            String street = jsonFavorite.getString("street");
//                            String number = jsonFavorite.getString("number");
//                            String neighborhood = jsonFavorite.getString("neighborhood");
//                            String city = jsonFavorite.getString("city");
//                            String state = jsonFavorite.getString("state");
//                            String site = jsonFavorite.getString("site");
//                            String socialNetwork = jsonFavorite.getString("socialNetwork");
//                            //String pictury = jsonProfessional.getString("pictury");
//                            String email = jsonFavorite.getString("email");
//                            String services = jsonFavorite.getString("services");
//                            services = services.replace("[", "");
//                            services = services.replace("]", "");
//                            services = services.replaceAll("\"", "");
//                            String[] listServices = services.split(",");
//
//                            float evaluationAverage = Float.valueOf(jsonFavorite.getString("avg"));
//                            try {
////                                Professional professional = new Professional(name, cpf, phone,
////                                        street, number, neighborhood, city, state, site, socialNetwork,
////                                        email, listServices);
////                                professional.setEvaluationsAverage(evaluationAverage);
////                                professional.setLocation(new Location(street + ", " + number + " " +
////                                        city + " " + state));
////
////                                professionals.add(professional);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                        Message message = new Message();
//                        message.what = 108;
//                        handler.sendMessage(message);
//                    }
//
//                } else {
//                    new AlertDialog.Builder(mActivity)
//                            .setTitle("Erro")
//                            .setMessage(response.getString("msg")) //TODO internacionalizar
//                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    //  mActivity.mLoadingLogin.setVisibility(View.GONE);
//                                }
//                            })
//                            .create()
//                            .show();
//                }
//
//            }
//
//            @Override
//            public void onTimeout() {
//                new AlertDialog.Builder(mActivity)
//                        .setTitle("Erro")
//                        .setMessage(mActivity.getString(R.string.err_unavailable_connection))
//                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //  mActivity.mLoadingLogin.setVisibility(View.GONE);
//                            }
//                        })
//                        .create()
//                        .show();
//            }
//        });
//
//        return professionals;
//    }


    public List<User> getUser(final String login, final Handler handler){
        //UserProfileActivity.loadingUserProfile.setVisibility(View.VISIBLE);
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
                            .setMessage(result.getString("msg")) //TODO internacionalizar com mensagem certa
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //UserProfileActivity.loadingUserProfile.setVisibility(View.GONE);
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
                        .setMessage(mActivity.getString(R.string.err_unavailable_connection))
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //UserProfileActivity.loadingUserProfile.setVisibility(View.GONE);
                            }
                        })
                        .create()
                        .show();
            }
        });

        return userList;
    }

}