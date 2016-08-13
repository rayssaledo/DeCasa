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
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.adapters.DrawerListAdapter;
import projeto1.ufcg.edu.decasa.adapters.ProfessionalsAdapter;
import projeto1.ufcg.edu.decasa.controllers.ProfessionalController;
import projeto1.ufcg.edu.decasa.models.NavItem;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class ProfessionalsActivity extends AppCompatActivity {

    private ListView listViewProfessionals;
    private ProfessionalsAdapter professionalsAdapter;
    private ProfessionalController professionalController;
    public static View mLoadingProfessionals;
    private EditText ed_address;
    private TextInputLayout til_address;
    private String address;
    private MySharedPreferences mySharedPreferences;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;

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
        Button btnFindNearest = (Button) findViewById(R.id.btn_find_nearest);
        Intent it = getIntent();
        String service = (String) it.getSerializableExtra("SERVICE");
        setTitle(service);

        professionalController = new ProfessionalController(ProfessionalsActivity.this);

        setList(service);

        listViewProfessionals.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Professional professional = (Professional) professionalsAdapter.getItem(position);
                if (mySharedPreferences.isUserLoggedIn()) {
                    Intent intent = new Intent(ProfessionalsActivity.this,
                            ProfileProfessionalActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ProfessionalsActivity.this,
                            CadastreOrLoginActivity.class);
                    intent.putExtra("PROFESSIONAL", professional);
                    startActivity(intent);
                }
            }
        });

        btnFindNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogChoose();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationMenu();
    }

    public void updateNavigationMenu() {

        ArrayList<NavItem> mNavItems = new ArrayList<>();
        setmDrawer(mNavItems);

        Button btn_profile_or_login = (Button) findViewById(R.id.btn_profile_or_login);
        TextView tv_name_user_or_welcome = (TextView) findViewById(R.id.tv_name_user_or_welcome);

        if (mySharedPreferences.isUserLoggedIn()) {
            HashMap<String, String> userDetails = mySharedPreferences.getUserDetails();
            String username = userDetails.get(MySharedPreferences.KEY_USERNAME_USER);
            tv_name_user_or_welcome.setText(username);
            btn_profile_or_login.setText(getApplication().getString(R.string.btn_view_profile));
            btn_profile_or_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Setar par a tela de perfil do usuário
                }
            });
        } else {
            tv_name_user_or_welcome.setText(getApplication().getString(R.string.welcome));
            btn_profile_or_login.setText(getApplication().getString(R.string.btn_enter));
            btn_profile_or_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setView(ProfessionalsActivity.this, LoginActivity.class);
                }
            });
        }
    }

    private void createDialogChoose() {
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
                        dialogFindNearest.dismiss();
                    } else {
                        displayPromptForEnablingGPS(ProfessionalsActivity.this,
                                getApplication().getString(R.string.message_dialog_gps),
                                getApplication().getString(R.string.cancel), dialogFindNearest);
                    }
                    mySharedPreferences.saveUserLocation(null);
                } else if (rd2.isChecked()) {
                    createDialogAddress(dialogFindNearest);
                }
            }
        });
    }

    private void createDialogAddress(final Dialog dialogFindNearest) {
        final Dialog dialogAddress = new Dialog(ProfessionalsActivity.this);
        dialogAddress.setContentView(R.layout.dialog_address);
        dialogAddress.setTitle(getApplication().getString(R.string.title_dialog_address_form));
        dialogAddress.setCancelable(true);
        dialogAddress.show();

        final EditText input_street = (EditText) dialogAddress.findViewById(R.id.input_address);
        final Button btn_ok = (Button) dialogAddress.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_address = (EditText) dialogAddress.
                        findViewById(R.id.input_address);
                til_address = (TextInputLayout) dialogAddress.
                        findViewById(R.id.input_layout_address);
                address = ed_address.getText().toString();
                mySharedPreferences.saveUserLocation(address);
                setView(ProfessionalsActivity.this, MapsActivity.class);
                dialogAddress.dismiss();
                dialogFindNearest.dismiss();
            }
        });

        final Button btn_cancel = (Button) dialogAddress.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddress.dismiss();
            }
        });
    }

    public void setList(String service){
        List<Professional> listProfessionals = new ArrayList<>();
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
        Log.d("SIZELIST", listProfessionals.size()+"");
        professionalsAdapter = new ProfessionalsAdapter(ProfessionalsActivity.this,
                listProfessionals);
        listViewProfessionals.setAdapter(professionalsAdapter);
    }

    public static void displayPromptForEnablingGPS(final Activity activity, String message,
                                                   String cancel, final Dialog dialogFindNearest) {

        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                                dialogFindNearest.dismiss();
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

    public void setmDrawer(ArrayList<NavItem> mNavItems) {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        ListView mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter2 = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter2);

        if (mySharedPreferences.isUserLoggedIn()) {
            mNavItems.add(new NavItem(getApplication().getString(R.string.text_my_favorites),
                    R.mipmap.ic_favorite_black_24dp));
            mNavItems.add(new NavItem(getApplication().getString(R.string.text_about),
                    R.mipmap.ic_info_black_24dp));
            mNavItems.add(new NavItem(getApplication().getString(R.string.text_logout),
                    R.mipmap.ic_exit_to_app_black_24dp));

            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) { // My favorites
//                    mDrawerLayout.closeDrawer(mDrawerPane);
//                    setView(ProfessionalsActivity.this, DonorsActivity.class);
                    } else if (position == 1) { // About
                        mDrawerLayout.closeDrawer(mDrawerPane);
                        setView(ProfessionalsActivity.this, AboutActivity.class);
                    } else if (position == 2) { // Logout
                        mDrawerLayout.closeDrawer(mDrawerPane);
                        mySharedPreferences.logoutUser();
                        setView(ProfessionalsActivity.this, MainActivity.class);
                        finish();
                    }
                }
            });
        } else {
            mNavItems.add(new NavItem(getApplication().getString(R.string.text_about),
                    R.mipmap.ic_info_black_24dp));

            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) { // About
                        mDrawerLayout.closeDrawer(mDrawerPane);
                        setView(ProfessionalsActivity.this, AboutActivity.class);
                    }
                }
            });
        }


        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
        actionBar.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        CharSequence mTitle = getTitle().toString();
        mDrawerToggle = new ActionBarDrawerToggle(
                ProfessionalsActivity.this,
                mDrawerLayout,
                R.mipmap.ic_menu_white_24dp,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        context.startActivity(it);
    }
}