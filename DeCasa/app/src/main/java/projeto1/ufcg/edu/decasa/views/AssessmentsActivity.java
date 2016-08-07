package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.EvaluationController;
import projeto1.ufcg.edu.decasa.models.Professional;

public class AssessmentsActivity extends AppCompatActivity {

    private EvaluationController evaluationController;
    private Professional professional;

    public static View mLoadingAssessments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);

        mLoadingAssessments =  findViewById(R.id.loadingAssessments);

        evaluationController = new EvaluationController(AssessmentsActivity.this);

        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONAL");

        evaluationController.getEvaluationsByProfessional(professional.getEmail());

        Button btn_to_evaluate = (Button) findViewById(R.id.btn_to_evaluate);
        btn_to_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentsActivity.this,
                        EvaluationProfessionalActivity.class);
                intent.putExtra("PROFESSIONAL", professional);
                startActivity(intent);
            }
        });
    }
}