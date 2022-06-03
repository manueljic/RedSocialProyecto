package com.example.redsocialproyecto.Opciones;

import java.util.Calendar;
import java.util.TimeZone;

public class Mensaje {
    String mensaje, nombre, fotoPerfil, type_mensaje,hora,urlFoto, keyEmisor;
    private boolean contieneFoto;
    private boolean soyEmisor;

    private String KEY_RECEPTOR;

    public String getKEY_RECEPTOR() {
        return KEY_RECEPTOR;
    }

    public void setKEY_RECEPTOR(String KEY_RECEPTOR) {
        this.KEY_RECEPTOR = KEY_RECEPTOR;
    }

    public boolean isSoyEmisor() {
        return soyEmisor;
    }

    public void setSoyEmisor(boolean soyEmisor) {
        this.soyEmisor = soyEmisor;
    }

    TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
    Calendar c = Calendar.getInstance(tz);
    int mHour =  c.get(Calendar.HOUR_OF_DAY);
    int mMinute = c.get(Calendar.MINUTE);

    String Hour = Integer.toString(mHour);
    String Minute = Integer.toString(mMinute);
    String Date = Hour+":"+Minute;

    public Mensaje() {
    }

    public Mensaje(String mensaje, String nombre, String fotoPerfil, String type_mensaje, String hora) {

        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.type_mensaje = type_mensaje;
        this.hora = Date;
    }

    public Mensaje(String mensaje, String nombre, String fotoPerfil, String type_mensaje, String hora, String urlFoto) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.type_mensaje = type_mensaje;
        this.hora = Date;
        this.urlFoto = urlFoto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }

    public String getHora() {
        return hora;
    }

    public boolean isContieneFoto() {
        return contieneFoto;
    }

    public void setContieneFoto(boolean contieneFoto) {
        this.contieneFoto = contieneFoto;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getKeyEmisor() {
        return keyEmisor;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public void setKeyEmisor(String keyEmisor) {
        this.keyEmisor = keyEmisor;
    }
}
