package com.nadia.juegocartas.modelos;

import java.util.List;

public class Contrincante {
    private int idContrincate;
    private String nombre;

    private List<Carta> cartasContrincante;

    public Contrincante() {
    }

    public Contrincante(int idContrincate, String nombre, List<Carta> cartasContrincante) {
        this.idContrincate = idContrincate;
        this.nombre = nombre;
        this.cartasContrincante = cartasContrincante;
    }

    public int getIdContrincate() {
        return idContrincate;
    }

    public void setIdContrincate(int idContrincate) {
        this.idContrincate = idContrincate;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Carta> getCartasContrincante() {
        return cartasContrincante;
    }

    public void setCartasContrincante(List<Carta> cartasContrincante) {
        this.cartasContrincante = cartasContrincante;
    }
}
