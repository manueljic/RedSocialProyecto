package com.example.redsocialproyecto.Opciones;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redsocialproyecto.CambiarPassword.CambiarPassword;
import com.example.redsocialproyecto.LUsuario;
import com.example.redsocialproyecto.R;
import com.example.redsocialproyecto.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IntroducirCodigo extends AppCompatActivity {

    EditText IntroduceCodET;
    Button BuscarParejaBTN;
    TextView FindLoveTitulo;


    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private FirebaseDatabase database;

    private static String CODIGO;

    public static String getCODIGO() {
        return CODIGO;
    }

    public static void setCODIGO(String CODIGO) {
        IntroducirCodigo.CODIGO = CODIGO;
    }

    public IntroducirCodigo() {

    }

    DatabaseReference BASE_DE_DATOS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducir_codigo);

        //ACTIONBAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirmamos que el título no es nulo
        actionBar.setTitle("Find-Love ;)"); //Introducimos título
        actionBar.setDisplayShowHomeEnabled(true); //Mostramos home button
        actionBar.setDisplayHomeAsUpEnabled(true);//Habilitamos botón de retroceso
        //ACTIONBAR



        IntroduceCodET = findViewById(R.id.IntroduceCodET);
        BuscarParejaBTN = findViewById(R.id.BuscarParejaBTN);
        FindLoveTitulo = findViewById(R.id.FindLoveTitulo);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();


        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");



            CambioDeLetra();


            //Boton introducir codigo
            BuscarParejaBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CODIGO = IntroduceCodET.getText().toString().trim();

                    //Creamos la siguiente condicion
                    if(TextUtils.isEmpty(CODIGO)){
                        Toast.makeText(IntroducirCodigo.this, "El campo de codigo está vacío", Toast.LENGTH_SHORT).show();
                    }else{
                        Usuario usu = new Usuario();
                        LUsuario lUsuario = new LUsuario(usu,CODIGO);
                        if(buscarUsuario(lUsuario,CODIGO)){
                            Intent intent = new Intent(IntroducirCodigo.this,Chats.class);
                            intent.putExtra("key_receptor",lUsuario.getKey());
                            startActivity(intent);
                        }
                    }


                }
            });
            //Boton introducir codigo





    }



    private void CambioDeLetra(){

        /*FUENTE DE LETRA*/
        String ubicacion = "fuentes/FORTE.ttf";
        Typeface Tf = Typeface.createFromAsset(IntroducirCodigo.this.getAssets(),ubicacion);
        /*FUENTE DE LETRA*/


        FindLoveTitulo.setTypeface(Tf);

    }

    //Compara si el codigo es el id del receptor y devuelve true
    public boolean buscarUsuario(LUsuario model, String key){
        if(key == model.getKey()){
            return true;
        }else{
            return false;
        }
    }
    //HABILITAMOS LA ACCIÓN PARA RETROCEDER(ir a la actividad anterior)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}