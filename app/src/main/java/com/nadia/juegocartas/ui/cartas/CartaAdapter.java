package com.nadia.juegocartas.ui.cartas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nadia.juegocartas.R;
import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.request.ApiClient;

import java.util.ArrayList;
import java.util.List;
public class CartaAdapter extends RecyclerView.Adapter<CartaAdapter.CartaViewHolder> {

    private List<Carta> lista;
    private ListaCartasViewModel viewModel;

    public CartaAdapter(List<Carta> lista, ListaCartasViewModel viewModel) {
        this.lista = lista;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public CartaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carta, parent, false);
        return new CartaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartaViewHolder holder, int position) {
        Carta carta = lista.get(position);

        // ---------- CARGAR IMAGEN ----------
        Glide.with(holder.itemView.getContext())
                .load(ApiClient.URL_BASE + carta.getImagen())
                .placeholder(null)
                .error("null")
                .into(holder.imgFrente);

        // ---------- TEXTO FRENTE ----------
        holder.tvNombreFrente.setText(carta.getPersonajeNombre());

        // ---------- TEXTO REVERSO ----------
        holder.tvNombre.setText(carta.getPersonajeNombre());

        holder.tvVida.setText("Vida: " + carta.getVida());
        holder.tvAtaque.setText("Ataque: " + carta.getAtaque());
        holder.tvTipo.setText("Tipo: " + (carta.getTipo() == 1 ? "Rango" : "Cuerpo"));

        // ---------- MOSTRAR ESTADO DEL FLIP ----------
        if (carta.isMostrandoFrente()) {
            holder.mostrarFrente();
        } else {
            holder.mostrarDorso();
        }

        // ---------- CHECKBOX SELECCIÓN ----------
        holder.checkSeleccionar.setOnCheckedChangeListener(null);
        holder.checkSeleccionar.setChecked(carta.isSeleccionado());

        holder.checkSeleccionar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.toggleSeleccion(carta);
        });

        // ---------- CLICK PARA DAR VUELTA ----------
        holder.itemView.setOnClickListener(v -> viewModel.flipCarta(carta));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void update(List<Carta> nuevas) {
        this.lista = nuevas;
        notifyDataSetChanged();
    }

    class CartaViewHolder extends RecyclerView.ViewHolder {

        View layoutFront, layoutBack;
        ImageView imgFrente;
        TextView tvNombreFrente, tvNombre, tvNivel, tvVida, tvAtaque, tvTipo;
        CheckBox checkSeleccionar;

        public CartaViewHolder(@NonNull View itemView) {
            super(itemView);

            // Frente
            layoutFront = itemView.findViewById(R.id.layoutFront);
            imgFrente = itemView.findViewById(R.id.imgFrente);
            tvNombreFrente = itemView.findViewById(R.id.tvNombreFrente);

            // Reverso
            layoutBack = itemView.findViewById(R.id.layoutBack);
            tvNombre = itemView.findViewById(R.id.tvNombre);

            tvVida = itemView.findViewById(R.id.tvVida);
            tvAtaque = itemView.findViewById(R.id.tvAtaque);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            checkSeleccionar = itemView.findViewById(R.id.checkSeleccionar);
        }

        void mostrarFrente() {
            layoutFront.setVisibility(View.VISIBLE);
            layoutBack.setVisibility(View.GONE);
        }

        void mostrarDorso() {
            layoutFront.setVisibility(View.GONE);
            layoutBack.setVisibility(View.VISIBLE);
        }
    }
}
