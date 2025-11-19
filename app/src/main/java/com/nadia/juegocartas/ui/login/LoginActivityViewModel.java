package com.nadia.juegocartas.ui.login;

import android.app.Application;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nadia.juegocartas.request.ApiClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivityViewModel extends AndroidViewModel {
    private  MutableLiveData<String> mMensaje;
    private MutableLiveData<Boolean> sesionCorrecta;
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<String> getmMensaje() {
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();

        }
        return mMensaje;

    }
    public LiveData<Boolean> getsesionCorrecta() {

        if (sesionCorrecta == null) {
            sesionCorrecta = new MutableLiveData<>();

        }
        return sesionCorrecta;

    }
    public void validarUsuario(String email, String password){
        if (email.isEmpty() || password.isEmpty()) {
            mMensaje.setValue("Todos los campos son obligatorios");

            return;
        }
       ApiClient.JuegoServicio juegoServicio= ApiClient.getJuegoServicio();
       Call<String> call = juegoServicio.loginForm(email, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){

                    String token= response.body();

                    Log.d("token ",token);
                   ApiClient.guardarToken(getApplication(), token);
                    try {
                        String[] partes = token.split("\\.");

                        // Decodificar payload (parte 1 → header, parte 2 → payload)
                        String payload = new String(android.util.Base64.decode(partes[1], Base64.DEFAULT));

                        JSONObject json = new JSONObject(payload);

                        // Asegurate de que en tu C# agregaste:
                        // new Claim("UserId", p.Id.ToString())
                        int userId = json.getInt("UserId");

                        Log.d("LOGIN", "UserID decodificado: " + userId);

                        // Guardar UserId y Token
                        ApiClient.guardarToken(getApplication(), token);
                        ApiClient.guardarUserId(getApplication(), userId);

                    } catch (Exception e) {
                        Log.e("LOGIN", "⚠ Error al decodificar JWT: " + e.getMessage());
                    }

                    sesionCorrecta.setValue(true);

                }else{
                    Log.d("token error mensaje", response.message());
                    Log.d("token codigo", response.code()+"");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.d("ERROR VALIDAR USUARIO", throwable.getMessage());
               // sesionCorrecta.setValue(true);
            }
        });

//


        }
        }