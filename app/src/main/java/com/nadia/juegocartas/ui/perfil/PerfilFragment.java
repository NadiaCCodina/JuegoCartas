package com.nadia.juegocartas.ui.perfil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.nadia.juegocartas.databinding.FragmentPerfilBinding;
import com.nadia.juegocartas.request.ApiClient;


public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel mViewModel;

    private ActivityResultLauncher<Intent> abrirGaleria;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar launchers
        configurarLaunchers();

        // Cuando el usuario seleccione una nueva foto:
        mViewModel.getUriMutableLiveData().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                Glide.with(requireContext())
                        .load(uri)
                        .circleCrop()
                        .into(binding.imgAvatar);
            }
        });



        mViewModel.getmEstado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.tvNombre.setEnabled(aBoolean);
                binding.tvclave.setEnabled(aBoolean);
            }
        });

        mViewModel.getmNombre().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btnEditar.setText(s);
            }
        });

        mViewModel.getmUsuario().observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                binding.tvNombre.setText(usuario.getNombre());
                binding.tvEmail.setText(usuario.getEmail());
                binding.tvMonedas.setText(String.valueOf(usuario.getPuntosHabilidad()));

                Glide.with(requireContext())
                        .load(ApiClient.URL_BASE + usuario.getAvatar())
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

        // al tocar la imagen = cambiar foto
        binding.imgAvatar.setOnClickListener(v -> pedirPermisosYAbrirGaleria());

        return root;
    }

    // PERMISOS Y ABRIR GALERÍA


    private void configurarLaunchers() {

        abrirGaleria = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> mViewModel.recibirFoto(result)
        );

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        seleccionarImagen();
                    } else {
                        Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void pedirPermisosYAbrirGaleria() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED) {

                seleccionarImagen();

            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }

        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) {

                seleccionarImagen();

            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        abrirGaleria.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
