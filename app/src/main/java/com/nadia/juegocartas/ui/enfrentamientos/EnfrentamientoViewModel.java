package com.nadia.juegocartas.ui.enfrentamientos;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.modelos.Contrincante;
import com.nadia.juegocartas.modelos.Enfrentamiento;
import com.nadia.juegocartas.modelos.EnfrentamientoPeticion;
import com.nadia.juegocartas.request.ApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnfrentamientoViewModel extends AndroidViewModel {
    private List<Carta> cartasRetador;
    private MutableLiveData<Contrincante> mContrincante = new MutableLiveData<>();

    private MutableLiveData<String> mResultado = new MutableLiveData<>();

    public EnfrentamientoViewModel(@NonNull Application application) {
        super(application);
    }

    public void setCartasRetador(List<Carta> cartas) {
        this.cartasRetador = cartas;


    }

    public MutableLiveData<Contrincante> getmContrincante() {
        return mContrincante;
    }

    public void setmContrincante(MutableLiveData<Contrincante> mContrincante) {
        this.mContrincante = mContrincante;
    }

    public MutableLiveData<String> getmResultado() {
        return mResultado;
    }

    public void setmResultado(MutableLiveData<String> mResultado) {
        this.mResultado = mResultado;
    }

    public void obtenerListaCartasContrincante() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.JuegoServicio api = ApiClient.getJuegoServicio();
        Call<Contrincante> call = api.getCartasContrincante();

        call.enqueue(new Callback<Contrincante>() {
            @Override
            public void onResponse(Call<Contrincante> call, Response<Contrincante> response) {
                if (response.isSuccessful()) {
                    mContrincante.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "No se obtuvieron Cartas", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Contrincante> call, Throwable throwable) {
                Log.d("errorContrincante", throwable.getMessage());

                Toast.makeText(getApplication(), "Error al obtener Cartas", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void enfrentamiento(int idRetador, int idContrintante, List<Carta> cartasRetador, List<Carta> cartasContrincante) {
        List<Integer> idsCartasRetador = new ArrayList<>();
        List<Integer> idsCartasContrincante = new ArrayList<>();

        if (cartasRetador != null) {
            for (Carta c : cartasRetador) {
                idsCartasRetador.add(c.getId());
            }
        }

        if (cartasContrincante != null) {
            for (Carta c : cartasContrincante) {
                idsCartasContrincante.add(c.getId());
            }
        }

        EnfrentamientoPeticion enfrentamientoPeticion =
                new EnfrentamientoPeticion(idRetador, idContrintante, idsCartasRetador, idsCartasContrincante);

        String token = ApiClient.leerToken(getApplication());
        ApiClient.JuegoServicio api = ApiClient.getJuegoServicio();
        Call<Enfrentamiento> call = api.enfrentamiento("Bearer " + token, enfrentamientoPeticion);

        call.enqueue(new Callback<Enfrentamiento>() {

            @Override
            public void onResponse(Call<Enfrentamiento> call, Response<Enfrentamiento> response) {

                if (!response.isSuccessful()) {
                    mResultado.setValue("Error del servidor: " + response.code());
                    return;
                }

                Enfrentamiento respuesta = response.body();

                if (respuesta == null) {
                    mResultado.setValue("Respuesta vacía del servidor");
                    return;
                }

                int resultado = respuesta.getResultado();

                switch (resultado) {
                    case 1:
                        mResultado.setValue("Ganaste!!");
                        break;
                    case 2:
                        mResultado.setValue("Empate");
                        break;
                    case 3:
                        mResultado.setValue("Perdiste");
                        break;
                    default:
                        mResultado.setValue("Resultado desconocido: " + resultado);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Enfrentamiento> call, Throwable t) {
                mResultado.setValue("Error de conexión: " + t.getMessage());
            }
        });

    }
}