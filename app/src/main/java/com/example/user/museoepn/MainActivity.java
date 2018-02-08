package com.example.user.museoepn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView t_Id, t_Username, t_Email, t_Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,LogInActivity.class));
        }

        t_Id = (TextView)findViewById(R.id.restxtId);
        t_Username = (TextView)findViewById(R.id.restxtUsername);
        t_Email = (TextView)findViewById(R.id.restxtEmail);
        t_Gender = (TextView)findViewById(R.id.restxtGender);

        User user = SharedPrefManager.getInstance(this).getUser();

        t_Id.setText(String.valueOf(user.getId()));
        t_Username.setText(user.getUsername());
        t_Email.setText(user.getEmail());
        t_Gender.setText(user.getGender());

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }
}
