package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professionals);

        listViewProfessionals = (ListView) findViewById(R.id.lv_professionals);

        //Intent it = getIntent();
        //service = (String) it.getSerializableExtra("SERVICE");
        //setTitle(service);
        //Log.d("SERVICE", " "+ service);

        professionalController = new ProfessionalController(ProfessionalsActivity.this);

        if(MainActivity.mFitter){
            Log.d("TESTE", "Passou aqui");
            listProfessionals = professionalController.getProfessionalsByService("Montador");
        }

        if(listProfessionals.size() > 0) {
            professionalsAdapter = new ProfessionalsAdapter(ProfessionalsActivity.this, listProfessionals);
            listViewProfessionals.setAdapter(professionalsAdapter);
        }

        listViewProfessionals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

}