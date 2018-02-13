package com.example.user.museoepn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by user on 7/2/2018.
 */

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_ID = "keyid";
    private static final String KEY_ID_RESERVA="keyidreserva";
    private static final String KEY_FECHA = "keyfecha";
    private static final String KEY_HORARIO = "keyhorario";
    private static final String KEY_MOTIVO= "keymotivo";
    private static final String KEY_NOMBRE_INSTITUCION ="keynombreinstitucion";
    private static final String KEY_NUM_PERSONAS="keynumpersonas";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_GENDER, user.getGender());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public void meetingUser(Meeting meet){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID_RESERVA, meet.getId_reserva());
        editor.putString(KEY_USERNAME, meet.getUsername());
        editor.putString(KEY_EMAIL, meet.getEmail());
        editor.putString(KEY_FECHA, meet.getFecha());
        editor.putString(KEY_HORARIO, meet.getHorario());
        editor.putString(KEY_MOTIVO, meet.getMotivo());
        editor.putString(KEY_NOMBRE_INSTITUCION,meet.getNombre_institucion());
        editor.putString(KEY_NUM_PERSONAS, meet.getNum_personas());
        editor.apply();
    }

    public Meeting getMeeting(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Meeting(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_FECHA, null),
                sharedPreferences.getString(KEY_HORARIO, null),
                sharedPreferences.getString(KEY_MOTIVO,null),
                sharedPreferences.getString(KEY_NOMBRE_INSTITUCION,null),
                sharedPreferences.getString(KEY_NUM_PERSONAS,null)
        );
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_GENDER, null)
        );
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LogInActivity.class));
    }
}
