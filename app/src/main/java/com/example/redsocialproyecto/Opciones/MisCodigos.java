package com.example.redsocialproyecto.Opciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocialproyecto.Inicio;
import com.example.redsocialproyecto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MisCodigos extends AppCompatActivity {

    TextView codigoDatoTXT, codigoDato, textoTXT,MisCodigosTXT;
    ImageView CodigosImagen;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private FirebaseDatabase database;



    DatabaseReference BASE_DE_DATOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_codigos);

        //ACTIONBAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirmamos que el título no es nulo
        actionBar.setTitle("Mis Codigos"); //Introducimos título
        actionBar.setDisplayShowHomeEnabled(true); //Mostramos home button
        actionBar.setDisplayHomeAsUpEnabled(true);//Habilitamos botón de retroceso
        //ACTIONBAR

        CodigosImagen = findViewById(R.id.CodigosImagen);
        codigoDatoTXT = findViewById(R.id.codigoDatoTXT);
        codigoDato = findViewById(R.id.codigoDato);
        textoTXT = findViewById(R.id.textoTXT);
        MisCodigosTXT = findViewById(R.id.MisCodigosTXT);



        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();


        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");


        //OBTENEMOS LA IMAGENDEL USUARIO
        BASE_DE_DATOS.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if(snapshot.exists()){


                    String imagen = ""+snapshot.child("imagen").getValue();



                    //Para obtener imagen hacemos lo siguiente
                    try {
                        //Si existe imagen
                        Picasso.get().load(imagen).placeholder(R.drawable.img_perfil).into(CodigosImagen);
                    }catch (Exception e){
                        //Si no existe imagen
                        Picasso.get().load(R.drawable.img_perfil).into(CodigosImagen);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //OBTENEMOS LA IMAGENDEL USUARIO



        //Copiar codigo al portapapeles
        codigoDato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip;
                String text = codigoDato.getText().toString();
                clip = ClipData.newPlainText("text",  text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MisCodigos.this, "Copiado", Toast.LENGTH_SHORT).show();
            }
        });
        //Copiar codigo al portapapeles


    }

    private void CambioDeLetra(){

        /*FUENTE DE LETRA*/
        String ubicacion = "fuentes/FORTE.ttf";
        Typeface Tf = Typeface.createFromAsset(MisCodigos.this.getAssets(),ubicacion);
        /*FUENTE DE LETRA*/


    }

    //HABILITAMOS LA ACCIÓN PARA RETROCEDER(ir a la actividad anterior)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}