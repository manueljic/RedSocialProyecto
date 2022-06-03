package com.example.redsocialproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocialproyecto.Opciones.Chats;
import com.example.redsocialproyecto.Opciones.IntroducirCodigo;
import com.example.redsocialproyecto.Opciones.MisCodigos;
import com.example.redsocialproyecto.Opciones.Mis_Datos;
import com.example.redsocialproyecto.Opciones.VerUsuarios_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Inicio extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    //Declaramos base de datos
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;



    //Declaramos nuestras vistas
    ImageView foto_perfil;
    TextView uidPerfil, correoPerfil, nombresPerfil, Bienvenidotxt, uidtxt, correotxt;
    TextView fecha;

    Button MisDatosOpcion, UsuarioOpcion, CodigoOpcion,CerrarSesion,MisCodigosOpcion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirmamos que el título no es nulo
        actionBar.setTitle("Inicio"); //Introducimos título


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        firebaseDatabase = FirebaseDatabase.getInstance();//Inicializamos la instancia
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS_DE_APP");

        Bienvenidotxt = findViewById(R.id.Bienvenidotxt);
        foto_perfil = findViewById(R.id.foto_perfil);
        nombresPerfil = findViewById(R.id.nombresPerfil);
        fecha = findViewById(R.id.fecha);

        //OPCIONES
        MisDatosOpcion = findViewById(R.id.MisDatosOpcion);
        UsuarioOpcion = findViewById(R.id.UsuarioOpcion);
        CerrarSesion = findViewById(R.id.CerrarSesion);
        CodigoOpcion = findViewById(R.id.CodigoOpcion);
        MisCodigosOpcion = findViewById(R.id.MisCodigosOpcion);
        //OPCIONES

        CambioDeLetra();

        //Evento ir a ver datos
        MisDatosOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Mis_Datos.class);
                startActivity(intent);
                Toast.makeText(Inicio.this, "Mis Datos", Toast.LENGTH_SHORT).show();
            }
        });



        //Evento ver usuarios
        UsuarioOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, VerUsuarios_Activity.class);
                startActivity(intent);
                Toast.makeText(Inicio.this, "Usuarios", Toast.LENGTH_SHORT).show();
            }
        });

        //Evento insertar codigo
        CodigoOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Chats.isEntraAlChat()){
                    Intent intent = new Intent(Inicio.this, IntroducirCodigo.class);
                    startActivity(intent);
                    Toast.makeText(Inicio.this, "Inserte su codigo", Toast.LENGTH_SHORT).show();
                }else{
                    Usuario usu = new Usuario();
                    LUsuario lUsuario = new LUsuario(usu,IntroducirCodigo.getCODIGO());

                    Intent intent = new Intent(Inicio.this,Chats.class);
                    intent.putExtra("key_receptor",lUsuario.getKey());
                    startActivity(intent);
                }

            }
        });

        //EVENTO MIS CODIGOS
        MisCodigosOpcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, MisCodigos.class);
                startActivity(intent);
            }
        });
        //EVENTO MIS CODIGOS



        //FECHA
        Date date = new Date();
        SimpleDateFormat fechaC = new SimpleDateFormat("d 'de' MMM 'del' yyyy");
        String sFecha = fechaC.format(date);
        fecha.setText(sFecha);
        //FECHA

        //EVENTO CERRAR SESIÓN
        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos un método para cerrar sesión
                CerrarSesion();
            }
        });
    }

    //Método para cambiar la letra
    private void CambioDeLetra(){

        /*FUENTE DE LETRA*/
        String ubicacion = "fuentes/FORTE.ttf";
        Typeface Tf = Typeface.createFromAsset(Inicio.this.getAssets(),ubicacion);
        /*FUENTE DE LETRA*/

        fecha.setTypeface(Tf);
        Bienvenidotxt.setTypeface(Tf);
        nombresPerfil.setTypeface(Tf);

        //Cambio de letra a botones
        MisDatosOpcion.setTypeface(Tf);
        UsuarioOpcion.setTypeface(Tf);
        CerrarSesion.setTypeface(Tf);
        CodigoOpcion.setTypeface(Tf);
        MisCodigosOpcion.setTypeface(Tf);
    }

    //Llamamos a onStart


    @Override
    protected void onStart() {
        VerificacionInicioSesion();
        super.onStart();
    }

    //MÉTODO QUE VERIFICA SI EL USUARIO HA INICIADO SESIÓN PREVIAMENTE
    private void VerificacionInicioSesion(){
        if (firebaseUser != null){
            CargarDatos();


        }else{
            startActivity(new Intent(Inicio.this, MainActivity.class));
            finish();
        }
    }

    //CREAMOS UN MÉTODO PARA RECUPERAR LOS DATOS DE FIREBASE DEL USUARIO ACTUAL
    private void CargarDatos(){
        Query query = BASE_DE_DATOS.orderByChild("correo").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recorremos los usuarios registrados en la base de datos, hasta encontrar el usuario actual
                for(DataSnapshot ds : snapshot.getChildren()){

                    //Obtenemos los valores
                    String uid = ""+ds.child("uid").getValue();
                    String correo = ""+ds.child("correo").getValue();
                    String nombres = ""+ds.child("nombres").getValue();
                    String imagen = ""+ds.child("imagen").getValue();

                    //Set de nuestros datos en las vistas
                    nombresPerfil.setText(nombres);

                    //Try catch para gestionar nuestra foto de perfil
                    try {

                        //Si existe una imagen en la base de datos del usuario actual
                        Picasso.get().load(imagen).placeholder(R.drawable.img_perfil).into(foto_perfil);

                    }catch (Exception e){
                        //Si el usuario no cuenta con una imagen en la base de datos
                        Picasso.get().load(R.drawable.img_perfil).into(foto_perfil);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //MËTODO CERRAR SESIÓN
    private void CerrarSesion(){
        firebaseAuth.signOut();
        Toast.makeText(this, "Se ha cerrado la sesión", Toast.LENGTH_SHORT).show();
        //después de cerrar sesión nos dirigimos al mainActivity
        startActivity(new Intent(Inicio.this,MainActivity.class));
    }

    //No se puede retroceder
    @Override public void onBackPressed() { finish(); }
}