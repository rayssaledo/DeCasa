package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.EvaluationController;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.Evaluation;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class ProfileProfessionalActivity extends AppCompatActivity {

    private EvaluationController evaluationController;
    private Professional professional;
    private TextView tv_professional_name;
    private TextView tv_services;
    private TextView tv_address_professional;
    private TextView tv_phone_professional;
    private TextView tv_social_network;
    private TextView tv_website;
    private RatingBar rb_evaluation;
    private TextView tv_number_assessments;
    private List<Evaluation> assessments;
    private List<Float> assessmentsAverage;
    private float assessmentsAverageValue;
    private UserController userController;
    private  List<Integer> list_favorite;
    private MySharedPreferences mySharedPreferences;
    private String username;
    private  ImageButton ib_favorite;
    private List<Integer> list_is_favorite;

    public static View mLoadingProfileProfessional;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int numAssessmentsProfessional = assessments.size();
            if (msg.what == 101) {
                if (numAssessmentsProfessional == 1) {
                    tv_number_assessments.setText(numAssessmentsProfessional + " " +
                            getApplication().getString(R.string.text_number_evaluation));
                } else  {
                    tv_number_assessments.setText(numAssessmentsProfessional + " " +
                            getApplication().getString(R.string.text_number_assessments));
                }
            }
            if (msg.what == 104) {
                if (assessmentsAverage != null) {
                    assessmentsAverageValue = assessmentsAverage.get(0);
                    rb_evaluation.setRating(assessmentsAverageValue);
                }
            }
            if (msg.what == 105) {
                if (list_favorite.size() == 1 && list_favorite.get(0) == 1){
                    Toast.makeText(ProfileProfessionalActivity.this, "Adicionado a seus favoritos", Toast.LENGTH_LONG).show();
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
                Toast.makeText(ProfileProfessionalActivity.this, "Removido dos seus favoritos", Toast.LENGTH_LONG).show();
                ib_favorite.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_professional);

        mLoadingProfileProfessional =  findViewById(R.id.loadingProfileProfessional);
        mySharedPreferences = new MySharedPreferences(getApplicationContext());
        evaluationController = new EvaluationController(ProfileProfessionalActivity.this);
        userController = new UserController(ProfileProfessionalActivity.this);
        tv_professional_name = (TextView) findViewById(R.id.tv_professional_name);
        tv_services = (TextView) findViewById(R.id.tv_services);
        tv_address_professional = (TextView) findViewById(R.id.tv_address_professional);
        tv_phone_professional = (TextView) findViewById(R.id.tv_phone_professional);
        tv_social_network = (TextView) findViewById(R.id.tv_social_network);
        tv_website = (TextView) findViewById(R.id.tv_website);
        rb_evaluation = (RatingBar) findViewById(R.id.rb_evaluation);
        tv_number_assessments = (TextView) findViewById(R.id.tv_number_assessments);

        username = mySharedPreferences.getUserLogged();
        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONAL");

        list_is_favorite = new ArrayList<>();
        list_is_favorite = userController.getIsFavorite(username, professional.getEmail(), handler);

        setTitle(professional.getName());
        setProfile();

        ImageButton ib_call = (ImageButton) findViewById(R.id.ib_phone_professional);
        ib_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = professional.getPhone().substring(1,3) + professional.getPhone().
                        substring(4,9) + professional.getPhone().substring(9);
                Uri uri = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });

        ib_favorite = (ImageButton) findViewById(R.id.ib_favorite);
        ib_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(list_is_favorite.get(0) == 0) {
                   list_favorite = userController.addFavorite(username, professional.getEmail(), handler);
                   list_is_favorite.add(0,1);
               } else {
                   userController.removeFavorite(username, professional.getEmail(), handler);
                   list_is_favorite.add(0,0);
               }
            }
        });

        Button btn_evaluations = (Button) findViewById(R.id.btn_evaluations);
        btn_evaluations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileProfessionalActivity.this,
                        AssessmentsActivity.class);
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
        assessments = evaluationController.getEvaluationsByProfessional(professional.getEmail(),
                handler);
        assessmentsAverage = evaluationController.getAssessmentsAverageByProfessional(professional.
                getEmail(), handler);

    }

    private void setProfile() {
        String address = professional.getStreet() + ", " + professional.getNumber() + ", " +
                professional.getNeighborhood()
                + " " + professional.getCity() + " - " + professional.getState();

        tv_professional_name.setText(professional.getName());
        tv_address_professional.setText(address);
        tv_phone_professional.setText(professional.getPhone());
        if(professional.getSite() != null && !professional.getSite().equals("")){
            tv_website.setText(professional.getSite());
        } else {
            tv_website.setVisibility(View.INVISIBLE);
        }
        if(professional.getSocialNetwork() != null && !professional.getSocialNetwork().equals("")){
            tv_social_network.setText(professional.getSocialNetwork());
        } else {
            tv_social_network.setVisibility(View.INVISIBLE);
        }

        String services = "";
        for (int i = 0; i < professional.getServices().length ; i++) {
            services += professional.getServices()[i];
            if(i != professional.getServices().length - 1){
                services += ", ";
            }
        }
        tv_services.setText(services);
        rb_evaluation.setRating(assessmentsAverageValue);
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