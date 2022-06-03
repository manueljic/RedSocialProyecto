package com.example.redsocialproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class PantallaDeCarga extends AppCompatActivity {


    TextView app_name;
    TextView desarrolladortxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_carga);

        app_name = findViewById(R.id.app_name);
        desarrolladortxt = findViewById(R.id.desarrolladortxt);

        CambioDeLetra();

        //Se representa en segundos
        final int Duracion = 2500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Esto se jecutará una vez hayan pasado los segundos que hemos establecido
                Intent intent = new Intent(PantallaDeCarga.this, Inicio.class);
                startActivity(intent);
                finish();
                //Nos dirige de esta actividad al MainActivity

            }
        },Duracion);
    }

    private void CambioDeLetra(){

        /*FUENTE DE LETRA*/
        String ubicacion = "fuentes/FORTE.ttf";
        Typeface Tf = Typeface.createFromAsset(PantallaDeCarga.this.getAssets(),ubicacion);
        /*FUENTE DE LETRA*/

        app_name.setTypeface(Tf);
        desarrolladortxt.setTypeface(Tf);


    }
}