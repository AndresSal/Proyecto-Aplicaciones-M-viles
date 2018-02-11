package com.example.user.museoepn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView t_Id, t_Username, t_Email, t_Gender;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,LogInActivity.class));
        }

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.MenuNavegacion);
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

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home_I:
                        startActivity(new Intent(MainActivity.this,MainActivity.class));
                        break;
                    case R.id.reserva_I:
                        startActivity(new Intent(MainActivity.this,MeetingListActivity.class));
                        break;
                    case R.id.salir_I:
                        finish();
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                        break;
                }
                return false;
            }
        });
    }

    public void llamarListaReservas(View view)
    {
        Intent intent = new Intent(this, MeetingListActivity.class);
        startActivity(intent);
    }
}
