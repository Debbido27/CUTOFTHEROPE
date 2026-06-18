        package LOGIC;

        import java.io.Serializable;  
        import java.time.LocalDate;

        public class PartidaHistorial implements Serializable {  

        private int nivel;
        private int puntaje;
        private int estrellas;
        private int fallos;
        private long tiempoMs;
        private LocalDate fecha;
        private boolean gano;

        public PartidaHistorial(int nivel, int puntaje, int estrellas,
                            int fallos, long tiempoMs, LocalDate fecha, boolean gano) {
        this.nivel = nivel;
        this.puntaje = puntaje;
        this.estrellas = estrellas;
        this.fallos = fallos;
        this.tiempoMs = tiempoMs;
        this.fecha = fecha;
        this.gano = gano;
        }

        public PartidaHistorial(int nivel, boolean gano, int estrellas,
                            int puntaje, long tiempoMs, LocalDate fecha) {
        this(nivel, puntaje, estrellas, 0, tiempoMs, fecha, gano);
        }

        public int getNivel() { return nivel; }
        public int getPuntaje() { return puntaje; }
        public int getEstrellas() { return estrellas; }
        public int getFallos() { return fallos; }
        public long getTiempoMs() { return tiempoMs; }
        public LocalDate getFecha() { return fecha; }
        public boolean isGano() { return gano; }

        public void setNivel(int nivel) { this.nivel = nivel; }
        public void setPuntaje(int puntaje) { this.puntaje = puntaje; }
        public void setEstrellas(int estrellas) { this.estrellas = estrellas; }
        public void setFallos(int fallos) { this.fallos = fallos; }
        public void setTiempoMs(long tiempoMs) { this.tiempoMs = tiempoMs; }
        public void setFecha(LocalDate fecha) { this.fecha = fecha; }
        public void setGano(boolean gano) { this.gano = gano; }

        @Override
        public String toString() {
        return "Nivel " + nivel + ": " + puntaje + " pts, " +
            estrellas + "*, " + fallos + " fallos, " +
            (gano ? "V/ Ganado" : "X Perdido") + " - " + fecha;
        }
        }
