package projeto1.ufcg.edu.decasa.views;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.Professional;

public class LoginActivity extends AppCompatActivity {

    private Button btn_register;
    private Button btn_login;
    private EditText et_email;
    private EditText et_password;
    private TextInputLayout layout_email;
    private TextInputLayout layout_password;

    private String email;
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
        btn_login = (Button) findViewById(R.id.btn_signin);
        btn_register =  (Button) findViewById(R.id.btn_register);
        et_email = (EditText) findViewById(R.id.input_user_email);
        et_password = (EditText) findViewById(R.id.input_user_password);
        layout_email = (TextInputLayout) findViewById(R.id.input_layout_username);
        layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        loading =  findViewById(R.id.loadingLogin);

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString();
                password = et_password.getText().toString();

                if (validateEmail() && validatePassword()) {
                    if (professional == null){
                        userController.login(email, password, MainActivity.class, professional);
                    } else {
                        userController.login(email, password, ProfileProfessionalActivity.class,
                                professional);
                    }
                    //userController.login(email, password, MainActivity.class, professional);
                } else if (!validateEmail()){
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
               //setView(LoginActivity.this, UserCadastreActivity.class);
            }
        });

    }

    public void setView(Context context, Class classe) {
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }
    private boolean validateEmail(){
        if (email.trim().isEmpty()) {
            layout_email.setError(getString(R.string.err_msg_username));
            requestFocus(et_email);
            return false;
        } else {
            layout_email.setErrorEnabled(false);
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
