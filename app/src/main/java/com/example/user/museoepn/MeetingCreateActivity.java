package com.example.user.museoepn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Date;

public class MeetingCreateActivity extends AppCompatActivity  {

   private TextView dateMet,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_create);

        name = (TextView) findViewById(R.id.mettxtUsername);
        dateMet = (TextView) findViewById(R.id.mettxtDatePicked);
        User user = SharedPrefManager.getInstance(this).getUser();
        name.setText(user.getUsername());
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        dateMet.setText(date);
    }


}
