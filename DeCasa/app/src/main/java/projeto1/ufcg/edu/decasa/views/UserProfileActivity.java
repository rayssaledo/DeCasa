package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.controllers.UserController;
import projeto1.ufcg.edu.decasa.models.User;
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView ivPhotoUserProfile;
    private TextView tvNameUser;
    private TextView tvDateOfBirth;
    private TextView tvGender;
    private TextView tvAddressUser;
    private MySharedPreferences mySharedPreferences;
    private UserController userController;
    private List<User> userList;
    private User user;

    public static View loadingUserProfile;

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
        setContentView(R.layout.activity_user_profile);

        loadingUserProfile =  findViewById(R.id.rl_loading_user_profile);

        userController = new UserController(UserProfileActivity.this);
        mySharedPreferences = new MySharedPreferences(getApplicationContext());

        ivPhotoUserProfile = (ImageView) findViewById(R.id.iv_photo_user_profile);
        tvNameUser = (TextView) findViewById(R.id.tv_name_user);
        tvDateOfBirth = (TextView) findViewById(R.id.tv_date_of_birth);
        tvGender = (TextView) findViewById(R.id.tv_gender);
        tvAddressUser = (TextView) findViewById(R.id.tv_address_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = mySharedPreferences.getUserLogged();
        userList = userController.getUser(username, handler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
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

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent intent = new Intent(UserProfileActivity.this,
                    EditUserProfileActivity.class);
            intent.putExtra("USER", user);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFields() {

        tvNameUser.setText(user.getName());
        tvDateOfBirth.setText(user.getDateOfBirth());
        if (user.getGender().equals("F"))  {
            tvGender.setText(getApplication().getString(R.string.female));
        } else {
            tvGender.setText(getApplication().getString(R.string.male));
        }

        String addressUser = user.getStreet() + ", " + user.getNumber() + ", " +
                user.getNeighborhood() + ", " + user.getCity() + " - " + user.getState();
        tvAddressUser.setText(addressUser);

        String photoUser = user.getPhoto();
        byte[] photoByte = Base64.decode(photoUser, Base64.DEFAULT);
        Bitmap bitmapPhoto = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);
        ivPhotoUserProfile.setImageBitmap(bitmapPhoto);

    }

}
