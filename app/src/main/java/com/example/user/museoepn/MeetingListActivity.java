package com.example.user.museoepn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MeetingListActivity extends AppCompatActivity {


    private ArrayList<String> listaId;
    private ListView lstView;
    private ArrayList<String> listViewItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private BottomNavigationView bottomNavigationView;
    private User user = SharedPrefManager.getInstance(this).getUser();
    private final String username = user.getUsername();
    ArrayList<String>AvailableHours;

    CalendarView calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        AvailableHours = new ArrayList<String>();
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.MenuNavegacion);
        calendario = (CalendarView) findViewById(R.id.calendarView);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {


            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                //Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                final String date = dayOfMonth+"/"+(month+1)+"/"+year;
                /*if(!AvailableHours.isEmpty()){
                    Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), ":C", Toast.LENGTH_LONG).show();
                }*/
                Intent intent = new Intent(MeetingListActivity.this,MeetingCreateActivity.class);
                intent.putExtra("date",date);

                //intent.putExtra("listahoras", AvailableHours);

                startActivity(intent);
            }

        });

        showContact();
        //Horarios(fecha);
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




    public void Horarios(final String date){

        //final ArrayList<String> horarios = new ArrayList<String>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url3 ="http://192.168.1.116/fecha.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray contacts =
                                    obj.getJSONArray("reserva");
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);

                                String horario = c.getString("horario");

                                AvailableHours.add(horario);

                            }
                            //Log.d("My App", obj.toString());
                            //System.out.println(obj.toString());
                            //Toast.makeText(MeetingListActivity.this,"hola",Toast.LENGTH_LONG).show();
                        } catch (Throwable t) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MeetingListActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",date);
                return params;
            }
        };
        queue.add(stringRequest);




    }

    public void showContact(){

        listaId = new ArrayList<>();
        lstView = (ListView) findViewById(R.id.listaReservas);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listViewItems);
        lstView.setAdapter(adapter);

        //Intent incomingIntent = getIntent();
        //final String id = incomingIntent.getStringExtra("id");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url3 ="http://192.168.1.116/reserva2.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray contacts =
                                    obj.getJSONArray("reserva");
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);

                                String horario = c.getString("horario");
                                String fecha = c.getString("fecha");
                                adapter.add(""+fecha+"-"+horario);
                                //AvailableHours.add(horario);
                            }
                            Log.d("My App", obj.toString());
                        } catch (Throwable t) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MeetingListActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",username);
                return params;
            }
        };
        queue.add(stringRequest);
        adapter.notifyDataSetChanged();
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //String id =listaId.get(i);
                Intent intent = new Intent(MeetingListActivity.this,InfoMeetingActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);

            }
        });
    }


}
