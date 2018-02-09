package com.example.user.museoepn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MeetingListActivity extends AppCompatActivity {

    private ListView lstView;
    private ArrayList<String> listViewItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    CalendarView calendario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

        calendario = (CalendarView) findViewById(R.id.calendarView);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {


            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                //Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                Intent intent = new Intent(MeetingListActivity.this,MeetingCreateActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });


        lstView = (ListView)findViewById(R.id.listaReservas);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listViewItems);
        lstView.setAdapter(adapter);
        adapter.add("Reserva 1");
        adapter.add("Reserva 2");
        adapter.add("Reserva 3");
        adapter.notifyDataSetChanged();

    }
}
