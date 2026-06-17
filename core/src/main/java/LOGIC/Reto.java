package LOGIC;

import java.time.LocalDateTime;

public class Reto {
    public enum Estado { PENDIENTE, ACEPTADO, RECHAZADO, COMPLETADO }

    public String        retador;
    public String        retado;
    public int           nivel;
    public LocalDateTime fecha;
    public Estado        estado;
    public int           puntajeRetador;
    public int           puntajeRetado;
    public String        ganador; 

    public Reto(String retador, String retado, int nivel) {
        this.retador         = retador;
        this.retado          = retado;
        this.nivel           = nivel;
        this.fecha           = LocalDateTime.now();
        this.estado          = Estado.PENDIENTE;
        this.puntajeRetador  = 0;
        this.puntajeRetado   = 0;
        this.ganador         = null;
    }

    public String getId() {
        return retador + "_vs_" + retado + "_n" + nivel + "_" + fecha.toLocalDate();
    }
}
