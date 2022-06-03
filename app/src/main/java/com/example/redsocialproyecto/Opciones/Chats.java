package com.example.redsocialproyecto.Opciones;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.redsocialproyecto.ChatsDAO;
import com.example.redsocialproyecto.Inicio;
import com.example.redsocialproyecto.R;
import com.example.redsocialproyecto.UsuarioDAO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kbeanie.multipicker.api.Picker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chats extends AppCompatActivity {

    CircleImageView fotoPerfil;
    TextView nombre;
    RecyclerView rvMensajes;
    EditText txtMensaje;
    Button btnEnviar,btnSalir;
    //ImageButton btnEnviarFoto;

    List<HolderMensajes> mChat;

    private static boolean entraAlChat;

    AdapterMensajes adapter;

    private static final int PHOTO_SEND = 1;

    private FirebaseStorage storage;
    private StorageReference storageReference;


    public Chats() {
    }

    public static String getKeyReceptor() {
        return KEY_RECEPTOR;
    }

    public static void setKeyReceptor(String keyReceptor) {
        KEY_RECEPTOR = keyReceptor;
    }

    FirebaseAuth firebaseAuth;
    DatabaseReference BASE_DE_DATOS;
    FirebaseUser user;

    private static String KEY_RECEPTOR;


    private boolean emisor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirmamos que el título no es nulo
        actionBar.setTitle("Chat"); //Introducimos título
        actionBar.setDisplayShowHomeEnabled(true); //Mostramos home button
        actionBar.setDisplayHomeAsUpEnabled(true);//Habilitamos botón de retroceso

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            KEY_RECEPTOR = bundle.getString("key_receptor");
        }else{
            finish();
        }


        fotoPerfil = findViewById(R.id.fotoPerfil);
        nombre = findViewById(R.id.nombre);
        rvMensajes = findViewById(R.id.rvMensajes);
        txtMensaje = findViewById(R.id.txtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnSalir = findViewById(R.id.btnSalir);
        //btnEnviarFoto = findViewById(R.id.btnEnviarFoto);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);



        storage = FirebaseStorage.getInstance();

        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");



        entraAlChat = true;




        CambioDeLetra(); //Llamamos al método para cambiar la fuente de la letra


        //BOTÓN ENVIAR MENSAJES
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensajeEnviar = txtMensaje.getText().toString();
                if(!mensajeEnviar.isEmpty()){
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMensaje(mensajeEnviar);
                    mensaje.setKeyEmisor(UsuarioDAO.getInstancia().getKeyUsuario());
                    ChatsDAO.getInstancia().nuevoMensaje(UsuarioDAO.getInstancia().getKeyUsuario(), KEY_RECEPTOR,mensaje);
                    txtMensaje.setText("");
                }

            }
        });



        //BOTÓN SALIR
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Chats.this);
                builder.setTitle("¿Quieres cerrar la app?");
                builder.setMessage("Si usted acepta saldrá de esta conversación");

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        entraAlChat = false;
                        Intent intent = new Intent(Chats.this, Inicio.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });




        //Obtenemos la foto de perfil y nombre del receptor
        BASE_DE_DATOS.child(KEY_RECEPTOR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if(snapshot.exists()){

                    //Los datos se rescatan tal cual fueron registrados

                    String imagen = ""+snapshot.child("imagenUserURL").getValue();
                    String nombres = ""+snapshot.child("nombres").getValue();


                    //Seteamos el nombre
                    nombre.setText(nombres);


                    //Para obtener imagen hacemos lo siguiente
                    try {
                        //Si existe imagen
                        Picasso.get().load(imagen).placeholder(R.drawable.img_perfil).into(fotoPerfil);
                    }catch (Exception e){
                        //Si no existe imagen
                        Picasso.get().load(R.drawable.img_perfil).into(fotoPerfil);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        readMessages(UsuarioDAO.getInstancia().getKeyUsuario(),KEY_RECEPTOR);


      /*  btnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una foto: "),PHOTO_SEND);

            }
        });*/






        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        FirebaseDatabase.
                getInstance().
                getReference("Mensajes").
                child(UsuarioDAO.getInstancia().getKeyUsuario()).
                child(KEY_RECEPTOR).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensaje m = snapshot.getValue(Mensaje.class);
                adapter.addMensaje(m);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Enviar foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).continueWithTask(task -> {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return fotoReferencia.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Uri uri = task.getResult();
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMensaje("Foto enviada");
                    mensaje.setUrlFoto(uri.toString());
                    mensaje.setContieneFoto(true);
                    mensaje.setKeyEmisor(UsuarioDAO.getInstancia().getKeyUsuario());
                    txtMensaje.setText("");
                    ChatsDAO.getInstancia().nuevoMensaje(UsuarioDAO.getInstancia().getKeyUsuario(), KEY_RECEPTOR,mensaje);
                    BASE_DE_DATOS.push().setValue(mensaje);

                }
            });
        }
    }
    //Enviar foto

    //METODO LEER MENSAJES
    private void readMessages(final String myid, final String idEmisor){
        mChat = new ArrayList<>();

        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("Chats");
        BASE_DE_DATOS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    HolderMensajes chat = snapshot.getValue(HolderMensajes.class);
                    if (chat.getKEY_RECEPTOR().equals(myid) && chat.getKEY_EMISOR().equals(idEmisor) ||
                            chat.getKEY_RECEPTOR().equals(idEmisor) && chat.getKEY_EMISOR().equals(myid)){
                        mChat.add(chat);
                    }

                    adapter = new AdapterMensajes(Chats.this);
                    rvMensajes.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    public static boolean isEntraAlChat() {
        return entraAlChat;
    }

    public static void setEntraAlChat(boolean entraAlChat) {
        Chats.entraAlChat = entraAlChat;
    }

    //HABILITAMOS LA ACCIÓN PARA RETROCEDER(ir a la actividad anterior)
   @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent intent = new Intent(Chats.this, Inicio.class);
        startActivity(intent);
        return super.onSupportNavigateUp();
    }

    //Con este codigo el botón de atras del hardware nos saca de la app
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }



    public boolean isEmisor() {
        return emisor;
    }

    public void setEmisor(boolean emisor) {
        this.emisor = emisor;
    }

    //Método cambiar de fuente
    private void CambioDeLetra(){
        /*FUENTE DE LETRA*/
        String ubicacion = "fuentes/sans_medio.ttf";
        Typeface Tf = Typeface.createFromAsset(Chats.this.getAssets(),ubicacion);
        /*FUENTE DE LETRA*/

        nombre.setTypeface(Tf);
        txtMensaje.setTypeface(Tf);
        btnEnviar.setTypeface(Tf);
    }

    //Enviar KEY_RECEPTOR a HolderMensajes
    public String obtenerKeyReceptor(){
        return  KEY_RECEPTOR;
    }










}