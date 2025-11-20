package com.nadia.juegocartas.modelos;

public class Personaje {
    private int id;
    private String nombre;

    private int caraId;
    private int cabezaId;
    private int cuerpoId;

    private String imagen;
    private int vida;
    private int ataque;
    private int tipo;
    private int puntosHabilidad;

    public Personaje() {
    }

    public Personaje(int id, String nombre, int caraId, int cabezaId, int cuerpoId, String imagen, int vida, int ataque, int tipo, int puntosHabilidad) {
        this.id = id;
        this.nombre = nombre;
        this.caraId = caraId;
        this.cabezaId = cabezaId;
        this.cuerpoId = cuerpoId;
        this.imagen = imagen;
        this.vida = vida;
        this.ataque = ataque;
        this.tipo = tipo;
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

    public int getCaraId() {
        return caraId;
    }

    public void setCaraId(int caraId) {
        this.caraId = caraId;
    }

    public int getCabezaId() {
        return cabezaId;
    }

    public void setCabezaId(int cabezaId) {
        this.cabezaId = cabezaId;
    }

    public int getCuerpoId() {
        return cuerpoId;
    }

    public void setCuerpoId(int cuerpoId) {
        this.cuerpoId = cuerpoId;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getPuntosHabilidad() {
        return puntosHabilidad;
    }

    public void setPuntosHabilidad(int puntosHabilidad) {
        this.puntosHabilidad = puntosHabilidad;
    }
}
