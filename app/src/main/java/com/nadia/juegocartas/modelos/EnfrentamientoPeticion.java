package com.nadia.juegocartas.modelos;

import java.util.List;

public class EnfrentamientoPeticion {
    private int retadorId;
    private int contrincanteId;
    private List<Integer> cartasRetadorIds;
    private List<Integer> cartasContrincanteIds;

    public EnfrentamientoPeticion(int retadorId, int contrincanteId,
                                  List<Integer> cartasRetadorIds,
                                  List<Integer> cartasContrincanteIds) {
        this.retadorId = retadorId;
        this.contrincanteId = contrincanteId;
        this.cartasRetadorIds = cartasRetadorIds;
        this.cartasContrincanteIds = cartasContrincanteIds;
    }

    public int getRetadorId() {
        return retadorId;
    }

    public void setRetadorId(int retadorId) {
        this.retadorId = retadorId;
    }

    public int getContrincanteId() {
        return contrincanteId;
    }

    public void setContrincanteId(int contrincanteId) {
        this.contrincanteId = contrincanteId;
    }

    public List<Integer> getCartasRetadorIds() {
        return cartasRetadorIds;
    }

    public void setCartasRetadorIds(List<Integer> cartasRetadorIds) {
        this.cartasRetadorIds = cartasRetadorIds;
    }

    public List<Integer> getCartasContrincanteIds() {
        return cartasContrincanteIds;
    }

    public void setCartasContrincanteIds(List<Integer> cartasContrincanteIds) {
        this.cartasContrincanteIds = cartasContrincanteIds;
    }
}
