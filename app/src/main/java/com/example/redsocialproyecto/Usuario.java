package com.example.redsocialproyecto;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Usuario {

    DatabaseReference BASE_DE_DATOS;
    FirebaseUser user;
    FirebaseAuth firebaseauth;


    private String Nombres;
    private String imagenUserURL;
    private long fechaDeNacimiento;

    //Constructor
    public Usuario(){

    }

    //Sets and gets
    public String getImagenUserURL() {
        return imagenUserURL;
    }

    public void setImagenUserURL(String imagenUserURL) {

        this.imagenUserURL = imagenUserURL;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getNombres(){
        firebaseauth = FirebaseAuth.getInstance();
        user = firebaseauth.getCurrentUser();
        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");

        //OBTENEMOS LOS DATOS DEL USUARIO
        BASE_DE_DATOS.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if(snapshot.exists()){

                    //Los datos se rescatan tal cual fueron registrados
                    String nombres = ""+snapshot.child("nombres").getValue();


                    //Seteamos los datos en TextView e ImageView
                    Nombres = nombres;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return Nombres;
    }

    public long getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(long fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    //Sets and gets


}
