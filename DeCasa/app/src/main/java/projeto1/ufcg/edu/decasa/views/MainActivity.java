package projeto1.ufcg.edu.decasa.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.adapters.DrawerListAdapter;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.NavItem;
import projeto1.ufcg.edu.decasa.models.User;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class MainActivity extends AppCompatActivity {

    public static Activity mMainActivity;

    private Intent it;
    private String service;
    private MySharedPreferences mySharedPreferences;
    private UserController userController;
    private List<User> listUser;
    private TextView tv_message_welcome;

    private Button btn_register;
    private TextView tv_welcome;

    private String username;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 103) {
                if (listUser.size() == 1) {
                    String fullName = listUser.get(0).getName();
                    String[] name = fullName.split(" ");
                    String firstName = name[0];
                    String gender = listUser.get(0).getGender();
                    if (gender.equals("F")) {
                        tv_welcome.setText(getApplication().getString(R.string.begin_welcome_female) + " " +
                                firstName +", " + getApplication().getString(R.string.end_welcome));
                    } else {
                        tv_welcome.setText(getApplication().getString(R.string.begin_welcome_male) + " " +
                                firstName +", " + getApplication().getString(R.string.end_welcome));
                    }
                } else {
                    tv_welcome.setText(getApplication().getString(R.string.text_welcome));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainActivity = this;

        mySharedPreferences = new MySharedPreferences(getApplicationContext());
        userController = new UserController(MainActivity.this);

        btn_register = (Button) findViewById(R.id.btn_register);
        tv_welcome = (TextView) findViewById(R.id.welcome);
        HashMap<String, String> userDetails = mySharedPreferences.getUserDetails();
        username = userDetails.get(MySharedPreferences.KEY_USERNAME_USER);

        ImageButton ib_electrician = (ImageButton) findViewById(R.id.ib_electrician);
        ImageButton ib_plumber = (ImageButton) findViewById(R.id.ib_plumber);
        ImageButton ib_fitter = (ImageButton) findViewById(R.id.ib_fitter);
        Button btn_register = (Button)  findViewById(R.id.btn_register);

        it = new Intent(MainActivity.this, ProfessionalsActivity.class);

        ib_electrician.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                service = getApplication().getString(R.string.title_electricians);
                mySharedPreferences.saveService(service);
                it.putExtra("SERVICE",service);
                startActivity(it);
            }
        });

        ib_plumber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                service = getApplication().getString(R.string.title_plumbers);
                mySharedPreferences.saveService(service);
                it.putExtra("SERVICE",service);
                startActivity(it);
            }
        });

        ib_fitter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                service = getApplication().getString(R.string.title_fitters);
                mySharedPreferences.saveService(service);
                it.putExtra("SERVICE",service);
                startActivity(it);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(MainActivity.this, UserCadastreActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateInformationMenu();
        if (mySharedPreferences.isUserLoggedIn()){
            listUser = userController.getUser(username, myHandler);
        }
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
                    mDrawerLayout.closeDrawers();
                    setView(MainActivity.this, UserProfileActivity.class);
                }
            });
        } else {
            tv_name_user_or_welcome.setText(getApplication().getString(R.string.welcome));
            btn_profile_or_login.setText(getApplication().getString(R.string.btn_enter));
            btn_profile_or_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.closeDrawers();
                    setView(MainActivity.this, LoginActivity.class);
                }
            });
        }
    }


    public void updateInformationMenu() {
        if (mySharedPreferences.isUserLoggedIn()) {
            btn_register.setVisibility(View.GONE);
        }
    }

    public void setmDrawer(ArrayList<NavItem> mNavItems) {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        final ListView mDrawerList = (ListView) findViewById(R.id.navList);
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
                    if (position == 0) { // My Favorites
                        mDrawerLayout.closeDrawers();
                        setView(MainActivity.this, MyFavoritesActivity.class);
                    } else if (position == 1) { // About
                        mDrawerLayout.closeDrawers();
                        setView(MainActivity.this, AboutActivity.class);
                    } else if (position == 2) { // Logout
                        mDrawerLayout.closeDrawers();
                        mySharedPreferences.logoutUser();
                        setView(MainActivity.this, MainActivity.class);
                        if (MainActivity.mMainActivity != null){
                            MainActivity.mMainActivity.finish();
                        }
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
                    if (position == 0) { //About
                        mDrawerLayout.closeDrawers();
                        setView(MainActivity.this, AboutActivity.class);
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
                MainActivity.this,
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
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