package com.nadia.juegocartas.ui.gallery;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.modelos.Personaje;
import com.nadia.juegocartas.request.ApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearCartaViewModel extends AndroidViewModel {

    private MutableLiveData<Personaje> personajeSeleccionado = new MutableLiveData<>();
    private MutableLiveData<String> mMensaje = new MutableLiveData<>();

    public CrearCartaViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Personaje> getPersonajeSeleccionado() {
        return personajeSeleccionado;
    }

    public LiveData<String> getmMensaje() {
        return mMensaje;
    }

    // --------------------------
    // BUSCAR PERSONAJE POR PARTES
    // --------------------------
    public void buscarPersonajePorPartes(int cara, int cabeza, int cuerpo) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.JuegoServicio api = ApiClient.getJuegoServicio();
        Log.d("Carta", "cara "+cara+ " Cabeza "+cabeza +" Cuerpo "+cuerpo);
        Call<Personaje> call = api.buscarPersonajePorPartes("Bearer " + token, cara, cabeza, cuerpo);

        call.enqueue(new Callback<Personaje>() {
            @Override
            public void onResponse(Call<Personaje> call, Response<Personaje> response) {
                if (response.isSuccessful() && response.body() != null) {
                    personajeSeleccionado.postValue(response.body());
                    Log.d("errorPersonaje", personajeSeleccionado.toString());
                } else {
                    Toast.makeText(getApplication(), "No se encontró personaje con esas partes", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Personaje> call, Throwable throwable) {
                Log.d("errorPersonaje", throwable.getMessage());
                Toast.makeText(getApplication(), "Error al buscar personaje", Toast.LENGTH_LONG).show();
            }
        });
    }


    // CREAR CARTA

    public void crearCarta(int personajeId) {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.JuegoServicio api = ApiClient.getJuegoServicio();

        Call<Carta> call = api.crearCarta("Bearer " + token, personajeId);

        call.enqueue(new Callback<Carta>() {
            @Override
            public void onResponse(Call<Carta> call, Response<Carta> response) {
                if (response.isSuccessful() && response.body() != null) {
                   mMensaje.setValue("Personaje Creado Correctamente");

                } else {
                    try {
                        String errorMsg = response.errorBody() != null ? response.errorBody().string() : "Error al crear carta";
                        Toast.makeText(getApplication(), errorMsg, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(getApplication(), "Error al crear carta", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Carta> call, Throwable throwable) {
                Log.d("errorCarta", throwable.getMessage());
                Toast.makeText(getApplication(), "Error al crear carta", Toast.LENGTH_LONG).show();
            }


        });
    }
}