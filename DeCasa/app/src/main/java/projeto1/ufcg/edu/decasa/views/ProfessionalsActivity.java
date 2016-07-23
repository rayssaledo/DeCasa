package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.adapters.ProfessionalsAdapter;
import projeto1.ufcg.edu.decasa.controllers.ProfessionalController;
import projeto1.ufcg.edu.decasa.models.Professional;

public class ProfessionalsActivity extends AppCompatActivity {

    private ListView listViewProfessionals;
    private List<Professional> listProfessionals;
    private ProfessionalsAdapter professionalsAdapter;
    private ProfessionalController professionalController;
    private String service;
    public static View mLoading;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                listViewProfessionals.setAdapter(professionalsAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professionals);

        //mLoading = findViewById(R.id.rl_loading);

        listViewProfessionals = (ListView) findViewById(R.id.lv_professionals);
        Intent it = getIntent();
        service = (String) it.getSerializableExtra("SERVICE");
        setTitle(service);

        professionalController = new ProfessionalController(ProfessionalsActivity.this);

        setList(service);

        listViewProfessionals.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Professional professional = (Professional) professionalsAdapter.getItem(position);
                Intent intent = new Intent(ProfessionalsActivity.this,
                        ProfileProfessionalActivity.class);
                intent.putExtra("PROFESSIONAL", professional);
                startActivity(intent);
            }
        });
    }

    public void setList(String service){
        listProfessionals = new ArrayList<>();
        if (service.equals(getApplication().getString(R.string.title_electricians))){
            listProfessionals = professionalController.getProfessionalsByService("Eletricista", handler);
        } else  if (service.equals(getApplication().getString(R.string.title_plumbers))){
            listProfessionals = professionalController.getProfessionalsByService("Encanador", handler);
        } else {
            listProfessionals = professionalController.getProfessionalsByService("Montador", handler);
        }
        professionalsAdapter = new ProfessionalsAdapter(ProfessionalsActivity.this, listProfessionals);
        listViewProfessionals.setAdapter(professionalsAdapter);
    }
}