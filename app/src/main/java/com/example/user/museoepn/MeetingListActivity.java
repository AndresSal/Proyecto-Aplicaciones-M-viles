package com.example.user.museoepn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MeetingListActivity extends AppCompatActivity {

    private ListView lstView;
    private ArrayList<String> listViewItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private BottomNavigationView bottomNavigationView;

    CalendarView calendario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.MenuNavegacion);
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
        adapter.add("Reserva 4");
        adapter.notifyDataSetChanged();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home_I:
                        startActivity(new Intent(MeetingListActivity.this,MainActivity.class));
                        break;
                    case R.id.reserva_I:
                        startActivity(new Intent(MeetingListActivity.this,MeetingListActivity.class));
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
}
