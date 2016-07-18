package projeto1.ufcg.edu.decasa.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import projeto1.ufcg.edu.decasa.R;

public class ProfessionalsActivity extends AppCompatActivity {

    private ListView listViewProfessionals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professionals);

        listViewProfessionals = (ListView) findViewById(R.id.lv_professionals);
        listViewProfessionals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

}
