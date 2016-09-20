package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import projeto1.ufcg.edu.decasa.R;

public class ProfessionalProfileSilverPlanActivity extends AppCompatActivity {

    private CircularImageView iv_professional;
    private TextView tv_professional_name;
    private TextView tv_number_assessments;
    private TextView tv_service;
    private TextView tv_description;
    private TextView tv_address_professional;
    private TextView tv_phone_professional;
    private TextView tv_social_network;
    private TextView tv_website;
    private ImageButton ib_map_professional;
    private ImageButton ib_favorite;
    private Button btn_evaluations;
    private RatingBar rb_evaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_profile_silver_plan);

        iv_professional = (CircularImageView) findViewById(R.id.iv_professional);
        tv_professional_name = (TextView) findViewById(R.id.tv_professional_name);
        rb_evaluation = (RatingBar) findViewById(R.id.rb_evaluation);
        //tv_number_assessments = (TextView) findViewById(R.id.tv_number_assessments);
        tv_service = (TextView) findViewById(R.id.tv_service);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_address_professional = (TextView) findViewById(R.id.tv_address_professional);
        tv_phone_professional = (TextView) findViewById(R.id.tv_phone_professional);
        tv_social_network = (TextView) findViewById(R.id.tv_social_network);
        tv_website = (TextView) findViewById(R.id.tv_website);

        ib_favorite = (ImageButton) findViewById(R.id.ib_favorite);
        ib_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //incluir a ação
            }
        });

        btn_evaluations = (Button) findViewById(R.id.btn_evaluations);
        btn_evaluations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //incluir a ação
            }
        });

        ib_map_professional = (ImageButton) findViewById(R.id.ib_map_professional);
        ib_map_professional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //incluir a ação
            }
        });

        ImageButton ib_call = (ImageButton) findViewById(R.id.ib_phone_professional);
        ib_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //incluir a ação
            }
        });
    }
}
