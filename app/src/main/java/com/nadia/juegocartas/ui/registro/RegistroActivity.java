package com.nadia.juegocartas.ui.registro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.nadia.juegocartas.R;
import com.nadia.juegocartas.databinding.ActivityRegistroBinding;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private RegistroViewModel viewModel;

    // Launcher para seleccionar imagen
    private ActivityResultLauncher<Intent> abrirGaleria;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(RegistroViewModel.class);

        configurarLaunchers();
        configurarObservers();
        configurarListeners();
        configurarValidacionTiempoReal();
    }

    private void configurarLaunchers() {
        // Launcher para abrir galería
        abrirGaleria = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> viewModel.recibirFoto(result)
        );

        // Launcher para pedir permisos
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        seleccionarImagen();
                    } else {
                        Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void configurarObservers() {
        // Mensajes
        viewModel.getMensaje().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

        // Registro exitoso
        viewModel.getRegistroExitoso().observe(this, exitoso -> {
            if (exitoso != null && exitoso) {
                finish();
            }
        });

        // Foto seleccionada
        viewModel.getFotoSeleccionada().observe(this, uri -> {
            if (uri != null) {
                Glide.with(this)
                        .load(uri)
                        .circleCrop()
                        .into(binding.imgFotoPerfil);
            }
        });

        // Errores de validación
        viewModel.getErrorNombre().observe(this, error ->
                binding.etNombre.setError(error));

        viewModel.getErrorEmail().observe(this, error ->
                binding.etEmail.setError(error));

        viewModel.getErrorPassword().observe(this, error ->
                binding.etPassword.setError(error));

        viewModel.getErrorConfirmPassword().observe(this, error ->
                binding.etConfirmPassword.setError(error));
    }

    private void configurarListeners() {
        // Cambiar foto
        binding.btnCambiarFoto.setOnClickListener(v -> pedirPermisosYAbrirGaleria());

        // Botón Registrarse
        binding.btnRegistro.setOnClickListener(v -> {

            String nombre = binding.etNombre.getText().toString();
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            String confirmPassword = binding.etConfirmPassword.getText().toString();
            Toast.makeText(getApplication(), "Entro al boton registro "+ nombre+" "+ password, Toast.LENGTH_LONG).show();

            viewModel.registrarUsuario(nombre, email, password, confirmPassword);
        });

        // Ir a Login
        binding.tvIrLogin.setOnClickListener(v -> finish());
    }

    private void pedirPermisosYAbrirGaleria() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) {
                seleccionarImagen();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
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

    private void configurarValidacionTiempoReal() {
        binding.etNombre.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.validarNombre(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.validarEmail(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.validarPassword(s.toString());
                String confirm = binding.etConfirmPassword.getText().toString();
                if (!confirm.isEmpty()) {
                    viewModel.validarConfirmPassword(s.toString(), confirm);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        binding.etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = binding.etPassword.getText().toString();
                viewModel.validarConfirmPassword(password, s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}