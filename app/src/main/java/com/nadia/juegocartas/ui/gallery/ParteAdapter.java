package com.nadia.juegocartas.ui.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nadia.juegocartas.R;
import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.request.ApiClient;

import java.util.List;

public class ParteAdapter extends RecyclerView.Adapter<ParteAdapter.ParteViewHolder> {

    public interface OnParteClickListener {
        void onParteClick(int parteId);
    }

    private List<Carta> listaPartes;
    private OnParteClickListener listener;

    public ParteAdapter(List<Carta> listaPartes, OnParteClickListener listener) {
        this.listaPartes = listaPartes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parte, parent, false);
        return new ParteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParteViewHolder holder, int position) {
        Carta parte = listaPartes.get(position);
        Glide.with(holder.itemView.getContext()).load(ApiClient.URL_BASE + parte.getImagen()).into(holder.imgParte);
        holder.itemView.setOnClickListener(v -> listener.onParteClick(parte.getId()));
    }

    @Override
    public int getItemCount() {
        return listaPartes.size();
    }

    static class ParteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgParte;

        public ParteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgParte = itemView.findViewById(R.id.imgParte);
        }
    }
}
