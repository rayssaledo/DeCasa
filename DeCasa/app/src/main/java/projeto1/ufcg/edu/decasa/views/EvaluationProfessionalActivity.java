package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.EvaluationController;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.Evaluation;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.models.User;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class EvaluationProfessionalActivity extends AppCompatActivity {

    private EvaluationController evaluationController;
    private UserController userController;
    private MySharedPreferences mySharedPreferences;

    private Professional professional;
    private String professionalEmail;

    private RatingBar rbEvaluation;
    private EditText edComment;

    private List<User> userList;
    private User user;

    private String username;
    private String photoUser;

    public static View mLoadingEvaluation;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 103) {
                user = userList.get(0);
                photoUser = user.getPhoto();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_professional);

        mLoadingEvaluation =  findViewById(R.id.loadingEvaluation);

        evaluationController = new EvaluationController(EvaluationProfessionalActivity.this);
        userController = new UserController(EvaluationProfessionalActivity.this);
        mySharedPreferences = new MySharedPreferences(getApplicationContext());

        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONAL");

        if (professional != null) {
            professionalEmail = professional.getEmail();
        }

        rbEvaluation = (RatingBar) findViewById(R.id.rb_evaluation);
        edComment = (EditText) findViewById(R.id.ed_comment);

        Button btn_evaluate = (Button) findViewById(R.id.btn_evaluate);
        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvaluation();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setFields();
    }

    private void setFields() {
        Intent it = getIntent();
        Evaluation evaluation = it.getParcelableExtra("EVALUATION");
        if (evaluation != null) {
            professionalEmail = evaluation.getProfessionalValued();
            rbEvaluation.setRating(evaluation.getEvaluationValue());
            edComment.setText(evaluation.getComment());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, String> userDetails = mySharedPreferences.getUserDetails();
        username = userDetails.get(MySharedPreferences.KEY_USERNAME_USER);
        userList = userController.getUser(username, handler);
    }

    private void addEvaluation() {
        float evaluationValue = rbEvaluation.getRating();
        String stringEvaluationValue = String.valueOf(evaluationValue);
        String comment = edComment.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String stringEvaluationDate = dateFormat.format(date);
        String service = mySharedPreferences.getService();
        if (service.equals(getApplication().getString(R.string.title_electricians))){
            service = "Eletricista";
        } else  if (service.equals(getApplication().getString(R.string.title_plumbers))){
            service = "Encanador";
        } else {
            service = "Montador";
        }
        evaluationController.addEvaluation(professionalEmail, username, stringEvaluationValue,
                comment, stringEvaluationDate, photoUser, service,
                AssessmentsActivity.class, professional);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
