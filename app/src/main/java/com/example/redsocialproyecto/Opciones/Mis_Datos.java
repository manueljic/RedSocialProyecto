package com.example.redsocialproyecto.Opciones;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocialproyecto.CambiarPassword.CambiarPassword;
import com.example.redsocialproyecto.R;
import com.example.redsocialproyecto.Usuario;
import com.example.redsocialproyecto.UsuarioDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class Mis_Datos extends AppCompatActivity {

    ImageView ImagenDato;
    TextView UidDato,UidDatoTXT, NombresDato,NombresDatoTXT, ApellidosDato,ApellidosDatoTXT, CorreoDato,CorreoDatoTXT, EdadDato,EdadDatoTXT, DireccionDato,DireccionDatoTXT, TelefonoDato,TelefonoDatoTXT,MisDatosTXT;
    Button CambiarFotoPerfil, ActualizarPass;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private FirebaseDatabase database;



    DatabaseReference BASE_DE_DATOS;

    private ImagePicker imagePicker;

    private Uri fotoPerfilUri;

    public ImageView getImagenDato() {
        return ImagenDato;
    }

    public TextView getNombresDato() {
        return NombresDato;
    }

    private boolean escogido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_datos);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirmamos que el título no es nulo
        actionBar.setTitle("Mis Datos"); //Introducimos título
        actionBar.setDisplayShowHomeEnabled(true); //Mostramos home button
        actionBar.setDisplayHomeAsUpEnabled(true);//Habilitamos botón de retroceso





        ImagenDato = findViewById(R.id.ImagenDato);
        UidDato = findViewById(R.id.uidDato);
        NombresDato = findViewById(R.id.NombresDato);
        ApellidosDato = findViewById(R.id.ApellidosDato);
        CorreoDato = findViewById(R.id.CorreoDato);

        EdadDato = findViewById(R.id.EdadDato);
        DireccionDato = findViewById(R.id.DireccionDato);
        TelefonoDato = findViewById(R.id.TelefonoDato);
        UidDatoTXT = findViewById(R.id.uidDatoTXT);
        NombresDatoTXT = findViewById(R.id.NombresDatoTXT);
        ApellidosDatoTXT = findViewById(R.id.ApellidosDatoTXT);
        CorreoDatoTXT = findViewById(R.id.CorreoDatoTXT);

        EdadDatoTXT = findViewById(R.id.EdadDatoTXT);
        DireccionDatoTXT = findViewById(R.id.DireccionDatoTXT);
        TelefonoDatoTXT = findViewById(R.id.TelefonoDatoTXT);
        MisDatosTXT = findViewById(R.id.MisDatosTXT);





        CambiarFotoPerfil = findViewById(R.id.CambiarFotoPerfil);
        ActualizarPass = findViewById(R.id.ActualizarPass);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();


        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");


        imagePicker = new ImagePicker(this);

        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                if(!list.isEmpty()){
                    String path = list.get(0).getOriginalPath();
                    fotoPerfilUri = Uri.parse(path);
                    ImagenDato.setImageURI(fotoPerfilUri);
                }
            }

            @Override
            public void onError(String s) {
                Toast.makeText(Mis_Datos.this, "Error: "+s, Toast.LENGTH_SHORT).show();
            }
        });

        CambioDeLetra(); //Llamamos al método para cambiar la fuente de la letra

        //OBTENEMOS LOS DATOS DEL USUARIO
        BASE_DE_DATOS.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if(snapshot.exists()){

                    //Los datos se rescatan tal cual fueron registrados
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombres = ""+snapshot.child("nombres").getValue();
                    String apellidos = ""+snapshot.child("apellidos").getValue();
                    String correo = ""+snapshot.child("correo").getValue();
                    String password = ""+snapshot.child("pass").getValue();
                    String direccion = ""+snapshot.child("direccion").getValue();
                    String edad = ""+snapshot.child("edad").getValue();
                    String telefono = ""+snapshot.child("telefono").getValue();
                    String imagen = ""+snapshot.child("imagen").getValue();

                    //Seteamos los datos en TextView e ImageView
                    UidDato.setText(uid);
                    NombresDato.setText(nombres);
                    ApellidosDato.setText(apellidos);
                    CorreoDato.setText(correo);
                    EdadDato.setText(edad);
                    DireccionDato.setText(direccion);
                    TelefonoDato.setText(telefono);

                    //Para obtener imagen hacemos lo siguiente
                    try {
                        //Si existe imagen
                        Picasso.get().load(imagen).placeholder(R.drawable.img_perfil).into(ImagenDato);
                    }catch (Exception e){
                        //Si no existe imagen
                        Picasso.get().load(R.drawable.img_perfil).into(ImagenDato);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //ACTUALIZAR CONTRASEÑA
        ActualizarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nos mandará a la actividad para cambiar la contraseña
                startActivity(new Intent(Mis_Datos.this, CambiarPassword.class));
            }
        });
        //ACTUALIZAR CONTRASEÑA


        //CAMBIAR FOTO DE PERFIL
        CambiarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            Usuario usuario = new Usuario();

            @Override
            public void onClick(View v) {

                CambiarFotoPerfil.setText("CONFIRMAR FOTO");
                if(!escogido){

                    imagePicker.pickImage();

                }


                if(fotoPerfilUri != null){

                    Toast.makeText(Mis_Datos.this, "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();

                    UsuarioDAO.getInstancia().subirFotoUri(fotoPerfilUri, new UsuarioDAO.IDevolverUrlFoto() {
                        @Override
                        public void devolverUrlString(String url) {

                            usuario.setImagenUserURL(url);
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            DatabaseReference reference = database.getReference("USUARIOS_DE_APP/"+currentUser.getUid()+"/imagen/");
                            DatabaseReference referenceTwo = database.getReference("USUARIOS_DE_APP/"+currentUser.getUid()+"/imagenUserURL/");
                            reference.setValue(usuario.getImagenUserURL());
                            referenceTwo.setValue(usuario.getImagenUserURL());
                            finish();

                        }
                    });

                }else{
                    //usuario.setImagenUserURL("https://wrmx00.epimg.net/radio/imagenes/2017/03/22/internacional/1490203911_429057_1490204200_noticia_normal.jpg");
                }
                escogido = true;

            }
        });
        //CAMBIAR FOTO DE PERFIL

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Picker.PICK_IMAGE_DEVICE && resultCode == RESULT_OK){
            imagePicker.submit(data);
        }
    }

    private void CambioDeLetra(){

        /*FUENTE DE LETRA*/
        String ubicacion = "fuentes/FORTE.ttf";
        Typeface Tf = Typeface.createFromAsset(Mis_Datos.this.getAssets(),ubicacion);
        /*FUENTE DE LETRA*/

        MisDatosTXT.setTypeface(Tf);
        UidDatoTXT.setTypeface(Tf);
        NombresDatoTXT.setTypeface(Tf);
        ApellidosDatoTXT.setTypeface(Tf);
        CorreoDatoTXT.setTypeface(Tf);
        EdadDatoTXT.setTypeface(Tf);
        DireccionDatoTXT.setTypeface(Tf);
        TelefonoDatoTXT.setTypeface(Tf);

        UidDato.setTypeface(Tf);
        NombresDato.setTypeface(Tf);
        ApellidosDato.setTypeface(Tf);
        CorreoDato.setTypeface(Tf);
        EdadDato.setTypeface(Tf);
        DireccionDato.setTypeface(Tf);
        TelefonoDato.setTypeface(Tf);

        CambiarFotoPerfil.setTypeface(Tf);
        ActualizarPass.setTypeface(Tf);
    }

    //HABILITAMOS LA ACCIÓN PARA RETROCEDER(ir a la actividad anterior)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}