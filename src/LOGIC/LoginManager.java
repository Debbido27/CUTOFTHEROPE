
package LOGIC;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;


public class LoginManager {
    private static final String BASE_FOLDER = "CTR_RAIZ";
    private static final String USERS_FILE  = BASE_FOLDER + "/users.ctr";
    private static final String USERS_TEMP  = BASE_FOLDER + "/users_temp.ctr";

    private USER currentUser;
    
    public LoginManager() {
        File base = new File(BASE_FOLDER);
        if (!base.exists()) base.mkdir();
        try { new File(USERS_FILE).createNewFile(); } catch (IOException e) {}
        limpiarUsuariosHuerfanos();
        currentUser = cargarUltimoUsuarioActivo();
    }
    
    
            private USER leerRegistro(RandomAccessFile f) throws IOException {
            String username   = f.readUTF();
            String password   = f.readUTF();
            String fullname   = f.readUTF();
            long fechaReg     = f.readLong();
            long ultimaSesion = f.readLong();
            String avatarPath = f.readUTF();
            int nivelActual   = f.readInt();

            boolean[] niveles = new boolean[5];
            for (int i = 0; i < 5; i++) niveles[i] = f.readBoolean();

            int[] puntajes = new int[5];
            for (int i = 0; i < 5; i++) puntajes[i] = f.readInt();

            long tiempoTotal = f.readLong();
            int partidas     = f.readInt();
            int fallos       = f.readInt();
            int estrellas    = f.readInt();
            int puntuacion   = f.readInt();
            float volumen    = f.readFloat();
            String idioma    = f.readUTF();

            int totalAmigos  = f.readInt();
            String[] amigos  = new String[totalAmigos];
            for (int i = 0; i < totalAmigos; i++) amigos[i] = f.readUTF();

            USER  u = new USER(username, password, fullname);
            u.setFechaRegistro(LocalDate.ofEpochDay(fechaReg));
            u.setUltimaSesion(ultimaSesion);
            u.setAvatarPath(avatarPath);
            u.setNivelActual(nivelActual);
            for (int i = 0; i < 5; i++) if (niveles[i]) u.desbloquearNivel(i);
            for (int i = 0; i < 5; i++) u.setPuntajeNivel(i, puntajes[i]);
            u.setTiempoTotalJugado(tiempoTotal);
            u.setPartidasJugadas(partidas);
            u.setFallosTotales(fallos);
            u.setEstrellasTotal(estrellas);
            u.setPuntuacionGeneral(puntuacion);
            u.setVolumen(volumen);
            u.setAmigos(amigos);
            return u;
        }

    
}
