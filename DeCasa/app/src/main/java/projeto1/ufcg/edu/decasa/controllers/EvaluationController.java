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

    public void addEvaluation(final String emailProfessional, final String emailUser,
                              final String evaluation, final String comment,
                              final String date, final String userPhoto, final String service,
                              final Class classDest, final Professional professional) {

        EvaluationProfessionalActivity.mLoadingEvaluation.setVisibility(View.VISIBLE);
        String urlAddEvaluation = url + "add-evaluation";
        JSONObject json = new JSONObject();
        try {
            json.put("emailProfessional", emailProfessional);
            json.put("emailUser", emailUser);
            json.put("evaluation", evaluation);
            json.put("comment", comment);
            json.put("date", date);
            json.put("userPhoto", userPhoto);
            json.put("service", service);
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

    public List<Evaluation> getAssessmentsByProfessional(final String professionalEmail,
                                                         final Handler handler) {

        final List<Evaluation> assessmentsList = new ArrayList<Evaluation>();
        String urlAssessmentsByProfessional = url + "get-evaluations-professional?email=" +
                professionalEmail;
        mHttp.get(urlAssessmentsByProfessional, new HttpListener() {
            @Override
            public void onSucess(JSONObject response) throws JSONException {
                if (response.getInt("ok") == 1) {
                    JSONArray result = response.getJSONArray("result");
                    JSONObject jsonObject = result.getJSONObject(0);
                    JSONArray jsonArray = jsonObject.getJSONArray("evaluations");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonEvaluation = jsonArray.getJSONObject(i);
                        String usernameValuer = jsonEvaluation.getString("emailValuer");
                        String evaluationValue = jsonEvaluation.getString("evaluation");
                        String comment = jsonEvaluation.getString("comment");
                        String date = jsonEvaluation.getString("date");
                        String photo = jsonEvaluation.getString("userPhoto");
                        try{
                            float evaluationValueFloat = Float.valueOf(evaluationValue);
                            Evaluation evaluation = new Evaluation(professionalEmail,
                                    usernameValuer, evaluationValueFloat, comment, date, photo);
                            assessmentsList.add(evaluation);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Message message = new Message();
                    message.what = 101;
                    handler.sendMessage(message);
                } else {

                }
                Message message = new Message();
                message.what = 101;
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
                                mActivity.finish();
                            }
                        })
                        .create()
                        .show();
            }
        });

        return assessmentsList;
    }

    public List<Float> getAssessmentsAverageByProfessional(final String professionalEmail,
                                                           final Handler handler) {
        final List<Float> assessmentsAverageList = new ArrayList<Float>();

        String urlAssessmentsAverageByProfessional = url + "/get-avg-professional?email=" +
                professionalEmail;
        mHttp.get(urlAssessmentsAverageByProfessional, new HttpListener() {
            @Override
            public void onSucess(JSONObject response) throws JSONException {
                if (response.getInt("ok") == 1) {
                    JSONArray result = response.getJSONArray("result");
                    JSONObject avg = result.getJSONObject(0);
                    try{
                        float assessmentsAverageFloat = Float.parseFloat(avg.getString("avg"));
                        assessmentsAverageList.add(assessmentsAverageFloat);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 104;
                    handler.sendMessage(message);
                } else {

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