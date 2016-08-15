package projeto1.ufcg.edu.decasa.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.models.Evaluation;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.HttpListener;
import projeto1.ufcg.edu.decasa.utils.HttpUtils;
import projeto1.ufcg.edu.decasa.views.AssessmentsActivity;
import projeto1.ufcg.edu.decasa.views.EvaluationProfessionalActivity;
import projeto1.ufcg.edu.decasa.views.ProfileProfessionalActivity;

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
                              final String date, final String photo, final Class classDest,
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
            json.put("fotoUsuario", photo);
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
                                        setVisibility(View.GONE);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    public List<Evaluation> getEvaluationsByProfessional(final String professionalEmail,
                                                         final Handler handler) {


        final List<Evaluation> evaluationsList = new ArrayList<Evaluation>();
        String urlEvaluationsByProfessional = url + "/get-avaliacoes-profissional?email=" +
                professionalEmail;
        mHttp.get(urlEvaluationsByProfessional, new HttpListener() {
            @Override
            public void onSucess(JSONObject response) throws JSONException {
                if (response.getInt("ok") == 1) {
                    JSONArray jsonResult = response.getJSONArray("result");
                    JSONObject jsonObjectEvaluation = jsonResult.getJSONObject(0);
                    //Recuperando a lista de avaliações
                    JSONArray jsonArrayEvaluation = jsonObjectEvaluation.getJSONArray("avaliacoes");
                    for (int i = 0; i < jsonArrayEvaluation.length(); i++) {
                        JSONObject jsonEvaluation = jsonArrayEvaluation.getJSONObject(i);
                        String usernameValuer = jsonEvaluation.getString("emailAvaliador");
                        String evaluationValue = jsonEvaluation.getString("avaliacao");
                        String comment = jsonEvaluation.getString("comentario");
                        String date = jsonEvaluation.getString("data");
                        String photo = jsonEvaluation.getString("fotoUsuario");
                        try{
                            float evaluationValueFloat = Float.valueOf(evaluationValue);
                            Evaluation evaluation = new Evaluation(professionalEmail,
                                    usernameValuer, evaluationValueFloat, comment, date, photo);
                            evaluationsList.add(evaluation);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Message message = new Message();
                    message.what = 101;
                    handler.sendMessage(message);
                    if (AssessmentsActivity.mLoadingAssessments != null) {
                        AssessmentsActivity.mLoadingAssessments.setVisibility(View.GONE);
                    }
                } else {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("Erro")
                            .setMessage("") //TODO internacionalizar com mensagem certa
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (AssessmentsActivity.mLoadingAssessments != null) {
                                        AssessmentsActivity.mLoadingAssessments.
                                                setVisibility(View.GONE);
                                    }
                                }
                            })
                            .create()
                            .show();
                }
                if (AssessmentsActivity.mLoadingAssessments != null) {
                    AssessmentsActivity.mLoadingAssessments.setVisibility(View.GONE);
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
                                if (AssessmentsActivity.mLoadingAssessments != null) {
                                    AssessmentsActivity.mLoadingAssessments.
                                            setVisibility(View.GONE);
                                }
                                mActivity.finish();
                            }
                        })
                        .create()
                        .show();
            }
        });

        return evaluationsList;
    }

    public List<Float> getAssessmentsAverageByProfessional(final String professionalEmail,
                                                    final Handler handler) {
        final List<Float> assessmentsAverageList = new ArrayList<Float>();

        String urlEvaluationsByProfessional = url + "/get-avaliacoes-profissional?email=" +
                professionalEmail;
        mHttp.get(urlEvaluationsByProfessional, new HttpListener() {
            @Override
            public void onSucess(JSONObject response) throws JSONException {
                if (response.getInt("ok") == 1) {
                    JSONArray jsonResult = response.getJSONArray("result");
                    JSONObject jsonObjectEvaluation = jsonResult.getJSONObject(0);
                    try{
                        float assessmentsAverageFloat = Float.valueOf(jsonObjectEvaluation.
                                getString("avg"));
                        assessmentsAverageList.add(assessmentsAverageFloat);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 104;
                    handler.sendMessage(message);
                } else {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("Erro")
                            .setMessage("") //TODO internacionalizar com mensagem certa
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (AssessmentsActivity.mLoadingAssessments != null) {
                                        AssessmentsActivity.mLoadingAssessments.
                                                setVisibility(View.GONE);
                                    }
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
                                mActivity.finish();
                            }
                        })
                        .create()
                        .show();
            }
        });

        return assessmentsAverageList;
    }
}