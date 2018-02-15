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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
                String date = dayOfMonth+"/"+(month+1)+"/"+year;
               // AvailableHours = getHours(date);
                Intent intent = new Intent(MeetingListActivity.this,MeetingCreateActivity.class);
                intent.putExtra("date",date);
                intent.putExtra("listahoras", AvailableHours);
                startActivity(intent);
            }
        });


        getReservas();
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

    /*private ArrayList<String> getHours(String horario){
        final ArrayList<String> ListaHours = new ArrayList<String>();

        final String _horario = horario;

        StringRequest SR = new StringRequest(Request.Method.POST, URLs.URL_NEW_HORARIOS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    //en caso de una consulta exitosa
                    if(!obj.getBoolean("error")){
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONArray resultados = obj.getJSONArray("hours");
                        for (int i = 0; i < resultados.length(); i++){
                            JSONObject h = resultados.getJSONObject(i);
                            ListaHours.add(h.getString("horario"));
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("horario",_horario);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(SR);
        return ListaHours;
    }*/

    public void getReservas(){
        listaId = new ArrayList<>();
        lstView = (ListView) findViewById(R.id.listaReservas);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listViewItems);
        lstView.setAdapter(adapter);
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url2 ="https://api.androidhive.info/contacts/";
        String url2 ="http://192.168.100.38:85/Museo/reservas.php";

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new
                        Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject jsonObj = new
                                            JSONObject(response.toString());
                                    JSONArray reservas = jsonObj.getJSONArray("reserva");
                                    for (int i = 0; i < reservas.length(); i++) {
                                        JSONObject c = reservas.getJSONObject(i);
                                        String id = c.getString("id_reserva");
                                        Log.d(" el id ",id);
                                        String horario = c.getString("horario");

                                        adapter.add(horario);
                                        listaId.add(id);
                                    }
                                }
                                catch (final JSONException e) {
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // mTextView.setText("That didn't work!");
                    }
                });
        queue.add(jsObjRequest);
        adapter.notifyDataSetChanged();
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String id =listaId.get(i);
                Intent intent = new Intent(MeetingListActivity.this,InfoMeetingActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });
    }
}
