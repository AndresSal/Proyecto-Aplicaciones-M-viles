package com.example.user.museoepn;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MeetingCreateActivity extends AppCompatActivity  {

    private static final String TAG = "MeetingCreateActivity";
    private TextView fecha, username;
    private  DatePickerDialog.OnDateSetListener DateSetListener;
    private RadioGroup horario;
    private Spinner motivo;
    private EditText nombre_institucion, num_personas;
    private Button btnReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_create);
        username = (TextView) findViewById(R.id.mettxtUsername);
        fecha = (TextView) findViewById(R.id.mettxtDatePicked);
        horario = (RadioGroup) findViewById(R.id.radioHour);
        motivo = (Spinner) findViewById(R.id.spinnerVisita);
        nombre_institucion = (EditText) findViewById(R.id.txtInstitucion);
        num_personas = (EditText) findViewById(R.id.txtPersonas);
        btnReserva = (Button)findViewById(R.id.btnCrearReserva);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,LogInActivity.class));
        }

        User user = SharedPrefManager.getInstance(this).getUser();
        username.setText(user.getUsername());
        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        fecha.setText(date);


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
                fecha.setText(date);
            }
        };
        motivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                if(item.equals("educativo")){
                    nombre_institucion.setVisibility(View.VISIBLE);
                }
                else{
                    nombre_institucion.setVisibility(View.GONE);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewMeeting();
            }
        });

    }

    private void registerNewMeeting(){

        //definir cada uno de las columnas del a ser llenadas

        final String _username=username.getText().toString().trim();
        final String _email = SharedPrefManager.getInstance(this).getUser().getEmail();
        final String _fecha = fecha.getText().toString().trim();
        final String _horario = ((RadioButton)findViewById(horario.getCheckedRadioButtonId())).getText().toString();
        final String _motivo = String.valueOf((motivo.getSelectedItem()));
        final String _nombre_institucion = nombre_institucion.getText().toString().trim();
        final String _num_personas = num_personas.getText().toString().trim();

        //definir condiciones en el ingreso de datos
        if(TextUtils.isEmpty(_nombre_institucion)){
            nombre_institucion.setError("Por favor ingrese el nombre de la institución");
            nombre_institucion.requestFocus();
        }
        if(TextUtils.isEmpty(_num_personas)){
            num_personas.setError("Por favor ingrese el número de personas");
            num_personas.requestFocus();
        }

        StringRequest SR = new StringRequest(Request.Method.POST, URLs.URL_NEW_MEET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    //en caso de una consulta exitosa
                    if(!obj.getBoolean("error")){
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject meetingJSON = obj.getJSONObject("reserva");
                        Meeting meeting = new Meeting(
                                meetingJSON.getInt("id_reserva"),
                                meetingJSON.getString("username"),
                                meetingJSON.getString("email"),
                                meetingJSON.getString("fecha"),
                                meetingJSON.getString("horario"),
                                meetingJSON.getString("motivo"),
                                meetingJSON.getString("nombre_institucion"),
                                meetingJSON.getString("num_personas"));

                        //guardar en el SharedPrefManager el meeting creado
                        SharedPrefManager.getInstance(getApplicationContext()).meetingUser(meeting);

                        finish();
                        startActivity(new Intent(getApplicationContext(),MeetingListActivity.class));
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
                params.put("username",_username);
                params.put("email",_email);
                params.put("fecha",_fecha);
                params.put("horario",_horario);
                params.put("motivo",_motivo);
                params.put("nombre_institucion",_nombre_institucion);
                params.put("num_personas",_num_personas);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(SR);
    }
}
