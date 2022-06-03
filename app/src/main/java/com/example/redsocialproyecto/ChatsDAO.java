package com.example.redsocialproyecto;

import com.example.redsocialproyecto.Opciones.Mensaje;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class ChatsDAO {

    private static ChatsDAO chatsDAO;

    FirebaseDatabase database;
    DatabaseReference referenceMensajeria;

    //Métodos
    public static ChatsDAO getInstancia(){
        if(chatsDAO==null) chatsDAO = new ChatsDAO();

        return  chatsDAO;
    }

    private ChatsDAO(){
        database = FirebaseDatabase.getInstance();
        referenceMensajeria = database.getReference("Mensajes");
        //storage = FirebaseStorage.getInstance();
        //referenceUsuarios = database.getReference("USUARIOS_DE_APP");
        //referenceFotoDePerfil = storage.getReference("drawable/img_perfil.png");
    }

    public void nuevoMensaje(String keyEmisor, String keyReceptor, Mensaje mensaje){
        DatabaseReference referenceEmisor = referenceMensajeria.child(keyEmisor).child(keyReceptor);
        DatabaseReference referenceReceptor = referenceMensajeria.child(keyReceptor).child(keyEmisor);

        referenceEmisor.push().setValue(mensaje);
        referenceReceptor.push().setValue(mensaje);
    }
}
