package projeto1.ufcg.edu.decasa.views;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import projeto1.ufcg.edu.decasa.R;

public class ProfileProfessionalActivity extends AppCompatActivity {

    private ImageButton ib_call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_professional);

        ib_call = (ImageButton) findViewById(R.id.ib_phone_professional);

        ib_call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:" + "987098580");//TODO colocar telefone do profissional quando recuperar
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });
    }
}
