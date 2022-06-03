package com.example.redsocialproyecto.Opciones;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redsocialproyecto.R;
import com.example.redsocialproyecto.UsuarioDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderMensajes extends RecyclerView.ViewHolder {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    DatabaseReference BASE_DE_DATOS;

      TextView nombreMensaje,mensajeMensaje,horaMensaje;
      CircleImageView fotoPerfilMensaje;

     String KEY_RECEPTOR;

    String KEY_EMISOR;

    SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("HH:mm");
    Date curDate =  new Date(System.currentTimeMillis());






    public HolderMensajes(@NonNull View itemView) {
        super(itemView);
        nombreMensaje = (TextView) itemView.findViewById(R.id.nombreMensaje);
        mensajeMensaje = (TextView) itemView.findViewById(R.id.mensajeMensaje);
        horaMensaje = (TextView) itemView.findViewById(R.id.horaMensaje);


        fotoPerfilMensaje = (CircleImageView) itemView.findViewById(R.id.fotoPerfilMensaje);




        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();




        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");



        Chats chats = new Chats();
        KEY_RECEPTOR = chats.obtenerKeyReceptor();
        KEY_EMISOR = UsuarioDAO.getInstancia().getKeyUsuario();


        //OBTENEMOS LOS DATOS DEL USUARIO
            BASE_DE_DATOS.child(UsuarioDAO.getInstancia().getKeyUsuario()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Si el usuario existe
                    if(snapshot.exists()){

                        //Los datos se rescatan tal cual fueron registrados

                        String imagen = ""+snapshot.child("imagen").getValue();
                        String nombres = ""+snapshot.child("nombres").getValue();

                        //Seteamos los datos en TextView e ImageView
                        nombreMensaje.setText(nombres);

                        //Obtiene hora actual
                        String   str   =   formatter.format(curDate);

                        horaMensaje.setText(str);



                        //Para obtener imagen hacemos lo siguiente
                        try {
                            //Si existe imagen
                            Picasso.get().load(imagen).placeholder(R.drawable.img_perfil).into(fotoPerfilMensaje);
                        }catch (Exception e){
                            //Si no existe imagen
                            Picasso.get().load(R.drawable.img_perfil).into(fotoPerfilMensaje);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //MUESTRA LA INFO DEL RECEPTOR (SOLO MENSAJE Y FOTO WTF)
           /* BASE_DE_DATOS.child(Chats.getKeyReceptor()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Si el usuario existe
                    if(snapshot.exists()){

                        //Los datos se rescatan tal cual fueron registrados

                        String imagen = ""+snapshot.child("imagen").getValue();
                        String nombres = ""+snapshot.child("nombres").getValue();

                        //Seteamos los datos en TextView e ImageView
                        nombreMensaje.setText(nombres);

                        //Obtiene hora actual
                        String   str   =   formatter.format(curDate);

                        horaMensaje.setText(str);



                        //Para obtener imagen hacemos lo siguiente
                        try {
                            //Si existe imagen
                            Picasso.get().load(imagen).placeholder(R.drawable.img_perfil).into(fotoPerfilMensaje);
                        }catch (Exception e){
                            //Si no existe imagen
                            Picasso.get().load(R.drawable.img_perfil).into(fotoPerfilMensaje);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/







    }





    public TextView getNombreMensaje() {
        return nombreMensaje;
    }



    public TextView getMensajeMensaje() {
        return mensajeMensaje;
    }



    public TextView getHoraMensaje() {
        return horaMensaje;
    }

    public String getKEY_EMISOR() {
        return KEY_EMISOR;
    }

    public String getKEY_RECEPTOR() {
        return KEY_RECEPTOR;
    }

    public void setKEY_RECEPTOR(String KEY_RECEPTOR) {
        this.KEY_RECEPTOR = KEY_RECEPTOR;
    }

    public void setKEY_EMISOR(String KEY_EMISOR) {
        this.KEY_EMISOR = KEY_EMISOR;
    }

    public CircleImageView getFotoPerfilMensaje() {
        return fotoPerfilMensaje;
    }


}
