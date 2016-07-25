package projeto1.ufcg.edu.decasa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class MainActivity extends AppCompatActivity {

    private ImageButton ib_electrician;
    private ImageButton ib_plumber;
    private ImageButton ib_fitter;
    private Intent it;
    private String service;
    private MySharedPreferences mySharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySharedPreferences = new MySharedPreferences(getApplicationContext());

        ib_electrician = (ImageButton) findViewById(R.id.ib_electrician);
        ib_plumber = (ImageButton) findViewById(R.id.ib_plumber);
        ib_fitter = (ImageButton) findViewById(R.id.ib_fitter);
        it = new Intent(MainActivity.this, ProfessionalsActivity.class);

        ib_electrician.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                service = getApplication().getString(R.string.title_electricians);
                mySharedPreferences.saveService(service);
                it.putExtra("SERVICE",service);
                startActivity(it);
            }
        });

        ib_plumber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                service = getApplication().getString(R.string.title_plumbers);
                mySharedPreferences.saveService(service);
                it.putExtra("SERVICE",service);
                startActivity(it);
            }
        });

        ib_fitter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                service = getApplication().getString(R.string.title_fitters);
                mySharedPreferences.saveService(service);
                it.putExtra("SERVICE",service);
                startActivity(it);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }
}