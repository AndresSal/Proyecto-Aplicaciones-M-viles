package com.example.user.museoepn;

/**
 * Created by user on 12/2/2018.
 */

public class Meeting {

    private int id_reserva;
    private String username, email, fecha, horario, motivo, nombre_institucion, num_personas;

    public Meeting(int id_reserva, String username, String email, String fecha, String horario, String motivo, String nombre_institucion, String num_personas) {
        this.id_reserva =id_reserva;
        this.username=username;
        this.email=email;
        this.fecha=fecha;
        this.horario=horario;
        this.motivo=motivo;
        this.nombre_institucion=nombre_institucion;
        this.num_personas=num_personas;
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getNum_personas() {
        return num_personas;
    }

    public void setNum_personas(String num_personas) {
        this.num_personas = num_personas;
    }
}
