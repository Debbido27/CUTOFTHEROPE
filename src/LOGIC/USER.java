
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
   private int fallosTotal;
   private int estrellasTotal;
   private int puntuacionGenera;
   
   
}
