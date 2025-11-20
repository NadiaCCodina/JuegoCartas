package com.nadia.juegocartas.ui.gallery;

import android.os.Bundle;
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
import com.nadia.juegocartas.databinding.FragmentGalleryBinding;
import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.modelos.Personaje;
import com.nadia.juegocartas.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class CrearCartaFragment extends Fragment {

    private CrearCartaViewModel viewModel;

    private ImageView imgCara, imgCabeza, imgCuerpo;
    private TextView tvAtaque, tvVida, tvTipo;
    private Button btnCrear;

    private RecyclerView rvCaras, rvCabezas, rvCuerpos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_carta, container, false);

        // Vistas
        imgCara = view.findViewById(R.id.imgCara);
        imgCabeza = view.findViewById(R.id.imgCabeza);
        imgCuerpo = view.findViewById(R.id.imgCuerpo);
        tvAtaque = view.findViewById(R.id.tvAtaque);
        tvVida = view.findViewById(R.id.tvVida);
        tvTipo = view.findViewById(R.id.tvTipo);
        btnCrear = view.findViewById(R.id.btnCrear);

        rvCaras = view.findViewById(R.id.rvCaras);
        rvCabezas = view.findViewById(R.id.rvCabezas);
        rvCuerpos = view.findViewById(R.id.rvCuerpos);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(CrearCartaViewModel.class);

        // Observador
        viewModel.getPersonajeSeleccionado().observe(getViewLifecycleOwner(), personaje -> {
            if (personaje != null) {

                Glide.with(this).load(ApiClient.URL_BASE + personaje.getImagen()).into(imgCara);
                tvAtaque.setText("Ataque: " + personaje.getAtaque());
                tvVida.setText("Vida: " + personaje.getVida());
                tvTipo.setText("Tipo: " + (personaje.getTipo() == 0 ? "Cuerpo a Cuerpo" : "Distancia"));
            }
        });

        // Simulación de lista de partes (obtener de API o local)
        List<Carta> listaCaras = getCaras();
        List<Carta> listaCabezas = getCabezas();
        List<Carta> listaCuerpos = getCuerpos();

        // Adaptadores
        ParteAdapter adapterCaras = new ParteAdapter(listaCaras, viewModel::seleccionarCara);
        ParteAdapter adapterCabezas = new ParteAdapter(listaCabezas, viewModel::seleccionarCabeza);
        ParteAdapter adapterCuerpos = new ParteAdapter(listaCuerpos, viewModel::seleccionarCuerpo);

        rvCaras.setAdapter(adapterCaras);
        rvCaras.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rvCabezas.setAdapter(adapterCabezas);
        rvCabezas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rvCuerpos.setAdapter(adapterCuerpos);
        rvCuerpos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Botón crear
        btnCrear.setOnClickListener(v -> {
            Personaje p = viewModel.getPersonajeSeleccionado().getValue();
            if (p != null) {
                Toast.makeText(getContext(),
                        "Carta creada: Cara " + p.getCaraId() +
                                ", Cabeza " + p.getCabezaId() +
                                ", Cuerpo " + p.getCuerpoId(),
                        Toast.LENGTH_SHORT).show();
                // Llamar API para crear carta usando IDs
            } else {
                Toast.makeText(getContext(), "Selecciona todas las partes", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Métodos de ejemplo para obtener listas de partes
    private List<Carta> getCaras() { return new ArrayList<>(); }
    private List<Carta> getCabezas() { return new ArrayList<>(); }
    private List<Carta> getCuerpos() { return new ArrayList<>(); }
}
