package projeto1.ufcg.edu.decasa.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.adapters.ProfessionalsAdapter;
import projeto1.ufcg.edu.decasa.controllers.ProfessionalController;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class ProfessionalsActivity extends AppCompatActivity {

    private ListView listViewProfessionals;
    private static List<Professional> listProfessionals;
    private ProfessionalsAdapter professionalsAdapter;
    private ProfessionalController professionalController;
    private String service;
    private Button btnFindNearest;
    public static View mLoadingProfessionals;
    private EditText ed_address;
    private TextInputLayout til_address;
    private String address;
    private MySharedPreferences mySharedPreferences;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                listViewProfessionals.setAdapter(professionalsAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professionals);

        mySharedPreferences = new MySharedPreferences(getApplicationContext());

        mLoadingProfessionals = findViewById(R.id.rl_loading_professionals);

        listViewProfessionals = (ListView) findViewById(R.id.lv_professionals);
        btnFindNearest = (Button) findViewById(R.id.btn_find_nearest);
        Intent it = getIntent();
        service = (String) it.getSerializableExtra("SERVICE");
        setTitle(service);

        professionalController = new ProfessionalController(ProfessionalsActivity.this);

        setList(service);

        listViewProfessionals.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mySharedPreferences.isUserLoggedIn()) {
                    Professional professional = (Professional) professionalsAdapter.getItem(position);
                    Intent intent = new Intent(ProfessionalsActivity.this,
                            ProfileProfessionalActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else {
                    setView(ProfessionalsActivity.this, CadastreOrLoginActivity.class);
                }
            }
        });

        btnFindNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogFindNearest = new Dialog(ProfessionalsActivity.this);
                dialogFindNearest.setContentView(R.layout.dialog_find_nearest);
                dialogFindNearest.setTitle(getApplication().getString(R.string.title_dialog_find_nearest));
                dialogFindNearest.setCancelable(true);

                final RadioButton rd1 = (RadioButton) dialogFindNearest.findViewById(R.id.rd1);
                final RadioButton rd2 = (RadioButton) dialogFindNearest.findViewById(R.id.rd2);
                final Button okButton = (Button) dialogFindNearest.findViewById(R.id.btn_ok);
                final Button cancelButton = (Button) dialogFindNearest.findViewById(R.id.btn_cancel);

                rd1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rd1.setChecked(true);
                        rd2.setChecked(false);
                    }
                });
                rd2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rd2.setChecked(true);
                        rd1.setChecked(false);
                    }
                });

                dialogFindNearest.show();

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogFindNearest.dismiss();
                    }
                });

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (rd1.isChecked()) {
                            LocationManager manager = (LocationManager) getSystemService(Context.
                                    LOCATION_SERVICE);
                            boolean isOn = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);
                            if(isOn) {
                                setView(ProfessionalsActivity.this, MapsActivity.class);
                            } else {
                                displayPromptForEnablingGPS(ProfessionalsActivity.this,
                                        getApplication().getString(R.string.message_dialog_gps),
                                        getApplication().getString(R.string.cancel) );
                            }
                            mySharedPreferences.saveUserLocation(null);
                            dialogFindNearest.dismiss();
                        } else if (rd2.isChecked()) {
                            final Dialog dialogAddressForm = new Dialog(ProfessionalsActivity.this);
                            dialogAddressForm.setContentView(R.layout.dialog_address_form);
                            dialogAddressForm.setTitle(getApplication().getString(R.string.title_dialog_address_form));
                            dialogAddressForm.setCancelable(true);
                            dialogAddressForm.show();

                            final EditText input_street = (EditText) dialogAddressForm.
                                    findViewById(R.id.input_address);

                            final Button btn_ok = (Button) dialogAddressForm.findViewById(R.id.btn_ok);

                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ed_address = (EditText) dialogAddressForm.
                                            findViewById(R.id.input_address);

                                    til_address = (TextInputLayout) dialogAddressForm.
                                            findViewById(R.id.input_layout_address);

                                    address = ed_address.getText().toString();

                                    mySharedPreferences.saveUserLocation(address);
                                    setView(ProfessionalsActivity.this, MapsActivity.class);

                                }
                            });

                            final Button btn_cancel = (Button) dialogAddressForm.findViewById(R.id.btn_cancel);

                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogAddressForm.dismiss();
                                }
                            });

                        }
                    }
                });
            }
        });
    }

    public void setList(String service){
        listProfessionals = new ArrayList<>();
        if (service.equals(getApplication().getString(R.string.title_electricians))){
            listProfessionals = professionalController.getProfessionalsByService("Eletricista",
                    handler);
        } else  if (service.equals(getApplication().getString(R.string.title_plumbers))){
            listProfessionals = professionalController.getProfessionalsByService("Encanador",
                    handler);
        } else {
            listProfessionals = professionalController.getProfessionalsByService("Montador",
                    handler);
        }
        professionalsAdapter = new ProfessionalsAdapter(ProfessionalsActivity.this,
                listProfessionals);
        listViewProfessionals.setAdapter(professionalsAdapter);
    }

    public static void displayPromptForEnablingGPS(final Activity activity, String message,
                                                   String cancel) {

        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton(cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }

    public static void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        context.startActivity(it);
    }

}