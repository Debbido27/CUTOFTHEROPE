package LOGIC;

public class FriendsManager {

    private LoginManager loginManager;

    public FriendsManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public synchronized boolean enviarSolicitud(String de, String para) {
        if (loginManager.buscarUser(para) == null) return false;
        if (sonAmigos(de, para)) return false;
        if (tieneSolicitudPendiente(de, para)) return false;
        if (de.equals(para)) return false;

        loginManager.guardarSolicitud(para, de);
        return true;
    }

    public synchronized boolean aceptarSolicitud(String solicitante, String receptor) {
        if (!tieneSolicitudPendiente(solicitante, receptor)) return false;

        loginManager.eliminarSolicitud(receptor, solicitante);
        loginManager.agregarAmigo(receptor, solicitante);
        loginManager.agregarAmigo(solicitante, receptor);

        return true;
    }

    public synchronized boolean rechazarSolicitud(String solicitante, String receptor) {
        return loginManager.eliminarSolicitud(receptor, solicitante);
    }

    public synchronized boolean eliminarAmigo(String userA, String userB) {
        loginManager.eliminarAmigo(userA, userB);
        loginManager.eliminarAmigo(userB, userA);
        return true;
    }

    public synchronized boolean sonAmigos(String userA, String userB) {
        for (String amigo : loginManager.cargarAmigos(userA)) {
            if (amigo.equals(userB)) return true;
        }
        return false;
    }

    public synchronized boolean tieneSolicitudPendiente(String solicitante, String receptor) {
        return loginManager.tieneSolicitudPendiente(solicitante, receptor);
    }

    public synchronized String[] getAmigos(String username) {
        return loginManager.cargarAmigos(username);
    }

    public synchronized String[] getSolicitudes(String username) {
        return loginManager.cargarSolicitudes(username).toArray(new String[0]);
    }

    public synchronized int contarAmigos(String username) {
        return getAmigos(username).length;
    }
}
