package com.nadia.juegocartas.ui.enfrentamientos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nadia.juegocartas.R;

public class EnfrentamientoResultadoFragment extends Fragment {

    private EnfrentamientoResultadoViewModel mViewModel;

    public static EnfrentamientoResultadoFragment newInstance() {
        return new EnfrentamientoResultadoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enfrentamiento_resultado, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EnfrentamientoResultadoViewModel.class);
        // TODO: Use the ViewModel
    }

}