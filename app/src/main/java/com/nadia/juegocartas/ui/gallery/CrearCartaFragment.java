package com.nadia.juegocartas.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nadia.juegocartas.R;
import com.nadia.juegocartas.databinding.FragmentCrearCartaBinding;

import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.modelos.Personaje;
import com.nadia.juegocartas.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class CrearCartaFragment extends Fragment {

    private FragmentCrearCartaBinding binding;
    private CrearCartaViewModel viewModel;

    // IDs de las partes seleccionadas actualmente
    private int caraSeleccionada = 1;
    private int cabezaSeleccionada = 1;
    private int cuerpoSeleccionado = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCrearCartaBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CrearCartaViewModel.class);


        // OBSERVERS

        viewModel.getPersonajeSeleccionado().observe(getViewLifecycleOwner(), personaje -> {
            if (personaje != null) {

                // IMÁGENES
                Glide.with(requireContext())
                        .load(ApiClient.URL_BASE + personaje.getImagen())
                        .into(binding.imgCartaCentral);
                Log.d("crear carta", "Entro a mostrar personaje "+personaje.getImagen() );
                // STATS
                binding.tvAtaque.setText("Ataque: " + personaje.getAtaque());
                binding.tvVida.setText("Vida: " + personaje.getVida());
                binding.tvTipo.setText("Tipo: " + (personaje.getTipo() == 0 ?
                        "Cuerpo a Cuerpo" : "Distancia"));
            }
        });


        // CARGAR PERSONAJE INICIAL

        buscarPersonajePorPartes();


        // LISTENERS PARA CARAS

        binding.btnCara1.setOnClickListener(v -> {
            caraSeleccionada = 1;
            buscarPersonajePorPartes();
        });

        binding.btnCara2.setOnClickListener(v -> {
            caraSeleccionada = 4;
            buscarPersonajePorPartes();
        });

        binding.btnCara3.setOnClickListener(v -> {
            caraSeleccionada = 5;
            buscarPersonajePorPartes();
        });


        // LISTENERS PARA CABEZAS
        binding.btnCabeza1.setOnClickListener(v -> {
            cabezaSeleccionada = 1;
            buscarPersonajePorPartes();
        });

        binding.btnCabeza2.setOnClickListener(v -> {
            cabezaSeleccionada = 2;
            buscarPersonajePorPartes();
        });

        binding.btnCabeza3.setOnClickListener(v -> {
            cabezaSeleccionada = 3;
            buscarPersonajePorPartes();
        });


        // LISTENERS PARA CUERPOS

        binding.btnCuerpo1.setOnClickListener(v -> {
            cuerpoSeleccionado = 1;
            buscarPersonajePorPartes();
        });

        binding.btnCuerpo2.setOnClickListener(v -> {
            cuerpoSeleccionado = 3;
            buscarPersonajePorPartes();
        });

        binding.btnCuerpo3.setOnClickListener(v -> {
            cuerpoSeleccionado = 4;
            buscarPersonajePorPartes();
        });


        // BOTÓN CREAR

        binding.btnCrear.setOnClickListener(v -> {
            Personaje p = viewModel.getPersonajeSeleccionado().getValue();

            if (p == null) {
                Toast.makeText(requireContext(), "No se pudo cargar el personaje", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamar al endpoint para crear la carta
            viewModel.crearCarta(p.getId());
        });

        return binding.getRoot();
    }


    // BUSCAR PERSONAJE POR PARTES

    private void buscarPersonajePorPartes() {
        viewModel.buscarPersonajePorPartes(caraSeleccionada, cabezaSeleccionada, cuerpoSeleccionado);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}