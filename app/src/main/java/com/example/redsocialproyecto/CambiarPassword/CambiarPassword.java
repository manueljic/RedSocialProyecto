package com.example.redsocialproyecto.CambiarPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocialproyecto.Login;
import com.example.redsocialproyecto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CambiarPassword extends AppCompatActivity {

    TextView MisCredencialesTXT, CorreoActualTXT, CorreoActual;
    EditText ActualPassET, NuevoPassEt;
    Button CAMBIARPASSBTN;
    DatabaseReference USUARIOS_DE_APP;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_password);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirmamos que el título no es nulo
        actionBar.setTitle("Cambiar Contraseña"); //Introducimos título
        actionBar.setDisplayShowHomeEnabled(true); //Mostramos home button
        actionBar.setDisplayHomeAsUpEnabled(true);//Habilitamos botón de retroceso


        MisCredencialesTXT = findViewById(R.id.MisCredencialesTXT);
        CorreoActualTXT = findViewById(R.id.CorreoActualTXT);
        CorreoActual = findViewById(R.id.CorreoActual);

        ActualPassET = findViewById(R.id.ActualPassET);
        NuevoPassEt = findViewById(R.id.NuevoPassEt);
        CAMBIARPASSBTN = findViewById(R.id.CAMBIARPASSBTN);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        USUARIOS_DE_APP = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");
        progressDialog = new ProgressDialog(CambiarPassword.this);

        Cambiar_De_Letra();

        //CONSULTAREMOS EL CORREO Y CONTRASEÑA DEL USUARIO
        Query query = USUARIOS_DE_APP.orderByChild("correo").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){

                    //OBTENEMOS LOS VALORES
                    String correo = ""+ds.child("correo").getValue();
                    String pass = ""+ds.child("pass").getValue();

                    //Seteamos los datos en los textView
                    CorreoActual.setText(correo);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //CREAMOS EL EVENTO PARA CAMBIAR CONTRASEÑA
        CAMBIARPASSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PASS_ANTERIOR = ActualPassET.getText().toString().trim();
                String NUEVO_PASS = NuevoPassEt.getText().toString().trim();

                //Creamos la siguientes condiciones
                if(TextUtils.isEmpty(PASS_ANTERIOR)){
                    Toast.makeText(CambiarPassword.this, "El campo de contraseña actual está vacío", Toast.LENGTH_SHORT).show();

                }
                if(TextUtils.isEmpty(NUEVO_PASS)){
                    Toast.makeText(CambiarPassword.this, "El campo de nueva contraseña está vacío", Toast.LENGTH_SHORT).show();

                }
                if (!NUEVO_PASS.equals("") && NUEVO_PASS.length() >= 6 ){
                    //Se ejecuta el método para actualizar password
                    Cambio_De_Password(PASS_ANTERIOR, NUEVO_PASS);
                }else{
                    NuevoPassEt.setError("La contraseña debe ser mayor de 6 caracteres");
                    NuevoPassEt.setFocusable(true);
                }
            }
        });
    }

    //MÉTODO PARA CAMBIAR DE CONTRASEÑA
    private void Cambio_De_Password(String pass_anterior, String nuevo_pass) {
        progressDialog.show();
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere por favor");
        user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(),pass_anterior);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(nuevo_pass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        String value = NuevoPassEt.getText().toString().trim();
                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put("pass",value);
                                        //ACTUALIZAMOS LA NUEVA CONTRASEÑA EN LA BD
                                        USUARIOS_DE_APP.child(user.getUid()).updateChildren(result)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(CambiarPassword.this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                        //LUEGO SE CERRARÁ LA SESIÓN
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(CambiarPassword.this, Login.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        progressDialog.dismiss();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CambiarPassword.this, "La contraseña actual no es la correcta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //MÉTODO PARA CAMBIAR DE LETRA
    private void Cambiar_De_Letra(){
        /*FUENTE DE LETRA*/
        String ubicacion = "fuentes/sans_negrita.ttf";
        Typeface Tf = Typeface.createFromAsset(CambiarPassword.this.getAssets(),ubicacion);


        MisCredencialesTXT.setTypeface(Tf);
        CorreoActualTXT.setTypeface(Tf);
        CorreoActual.setTypeface(Tf);
        ActualPassET.setTypeface(Tf);
        NuevoPassEt.setTypeface(Tf);
        /*FUENTE DE LETRA*/
    }

    //HABILITAMOS LA ACCIÓN PARA RETROCEDER(ir a la actividad anterior)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}