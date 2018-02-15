package com.example.user.museoepn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoMeetingActivity extends AppCompatActivity {
TextView txtUser,txtFecha,txtMeeting,txtInstitucion,txtPersonas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_meeting);
        showContact();

    }

    public void showContact(){

         txtUser= (TextView) findViewById(R.id.txtvUsername);
        txtFecha = (TextView) findViewById(R.id.txtvDatePicked);
        txtMeeting = (TextView) findViewById(R.id.txtvMeeting);
        txtInstitucion = (TextView) findViewById(R.id.txtvInstitucion);
        txtPersonas = (TextView) findViewById(R.id.txtvPersonas);
        Intent incomingIntent = getIntent();
        final String id = incomingIntent.getStringExtra("id");
        Toast.makeText(InfoMeetingActivity.this,id.toString(),Toast.LENGTH_LONG).show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url3 ="http://192.168.100.38:85/Museo/reserva2.php";
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
                                String name = c.getString("username");
                                String fecha = c.getString("fecha");
                                String motivo = c.getString("montivo");
                                String institucion = c.getString("nombre_institucion");
                                String personas = c.getString("num_personas");
                                txtUser.setText(name);
                                txtFecha.setText(fecha);
                                txtMeeting.setText(motivo);
                                txtInstitucion.setText(institucion);
                                txtPersonas.setText(personas);
                            }
                            Log.d("My App", obj.toString());
                        } catch (Throwable t) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(InfoMeetingActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id_reserva", id);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
