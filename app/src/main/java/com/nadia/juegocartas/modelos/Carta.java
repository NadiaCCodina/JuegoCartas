package com.nadia.juegocartas.modelos;

import java.io.Serializable;

public class Carta implements Serializable {


    private int id;
    private int personajeId;
    private int mazoId;
    private Integer nivel;
    private String personajeNombre;
    private Integer puntosHabilidad;
    private String imagen;     // Ruta o nombre de imagen
    private int vida;
    private int ataque;
    private int tipo;
    private boolean seleccionado = false;

    private boolean mostrandoFrente = true;



    public boolean isMostrandoFrente() { return mostrandoFrente; }
    public void setMostrandoFrente(boolean m) { mostrandoFrente = m; }


    public Carta() {
    }

    public Carta(int id, int personajeId, int mazoId, Integer nivel, String personajeNombre, Integer puntosHabilidad, String imagen, int vida, int ataque, int tipo, boolean seleccionado) {
        this.id = id;
        this.personajeId = personajeId;
        this.mazoId = mazoId;
        this.nivel = nivel;
        this.personajeNombre = personajeNombre;
        this.puntosHabilidad = puntosHabilidad;
        this.imagen = imagen;
        this.vida = vida;
        this.ataque = ataque;
        this.tipo = tipo;
        this.seleccionado = seleccionado;

    }
    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonajeId() {
        return personajeId;
    }

    public void setPersonajeId(int personajeId) {
        this.personajeId = personajeId;
    }

    public int getMazoId() {
        return mazoId;
    }

    public void setMazoId(int mazoId) {
        this.mazoId = mazoId;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getPersonajeNombre() {
        return personajeNombre;
    }

    public void setPersonajeNombre(String personajeNombre) {
        this.personajeNombre = personajeNombre;
    }

    public Integer getPuntosHabilidad() {
        return puntosHabilidad;
    }

    public void setPuntosHabilidad(Integer puntosHabilidad) {
        this.puntosHabilidad = puntosHabilidad;
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
}
