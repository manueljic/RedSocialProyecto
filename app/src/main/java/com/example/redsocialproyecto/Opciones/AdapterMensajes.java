package com.example.redsocialproyecto.Opciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redsocialproyecto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensajes>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    List<Mensaje> listMensaje = new ArrayList<>();
    Context c;

    FirebaseUser user;


    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(Mensaje m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @NonNull
    @Override
    public HolderMensajes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_right, parent, false); //DCHA
            return new HolderMensajes(view);
        } else {
            View view = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_left, parent, false);//IZQDA
            return new HolderMensajes(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensajes holder, int position) {

        holder.getNombreMensaje().setText(listMensaje.get(position).getNombre());
        holder.getMensajeMensaje().setText(listMensaje.get(position).getMensaje());
        holder.getHoraMensaje().setText(listMensaje.get(position).getHora());


    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (listMensaje.get(position).getKeyEmisor().equals(user.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }



    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
