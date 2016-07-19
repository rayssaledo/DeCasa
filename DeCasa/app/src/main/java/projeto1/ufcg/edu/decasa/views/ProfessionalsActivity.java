package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

        listViewProfessionals = (ListView) findViewById(R.id.lv_professionals);
        Intent it = getIntent();
        service = (String) it.getSerializableExtra("SERVICE");
        setTitle(service);

        professionalController = new ProfessionalController(ProfessionalsActivity.this);

        setList(service);

    }

    void setList(String service){
        listProfessionals = new ArrayList<>();
        if (service.equals("Eletricistas")){
            listProfessionals = professionalController.getProfessionalsByService("Eletricista", handler);
        } else  if (service.equals("Encanadores")){
            listProfessionals = professionalController.getProfessionalsByService("Encanador", handler);
        } else {
            listProfessionals = professionalController.getProfessionalsByService("Montador", handler);
        }
        professionalsAdapter = new ProfessionalsAdapter(ProfessionalsActivity.this, listProfessionals);
        listViewProfessionals.setAdapter(professionalsAdapter);
    }


}