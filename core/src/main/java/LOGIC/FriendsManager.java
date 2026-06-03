
package LOGIC;

import java.io.File;
import java.io.IOException;


public class FriendsManager {
    
    private static final String BASE_FOLDER ="../assets/CTR._RAIZ";
    
    private String getAmigosPath(String username){
        return BASE_FOLDER+"/"+username+"/amigos.ctr";
    }
    
    private String getSolicitudesPath(String username) {
        File f = new File(BASE_FOLDER + "/" + username + "/solicitudes.ctr");
        if (!f.exists()) {
            try { f.createNewFile(); } catch (IOException e) {}
        }
        return f.getPath();
    }
    
    
    public boolean enviarSolicitud(String de, String para, LoginManager loginManager) {
        if (loginManager.buscarUser(para) == null) return false;
        if (sonAmigos(de, para)) return false;
        if (tieneSolicitudPendiente(de, para)) return false;
        return agregarLinea(getSolicitudesPath(para), de);
    }

    public boolean aceptarSolicitud(String solicitante, String receptor) {
        if (!tieneSolicitudPendiente(solicitante, receptor)) return false;
        eliminarLinea(getSolicitudesPath(receptor), solicitante);
        agregarLinea(getAmigosPath(receptor), solicitante);
        agregarLinea(getAmigosPath(solicitante), receptor);
        return true;
    }
    
    public boolean rechazarSolicitud(String solicitante, String receptor) {
        return eliminarLinea(getSolicitudesPath(receptor), solicitante);
    }

    public boolean eliminarAmigo(String userA, String userB) {
        boolean a = eliminarLinea(getAmigosPath(userA), userB);
        boolean b = eliminarLinea(getAmigosPath(userB), userA);
        return a && b;
    }

    public boolean sonAmigos(String userA, String userB) {
        return contieneLinea(getAmigosPath(userA), userB);
    }

    public boolean tieneSolicitudPendiente(String solicitante, String receptor) {
        return contieneLinea(getSolicitudesPath(receptor), solicitante);
    }

}
