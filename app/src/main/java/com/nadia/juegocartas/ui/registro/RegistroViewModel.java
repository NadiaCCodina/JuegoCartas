package com.nadia.juegocartas.ui.registro;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.nadia.juegocartas.modelos.Usuario;
import com.nadia.juegocartas.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroViewModel extends AndroidViewModel {

    private MutableLiveData<String> mensaje = new MutableLiveData<>();
    private MutableLiveData<Boolean> registroExitoso = new MutableLiveData<>();
    private MutableLiveData<Uri> fotoSeleccionada = new MutableLiveData<>();

    // Estados de validación
    private MutableLiveData<String> errorNombre = new MutableLiveData<>();
    private MutableLiveData<String> errorEmail = new MutableLiveData<>();
    private MutableLiveData<String> errorPassword = new MutableLiveData<>();
    private MutableLiveData<String> errorConfirmPassword = new MutableLiveData<>();

    public RegistroViewModel(@NonNull Application application) {
        super(application);
    }

    // Getters
    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public LiveData<Boolean> getRegistroExitoso() {
        return registroExitoso;
    }

    public LiveData<Uri> getFotoSeleccionada() {
        return fotoSeleccionada;
    }

    public LiveData<String> getErrorNombre() {
        return errorNombre;
    }

    public LiveData<String> getErrorEmail() {
        return errorEmail;
    }

    public LiveData<String> getErrorPassword() {
        return errorPassword;
    }

    public LiveData<String> getErrorConfirmPassword() {
        return errorConfirmPassword;
    }


    // RECIBIR FOTO DESDE ACTIVITY

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                Log.d("RegistroFoto", uri.toString());
                fotoSeleccionada.setValue(uri);
            }
        }
    }


    // VALIDACIONES

    public boolean validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            errorNombre.setValue("Ingresá tu nombre");
            return false;
        }
        if (nombre.trim().length() < 3) {
            errorNombre.setValue("El nombre debe tener al menos 3 caracteres");
            return false;
        }
        errorNombre.setValue(null);
        return true;
    }

    public boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            errorEmail.setValue("Ingresá tu email");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorEmail.setValue("Ingresá un email válido");
            return false;
        }
        errorEmail.setValue(null);
        return true;
    }

    public boolean validarPassword(String password) {
        if (password == null || password.isEmpty()) {
            errorPassword.setValue("Ingresá una contraseña");
            return false;
        }
        if (password.length() < 6) {
            errorPassword.setValue("Mínimo 6 caracteres");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            errorPassword.setValue("Debe contener al menos una mayúscula");
            return false;
        }
        if (!password.matches(".*[0-9].*")) {
            errorPassword.setValue("Debe contener al menos un número");
            return false;
        }
        errorPassword.setValue(null);
        return true;
    }

    public boolean validarConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            errorConfirmPassword.setValue("Confirmá tu contraseña");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            errorConfirmPassword.setValue("Las contraseñas no coinciden");
            return false;
        }
        errorConfirmPassword.setValue(null);
        return true;
    }

    public boolean validarTodosCampos(String nombre, String email, String password, String confirmPassword) {
        boolean nombreValido = validarNombre(nombre);
        boolean emailValido = validarEmail(email);
        boolean passwordValido = validarPassword(password);
        boolean confirmPasswordValido = validarConfirmPassword(password, confirmPassword);

        return nombreValido && emailValido && passwordValido && confirmPasswordValido;
    }


    // TRANSFORMAR IMAGEN A BYTES

    private byte[] transformarImagen() {
        try {
            Uri uri = fotoSeleccionada.getValue();
            if (uri == null) {
                return new byte[]{}; // Sin imagen
            }

            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Comprimir imagen si es muy grande
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException ex) {
            Log.e("RegistroViewModel", "Error al transformar imagen: " + ex.getMessage());
            return new byte[]{};
        }
    }


    // REGISTRAR USUARIO

    public void registrarUsuario(String nombre, String email, String Clave, String confirmPassword) {
        // Validar todos los campos
        if (!validarTodosCampos(nombre, email, Clave, confirmPassword)) {
            mensaje.setValue("Por favor corregí los errores");
            Toast.makeText(getApplication(), "Corregi los errorres", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            ApiClient.JuegoServicio api = ApiClient.getJuegoServicio();

            // Convertir imagen a bytes
            byte[] imagenBytes = transformarImagen();

            // Preparar MultipartBody.Part para la imagen (opcional)
            MultipartBody.Part file = null;
            if (imagenBytes.length > 0) {
                Log.d("if imagen", "entro" );
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagenBytes);
                file = MultipartBody.Part.createFormData("file", "perfil.jpg", requestFile);
            }

          Usuario usuario = new Usuario(nombre, email, Clave);
            Gson gson = new Gson();
            String usuarioString = gson.toJson(usuario);





            Log.d("DEBUG_JSON", "JSON a enviar: " + usuarioString);

            RequestBody usuarioBody = RequestBody.create(
                    MediaType.parse("application/json"), usuarioString
            );
            Call<Usuario> call = api.registrarUsuario(usuarioBody, file);

            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mensaje.postValue("Registro exitoso. Iniciá sesión");
                        Toast.makeText(getApplication(), "Registro exitoso", Toast.LENGTH_LONG).show();
                        registroExitoso.postValue(true);
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                String errorMsg = response.errorBody().string(); // consumir solo 1 vez
                                Log.d("errorRegistro", errorMsg);
                                mensaje.postValue(errorMsg);
                                Toast.makeText(getApplication(), "Registro fallo: " + errorMsg, Toast.LENGTH_LONG).show();
                            } else {
                                mensaje.postValue("Error en el registro");
                            }
                        } catch (Exception e) {
                            mensaje.postValue("Error en el registro");
                        }
                        registroExitoso.postValue(false);
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable throwable) {
                    Log.d("errorRegistro", throwable.getMessage());
                    mensaje.postValue("Error de conexión. Verificá tu internet");
                    registroExitoso.postValue(false);
                }
            });

        } catch (Exception ex) {
            Log.e("errorRegistro", ex.getMessage());
            mensaje.setValue("Error al procesar los datos");
        }
    }

    // --------------------------
    // LIMPIAR ERRORES
    // --------------------------
    public void limpiarErrores() {
        errorNombre.setValue(null);
        errorEmail.setValue(null);
        errorPassword.setValue(null);
        errorConfirmPassword.setValue(null);
    }
}