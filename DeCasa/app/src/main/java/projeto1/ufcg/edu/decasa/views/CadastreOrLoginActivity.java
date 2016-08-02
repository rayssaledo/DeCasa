package projeto1.ufcg.edu.decasa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.models.Professional;

public class CadastreOrLoginActivity extends AppCompatActivity {

    private Button btn_register;
    private Button btn_login;
    private Professional professional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastre_or_login);

        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONAL");

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastreOrLoginActivity.this,
                        UserCadastreActivity.class);
                intent.putExtra("PROFESSIONAL", professional);
                startActivity(intent);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastreOrLoginActivity.this,
                        LoginActivity.class);
                intent.putExtra("PROFESSIONAL", professional);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }

}
