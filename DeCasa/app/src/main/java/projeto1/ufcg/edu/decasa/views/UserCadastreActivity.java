package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

import projeto1.ufcg.edu.decasa.R;

public class UserCadastreActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_photo_user;
    private ImageButton ib_camera;
    private ImageButton ib_gallery;
    private ImageButton ib_delete;
    private static final int RESULT_CAMERA = 111;
    private static final int RESULT_GALLERY = 222;
    private Bitmap bitmapPhoto;
    private EditText mDate_of_birth;
    private Spinner mGenders_spinner;
    private Spinner mStates_spinner;
    private List<String> genders;
    private List<String> states;
    private String mGender_user;
    private String mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cadastre);

        Bitmap avatar = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
        bitmapPhoto = avatar;
        iv_photo_user = (ImageView) findViewById(R.id.iv_user_photo);
        ib_camera = (ImageButton) findViewById(R.id.ib_camera);
        ib_camera.setOnClickListener(this);
        ib_gallery = (ImageButton) findViewById(R.id.ib_gallery);
        ib_gallery.setOnClickListener(this);
        ib_delete = (ImageButton) findViewById(R.id.ib_delete);
        ib_delete.setOnClickListener(this);

        mDate_of_birth = (EditText) findViewById(R.id.input_date_of_birth);
        mGenders_spinner = (Spinner) findViewById(R.id.sp_gender);
        mStates_spinner = (Spinner) findViewById(R.id.sp_state);

        MaskEditTextChangedListener maskDateOfBirth = new MaskEditTextChangedListener("##/##/####",
                mDate_of_birth);
        mDate_of_birth.addTextChangedListener(maskDateOfBirth);

        putGenderElementsOnSpinnerArray();
        ArrayAdapter<String> spinnerArrayAdapterGenders = new ArrayAdapter<>(this, android.R.
                layout.simple_spinner_item, genders);
        spinnerArrayAdapterGenders.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item); // The drop down view
        mGenders_spinner.setAdapter(spinnerArrayAdapterGenders);

        mGenders_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos,
                                       final long id) {
                Object item = parent.getItemAtPosition(pos);
                String itemGender = item.toString();

                if (itemGender.equals(getApplicationContext().getResources().
                        getString(R.string.female))) {
                    mGender_user = "F";
                } else if (itemGender.equals(getApplicationContext().getResources().
                        getString(R.string.male))) {
                    mGender_user = "M";
                }
            }
            public void onNothingSelected(final AdapterView<?> parent) {}
        });


        putBloodTypeElementsOnSpinnerArray();
        ArrayAdapter<String> spinnerArrayAdapterBloodTypes = new ArrayAdapter<>(this, android.R.
                layout.simple_spinner_item, states);
        spinnerArrayAdapterBloodTypes.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item); // The drop down view
        mStates_spinner.setAdapter(spinnerArrayAdapterBloodTypes);

        mStates_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos,
                                       final long id) {
                Object item = parent.getItemAtPosition(pos);
                mState = item.toString();
            }
            public void onNothingSelected(final AdapterView<?> parent) {}
        });

    }

    public void putGenderElementsOnSpinnerArray() {
        genders = new ArrayList<>();
        genders.add(getApplicationContext().getResources().getString(R.string.female));
        genders.add(getApplicationContext().getResources().getString(R.string.male));
    }

    public void putBloodTypeElementsOnSpinnerArray(){
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
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_GALLERY);
                break;
            default:
                Bitmap avatar = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
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
                iv_photo_user.setImageBitmap(Bitmap.createScaledBitmap(bitmapPhoto, 120, 120, true));
            }
        }
    }

}