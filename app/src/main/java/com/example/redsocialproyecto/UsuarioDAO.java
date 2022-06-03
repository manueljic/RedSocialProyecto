package com.example.redsocialproyecto;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.redsocialproyecto.Opciones.Mensaje;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioDAO {

    public interface IDevolverUrlFoto{
        public void devolverUrlString(String url);
    }

    private FirebaseDatabase database;
    private DatabaseReference referenceUsuarios;
    private FirebaseStorage storage;
    private StorageReference referenceFotoDePerfil;

    private static UsuarioDAO usuarioDAO;


    private UsuarioDAO(){
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        referenceUsuarios = database.getReference("USUARIOS_DE_APP");
        referenceFotoDePerfil = storage.getReference("Fotos/FotoPerfil/"+getKeyUsuario());
    }

    //Sets and gets
    public String getKeyUsuario(){
        return FirebaseAuth.getInstance().getUid();
    }
    //Sets and gets

    //Metodos
    public static UsuarioDAO getInstancia(){
        if(usuarioDAO==null) usuarioDAO = new UsuarioDAO();

        return  usuarioDAO;
    }

    public long fechaDeCreacionLong(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
    }

    public long fechaDeUltimaVezQueSeLogeoLong(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }

    public void añadirFotoDePerfilALosUsuariosQueNoTienenFoto(){
        referenceUsuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LUsuario> lUsuariosLista = new ArrayList<>();

                for(DataSnapshot childDataSnapShot : snapshot.getChildren()){
                    Usuario usuario = childDataSnapShot.getValue(Usuario.class);
                    LUsuario lUsuario = new LUsuario(usuario,childDataSnapShot.getKey());
                    lUsuariosLista.add(lUsuario);
                }

                for(LUsuario lUsuario : lUsuariosLista){

                    if(lUsuario.getUsuario().getImagenUserURL()==null){
                        referenceUsuarios.child(lUsuario.getKey()).child("imagenUserURL").setValue("https://wrmx00.epimg.net/radio/imagenes/2017/03/22/internacional/1490203911_429057_1490204200_noticia_normal.jpg");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void subirFotoUri(Uri uri, IDevolverUrlFoto iDevolverUrlFoto){

        String nombreFoto="";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("SSS.ss-mm-hh-dd-MM-yyyy");
        nombreFoto = simpleDateFormat.format(date);
        final StorageReference fotoReferencia = referenceFotoDePerfil.child(nombreFoto);
        fotoReferencia.putFile(uri).continueWithTask(task -> {
            if(!task.isSuccessful()){
                throw task.getException();
            }
            return fotoReferencia.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri uri = task.getResult();
                    iDevolverUrlFoto.devolverUrlString(uri.toString());
                }
            }


        });

    }

    public boolean isUsuarioLogeado(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser != null;
    }


}
