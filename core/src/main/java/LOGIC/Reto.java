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

    public String getId() {
        return retador + "_vs_" + retado + "_n" + nivel + "_" + fecha.toLocalDate();
    }
}
