package projeto1.ufcg.edu.decasa.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.Professional;

public class UserCadastreActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_photo_user;
    private static final int RESULT_CAMERA = 111;
    private static final int RESULT_GALLERY = 222;
    private Bitmap bitmapPhoto;

    private List<String> genders;
    private List<String> states;

    private String name_user;
    private String date_birth_user;
    private String gender_user;
    private String city_user;
    private String state_user;
    private String neighborhood_user;
    private String street_user;
    private String number_user;
    private String username_user;
    private String photo_user;
    private String password_user;
    private String password_confirm_user;

    private EditText mInput_name;
    private EditText mDate_of_birth;
    private EditText mInput_city;
    private EditText mInput_neighborhood;
    private EditText mInput_street;
    private EditText mInput_number;
    private EditText mInput_username;
    private EditText mInput_password;
    private EditText mInput_password_confirm;

    private TextInputLayout layout_name;
    private TextInputLayout layout_date_birth;
    private TextInputLayout layout_city;
    private TextInputLayout layout_neighborhood;
    private TextInputLayout layout_street;
    private TextInputLayout layout_number;
    private TextInputLayout layout_username;
    private TextInputLayout layout_password;
    private TextInputLayout layout_password_confirm;

    private UserController userController;
    public static View mLoadingCadastre;
    private Professional professional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cadastre);

        Intent it = getIntent();
        professional = it.getParcelableExtra("PROFESSIONAL");

        userController = new UserController(UserCadastreActivity.this);

        mLoadingCadastre = findViewById(R.id.rl_loading_cadastre);

        Bitmap avatar = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
        bitmapPhoto = avatar;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmapPhoto.compress(Bitmap.CompressFormat.JPEG, 100, b);
        byte[] photo_user_byte = b.toByteArray();
        photo_user = Base64.encodeToString(photo_user_byte, Base64.NO_WRAP);


        iv_photo_user = (ImageView) findViewById(R.id.iv_user_photo);
        ImageButton ib_camera = (ImageButton) findViewById(R.id.ib_camera);
        ib_camera.setOnClickListener(this);
        ImageButton ib_gallery = (ImageButton) findViewById(R.id.ib_gallery);
        ib_gallery.setOnClickListener(this);
        ImageButton ib_delete = (ImageButton) findViewById(R.id.ib_delete);
        ib_delete.setOnClickListener(this);

        Spinner mGenders_spinner = (Spinner) findViewById(R.id.sp_gender);
        Spinner mStates_spinner = (Spinner) findViewById(R.id.sp_state);

        Button btn_cadastre = (Button) findViewById(R.id.btn_cadastre);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        mInput_name = (EditText) findViewById(R.id.input_name);
        mDate_of_birth = (EditText) findViewById(R.id.input_date_of_birth);
        mInput_city = (EditText) findViewById(R.id.input_city);
        mInput_neighborhood = (EditText) findViewById(R.id.input_neighborhood);
        mInput_street = (EditText) findViewById(R.id.input_street);
        mInput_number = (EditText) findViewById(R.id.input_number);
        mInput_username = (EditText) findViewById(R.id.input_username);
        mInput_password = (EditText) findViewById(R.id.input_password);
        mInput_password_confirm = (EditText) findViewById(R.id.input_password_confirm);

        layout_name = (TextInputLayout) findViewById(R.id.input_layout_name);
        layout_date_birth = (TextInputLayout) findViewById(R.id.input_layout_date_of_birth);
        layout_city = (TextInputLayout) findViewById(R.id.input_layout_city);
        layout_neighborhood = (TextInputLayout) findViewById(R.id.input_layout_neighborhood);
        layout_street = (TextInputLayout) findViewById(R.id.input_layout_street);
        layout_number = (TextInputLayout) findViewById(R.id.input_layout_number);
        layout_username = (TextInputLayout) findViewById(R.id.input_layout_username);
        layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        layout_password_confirm = (TextInputLayout) findViewById(R.id.
                input_layout_password_confirm);

        MaskEditTextChangedListener maskDateOfBirth = new MaskEditTextChangedListener("##/##/####",
                mDate_of_birth);
        mDate_of_birth.addTextChangedListener(maskDateOfBirth);

        putGenderElementsOnSpinnerArray();
        mGenders_spinner.setAdapter(createArrayAdapterGender());

        mGenders_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos,
                                       final long id) {
                Object item = parent.getItemAtPosition(pos);
                String itemGender = item.toString();

                if (itemGender.equals(getApplicationContext().getResources().
                        getString(R.string.female))) {
                    gender_user = "F";
                } else if (itemGender.equals(getApplicationContext().getResources().
                        getString(R.string.male))) {
                    gender_user = "M";
                }
            }

            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });

        putStateElementsOnSpinnerArray();
        mStates_spinner.setAdapter(createArrayAdapterState());

        mStates_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos,
                                       final long id) {
                Object item = parent.getItemAtPosition(pos);
                state_user = item.toString();
            }

            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });

        btn_cadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_user = mInput_name.getText().toString();
                date_birth_user = mDate_of_birth.getText().toString();
                city_user = mInput_city.getText().toString();
                neighborhood_user = mInput_neighborhood.getText().toString();
                street_user = mInput_street.getText().toString();
                number_user = mInput_number.getText().toString();
                username_user = mInput_username.getText().toString();
                password_user = mInput_password.getText().toString();
                password_confirm_user = mInput_password_confirm.getText().toString();

                cadastreUser(name_user, date_birth_user, gender_user, street_user, number_user,
                        neighborhood_user, city_user, state_user, photo_user, username_user,
                        password_user);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCadastreActivity.this,
                        LoginActivity.class);
                intent.putExtra("PROFESSIONAL", professional);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setView(Context context, Class classe) {
        Intent it = new Intent();
        it.setClass(context, classe);
        startActivity(it);
    }

    private SpinnerAdapter createArrayAdapterState() {
        ArrayAdapter<String> spinnerArrayAdapterState = new ArrayAdapter<>(this, android.R.
                layout.simple_spinner_item, states);
        spinnerArrayAdapterState.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item); // The drop down view

        return spinnerArrayAdapterState;
    }

    private SpinnerAdapter createArrayAdapterGender() {
        ArrayAdapter<String> spinnerArrayAdapterGenders = new ArrayAdapter<>(this, android.R.
                layout.simple_spinner_item, genders);
        spinnerArrayAdapterGenders.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item); // The drop down view

        return spinnerArrayAdapterGenders;
    }

    public void putGenderElementsOnSpinnerArray() {
        genders = new ArrayList<>();
        genders.add(getApplicationContext().getResources().getString(R.string.female));
        genders.add(getApplicationContext().getResources().getString(R.string.male));
    }

    public void putStateElementsOnSpinnerArray(){
        states = new ArrayList<>();
        states.add("AC");
        states.add("AL");
        states.add("AP");
        states.add("AM");
        states.add("BA");
        states.add("CE");
        states.add("DF");
        states.add("ES");
        states.add("GO");
        states.add("MA");
        states.add("MT");
        states.add("MS");
        states.add("MG");
        states.add("PA");
        states.add("PB");
        states.add("PR");
        states.add("PE");
        states.add("PI");
        states.add("RJ");
        states.add("RN");
        states.add("RS");
        states.add("RO");
        states.add("RR");
        states.add("SC");
        states.add("SP");
        states.add("SE");
        states.add("TO");
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.ib_camera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMERA);
                break;
            case R.id.ib_gallery:
                intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_GALLERY);
                break;
            default:
                Bitmap avatar = BitmapFactory.decodeResource(getResources(),
                        R.drawable.default_avatar);
                iv_photo_user.setImageBitmap(avatar);
                iv_photo_user.setImageBitmap(Bitmap.createScaledBitmap(avatar, 120, 120, true));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAMERA && resultCode == RESULT_OK) {
            bitmapPhoto = (Bitmap)data.getExtras().get("data");
            iv_photo_user.setImageBitmap(bitmapPhoto);
            iv_photo_user.setImageBitmap(Bitmap.createScaledBitmap(bitmapPhoto, 120, 120, true));
        } else if (requestCode == RESULT_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            String[] columnFile = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri, columnFile, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(columnFile[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            bitmapPhoto = BitmapFactory.decodeFile(picturePath.toString());
            if (bitmapPhoto != null) {
                iv_photo_user.setImageBitmap(bitmapPhoto);
                iv_photo_user.setImageBitmap(Bitmap.
                        createScaledBitmap(bitmapPhoto, 120, 120, true));
            }
        }
    }

    private void cadastreUser(String name, String birthDate, String gender, String street,
                              String number, String neighborhood, String city, String state,
                              String photo, String username, String password) {

        if (validateName() && validateDateOfBirth() && validateCity() &&  validateNeighborhood()
                && validateStreet()&& validateNumber() && validateUsername() && validatePassword()
                && validatePasswordConfirm() && confirmationPassword()) {
            if (professional == null){
                userController.cadastre(name, birthDate, gender, street, number, neighborhood, city,
                        state, photo, username, password, MainActivity.class, professional);
            } else {
                userController.cadastre(name, birthDate, gender, street, number, neighborhood, city,
                        state, photo, username, password, ProfileProfessionalActivity.class,
                        professional);
            }
        } else if (!validateName()) {
            return;
        } else if (!validateDateOfBirth()) {
            return;
        } else if (!validateCity()) {
            return;
        } else if (!validateNeighborhood()) {
            return;
        } else if (!validateStreet()) {
            return;
        } else if (!validateNumber()) {
            return;
        } else if (!validateUsername()) {
            return;
        } else if (!validatePassword()) {
            return;
        } else if (!validatePasswordConfirm()) {
            return;
        } else if (!confirmationPassword()) {
            new AlertDialog.Builder(UserCadastreActivity.this)
                    .setMessage(getString(R.string.err_passwords_do_not_match))
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            mInput_password.setText("");
                            mInput_password_confirm.setText("");
                            requestFocus(mInput_password);
                        }
                    })
                    .create()
                    .show();
        }
    }

    private boolean validateName(){
        if (name_user.trim().isEmpty() || name_user == null) {
            layout_name.setError(getString(R.string.err_msg_name));
            requestFocus(mInput_name);
            return false;
        } else if (name_user.trim().length() < 10){
            layout_name.setError(getString(R.string.err_short_name));
            requestFocus(mInput_name);
            return false;
        } else {
            layout_name.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDateOfBirth(){
        String date = date_birth_user.replaceAll("/", "");
        if (date_birth_user.trim().isEmpty()) {
            layout_date_birth.setError(getString(R.string.err_msg_birth));
            requestFocus(mDate_of_birth);
            return false;
        } else if (date_birth_user.trim().length() != 10) {
            layout_date_birth.setError(getString(R.string.err_invalid_birth));
            requestFocus(mDate_of_birth);
            return false;
        } else if (Integer.parseInt(date.substring(0,2)) < 1 ||
                Integer.parseInt(date.substring(0,2)) > 31){
            layout_date_birth.setError(getString(R.string.err_invalid_day));
            requestFocus(mDate_of_birth);
            return false;
        } else if (Integer.parseInt(date.trim().substring(2,4)) < 1 ||
                Integer.parseInt(date.substring(2,4)) > 12){
            layout_date_birth.setError(getString(R.string.err_invalid_month));
            requestFocus(mDate_of_birth);
            return false;
        } else if (Integer.parseInt(date.substring(4)) > 2016){
            layout_date_birth.setError(getString(R.string.err_invalid_year));
            requestFocus(mDate_of_birth);
            return false;
        } else {
            layout_date_birth.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNeighborhood(){
        if (neighborhood_user.trim().isEmpty() || neighborhood_user == null) {
            layout_neighborhood.setError(getString(R.string.err_msg_neighborhood));
            requestFocus(mInput_neighborhood);
            return false;
        } else if (neighborhood_user.trim().length() < 4){
            layout_neighborhood.setError(getString(R.string.err_short_neighborhood_name));
            requestFocus(mInput_neighborhood);
            return false;
        } else {
            layout_neighborhood.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateStreet(){
        if (street_user.trim().isEmpty() || street_user == null) {
            layout_street.setError(getString(R.string.err_msg_street));
            requestFocus(mInput_street);
            return false;
        } else if (street_user.trim().length() < 4){
            layout_street.setError(getString(R.string.err_short_street_name));
            requestFocus(mInput_street);
            return false;
        } else {
            layout_street.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNumber(){
        if (number_user.trim().isEmpty() || number_user == null) {
            layout_number.setError(getString(R.string.err_msg_number));
            requestFocus(mInput_number);
            return false;
        } else {
            layout_number.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCity(){
        if (city_user.trim().isEmpty() || city_user == null) {
            layout_city.setError(getString(R.string.err_msg_city));
            requestFocus(mInput_city);
            return false;
        } else if (city_user.trim().length() < 4){
            layout_city.setError(getString(R.string.err_short_city_name));
            requestFocus(mInput_city);
            return false;
        } else {
            layout_city.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateUsername(){
        if (username_user.trim().isEmpty()) {
            layout_username.setError(getString(R.string.err_msg_username));
            requestFocus(mInput_username);
            return false;
        } else if (username_user.trim().length() < 5) {
            layout_username.setError(getString(R.string.err_short_username));
            requestFocus(mInput_username);
            return false;
        } else{
            layout_username.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword(){
        if (password_user.trim().isEmpty()) {
            layout_password.setError(getString(R.string.err_msg_password));
            requestFocus(mInput_password);
            return false;
        } else if (password_user.trim().length() < 6){
            layout_password.setError(getString(R.string.err_short_password));
            requestFocus(mInput_password);
            return false;
        } else {
            layout_password.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePasswordConfirm(){
        if (password_confirm_user.trim().isEmpty()) {
            layout_password_confirm.setError(getString(R.string.err_msg_password_confirm));
            requestFocus(mInput_password_confirm);
            return false;
        } else if (password_confirm_user.trim().length() < 6){
            layout_password_confirm.setError(getString(R.string.err_short_password));
            requestFocus(mInput_password_confirm);
            return false;
        } else {
            layout_password_confirm.setErrorEnabled(false);
        }
        return true;
    }

    private boolean confirmationPassword(){
        return password_user.trim().equals(password_confirm_user);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.
                    SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}