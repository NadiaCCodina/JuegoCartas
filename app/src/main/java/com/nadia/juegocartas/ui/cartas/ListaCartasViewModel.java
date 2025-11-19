package com.nadia.juegocartas.ui.cartas;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.request.ApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaCartasViewModel extends AndroidViewModel {


    private MutableLiveData<Carta> mCartA;
    private static final int MAX_SELECCION = 3;
    private MutableLiveData<List<Carta>> seleccionadasLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Carta>> mListaCarta = new MutableLiveData<>();
    public ListaCartasViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<Carta> getMCarta(){
        if (mCartA == null){
            mCartA = new MutableLiveData<>();
        }
        return mCartA;
    }
    public MutableLiveData<List<Carta>> getmListaCarta() {
        return mListaCarta;
    }

    public MutableLiveData<List<Carta>> getSeleccionadasLiveData() {
        return seleccionadasLiveData;
    }

    public void setSeleccionadasLiveData(MutableLiveData<List<Carta>> seleccionadasLiveData) {
        this.seleccionadasLiveData = seleccionadasLiveData;
    }

    public void obtenerListaCartas(int idUsuario){
        String token = ApiClient.leerToken(getApplication());
        ApiClient.JuegoServicio api = ApiClient.getJuegoServicio();
        Call<List<Carta>> call = api.getCartasUsuario("Bearer "+ token, idUsuario);

        call.enqueue(new Callback<List<Carta>>() {
            @Override
            public void onResponse(Call<List<Carta>> call, Response<List<Carta>> response) {
                if (response.isSuccessful()){
                    mListaCarta.postValue(response.body());
                }else {
                    Toast.makeText(getApplication(),"No se obtuvieron Cartas",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Carta>> call, Throwable throwable) {
                Log.d("errorInmueble",throwable.getMessage());

                Toast.makeText(getApplication(),"Error al obtener Cartas",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void toggleSeleccion(Carta p) {
        List<Carta> lista = mListaCarta.getValue();
        if (lista == null) return;

        long count = lista.stream().filter(Carta::isSeleccionado).count();

        // Si la carta NO está seleccionada y ya hay 3, no permite más
        if (!p.isSeleccionado() && count >= MAX_SELECCION) {
            Toast.makeText(getApplication(), "Solo se pueden seleccionar 3", Toast.LENGTH_LONG).show();
            return;
        }

        // Cambiar estado de selección
        p.setSeleccionado(!p.isSeleccionado());

        // Actualizar lista de seleccionadas
        List<Carta> seleccionadas = lista.stream()
                .filter(Carta::isSeleccionado)
                .collect(Collectors.toList());

        seleccionadasLiveData.setValue(seleccionadas);

        // Actualizar RecyclerView de todas las cartas
        mListaCarta.setValue(lista);
    }


    public void flipCarta(Carta p) {
        p.setMostrandoFrente(!p.isMostrandoFrente());
        mListaCarta.setValue(mListaCarta.getValue());
    }


}