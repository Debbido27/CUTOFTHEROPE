package LOGIC;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SesionJuego {

    private static SesionJuego instancia;

    private String       username;
    private LoginManager gestor;

    private int   estrellasNivel   = 0;
    private int   fallosNivel      = 0;
    private long  tiempoInicioNivel;
    private int   nivelActual;
    private List<PartidaHistorial> historial = new ArrayList<>();

    private SesionJuego() {}

    public static SesionJuego get() {
        if (instancia == null) instancia = new SesionJuego();
        return instancia;
    }

    public void iniciar(String username, LoginManager gestor) {
        this.username = username;
        this.gestor   = gestor;
        historial.clear();
    }

    public void iniciarNivel(int nivel) {
        this.nivelActual      = nivel;
        this.estrellasNivel   = 0;
        this.fallosNivel      = 0;
        this.tiempoInicioNivel = System.currentTimeMillis();
    }

    public void registrarEstrella() {
        estrellasNivel++;
    }

    public void registrarFallo() {
        fallosNivel++;
    }

    public void finalizarNivel(boolean gano) {
        long tiempoMs  = System.currentTimeMillis() - tiempoInicioNivel;
        int  puntuacion = calcularPuntuacion(gano, estrellasNivel, tiempoMs);

        gestor.registrarPartida(username, nivelActual - 1,
            puntuacion, estrellasNivel,
            fallosNivel, tiempoMs);

        historial.add(new PartidaHistorial(
            nivelActual, gano, estrellasNivel,
            puntuacion, tiempoMs, LocalDate.now()));
    }

    private int calcularPuntuacion(boolean gano, int estrellas, long tiempoMs) {
        if (!gano) return 0;
        int base     = 1000;
        int bonusEst = estrellas * 200;
        int penalTiempo = (int)(tiempoMs / 1000) * 5;
        return Math.max(0, base + bonusEst - penalTiempo);
    }

    public List<PartidaHistorial> getHistorial() { return historial; }
    public String getUsername()                  { return username;  }
}
