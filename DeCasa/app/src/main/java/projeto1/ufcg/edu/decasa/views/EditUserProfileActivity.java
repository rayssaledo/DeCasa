package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
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

    private String genderUser;
    private String stateUser;
    private String photoUser;

    private MySharedPreferences mySharedPreferences;
    private String username;
    private UserController userController;
    private List<User> userList;
    private User user;

    private Bitmap bitmapPhoto;
    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 103) {
                if (userList.size() == 1) {
                    user = userList.get(0);
                    setFields();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

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
        userList = userController.getUser(username, handler);
    }

    private void setFields() {
        etNameUser.setText(user.getName());
        etDateOfBirth.setText(user.getDateOfBirth());
        etCity.setText(user.getCity());
        etNeighborhoodUser.setText(user.getNeighborhood());
        etStreetUser.setText(user.getStreet());
        etNumber.setText(user.getNumber());

        String photoUserString = user.getPhoto();
        byte[] photoByte = Base64.decode(photoUserString, Base64.DEFAULT);
        bitmapPhoto = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);
        ivPhotoUser.setImageBitmap(bitmapPhoto);

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
        states.set(0, user.getState());
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
            Bitmap myBitmap = BitmapFactory.decodeFile(mImagePath);
            bitmapPhoto = myBitmap;
            ivPhotoUser.setImageBitmap(bitmapPhoto);
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
