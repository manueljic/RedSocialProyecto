package com.example.redsocialproyecto.Opciones;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.redsocialproyecto.LUsuario;
import com.example.redsocialproyecto.R;
import com.example.redsocialproyecto.Registro;
import com.example.redsocialproyecto.Usuario;
import com.example.redsocialproyecto.WrapContentLinearLayoutManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;

public class VerUsuarios_Activity extends AppCompatActivity {


    DatabaseReference BASE_DE_DATOS;
    FirebaseUser user;
    FirebaseAuth firebaseauth;


    private RecyclerView rvUsuarios;
    private FirebaseRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);

        //ACTIONBAR
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //Afirmamos que el título no es nulo
        actionBar.setTitle("Usuarios"); //Introducimos título
        actionBar.setDisplayShowHomeEnabled(true); //Mostramos home button
        actionBar.setDisplayHomeAsUpEnabled(true);//Habilitamos botón de retroceso
        //ACTIONBAR

        rvUsuarios = findViewById(R.id.rvUsuarios);






        firebaseauth = FirebaseAuth.getInstance();
        user = firebaseauth.getCurrentUser();


        LinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(this);
        rvUsuarios.setLayoutManager(linearLayoutManager);
        //rvUsuarios.setLayoutManager(new WrapContentLinearLayoutManager(VerUsuarios_Activity.this, LinearLayoutManager.HORIZONTAL, false));

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("USUARIOS_DE_APP");


        FirebaseRecyclerOptions<Usuario> options ;
        options = new FirebaseRecyclerOptions.Builder<Usuario>()
                .setQuery(query, Usuario.class)
                .build();

         adapter = new FirebaseRecyclerAdapter<Usuario, UsuarioViewHolder>(options) {
            @Override
            public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_usuario, parent, false);

                return new UsuarioViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UsuarioViewHolder holder, int position, Usuario model) {
                Glide.with(VerUsuarios_Activity.this).load(model.getImagenUserURL()).into(holder.getCivImagenPerfil());
               holder.getTxtNombreUsuario().setText(model.getNombres());

               final LUsuario lUsuario = new LUsuario(model,getSnapshots().getSnapshot(position).getKey());

               holder.getLayoutPrincipal().setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(VerUsuarios_Activity.this,Chats.class);
                       intent.putExtra("key_receptor",lUsuario.getKey());
                       startActivity(intent);
                   }
               });


            }
        };


        rvUsuarios.setAdapter(adapter);


        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("USUARIOS_DE_APP");







    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //HABILITAMOS LA ACCIÓN PARA RETROCEDER(ir a la actividad anterior)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}