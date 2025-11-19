package com.nadia.juegocartas.modelos;

import java.util.Date;

public class Enfrentamiento {



    private int id;

    private int retadorId;
    private int contrincanteId;
    private int resultado;
    private Date fecha;

    public Enfrentamiento() {
    }

    public Enfrentamiento(int id, int retadorId, int contrincanteId, int resultado, Date fecha) {
        this.id = id;
        this.retadorId = retadorId;
        this.contrincanteId = contrincanteId;
        this.resultado = resultado;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
