package com.example.redsocialproyecto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LUsuario {

    private Usuario usuario;
    private String key;

    //Constructor
    public LUsuario(Usuario usuario, String key) {
        this.usuario = usuario;
        this.key = key;
    }

    //Sets and gets

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    //Sets and gets

    //Métodos
    public String obtenerFechaDeCreacion(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(UsuarioDAO.getInstancia().fechaDeCreacionLong());
        return simpleDateFormat.format(date);
    }

    public String obtenerFechaDeUltimaVezQueSeLogeo(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(UsuarioDAO.getInstancia().fechaDeUltimaVezQueSeLogeoLong());
        return simpleDateFormat.format(date);
    }
}
