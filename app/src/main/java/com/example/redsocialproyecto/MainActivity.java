package com.example.redsocialproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button LoginBTN,RegistrarBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginBTN = findViewById(R.id.LoginBTN);
        RegistrarBTN = findViewById(R.id.RegistrarBTN);

        CambioDeLetra();


        LoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        });


        RegistrarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registro.class));
            }
        });
    }
    private void CambioDeLetra(){
        /*FUENTE DE LETRA*/
        String ubicacion = "fuentes/sans_negrita.ttf";
        Typeface Tf = Typeface.createFromAsset(MainActivity.this.getAssets(),ubicacion);
        /*FUENTE DE LETRA*/

        LoginBTN.setTypeface(Tf);
        RegistrarBTN.setTypeface(Tf);
    }
}