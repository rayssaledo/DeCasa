package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.EvaluationController;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.Evaluation;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.DownloadFile;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class ProfessionalProfileGoldPlanActivity extends AppCompatActivity {

    private EvaluationController evaluationController;
    private Professional professional;
    private TextView tv_professional_name;
    private TextView tv_service;
    private TextView tv_description;
    private TextView tv_address_professional;
    private TextView tv_phone_professional;
    private TextView tv_social_network;
    private TextView tv_website;
    private TextView tv_number_assessments;
    private RatingBar rb_evaluation;
    private List<Evaluation> assessments;
    private List<Float> assessmentsAverage;
    private UserController userController;
    private  List<Integer> listFavorite;
    private String username;
    private  ImageButton ib_favorite;
    private List<Integer> listIsFavorite;
    private CircularImageView iv_professional;

    public static View mLoadingProfileProfessional;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int numAssessmentsProfessional = assessments.size();
            if (msg.what == 101) {
                tv_number_assessments.setText(String.valueOf(numAssessmentsProfessional));
            }
            if (msg.what == 104) {
                if (assessmentsAverage != null) {
                    float assessmentsAverageValue = assessmentsAverage.get(0);
                    rb_evaluation.setRating(assessmentsAverageValue);
                }
            }
            if (msg.what == 105) {
                if (listFavorite.size() == 1 && listFavorite.get(0) == 1){
                    Toast.makeText(ProfessionalProfileGoldPlanActivity.this, getApplication().getString(
                            R.string.add_favorite),
                            Toast.LENGTH_LONG).show();
                    ib_favorite.setImageResource(R.mipmap.ic_favorite_red_24dp);
                }
            }
            if (msg.what == 106) {
                if (listIsFavorite.size() == 1 && listIsFavorite.get(0) == 1){
                    ib_favorite.setImageResource(R.mipmap.ic_favorite_red_24dp);
                } else {
                    ib_favorite.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
                }
            }
            if (msg.what == 107) {
                Toast.makeText(ProfessionalProfileGoldPlanActivity.this,getApplication().getString(
                        R.string.remove_favorite),
                        Toast.LENGTH_LONG).show();
                ib_favorite.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_profile_gold_plan);

        mLoadingProfileProfessional =  findViewById(R.id.loadingProfileProfessional);
        MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
        evaluationController = new EvaluationController(ProfessionalProfileGoldPlanActivity.this);
        userController = new UserController(ProfessionalProfileGoldPlanActivity.this);
        tv_professional_name = (TextView) findViewById(R.id.tv_professional_name);
        tv_service = (TextView) findViewById(R.id.tv_service);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_address_professional = (TextView) findViewById(R.id.tv_address_professional);
        tv_phone_professional = (TextView) findViewById(R.id.tv_phone_professional);
        tv_social_network = (TextView) findViewById(R.id.tv_social_network);
        tv_website = (TextView) findViewById(R.id.tv_website);
        rb_evaluation = (RatingBar) findViewById(R.id.rb_evaluation);
        tv_number_assessments = (TextView) findViewById(R.id.tv_number_assessments);
        iv_professional = (CircularImageView) findViewById(R.id.iv_professional);

        username = mySharedPreferences.getUserLogged();
        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONAL");

        listIsFavorite = new ArrayList<>();
        listIsFavorite = userController.getIsFavorite(username, professional.getEmail(),
                professional.getService(), handler);

        ImageButton ib_call = (ImageButton) findViewById(R.id.ib_phone_professional);
        ib_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = professional.getPhone1().substring(1,3) + professional.getPhone1().
                        substring(4,9) + professional.getPhone1().substring(9);
                Uri uri = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });

        ib_favorite = (ImageButton) findViewById(R.id.ib_favorite);
        ib_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(listIsFavorite.size() != 0 && listIsFavorite.get(0) == 0) {
                   listFavorite = userController.addFavorite(username, professional.getName(),
                           professional.getBusinessName(), professional.getCpf(),
                           professional.getPhone1(), professional.getPhone2(),
                           professional.getPhone3(), professional.getPhone4(),
                           professional.getStreet(), professional.getNumber(),
                           professional.getNeighborhood(), professional.getCity(),
                           professional.getState(), professional.getSite(),
                           professional.getSocialNetwork1(), professional.getSocialNetwork2(),
                           professional.getService(), professional.getDescription(),
                           professional.getEmail(), String.valueOf(professional.getAvg()),
                           professional.getPlan(), professional.getPicture(), handler);
                   listIsFavorite.add(0,1);
               } else if (listIsFavorite.size() != 0 && listIsFavorite.get(0) == 1) {
                   userController.removeFavorite(username, professional.getEmail(),
                           professional.getService(), handler);
                   listIsFavorite.add(0,0);
               }
            }
        });

        Button btn_evaluations = (Button) findViewById(R.id.btn_evaluations);
        btn_evaluations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfessionalProfileGoldPlanActivity.this,
                        AssessmentsActivity.class);
                intent.putExtra("PROFESSIONAL", professional);
                startActivity(intent);
            }
        });

        setTitle(professional.getName());

        ImageButton ib_map_professional = (ImageButton) findViewById(R.id.ib_map_professional);
        ib_map_professional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfessionalProfileGoldPlanActivity.this,
                        ProfessionalMapActivity.class);
                intent.putExtra("PROFESSIONALMAP", professional);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfile();
        assessments = evaluationController.getAssessmentsByProfessional(professional.getEmail(),
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
        tv_phone_professional.setText(professional.getPhone1());
        if(professional.getSite() != null && !professional.getSite().equals("")){
            tv_website.setText(professional.getSite());
        } else {
            tv_website.setVisibility(View.INVISIBLE);
        }
        if(professional.getSocialNetwork1() != null &&
                !professional.getSocialNetwork1().equals("")){
            tv_social_network.setText(professional.getSocialNetwork1());
        } else {
            tv_social_network.setVisibility(View.INVISIBLE);
        }
        tv_service.setText(professional.getService());
        tv_description.setText(professional.getDescription());
        rb_evaluation.setRating(professional.getAvg());
        if (professional.getPicture() != null) {
            File f = new File(DownloadFile.getPathDownload() + File.separator +
                    professional.getPicture());
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            iv_professional.setImageBitmap(bmp);
        }
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