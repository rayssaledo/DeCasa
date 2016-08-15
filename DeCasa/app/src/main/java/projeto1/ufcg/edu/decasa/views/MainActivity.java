package projeto1.ufcg.edu.decasa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.adapters.AssessmentsAdapter;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.NavItem;
import projeto1.ufcg.edu.decasa.models.User;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class MainActivity extends AppCompatActivity {

    private Intent it;
    private String service;
    private MySharedPreferences mySharedPreferences;
    private UserController userController;
    private List<User> listUser;
    private TextView tv_message_welcome;

    private Button btn_register;
    private TextView tv_welcome;

    private HashMap<String, String> userDetails;
    String username;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 103) {
                if (listUser.size() == 1) {
                    tv_welcome.setText(getApplication().getString(R.string.begin_welcome) + " " + listUser.get(0).getName() +", " + getApplication().getString(R.string.end_welcome) );
                    //tv_welcome.setText( "Welcome " + listUser.get(0).getName());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySharedPreferences = new MySharedPreferences(getApplicationContext());
        userController = new UserController(MainActivity.this);

        btn_register = (Button) findViewById(R.id.btn_register);
        tv_welcome = (TextView) findViewById(R.id.welcome);
        userDetails = mySharedPreferences.getUserDetails();
        username = userDetails.get(MySharedPreferences.KEY_USERNAME_USER);

        ImageButton ib_electrician = (ImageButton) findViewById(R.id.ib_electrician);
        ImageButton ib_plumber = (ImageButton) findViewById(R.id.ib_plumber);
        ImageButton ib_fitter = (ImageButton) findViewById(R.id.ib_fitter);
        Button btn_register = (Button)  findViewById(R.id.btn_register);

        Toast.makeText(MainActivity.this, mySharedPreferences.isUserLoggedIn() + "", Toast.LENGTH_SHORT).show();

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

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(MainActivity.this, UserCadastreActivity.class);
            }
        });

        updateInformationMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mySharedPreferences.isUserLoggedIn()){
            listUser = userController.getUser(username, myHandler);
        }
        //assessments = evaluationController.getEvaluationsByProfessional(professional.getEmail(), handler);
        //numAssessmentsProfessional = evaluationController.getNumAssessments(professional.getEmail(), handler);
    }

    public void updateInformationMenu() {

        if (mySharedPreferences.isUserLoggedIn()) {
            btn_register.setVisibility(View.GONE);
        }
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

    public static void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        context.startActivity(it);
    }
}