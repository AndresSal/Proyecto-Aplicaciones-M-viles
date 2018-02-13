package com.example.user.museoepn;

/**
 * Created by user on 12/2/2018.
 */

public class Meeting {

    private int id_user, id_meeting, num_personas;
    private String fecha, horario, motivo, nombre_institucion;

    public Meeting(int iduser, int idmeeting, int numpersonas, String fecha, String horario, String motivo, String nombreinstitucion) {
        this.id_user=iduser;
        this.id_meeting=idmeeting;
        this.num_personas=numpersonas;
        this.fecha=fecha;
        this.horario=horario;
        this.motivo=motivo;
        this.nombre_institucion=nombreinstitucion;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_meeting() {
        return id_meeting;
    }

    public void setId_meeting(int id_meeting) {
        this.id_meeting = id_meeting;
    }

    public int getNum_personas() {
        return num_personas;
    }

    public void setNum_personas(int num_personas) {
        this.num_personas = num_personas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getNombre_institucion() {
        return nombre_institucion;
    }

    public void setNombre_institucion(String nombre_institucion) {
        this.nombre_institucion = nombre_institucion;
    }
}
