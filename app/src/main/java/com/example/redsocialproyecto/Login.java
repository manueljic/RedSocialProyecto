package com.example.redsocialproyecto;


import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    EditText CorreoLogin, PasswordLogin;
    Button INGRESAR;
    TextView app_name;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Login");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        CorreoLogin = findViewById(R.id.CorreoLogin);
        PasswordLogin = findViewById(R.id.PasswordLogin);
        INGRESAR = findViewById(R.id.INGRESAR);
        app_name = findViewById(R.id.app_name);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this);

        dialog = new Dialog(Login.this);
        CambioDeLetra();


        //ASIGNAMOS UN EVENTO AL BOTÓN INGRESAR
        INGRESAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CONVERTIMOS A STRING CORREO Y CONTRASEÑA
                String correo = CorreoLogin.getText().toString();
                String pass = PasswordLogin.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    CorreoLogin.setError("Correo no válido");
                    CorreoLogin.setFocusable(true);
                }else if(pass.length() < 6){
                    PasswordLogin.setError("La contraseña debe tener 6 caracteres mínimo");
                    PasswordLogin.setFocusable(true);
                }else{
                    //Se ejecuta el método
                    LOGINUSUARIO(correo,pass);
                }
            }
        });

    }

    private void CambioDeLetra(){
        /*FUENTE DE LETRA*/
        String ubicacion = "fuentes/FORTE.ttf";
        Typeface Tf = Typeface.createFromAsset(Login.this.getAssets(),ubicacion);
        /*FUENTE DE LETRA*/


        app_name.setTypeface(Tf);
        INGRESAR.setTypeface(Tf);
        CorreoLogin.setTypeface(Tf);
        PasswordLogin.setTypeface(Tf);


    }

    //MÉTODO PARA LOGEAR USUARIO
    private void LOGINUSUARIO(String correo, String pass) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si se inició sesión correctamente
                        if(task.isSuccessful()){
                            progressDialog.dismiss();//El progress se cierra
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //Cuando iniciemos sesión nos mande a la actividad Inicio
                            startActivity(new Intent(Login.this,Inicio.class));
                            assert user != null;
                            Toast.makeText(Login.this, "Hola! Bienvenido (a)"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Dialog_No_Inicio();
                            //Toast.makeText(Login.this, "Algo va mal", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //CREAMOS EL DIALOG PERSONALIZADO
    private void Dialog_No_Inicio(){
        Button ok_no_inicio;

        dialog.setContentView(R.layout.no_sesion); //Hacemos la conexión con nuestra vista creada

        ok_no_inicio = findViewById(R.id.ok_no_inicio);

        //Al presionar en ok se cerrará el cuadro de diálogo
        ok_no_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);//La animación seguirá mostrandose hasta que se pulse en ok
        dialog.show();
    }

    //HABILITAMOS LA OPCIÓN PARA RETROCEDER
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}