package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.EvaluationController;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.Evaluation;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class ProfessionalProfileFreePlanActivity extends AppCompatActivity {

    private Professional professional;
    private MySharedPreferences mySharedPreferences;
    private CircularImageView iv_professional;
    private TextView tv_professional_name;
    private RatingBar rb_evaluation;
    private TextView tv_service;
    private TextView tv_description;
    private TextView tv_phone_professional;
    private  ImageButton ib_favorite;
    private Button btn_evaluations;

    private String service;
    private List<Evaluation> assessments;
    private List<Float> assessmentsAverage;
    private  List<Integer> list_favorite;
    private List<Integer> list_is_favorite;
    private float assessmentsAverageValue;
    private EvaluationController evaluationController;
    private UserController userController;
    private String username;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //int numAssessmentsProfessional = assessments.size();
            if (msg.what == 101) {
                //tv_number_assessments.setText(numAssessmentsProfessional);
            }
            if (msg.what == 104) {
                if (assessmentsAverage != null) {
                    assessmentsAverageValue = assessmentsAverage.get(0);
                    rb_evaluation.setRating(assessmentsAverageValue);
                }
            }
            if (msg.what == 105) {
                if (list_favorite.size() == 1 && list_favorite.get(0) == 1){
                    Toast.makeText(ProfessionalProfileFreePlanActivity.this, getApplication().getString(
                            R.string.add_favorite), Toast.LENGTH_LONG).show();
                    ib_favorite.setImageResource(R.mipmap.ic_favorite_red_24dp);
                }
            }
            if (msg.what == 106) {
                if (list_is_favorite.size() == 1 && list_is_favorite.get(0) == 1){
                    ib_favorite.setImageResource(R.mipmap.ic_favorite_red_24dp);
                } else {
                    ib_favorite.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
                }
            }
            if (msg.what == 107) {
                Toast.makeText(ProfessionalProfileFreePlanActivity.this,getApplication().getString(
                        R.string.remove_favorite), Toast.LENGTH_LONG).show();
                ib_favorite.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_profile_free_plan);

        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONAL");

        mySharedPreferences = new MySharedPreferences(getApplicationContext());
        evaluationController = new EvaluationController(ProfessionalProfileFreePlanActivity.this);
        userController = new UserController(ProfessionalProfileFreePlanActivity.this);
        iv_professional = (CircularImageView) findViewById(R.id.iv_professional);
        tv_professional_name = (TextView) findViewById(R.id.tv_professional_name);
        rb_evaluation = (RatingBar) findViewById(R.id.rb_evaluation);
        tv_service = (TextView) findViewById(R.id.tv_service);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_phone_professional = (TextView) findViewById(R.id.tv_phone_professional);

        username = mySharedPreferences.getUserLogged();
        setTitle(professional.getName());
        setProfile();

        service = mySharedPreferences.getService();
        if (service.equals(getApplication().getString(R.string.title_electricians))){
            service = "Electrician";
        } else if (service.equals(getApplication().getString(R.string.title_plumbers))){
            service = "Plumber";
        } else if (service.equals(getApplication().getString(R.string.title_fitters))){
            service = "Fitter";
        }

        list_is_favorite = new ArrayList<>();
        list_is_favorite = userController.getIsFavorite(username, professional.getEmail(), service, handler);

        btn_evaluations = (Button) findViewById(R.id.btn_evaluations);
        btn_evaluations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfessionalProfileFreePlanActivity.this,
                        AssessmentsActivity.class);
                intent.putExtra("PROFESSIONALPLANFREE", professional);
                startActivity(intent);
            }
        });

        ImageButton ib_call = (ImageButton) findViewById(R.id.ib_phone_professional);
        ib_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String phone = professional.getPhone1().substring(1,3) + professional.getPhone1().
//                        substring(4,9) + professional.getPhone1().substring(9);
//                Uri uri = Uri.parse("tel:" + phone);
//                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
//                startActivity(intent);
            }
        });

        ib_favorite = (ImageButton) findViewById(R.id.ib_favorite);
        ib_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //incluir a ação
            }
        });

    }

    private void setProfile() {
        tv_professional_name.setText(professional.getName());
//        tv_phone_professional.setText(professional.getPhone1());
//        tv_service.setText(professional.getService());
//        rb_evaluation.setRating(professional.getAvg());
        //falta setar a description
    }

    @Override
    protected void onResume() {
        super.onResume();
        String service = mySharedPreferences.getService();
        if (service.equals(getApplication().getString(R.string.title_electricians))){
            service = "Eletricista";
        } else  if (service.equals(getApplication().getString(R.string.title_plumbers))){
            service = "Encanador";
        } else {
            service = "Montador";
        }
//        assessments = evaluationController.getAssessmentsByProfessional(professional.getEmail(),
//                service, handler);
        assessmentsAverage = evaluationController.getAssessmentsAverageByProfessional(professional.
                getEmail(), handler);

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
