package com.nadia.juegocartas.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nadia.juegocartas.modelos.Usuario;
import com.nadia.juegocartas.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mEstado = new MutableLiveData<>();
    private MutableLiveData<String> mNombre = new MutableLiveData<>();
    private MutableLiveData<Uri> uriMutableLiveData= new MutableLiveData<>();
    private Context context;

    private MutableLiveData<Usuario> mUsuario = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<Usuario> getmUsuario() {
        return mUsuario;
    }

    public MutableLiveData<String> getmNombre() {
        return mNombre;
    }

    public MutableLiveData<Boolean> getmEstado() {
        return mEstado;
    }

    public MutableLiveData<Uri> getUriMutableLiveData() {
        return uriMutableLiveData;
    }

    public void setUriMutableLiveData(MutableLiveData<Uri> uriMutableLiveData) {
        this.uriMutableLiveData = uriMutableLiveData;
    }

    public void datosUsuario(int idUsuario)
    {String token = ApiClient.leerToken(getApplication());
    ApiClient.JuegoServicio juegoServicio= ApiClient.getJuegoServicio();
    Call<Usuario> call = juegoServicio.getUsuario("Bearer "+ token, idUsuario);
        call.enqueue(new Callback<Usuario>(){

            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()){
                    mUsuario.postValue(response.body());
                }else {
                Toast.makeText(getApplication(),"No se obtuvieron Cartas",Toast.LENGTH_LONG).show();
            }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable throwable) {
                Log.d("errorUsuario",throwable.getMessage());

                Toast.makeText(getApplication(),"Error al obtener Usuario",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void cambioBotonUsuario(String nombreBoton, String nombre, String clave) {

        if (nombreBoton.equalsIgnoreCase("EDITAR")) {

            mEstado.setValue(true);      // habilita edición
            mNombre.setValue("GUARDAR"); // cambia texto del botón
            return;
        }else {

            // Si llega acá → es GUARDAR
            mEstado.setValue(false);
            mNombre.setValue("EDITAR");

            if (!validarCampos(nombre)) {
                return;
            }

            // Obtener datos actuales del usuario
            Usuario original = mUsuario.getValue();
            if (original == null) {
                Toast.makeText(getApplication(), "Usuario no cargado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear objeto modificado
            Usuario usuarioModel = new Usuario();
            usuarioModel.setId(original.getId());
            usuarioModel.setNombre(nombre);
            usuarioModel.setEmail(clave);

            // FOTO si se eligió una nueva
            byte[] foto = transformarImagen();
            if (foto != null) {
                String fotoBase64 = Base64.encodeToString(foto, Base64.NO_WRAP);
                usuarioModel.setAvatar(fotoBase64); // ahora sí es String

            } else {
                usuarioModel.setAvatar(original.getAvatar()); // mantener la anterior
            }

            // TOKEN
            String token = ApiClient.leerToken(context);
            ApiClient.JuegoServicio api = ApiClient.getJuegoServicio();

            // PETICIÓN PUT
            Call<Usuario> call = api.editarUsuario(
                    "Bearer " + token,
                    original.getId(),
                    usuarioModel
            );

            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplication(), "Datos actualizados", Toast.LENGTH_LONG).show();

                        // Actualizar LiveData
                        mUsuario.postValue(response.body());
                    } else {
                        Toast.makeText(getApplication(), "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable throwable) {
                    Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private boolean validarCampos(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            Toast.makeText(getApplication(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
    }

    private byte[] transformarImagen() {
        try {
            Uri uri = uriMutableLiveData.getValue();
            if (uri == null) return null;

            InputStream is = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bmp = BitmapFactory.decodeStream(is);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);

            return baos.toByteArray();

        } catch (Exception e) {
            return null;
        }
    }


}