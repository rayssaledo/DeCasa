package projeto1.ufcg.edu.decasa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import projeto1.ufcg.edu.decasa.R;

public class CadastreOrLoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastre_or_login);

        Button btn_register = (Button) findViewById(R.id.btn_register);
        Button btn_login = (Button) findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(CadastreOrLoginActivity.this, UserCadastreActivity.class);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(CadastreOrLoginActivity.this, LoginActivity.class);
            }
        });
    }

    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }



}
