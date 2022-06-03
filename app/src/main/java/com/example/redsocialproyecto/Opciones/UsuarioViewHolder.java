package com.example.redsocialproyecto.Opciones;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redsocialproyecto.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView civImagenPerfil;
    private TextView txtNombreUsuario;

    private LinearLayout layoutPrincipal;

    public UsuarioViewHolder(@NonNull View itemView) {
        super(itemView);
        civImagenPerfil = (CircleImageView) itemView.findViewById(R.id.civImagenPerfil);
        txtNombreUsuario =(TextView) itemView.findViewById(R.id.txtNombreUsuario);
        layoutPrincipal = (LinearLayout) itemView.findViewById(R.id.layoutPrincipal);
    }

    //SETS AND GETS
    public CircleImageView getCivImagenPerfil() {
        return civImagenPerfil;
    }

    public void setCivImagenPerfil(CircleImageView civImagenPerfil) {
        this.civImagenPerfil = civImagenPerfil;
    }

    public TextView getTxtNombreUsuario() {
        return txtNombreUsuario;
    }

    public void setTxtNombreUsuario(TextView txtNombreUsuario) {
        this.txtNombreUsuario = txtNombreUsuario;
    }

    public LinearLayout getLayoutPrincipal() {
        return layoutPrincipal;
    }

    public void setLayoutPrincipal(LinearLayout layoutPrincipal) {
        this.layoutPrincipal = layoutPrincipal;
    }
}
