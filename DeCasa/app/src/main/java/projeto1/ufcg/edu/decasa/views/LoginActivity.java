package projeto1.ufcg.edu.decasa.views;

import android.widget.EditText;
import android.widget.Button;

import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.Professional;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private TextInputLayout layout_username;
    private TextInputLayout layout_password;

    private String username;
    private String password;
    private UserController userController;
    public static View loading;

    private Professional professional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONAL");

        userController = new UserController(LoginActivity.this);

        Button btn_login = (Button) findViewById(R.id.btn_signin);
        Button btn_register =  (Button) findViewById(R.id.btn_register);

        et_username = (EditText) findViewById(R.id.input_username);
        et_password = (EditText) findViewById(R.id.input_user_password);
        layout_username = (TextInputLayout) findViewById(R.id.input_layout_username);
        layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        loading =  findViewById(R.id.loadingLogin);

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();

                if (validateUsername() && validatePassword()) {
                    if (professional == null){
                        userController.login(username, password, MainActivity.class, null);
                    } else {
                        userController.login(username, password, ProfessionalProfileGoldPlanActivity.class,
                                professional);
                    }
                } else if (!validateUsername()){
                    return;
                } else if (!validatePassword()){
                    return;
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,
                        UserCadastreActivity.class);
                intent.putExtra("PROFESSIONAL", professional);
                startActivity(intent);
                finish();
            }
        });

    }

    private boolean validateUsername(){
        if (username.trim().isEmpty()) {
            layout_username.setError(getString(R.string.err_msg_username));
            requestFocus(et_username);
            return false;
        } else {
            layout_username.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword(){
        if (password.trim().isEmpty()) {
            layout_password.setError(getString(R.string.err_msg_password));
            requestFocus(et_password);
            return false;
        } else if (password.trim().length() < 6){
            layout_password.setError(getString(R.string.err_short_password));
            requestFocus(et_password);
            return false;
        } else {
           layout_password.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.
                    SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
