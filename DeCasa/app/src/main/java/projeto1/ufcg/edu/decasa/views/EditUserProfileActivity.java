package projeto1.ufcg.edu.decasa.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.User;
import projeto1.ufcg.edu.decasa.utils.Constants;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;
import projeto1.ufcg.edu.decasa.utils.PicModeSelectDialogFragment;

public class EditUserProfileActivity extends AppCompatActivity implements
        PicModeSelectDialogFragment.IPicModeSelectListener {

    private List<String> genders;
    private List<String> states;

    private ImageView ivPhotoUser;

    private EditText etNameUser;
    private EditText etDateOfBirth;
    private Spinner spGenderUser;
    private EditText etCity;
    private Spinner spStateUser;
    private EditText etNeighborhoodUser;
    private EditText etStreetUser;
    private EditText etNumber;
    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etConfirmNewPassword;
    private Button btnEdit;

    private TextInputLayout layout_name;
    private TextInputLayout layout_date_birth;
    private TextInputLayout layout_city;
    private TextInputLayout layout_neighborhood;
    private TextInputLayout layout_street;
    private TextInputLayout layout_number;
    private TextInputLayout layout_current_password;
    private TextInputLayout layout_password;
    private TextInputLayout layout_password_confirm;

    private String nameUser;
    private String dateOfBirth;
    private String genderUser;
    private String cityUser;
    private String stateUser;
    private String neighborhoodUser;
    private String streetUser;
    private String numberUser;
    private String username;
    private String photoUser;
    private String passwordCurrent;
    private String passwordUserNew;
    private String passwordConfirmUser;
    private String gender;
    private String state;

    private MySharedPreferences mySharedPreferences;
    private UserController userController;
    private User user;
    private String passwordNew;

    private Bitmap bitmapPhoto;
    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;

    public static View loadingEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        loadingEdit = findViewById(R.id.rl_loading_edit_user_profile);

        Intent it = getIntent();
        user = it.getParcelableExtra("USER");

        userController = new UserController(EditUserProfileActivity.this);
        mySharedPreferences = new MySharedPreferences(getApplicationContext());

        ivPhotoUser = (ImageView) findViewById(R.id.iv_photo_user);
        etNameUser = (EditText) findViewById(R.id.et_name_user);
        etDateOfBirth = (EditText) findViewById(R.id.et_date_of_birth);
        spGenderUser = (Spinner) findViewById(R.id.sp_gender);
        etCity = (EditText) findViewById(R.id.et_city);
        spStateUser = (Spinner) findViewById(R.id.sp_state);
        etNeighborhoodUser = (EditText) findViewById(R.id.et_neighborhood);
        etStreetUser = (EditText) findViewById(R.id.et_street);
        etNumber = (EditText) findViewById(R.id.et_number);
        etCurrentPassword = (EditText) findViewById(R.id.et_current_password);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etConfirmNewPassword = (EditText) findViewById(R.id.et_confirm_new_password);
        btnEdit = (Button) findViewById(R.id.btn_edit);

        layout_name = (TextInputLayout) findViewById(R.id.input_layout_name);
        layout_date_birth = (TextInputLayout) findViewById(R.id.input_layout_date_of_birth);
        layout_city = (TextInputLayout) findViewById(R.id.input_layout_city);
        layout_neighborhood = (TextInputLayout) findViewById(R.id.input_layout_neighborhood);
        layout_street = (TextInputLayout) findViewById(R.id.input_layout_street);
        layout_number = (TextInputLayout) findViewById(R.id.input_layout_number);
        layout_current_password = (TextInputLayout) findViewById(R.id.input_layout_current_password);
        layout_password = (TextInputLayout) findViewById(R.id.input_layout_new_password);
        layout_password_confirm = (TextInputLayout) findViewById(R.id.
                input_layout_confirm_new_password);

        photoUser = user.getPhoto();
        byte[] photoByte = Base64.decode(photoUser, Base64.DEFAULT);
        bitmapPhoto = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);

        MaskEditTextChangedListener maskDateOfBirth = new MaskEditTextChangedListener("##/##/####",
                etDateOfBirth);
        etDateOfBirth.addTextChangedListener(maskDateOfBirth);

        ImageButton ibCamera = (ImageButton) findViewById(R.id.ib_camera_edit_profile);
        ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProfilePicDialog();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        username = mySharedPreferences.getUserLogged();
        setFields();
    }

    private void setFields() {
        etNameUser.setText(user.getName());
        etDateOfBirth.setText(user.getDateOfBirth());
        etCity.setText(user.getCity());
        etNeighborhoodUser.setText(user.getNeighborhood());
        etStreetUser.setText(user.getStreet());
        etNumber.setText(user.getNumber());

        setBitmapPhotoUser(bitmapPhoto);

        putGenderElementsOnSpinnerArray();
        spGenderUser.setAdapter(createArrayAdapterGender());

        spGenderUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos,
                                       final long id) {
                Object item = parent.getItemAtPosition(pos);
                String itemGender = item.toString();

                if (itemGender.equals(getApplicationContext().getResources().
                        getString(R.string.female))) {
                    genderUser = "F";
                } else if (itemGender.equals(getApplicationContext().getResources().
                        getString(R.string.male))) {
                    genderUser = "M";
                }
            }

            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });

        putStateElementsOnSpinnerArray();
        spStateUser.setAdapter(createArrayAdapterState());

        spStateUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos,
                                       final long id) {
                Object item = parent.getItemAtPosition(pos);
                stateUser = item.toString();
            }

            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameUser = etNameUser.getText().toString();
                dateOfBirth = etDateOfBirth.getText().toString();
                gender = genderUser;
                cityUser = etCity.getText().toString();
                state = stateUser;
                neighborhoodUser = etNeighborhoodUser.getText().toString();
                streetUser = etStreetUser.getText().toString();
                numberUser = etNumber.getText().toString();
                passwordCurrent = etCurrentPassword.getText().toString();
                passwordNew = user.getPassword();
                passwordUserNew = etNewPassword.getText().toString();
                passwordConfirmUser = etConfirmNewPassword.getText().toString();

                updateUser(nameUser, dateOfBirth, gender, streetUser, numberUser,
                        neighborhoodUser, cityUser, state, photoUser, username);
            }
        });

    }

    private void setBitmapPhotoUser(Bitmap bitmapPhotoUser) {
        ivPhotoUser.setImageBitmap(bitmapPhotoUser);
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
        if (user.getGender().equals("F")) {
            genders.add(getApplicationContext().getResources().getString(R.string.female));
            genders.add(getApplicationContext().getResources().getString(R.string.male));
        } else {
            genders.add(getApplicationContext().getResources().getString(R.string.male));
            genders.add(getApplicationContext().getResources().getString(R.string.female));
        }

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
        states.remove(user.getState());
        states.add(0, user.getState());
    }

    private void updateUser(String name, String birthDate, String gender, String street,
                            String number, String neighborhood, String city, String state,
                            String photo, String username) {
        if (validateName() && validateDateOfBirth() && validateCity() &&  validateNeighborhood()
                && validateStreet()&& validateNumber() && validatePasswords()) {
            if (passwordConfirmUser != null && !passwordConfirmUser.trim().equals("")) {
                passwordNew = passwordConfirmUser;
            }
            userController.update(name, birthDate, gender, street, number, neighborhood, city,
                    state, photo, username, passwordNew, UserProfileActivity.class);
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
        } else if (!validatePasswords()) {
            return;
        }
    }

    private boolean validateName(){
        if (nameUser.trim().isEmpty() || nameUser == null) {
            layout_name.setError(getString(R.string.err_msg_name));
            requestFocus(etNameUser);
            return false;
        } else if (nameUser.trim().length() < 10){
            layout_name.setError(getString(R.string.err_short_name));
            requestFocus(etNameUser);
            return false;
        } else {
            layout_name.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDateOfBirth(){
        String dateBirthUser = dateOfBirth.replaceAll("/", "");

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentYear = cal.get(Calendar.YEAR);
        Log.d("YEAR", currentYear +"");
        if (dateOfBirth.trim().isEmpty()) {
            layout_date_birth.setError(getString(R.string.err_msg_birth));
            requestFocus(etDateOfBirth);
            return false;
        } else if (dateOfBirth.trim().length() != 10) {
            layout_date_birth.setError(getString(R.string.err_invalid_birth));
            requestFocus(etDateOfBirth);
            return false;
        } else if (Integer.parseInt(dateBirthUser.substring(0,2)) < 1 ||
                Integer.parseInt(dateBirthUser.substring(0,2)) > 31){
            layout_date_birth.setError(getString(R.string.err_invalid_day));
            requestFocus(etDateOfBirth);
            return false;
        } else if (Integer.parseInt(dateBirthUser.trim().substring(2,4)) < 1 ||
                Integer.parseInt(dateBirthUser.substring(2,4)) > 12){
            layout_date_birth.setError(getString(R.string.err_invalid_month));
            requestFocus(etDateOfBirth);
            return false;
        } else if (Integer.parseInt(dateBirthUser.substring(4)) < (currentYear - 100) ||
                Integer.parseInt(dateBirthUser.substring(4)) > currentYear){
            layout_date_birth.setError(getString(R.string.err_invalid_year));
            requestFocus(etDateOfBirth);
            return false;
        } else {
            layout_date_birth.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNeighborhood(){
        if (neighborhoodUser.trim().isEmpty() || neighborhoodUser == null) {
            layout_neighborhood.setError(getString(R.string.err_msg_neighborhood));
            requestFocus(etNeighborhoodUser);
            return false;
        } else if (neighborhoodUser.trim().length() < 4){
            layout_neighborhood.setError(getString(R.string.err_short_neighborhood_name));
            requestFocus(etNeighborhoodUser);
            return false;
        } else {
            layout_neighborhood.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateStreet(){
        if (streetUser.trim().isEmpty() || streetUser == null) {
            layout_street.setError(getString(R.string.err_msg_street));
            requestFocus(etStreetUser);
            return false;
        } else if (streetUser.trim().length() < 4){
            layout_street.setError(getString(R.string.err_short_street_name));
            requestFocus(etStreetUser);
            return false;
        } else {
            layout_street.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNumber(){
        if (numberUser.trim().isEmpty() || numberUser == null) {
            layout_number.setError(getString(R.string.err_msg_number));
            requestFocus(etNumber);
            return false;
        } else {
            layout_number.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCity(){
        if (cityUser.trim().isEmpty() || cityUser == null) {
            layout_city.setError(getString(R.string.err_msg_city));
            requestFocus(etCity);
            return false;
        } else if (cityUser.trim().length() < 4){
            layout_city.setError(getString(R.string.err_short_city_name));
            requestFocus(etCity);
            return false;
        } else {
            layout_city.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCurrentPassword() {
        if (!passwordCurrent.equals(user.getPassword())) {
            layout_current_password.setError(getString(R.string.err_incorrect_current_password));
            requestFocus(etCurrentPassword);
            return false;
        } else {
            layout_current_password.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNewPassword(){
        if (passwordUserNew == null || passwordUserNew.trim().isEmpty()) {
            layout_password.setError(getString(R.string.err_msg_password));
            requestFocus(etNewPassword);
            return false;
        } else if (passwordUserNew.trim().length() < 6){
            layout_password.setError(getString(R.string.err_short_password));
            requestFocus(etNewPassword);
            return false;
        } else {
            layout_password.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePasswordConfirm(){
        if (passwordConfirmUser == null || passwordConfirmUser.trim().isEmpty()) {
            layout_password_confirm.setError(getString(R.string.err_msg_password_confirm));
            requestFocus(etConfirmNewPassword);
            return false;
        } else if (passwordConfirmUser.trim().length() < 6){
            layout_password_confirm.setError(getString(R.string.err_short_password));
            requestFocus(etConfirmNewPassword);
            return false;
        } else {
            layout_password_confirm.setErrorEnabled(false);
        }
        return true;
    }

    private boolean confirmationPassword(){
        return passwordUserNew.trim().equals(passwordConfirmUser);
    }

    private  boolean validatePasswords(){
        if (passwordCurrent == null || passwordCurrent.trim().isEmpty()) {
            return true;
        } else {
            if (validateCurrentPassword() && validateNewPassword() && validatePasswordConfirm()) {
                if (!confirmationPassword()) {
                    new AlertDialog.Builder(EditUserProfileActivity.this)
                            .setMessage(getString(R.string.err_passwords_do_not_match))
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    etNewPassword.setText("");
                                    etConfirmNewPassword.setText("");
                                    requestFocus(etNewPassword);
                                }
                            })
                            .create()
                            .show();
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.
                    SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

    //Código do PhotoCrop

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == REQUEST_CODE_UPDATE_PIC) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(Constants.IntentExtras.IMAGE_PATH);
                showCroppedImage(imagePath);
            } else if (resultCode == RESULT_CANCELED) {

            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showCroppedImage(String mImagePath) {
        if (mImagePath != null) {
            bitmapPhoto = BitmapFactory.decodeFile(mImagePath);
            setBitmapPhotoUser(bitmapPhoto);
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            bitmapPhoto.compress(Bitmap.CompressFormat.JPEG, 50, b);
            byte[] photo_user_byte = b.toByteArray();
            photoUser = Base64.encodeToString(photo_user_byte, Base64.NO_WRAP);
        }
    }

    private void showAddProfilePicDialog() {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(this);
        dialogFragment.show(getFragmentManager(), "picModeSelector");
    }

    private void actionProfilePic(String action) {
        Intent intent = new Intent(this, ImageCropActivity.class);
        intent.putExtra("ACTION", action);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_PIC);
    }


    @Override
    public void onPicModeSelected(String mode) {
        String action = mode.equalsIgnoreCase(Constants.PicModes.CAMERA) ?
                Constants.IntentExtras.ACTION_CAMERA : Constants.IntentExtras.ACTION_GALLERY;
        actionProfilePic(action);
    }

}
