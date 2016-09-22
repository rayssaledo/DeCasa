package projeto1.ufcg.edu.decasa.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.adapters.AssessmentsAdapter;
import projeto1.ufcg.edu.decasa.controllers.EvaluationController;
import projeto1.ufcg.edu.decasa.models.Evaluation;
import projeto1.ufcg.edu.decasa.models.Professional;

public class AssessmentsActivity extends AppCompatActivity {

    public static Activity mAssessmentsActivity;
    private EvaluationController evaluationController;
    private Professional professional;
    private ListView listViewAssessments;
    private TextView tv_no_assessments;
    private TextView tv_label_assessments;
    private List<Evaluation> assessments;
    public static Button btn_to_evaluate;

    public static View mLoadingAssessments;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                int numAssessmentsProfessional = assessments.size();
                if (numAssessmentsProfessional == 0) {
                    tv_no_assessments.setVisibility(View.VISIBLE);
                    listViewAssessments.setVisibility(View.GONE);
                } else {
                    tv_no_assessments.setVisibility(View.GONE);
                    listViewAssessments.setVisibility(View.VISIBLE);
                    AssessmentsAdapter assessmentsAdapter =
                            new AssessmentsAdapter(AssessmentsActivity.this, assessments);
                    listViewAssessments.setAdapter(assessmentsAdapter);
                }
                if (numAssessmentsProfessional == 1) {
                    tv_label_assessments.setText(getApplication().getString(R.string.
                            text_number_evaluation) + " (" + numAssessmentsProfessional
                            + ")");
                } else {
                    tv_label_assessments.setText(getApplication().getString(R.string.
                            text_number_assessments) + " (" + numAssessmentsProfessional
                            + ")");
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);
        mAssessmentsActivity = this;
        mLoadingAssessments =  findViewById(R.id.loadingAssessments);
        listViewAssessments = (ListView) findViewById(R.id.lv_assessments);
        tv_no_assessments = (TextView) findViewById(R.id.tv_no_assessments);
        tv_label_assessments = (TextView) findViewById(R.id.tv_label_assessments);

        evaluationController = new EvaluationController(AssessmentsActivity.this);

        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONAL");

        btn_to_evaluate = (Button) findViewById(R.id.btn_to_evaluate);
        btn_to_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentsActivity.this,
                        EvaluationProfessionalActivity.class);
                intent.putExtra("PROFESSIONAL", professional);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_no_assessments.setVisibility(View.GONE);
        listViewAssessments.setVisibility(View.VISIBLE);
        assessments = evaluationController.getAssessmentsByProfessional(professional.getEmail(),
                handler);
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