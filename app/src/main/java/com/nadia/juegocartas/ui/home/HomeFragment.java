package com.nadia.juegocartas.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.nadia.juegocartas.R;
import com.nadia.juegocartas.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Navegar a la lista de cartas
        binding.iBTusCartas.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_nav_home_to_listaCartasFragment);
        });

        return view;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}