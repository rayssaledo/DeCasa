package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.EvaluationController;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class EvaluationProfessionalActivity extends AppCompatActivity {

    private EvaluationController evaluationController;
    private MySharedPreferences mySharedPreferences;

    private Professional professional;

    private RatingBar rbEvaluation;
    private EditText edComment;

    public static View mLoadingEvaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_professional);

        mLoadingEvaluation =  findViewById(R.id.loadingEvaluation);

        evaluationController = new EvaluationController(EvaluationProfessionalActivity.this);
        mySharedPreferences = new MySharedPreferences(getApplicationContext());

        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONAL");

        rbEvaluation = (RatingBar) findViewById(R.id.rb_evaluation);
        edComment = (EditText) findViewById(R.id.ed_comment);

        Button btn_evaluate = (Button) findViewById(R.id.btn_evaluate);
        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvaluation();
            }
        });


    }

    private void addEvaluation() {
        HashMap<String, String> userDetails = mySharedPreferences.getUserDetails();
        String username = userDetails.get(MySharedPreferences.KEY_USERNAME_USER);
        float evaluationValue = rbEvaluation.getRating();
        String stringEvaluationValue = String.valueOf(evaluationValue);
        String comment = edComment.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String stringEvaluationDate = dateFormat.format(date);
        evaluationController.addEvaluation(professional.getEmail(), username, stringEvaluationValue,
                comment, stringEvaluationDate, AssessmentsActivity.class, professional);
    }
}
