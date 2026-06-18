package LOGIC;

import java.time.LocalDateTime;

public class Notificacion implements java.io.Serializable {    public enum Tipo {
        SOLICITUD_RECIBIDA, SOLICITUD_ACEPTADA,
        RETO_RECIBIDO, RETO_ACEPTADO, RETO_RECHAZADO,
        RETO_GANADO, RETO_PERDIDO
    }

    public Tipo          tipo;
    public String        origen;
    public String        destino;
    public int           nivel;
    public LocalDateTime fecha;
    public boolean       leida;

    public Notificacion(Tipo tipo, String origen, String destino, int nivel) {
        this.tipo    = tipo;
        this.origen  = origen;
        this.destino = destino;
        this.nivel   = nivel;
        this.fecha   = LocalDateTime.now();
        this.leida   = false;
    }

    public String getId() {
        return origen + "_" + destino + "_" + nivel + "_" + fecha.toString();
    }
}
