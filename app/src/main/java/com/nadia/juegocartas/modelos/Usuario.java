package com.nadia.juegocartas.modelos;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String clave;
    private String avatar;
    private int puntosHabilidad;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String email, String clave, String avatar, int puntosHabilidad) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.clave = clave;
        this.avatar = avatar;
        this.puntosHabilidad = puntosHabilidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getPuntosHabilidad() {
        return puntosHabilidad;
    }

    public void setPuntosHabilidad(int puntosHabilidad) {
        this.puntosHabilidad = puntosHabilidad;
    }
}
