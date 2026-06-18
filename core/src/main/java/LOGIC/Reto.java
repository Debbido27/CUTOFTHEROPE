package LOGIC;

import java.time.LocalDateTime;

public class Reto implements java.io.Serializable {

    public enum Estado { PENDIENTE, ACEPTADO, ESPERANDO_RIVAL, COMPLETADO, RECHAZADO }

    public String        retador;
    public String        retado;
    public int           nivel;
    public LocalDateTime fecha;
    public Estado        estado;

    public int           puntajeRetador;
    public int           puntajeRetado;
    public int           estrellasRetador;
    public int           estrellasRetado;
    public int           tiempoRetador;
    public int           tiempoRetado;
    public boolean       jugoRetador;
    public boolean       jugoRetado;

    public String        ganador;

    public Reto(String retador, String retado, int nivel) {
        this.retador          = retador;
        this.retado           = retado;
        this.nivel            = nivel;
        this.fecha            = LocalDateTime.now();
        this.estado           = Estado.PENDIENTE;
        this.puntajeRetador   = 0;
        this.puntajeRetado    = 0;
        this.estrellasRetador = 0;
        this.estrellasRetado  = 0;
        this.tiempoRetador    = 0;
        this.tiempoRetado     = 0;
        this.jugoRetador      = false;
        this.jugoRetado       = false;
        this.ganador          = null;
    }

    
    public String getRetador() {
        return retador;
    }

    public String getRetado() {
        return retado;
    }

    public int getNivel() {
        return nivel;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public int getPuntajeRetador() {
        return puntajeRetador;
    }

    public int getPuntajeRetado() {
        return puntajeRetado;
    }

    public int getEstrellasRetador() {
        return estrellasRetador;
    }

    public int getEstrellasRetado() {
        return estrellasRetado;
    }

    public int getTiempoRetador() {
        return tiempoRetador;
    }

    public int getTiempoRetado() {
        return tiempoRetado;
    }

    public boolean isJugoRetador() {
        return jugoRetador;
    }

    public boolean isJugoRetado() {
        return jugoRetado;
    }

    public String getGanador() {
        return ganador;
    }


    public void setRetador(String retador) {
        this.retador = retador;
    }

    public void setRetado(String retado) {
        this.retado = retado;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setPuntajeRetador(int puntajeRetador) {
        this.puntajeRetador = puntajeRetador;
    }

    public void setPuntajeRetado(int puntajeRetado) {
        this.puntajeRetado = puntajeRetado;
    }

    public void setEstrellasRetador(int estrellasRetador) {
        this.estrellasRetador = estrellasRetador;
    }

    public void setEstrellasRetado(int estrellasRetado) {
        this.estrellasRetado = estrellasRetado;
    }

    public void setTiempoRetador(int tiempoRetador) {
        this.tiempoRetador = tiempoRetador;
    }

    public void setTiempoRetado(int tiempoRetado) {
        this.tiempoRetado = tiempoRetado;
    }

    public void setJugoRetador(boolean jugoRetador) {
        this.jugoRetador = jugoRetador;
    }

    public void setJugoRetado(boolean jugoRetado) {
        this.jugoRetado = jugoRetado;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public String getId() {
        return retador + "_vs_" + retado + "_n" + nivel + "_" + fecha.toLocalDate();
    }
}