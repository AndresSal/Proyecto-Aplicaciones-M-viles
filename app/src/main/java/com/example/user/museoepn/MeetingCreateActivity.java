package com.example.user.museoepn;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class MeetingCreateActivity extends AppCompatActivity  {

   private TextView dateMet,name;
   private static final String TAG = "MeetingCreateActivity";
   private  DatePickerDialog.OnDateSetListener DateSetListener;
   private Spinner spinner;
   private EditText txtinstitucion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_create);
        spinner = (Spinner) findViewById(R.id.spinnerVisita);
        txtinstitucion = (EditText) findViewById(R.id.txtInstitucion);

        name = (TextView) findViewById(R.id.mettxtUsername);
        dateMet = (TextView) findViewById(R.id.mettxtDatePicked);
        User user = SharedPrefManager.getInstance(this).getUser();
        name.setText(user.getUsername());
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        dateMet.setText(date);


        findViewById(R.id.mettxtDatePicked).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar. YEAR );
                int month = calendar.get(Calendar. MONTH );
                int day = calendar.get(Calendar. DAY_OF_MONTH );

                DatePickerDialog dialog = new DatePickerDialog(
                        MeetingCreateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListener,
                        year, month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String date = day +"/"+(month+1)+"/"+year;
                dateMet.setText(date);
            }
        };
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                if(item.equals("educativo")){
                    txtinstitucion.setVisibility(View.VISIBLE);
                }
                else{
                    txtinstitucion.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

   






}
