package com.example.user.museoepn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText txt_user_name, txt_email,txt_password;
    private RadioGroup radioGroupGender;
    private ProgressBar progressBar;
    //SharedPreferences SharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = (ProgressBar)findViewById(R.id.progressBarRegister);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        txt_user_name=(EditText)findViewById(R.id.txtNewUsername);
        txt_email=(EditText)findViewById(R.id.txtNewEmail);
        txt_password=(EditText)findViewById(R.id.txtNewPassword);
        radioGroupGender=(RadioGroup)findViewById(R.id.radioGender);

        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser(){
        final String user_name=txt_user_name.getText().toString().trim();
        final String password=txt_password.getText().toString().trim();
        final String email=txt_email.getText().toString().trim();

        final String gender = ((RadioButton)findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        if(TextUtils.isEmpty(user_name)){
            txt_user_name.setError("Por favor, ingrese el nombre de usuario");
            txt_user_name.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(email)){
            txt_email.setError("por favor, ingrese el correo electronico");
            txt_email.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txt_email.setError("ingrese un correo electrónico válido");
            txt_email.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)){
            txt_password.setError("por favor, ingrese una contraseña");
            txt_password.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("mensaje_error"), Toast.LENGTH_SHORT).show();
                        JSONObject userJson = obj.getJSONObject("user");

                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("gender"));
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("mensaje_correcto"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams()throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("username",user_name);
                params.put("email",email);
                params.put("password",password);
                params.put("gender",gender);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
