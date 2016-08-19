package projeto1.ufcg.edu.decasa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import projeto1.ufcg.edu.decasa.R;

public class MyFavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        TabHost mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();

        TabHost.TabSpec descritor = mTabHost.newTabSpec("aba1");
        descritor.setContent(R.id.tab_plumber);
        descritor.setIndicator("Plumber");
        mTabHost.addTab(descritor);

        descritor = mTabHost.newTabSpec("aba2");
        descritor.setContent(R.id.tab_electrician);
        descritor.setIndicator("Electrician");
        mTabHost.addTab(descritor);

        descritor = mTabHost.newTabSpec("aba3");
        descritor.setContent(R.id.tab_fitter);
        descritor.setIndicator("Fitter");
        mTabHost.addTab(descritor);
    }

    public static void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        context.startActivity(it);
    }
}