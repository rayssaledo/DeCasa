package projeto1.ufcg.edu.decasa.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.HttpListener;
import projeto1.ufcg.edu.decasa.utils.HttpUtils;
import projeto1.ufcg.edu.decasa.views.EvaluationProfessionalActivity;

public class EvaluationController {

    private HttpUtils mHttp;
    private Activity mActivity;
    private String url;

    public EvaluationController (Activity activity) {
        mActivity = activity;
        mHttp = new HttpUtils(mActivity);
        url = "http://decasa-decasa.rhcloud.com/";
    }

    public void addEvaluation(final String professionalValued, final String usernameValuer,
                              final String evaluationValue, final String comment,
                              final String date, final Class classDest,
                              final Professional professional) {

        EvaluationProfessionalActivity.mLoadingEvaluation.setVisibility(View.VISIBLE);
        String urlAddEvaluation = url + "/add-avaliacao";
        JSONObject json = new JSONObject();
        try {
            json.put("emailProfissional", professionalValued);
            json.put("emailUsuario", usernameValuer);
            json.put("avaliacao", evaluationValue);
            json.put("comentario", comment);
            json.put("data", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(urlAddEvaluation, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) throws JSONException{
                if (result.getInt("ok") == 0) {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("Erro")
                            .setMessage(result.getString("msg")) //TODO internacionalizar
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    EvaluationProfessionalActivity.mLoadingEvaluation.
                                            setVisibility(View.VISIBLE);
                                }
                            })
                            .create()
                            .show();
                } else {
                    new AlertDialog.Builder(mActivity)
                            .setMessage(mActivity.getString(R.string.
                                    text_successfully_completed_evaluation))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(mActivity,
                                            classDest);
                                    intent.putExtra("PROFESSIONAL", professional);
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
                        .setMessage(mActivity.getString(R.string.err_unavailable_connection))
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EvaluationProfessionalActivity.mLoadingEvaluation.
                                        setVisibility(View.VISIBLE);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}
