
package LOGIC;

import java.time.LocalDate;

public class USER {
    
    //ATRIBUTOS JUGADOR
   private String username;
   private String password;
   private String fullname;
   private LocalDate fechaRegistro;
   private long ultimaSesion;
   private String avatarPath;
   
   
   //ATRIBUTOS JUEGO
   private int nivelActual;
   private boolean[] nivelesDesbloqueados;
   private int[] puntajesPorNivel;
   private long tiempoTotalJugado;
   private int partidasJugadas;
   private int fallosTotales;
   private int estrellasTotal;
   private int puntuacionGeneral;
   
   //PREFERENCIAS
   private float volumen;
  
   //Social 
   private String[] amigos;
   
    public USER(String username, String password, String fullname) {
          this.username = username;
          this.password = password;
          this.fullname = fullname;
          this.fechaRegistro = LocalDate.now();
          this.ultimaSesion = System.currentTimeMillis();
          this.avatarPath = "";
          this.nivelActual = 1;
          this.nivelesDesbloqueados = new boolean[]{true, false, false, false, false};
          this.puntajesPorNivel = new int[5];
          this.tiempoTotalJugado = 0;
          this.partidasJugadas = 0;
          this.fallosTotales = 0;
          this.estrellasTotal = 0;
          this.puntuacionGeneral = 0;
          this.volumen = 1.0f;
          this.amigos = new String[0];
      }

}
