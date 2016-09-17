package projeto1.ufcg.edu.decasa.views;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.adapters.ProfessionalsAdapter;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class MyFavoritesActivity extends AppCompatActivity {

    private ListView lv_fitter;
    private ListView lv_plumber;
    private ListView lv_electrician;
    private List<Professional> list_fitter_professionals;
    private List<Professional> list_plumber_professionals;
    private List<Professional> list_electrician_professionals;
    private UserController userController;
    private MySharedPreferences mySharedPreferences;
    private String username;
    private ProfessionalsAdapter adapter_fitter;
    private ProfessionalsAdapter adapter_plumber;
    private ProfessionalsAdapter adapter_electrictian;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 108) {

                adapter_fitter = new ProfessionalsAdapter(MyFavoritesActivity.this,
                        list_fitter_professionals);
                lv_fitter.setAdapter(adapter_fitter);

                adapter_plumber = new ProfessionalsAdapter(MyFavoritesActivity.this,
                        list_plumber_professionals);
                lv_plumber.setAdapter(adapter_plumber);

                adapter_electrictian = new ProfessionalsAdapter(MyFavoritesActivity.this,
                        list_electrician_professionals);
                lv_electrician.setAdapter(adapter_electrictian);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        userController = new UserController(MyFavoritesActivity.this);
        mySharedPreferences = new MySharedPreferences(getApplicationContext());
        username = mySharedPreferences.getUserLogged();

        TabHost mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();

        TabHost.TabSpec descritor = mTabHost.newTabSpec("aba1");
        descritor.setContent(R.id.tab_plumber);
        descritor.setIndicator(getApplication().getString(R.string.title_plumbers));
        mTabHost.addTab(descritor);

        descritor = mTabHost.newTabSpec("aba2");
        descritor.setContent(R.id.tab_electrician);
        descritor.setIndicator(getApplication().getString(R.string.title_electricians));
        mTabHost.addTab(descritor);

        descritor = mTabHost.newTabSpec("aba3");
        descritor.setContent(R.id.tab_fitter);
        descritor.setIndicator(getApplication().getString(R.string.title_fitters));
        mTabHost.addTab(descritor);

        lv_fitter = (ListView) findViewById(R.id.lvf_fitter);
        lv_plumber = (ListView) findViewById(R.id.lvf_plumber);
        lv_electrician = (ListView) findViewById(R.id.lvf_electrician);


        lv_fitter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Professional professional = (Professional) adapter_fitter.getItem(position);
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileGoldPlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
            }
        });

        lv_plumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Professional professional = (Professional) adapter_plumber.getItem(position);
                Intent intent = new Intent(MyFavoritesActivity.this,
                        ProfessionalProfileGoldPlanActivity.class);
                intent.putExtra("PROFESSIONAL", professional);
                startActivity(intent);
            }
        });

        lv_electrician.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Professional professional = (Professional) adapter_electrictian.getItem(position);
                Intent intent = new Intent(MyFavoritesActivity.this,
                        ProfessionalProfileGoldPlanActivity.class);
                intent.putExtra("PROFESSIONAL", professional);
                startActivity(intent);
            }
        });


        setList();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    public void setList(){
        list_fitter_professionals = userController.getFavoritesFittersUser(username, handler);
        list_plumber_professionals = userController.getFavoritesPlumbersUser(username, handler);
        list_electrician_professionals = userController.getFavoritesElectriciansUser(username, handler);

        if (list_fitter_professionals.size() != 0){
            adapter_fitter = new ProfessionalsAdapter(getApplicationContext(),
                    list_fitter_professionals);
            lv_fitter.setAdapter(adapter_fitter);
        }
        if (list_plumber_professionals.size() != 0){
            adapter_plumber = new ProfessionalsAdapter(getApplicationContext(),
                    list_plumber_professionals);
            lv_plumber.setAdapter(adapter_plumber);
        }
        if (list_electrician_professionals.size() != 0){
            adapter_electrictian = new ProfessionalsAdapter(getApplicationContext(),
                    list_electrician_professionals);
            lv_electrician.setAdapter(adapter_electrictian);
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