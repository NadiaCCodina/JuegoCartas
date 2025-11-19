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
import com.nadia.juegocartas.request.ApiClient;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnfrentamientoViewModel extends AndroidViewModel {
    private List<Carta> cartasRetador;
    private MutableLiveData<Contrincante> mContrincante= new MutableLiveData<>();

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

    public void obtenerListaCartasContrincante(){
        String token = ApiClient.leerToken(getApplication());
        ApiClient.JuegoServicio api = ApiClient.getJuegoServicio();
        Call<Contrincante> call = api.getCartasContrincante();

        call.enqueue(new Callback<Contrincante>() {
            @Override
            public void onResponse(Call<Contrincante> call, Response<Contrincante> response) {
                if (response.isSuccessful()){
                    mContrincante.postValue(response.body());
                }else {
                    Toast.makeText(getApplication(),"No se obtuvieron Cartas",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Contrincante> call, Throwable throwable) {
                Log.d("errorContrincante",throwable.getMessage());

                Toast.makeText(getApplication(),"Error al obtener Cartas",Toast.LENGTH_LONG).show();
            }




        });
    }

}