package projeto1.ufcg.edu.decasa.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import projeto1.ufcg.edu.decasa.R;

public class MyFavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);
    }

    public static void setView(Context context, Class classe){
        Intent it = new Intent();
        it.setClass(context, classe);
        context.startActivity(it);
    }
}