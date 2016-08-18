package projeto1.ufcg.edu.decasa.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import projeto1.ufcg.edu.decasa.R;

public class EditUserProfileActivity extends AppCompatActivity {

    private List<String> genders;
    private List<String> states;

    private EditText etDateOfBirth;

    private String genderUser;
    private String stateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        etDateOfBirth = (EditText) findViewById(R.id.et_date_of_birth);

        MaskEditTextChangedListener maskDateOfBirth = new MaskEditTextChangedListener("##/##/####",
                etDateOfBirth);
        etDateOfBirth.addTextChangedListener(maskDateOfBirth);


        Spinner spGenders = (Spinner) findViewById(R.id.sp_gender);
        Spinner spStates = (Spinner) findViewById(R.id.sp_state);

        putGenderElementsOnSpinnerArray();
        spGenders.setAdapter(createArrayAdapterGender());

        spGenders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spStates.setAdapter(createArrayAdapterState());

        spStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos,
                                       final long id) {
                Object item = parent.getItemAtPosition(pos);
                stateUser = item.toString();
            }

            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

}
