package com.example.redsocialproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registro extends AppCompatActivity {

    DatabaseReference BASE_DE_DATOS;
    FirebaseUser user;
    FirebaseAuth firebaseauth;
    FirebaseDatabase database;
    private String nombre;

    TextView RegistroTXT;
    EditText Correo,Password,Nombres,Apellidos,Edad,Telefono,Direccion;
    Button REGISTRARUSUARIO;
    CircleImageView imagenDato;
    RadioGroup rgGenero;
    RadioButton rbHombre;
    RadioButton rbMujer;
    private ProgressDialog progressDialog;

    public Registro(){}





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirmamos que el título no es nulo
        actionBar.setTitle("Registro"); //Introducimos título
        actionBar.setDisplayShowHomeEnabled(true); //Mostramos home button
        actionBar.setDisplayHomeAsUpEnabled(true);//Habilitamos botón de retroceso

        //Hacemos la conexion de nuestras vistas con nuestro layout de registro
        Correo = findViewById(R.id.Correo);
        Password = findViewById(R.id.Password);
        Nombres = findViewById(R.id.Nombres);
        Apellidos = findViewById(R.id.Apellidos);
        Edad = findViewById(R.id.Edad);
        Telefono = findViewById(R.id.Telefono);
        Direccion = findViewById(R.id.Direccion);
        REGISTRARUSUARIO = findViewById(R.id.REGISTRARUSUARIO);
        RegistroTXT = findViewById(R.id.RegistroTXT);
        rgGenero = findViewById(R.id.rgGenero);
        rbHombre = findViewById(R.id.rbHombre);
        rbMujer = findViewById(R.id.rbMujer);


        //Creamos la instancia de firebaseAuth
        firebaseauth = FirebaseAuth.getInstance();
        user = firebaseauth.getCurrentUser();
        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");

        progressDialog = new ProgressDialog(Registro.this);//Iniciamos el progressdialog

        CambioDeLetra();

        //Creamos un evento al botón REGISTRARUSUARIO para que al presionar realice una acción
        REGISTRARUSUARIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = Correo.getText().toString();
                String pass = Password.getText().toString();

                //validamos
                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    Correo.setError("Correo no válido");
                    Correo.setFocusable(true);
                }else if (pass.length()<6){
                    Password.setError("Contraseña debe ser mayor de 6 caracteres");
                    Password.setFocusable(true);
                }else{
                    REGISTRAR(correo,pass);
                }
            }
        });



}







    private void CambioDeLetra(){

        /*FUENTE DE LETRA*/
        String ubicacion = "fuentes/FORTE.ttf";
        Typeface Tf = Typeface.createFromAsset(Registro.this.getAssets(),ubicacion);
        /*FUENTE DE LETRA*/


        RegistroTXT.setTypeface(Tf);
        Correo.setTypeface(Tf);
        Password.setTypeface(Tf);
        Nombres.setTypeface(Tf);
        Apellidos.setTypeface(Tf);
        Edad.setTypeface(Tf);
        Telefono.setTypeface(Tf);
        Direccion.setTypeface(Tf);
        REGISTRARUSUARIO.setTypeface(Tf);



    }


    //MÉTODO REGISTRAR
    private void REGISTRAR(String correo, String pass) {
        progressDialog.setTitle("Registrando");
        progressDialog.setMessage("Espere por favor...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseauth.createUserWithEmailAndPassword(correo,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Si el registro es exitoso
                if(task.isSuccessful()){
                    progressDialog.dismiss();//El progress se cierra
                    FirebaseUser user = firebaseauth.getCurrentUser();
                    //Aquí  van los datos que queremos registrar
                    /*DEBEMOS TOMAR EN CUENTA QUE LOS STRING  DEBEN SER
                     * DIFERENTES A NUESTRO EDITTEXT*/
                    assert user != null;
                    String uid = user.getUid(); //Para obtener el UID
                    String correo = Correo.getText().toString();
                    String pass = Password.getText().toString();
                    String nombres= Nombres.getText().toString();
                    String apellidos = Apellidos.getText().toString();
                    String edad = Edad.getText().toString();
                    String telefono = Telefono.getText().toString();
                    String direccion = Direccion.getText().toString();

                    final String genero;

                    if(rbHombre.isChecked()){
                        genero = "Hombre";
                    }else{
                        genero = "Mujer";
                    }


                    /*CREAMOS UN HASHMAP PARA MANDAR LOS DATOS A FIREBASE*/
                    HashMap<Object,String> DatosUsuario = new HashMap<>();
                    DatosUsuario.put("uid",uid);
                    DatosUsuario.put("correo",correo);
                    DatosUsuario.put("pass",pass);
                    DatosUsuario.put("nombres",nombres);
                    DatosUsuario.put("apellidos",apellidos);
                    DatosUsuario.put("edad",edad);
                    DatosUsuario.put("telefono",telefono);
                    DatosUsuario.put("direccion",direccion);
                    DatosUsuario.put("genero",genero);

                    //LA IMAGEN DE MOMENTO ESTARÁ VACÍA
                    DatosUsuario.put("imagen","");

                    //INICIALIZAMOS LA INSTANCIA A LA BASE DE DATOS
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //CREAMOS LA BASE DE DATOS
                    DatabaseReference reference = database.getReference("USUARIOS_DE_APP");//Nombre de la BBDD

                    reference.child(uid).setValue(DatosUsuario);
                    Toast.makeText(Registro.this, "Se registró exitosamente", Toast.LENGTH_SHORT).show();

                    //UNA VEZ QUE SE HA REGISTRADO NOS MANDARÁ AL APARTADO INICIO
                    startActivity(new Intent(Registro.this,Inicio.class));
                }else{
                    progressDialog.dismiss();//El progress se cierra
                    Toast.makeText(Registro.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();//El progress se cierra
                Toast.makeText(Registro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });//Crear usuario con correo y contraseña
    }

    //HABILITAMOS LA ACCIÓN PARA RETROCEDER(ir a la actividad anterior)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}