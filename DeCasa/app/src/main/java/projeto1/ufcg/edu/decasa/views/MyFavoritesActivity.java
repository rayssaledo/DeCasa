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
import android.widget.TextView;

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
    private TextView tv_no_plumber_as_favorite;
    private TextView tv_no_electrician_as_favorite;
    private TextView tv_no_fitter_as_favorite;
    private List<Professional> list_fitter_professionals;
    private List<Professional> list_plumber_professionals;
    private List<Professional> list_electrician_professionals;
    private UserController userController;
    private String username;
    private ProfessionalsAdapter adapter_fitter;
    private ProfessionalsAdapter adapter_plumber;
    private ProfessionalsAdapter adapter_electrictian;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 108) {
                if (list_fitter_professionals.size() != 0){
                    adapter_fitter = new ProfessionalsAdapter(getApplicationContext(),
                            list_fitter_professionals);
                    lv_fitter.setAdapter(adapter_fitter);
                    lv_fitter.setVisibility(View.VISIBLE);
                    tv_no_fitter_as_favorite.setVisibility(View.GONE);
                } else {
                    lv_fitter.setVisibility(View.GONE);
                    tv_no_fitter_as_favorite.setVisibility(View.VISIBLE);
                }

                if (list_plumber_professionals.size() != 0){
                    adapter_plumber = new ProfessionalsAdapter(getApplicationContext(),
                            list_plumber_professionals);
                    lv_plumber.setAdapter(adapter_plumber);
                    lv_plumber.setVisibility(View.VISIBLE);
                    tv_no_plumber_as_favorite.setVisibility(View.GONE);
                } else {
                    lv_plumber.setVisibility(View.GONE);
                    tv_no_plumber_as_favorite.setVisibility(View.VISIBLE);
                }

                if (list_electrician_professionals.size() != 0){
                    adapter_electrictian = new ProfessionalsAdapter(getApplicationContext(),
                            list_electrician_professionals);
                    lv_electrician.setAdapter(adapter_electrictian);
                    lv_electrician.setVisibility(View.VISIBLE);
                    tv_no_electrician_as_favorite.setVisibility(View.GONE);
                } else {
                    lv_electrician.setVisibility(View.GONE);
                    tv_no_electrician_as_favorite.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        userController = new UserController(MyFavoritesActivity.this);
        MySharedPreferences mySharedPreferences = new MySharedPreferences(getApplicationContext());
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
        tv_no_fitter_as_favorite = (TextView) findViewById(R.id.tv_no_fitter_as_favorite);
        lv_plumber = (ListView) findViewById(R.id.lvf_plumber);
        tv_no_plumber_as_favorite = (TextView) findViewById(R.id.tv_no_plumber_as_favorite);
        lv_electrician = (ListView) findViewById(R.id.lvf_electrician);
        tv_no_electrician_as_favorite = (TextView) findViewById(R.id.tv_no_electrician_as_favorite);

        lv_fitter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Professional professional = (Professional) adapter_fitter.getItem(position);
                if (professional.getPlan().toLowerCase().equals("gold")){
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileGoldPlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else if (professional.getPlan().toLowerCase().equals("silver")){
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileSilverPlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else if (professional.getPlan().toLowerCase().equals("bronze")){
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileBronzePlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileFreePlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                }
            }
        });

        lv_plumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Professional professional = (Professional) adapter_plumber.getItem(position);
                if (professional.getPlan().toLowerCase().equals("gold")){
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileGoldPlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else if (professional.getPlan().toLowerCase().equals("silver")){
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileSilverPlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else if (professional.getPlan().toLowerCase().equals("bronze")){
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileBronzePlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileFreePlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                }
            }
        });

        lv_electrician.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Professional professional = (Professional) adapter_electrictian.getItem(position);
                if (professional.getPlan().toLowerCase().equals("gold")){
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileGoldPlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else if (professional.getPlan().toLowerCase().equals("silver")){
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileSilverPlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else if (professional.getPlan().toLowerCase().equals("bronze")){
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileBronzePlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyFavoritesActivity.this,
                            ProfessionalProfileFreePlanActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                }
            }
        });

        setList();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

    public void setList(){
        list_fitter_professionals = userController.getFavoritesUserByService(username, "Montador",
                handler);
        list_plumber_professionals = userController.getFavoritesUserByService(username, "Encanador",
                handler);
        list_electrician_professionals = userController.getFavoritesUserByService(username,
                "Eletricista", handler);
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