package com.nadia.juegocartas.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.nadia.juegocartas.databinding.FragmentPerfilBinding;
import com.nadia.juegocartas.request.ApiClient;


public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mViewModel.getmEstado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.tvNombre.setEnabled(aBoolean);
                binding.tvclave.setEnabled(aBoolean);

            }
        });
        mViewModel.getmNombre().observe((getViewLifecycleOwner()), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btnEditar.setText(s);

            }
        });
        mViewModel.getmUsuario().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                binding.tvNombre.setText(usuario.getNombre());
                binding.tvEmail.setText( usuario.getEmail());
                binding.tvMonedas.setText(String.valueOf( usuario.getPuntosHabilidad()));

                // Si tenés avatar con Glide:
                Glide.with(requireContext())
                        .load(ApiClient.URL_BASE +  usuario.getAvatar())
                        .circleCrop()
                        .into(binding.imgAvatar);
            }
        });
        int userId = ApiClient.obtenerUserId(requireContext());

        mViewModel.datosUsuario(userId);
        binding.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewModel.cambioBotonUsuario(
                        binding.btnEditar.getText().toString(),
                        binding.tvNombre.getText().toString(),
                        binding.tvclave.getText().toString()
                );
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}