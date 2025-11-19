package com.nadia.juegocartas.ui.enfrentamientos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nadia.juegocartas.R;
import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class EnfrentamientoFragment extends Fragment {

    private EnfrentamientoViewModel mViewModel;

    public static EnfrentamientoFragment newInstance() {
        return new EnfrentamientoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EnfrentamientoViewModel.class);
        View view = inflater.inflate(R.layout.fragment_enfrentamiento, container, false);

        ArrayList<Carta> cartas = (ArrayList<Carta>) getArguments().getSerializable("cartasSeleccionadas");

        if (cartas != null) {
            mostrarCartas(view, cartas,
                    R.id.carta1,
                    R.id.carta2,
                    R.id.carta3
            );
        }

        // Observa cuando llegan las cartas del contrincante
        mViewModel.getmContrincante().observe(
                getViewLifecycleOwner(),
                contrincante -> {
                    if (contrincante != null && contrincante.getCartasContrincante() != null) {
                        Log.d("ENFRENTAMIENTO", "Entro a contrincante " );
                        mostrarCartas(
                                view,
                                contrincante.getCartasContrincante(),
                                R.id.oponenteCarta1,
                                R.id.oponenteCarta2,
                                R.id.oponenteCarta3
                        );
                    }
                }
        );
        mViewModel.obtenerListaCartasContrincante();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }
    private void mostrarCartas(View view, List<Carta> cartas,
                               int id1, int id2, int id3) {

        ImageView c1 = view.findViewById(id1);
        ImageView c2 = view.findViewById(id2);
        ImageView c3 = view.findViewById(id3);

        Glide.with(requireContext()).load(ApiClient.URL_BASE + cartas.get(0).getImagen()).into(c1);
        Glide.with(requireContext()).load(ApiClient.URL_BASE + cartas.get(1).getImagen()).into(c2);
        Glide.with(requireContext()).load(ApiClient.URL_BASE + cartas.get(2).getImagen()).into(c3);

        Log.d("ENFRENTAMIENTO", "Mostrando cartas: " + cartas.size());
    }


}