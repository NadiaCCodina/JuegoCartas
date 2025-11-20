package com.nadia.juegocartas.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nadia.juegocartas.modelos.Carta;
import com.nadia.juegocartas.modelos.Contrincante;
import com.nadia.juegocartas.modelos.Enfrentamiento;
import com.nadia.juegocartas.modelos.EnfrentamientoPeticion;
import com.nadia.juegocartas.modelos.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class ApiClient {

    public static final String URL_BASE = "http://192.168.100.6:5208/";

    public static JuegoServicio getJuegoServicio() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(JuegoServicio.class);


    }

    public interface JuegoServicio {
        @FormUrlEncoded
        @POST("api/Usuarios/login")
        Call<String> loginForm(@Field("Usuario") String usuario, @Field("Clave") String clave);

        @GET("/api/usuarios/{id}/cartas")
        Call<List<Carta>> getCartasUsuario(@Header("Authorization") String token,
                                   @Path("id") int idJugador);

        @PUT("/api/usuarios/{id}/editar")
        Call<Usuario> editarUsuario(@Header("Authorization") String token,
                                        @Path("id") int idJugador,
                                        @Body Usuario usuario);

        @GET("/api/usuarios/{id}")
        Call<Usuario> getUsuario(@Header("Authorization") String token,
                                 @Path("id") int idJugador);
        @GET("api/EnfrentamientoApi/Contrincante")
        Call<Contrincante> getCartasContrincante();

        @POST("/api/EnfrentamientoApi/Crear")
        Call<Enfrentamiento> enfrentamiento(@Header("Authorization") String token,
                                            @Body EnfrentamientoPeticion enfrentamientoPeticion);


    }
    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
    }
    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }

    public static void guardarUserId(Context context, int id) {
        SharedPreferences sp = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("userId", id);
        editor.apply();
    }

    public static int obtenerUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
        return sp.getInt("userId", -1);
    }


}
