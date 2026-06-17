package LOGIC;

import java.time.LocalDate;

public class PartidaHistorial {
    public int       nivel;
    public boolean   gano;
    public int       estrellas;
    public int       puntuacion;
    public long      tiempoMs;
    public LocalDate fecha;

    public PartidaHistorial(int nivel, boolean gano, int estrellas,
                            int puntuacion, long tiempoMs, LocalDate fecha) {
        this.nivel      = nivel;
        this.gano       = gano;
        this.estrellas  = estrellas;
        this.puntuacion = puntuacion;
        this.tiempoMs   = tiempoMs;
        this.fecha      = fecha;
    }
}
