package LOGIC;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RetosManager {

    private LoginManager loginManager;

    public RetosManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public RetosManager() {
        this.loginManager = null;
    }

    public boolean enviarReto(String retador, String retado, int nivel) {
        if (loginManager == null) return false;

        if (retador.equals(retado)) {
            System.out.println("No puedes retarte a ti mismo");
            return false;
        }

        List<Reto> retosRetado = loginManager.cargarRetos(retado);
        for (Reto r : retosRetado) {
            if (r.retador.equals(retador) && r.retado.equals(retado)
                && r.nivel == nivel && r.estado == Reto.Estado.PENDIENTE) {
                return false;
            }
        }

        Reto nuevoReto = new Reto(retador, retado, nivel);

        loginManager.guardarReto(retado, nuevoReto);

        Notificacion notif = new Notificacion(
            Notificacion.Tipo.RETO_RECIBIDO, retador, retado, nivel);
        loginManager.guardarNotificacion(retado, notif);

        return true;
    }

    public boolean aceptarReto(String retador, String retado, int nivel) {
        if (loginManager == null) return false;

        List<Reto> retosRetado = loginManager.cargarRetos(retado);
        boolean encontrado = false;
        for (int i = 0; i < retosRetado.size(); i++) {
            Reto r = retosRetado.get(i);
            if (r.retador.equals(retador) && r.retado.equals(retado)
                && r.nivel == nivel && r.estado == Reto.Estado.PENDIENTE) {
                r.estado = Reto.Estado.ACEPTADO;
                retosRetado.set(i, r);
                loginManager.actualizarReto(retado, r);
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) return false;

        List<Reto> retosRetador = loginManager.cargarRetos(retador);
        for (int i = 0; i < retosRetador.size(); i++) {
            Reto r = retosRetador.get(i);
            if (r.retador.equals(retador) && r.retado.equals(retado)
                && r.nivel == nivel && r.estado == Reto.Estado.PENDIENTE) {
                r.estado = Reto.Estado.ACEPTADO;
                retosRetador.set(i, r);
                loginManager.actualizarReto(retador, r);
                break;
            }
        }

        // Notificación para el retador
        Notificacion notif = new Notificacion(
            Notificacion.Tipo.RETO_ACEPTADO, retado, retador, nivel);
        loginManager.guardarNotificacion(retador, notif);
        
        return true;
    }

    public boolean rechazarReto(String retador, String retado, int nivel) {
        if (loginManager == null) return false;

        List<Reto> retosRetado = loginManager.cargarRetos(retado);
        boolean encontrado = false;
        for (int i = 0; i < retosRetado.size(); i++) {
            Reto r = retosRetado.get(i);
            if (r.retador.equals(retador) && r.retado.equals(retado)
                && r.nivel == nivel && r.estado == Reto.Estado.PENDIENTE) {
                r.estado = Reto.Estado.RECHAZADO;
                retosRetado.set(i, r);
                loginManager.actualizarReto(retado, r);
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) return false;

        List<Reto> retosRetador = loginManager.cargarRetos(retador);
        for (int i = 0; i < retosRetador.size(); i++) {
            Reto r = retosRetador.get(i);
            if (r.retador.equals(retador) && r.retado.equals(retado)
                && r.nivel == nivel && r.estado == Reto.Estado.PENDIENTE) {
                r.estado = Reto.Estado.RECHAZADO;
                retosRetador.set(i, r);
                loginManager.actualizarReto(retador, r);
                break;
            }
        }

        Notificacion notif = new Notificacion(
            Notificacion.Tipo.RETO_RECHAZADO, retado, retador, nivel);
        loginManager.guardarNotificacion(retador, notif);
        
        return true;
    }

   public void registrarResultadoJugador(String retador, String retado, int nivel,
                                      String quienJugo, int puntaje, int estrellas, int tiempoSeg) {
    if (loginManager == null) return;

    Reto retoEncontrado = null;
    String usuarioDondeEsta = null;
    
    String[] todosUsuarios = loginManager.listarUsuarios();
    for (String usuario : todosUsuarios) {
        List<Reto> retos = loginManager.cargarRetos(usuario);
        for (Reto r : retos) {
            if (r.retador.equals(retador) && r.retado.equals(retado) && r.nivel == nivel) {
                if (r.estado == Reto.Estado.ACEPTADO || r.estado == Reto.Estado.ESPERANDO_RIVAL) {
                    retoEncontrado = r;
                    usuarioDondeEsta = usuario;
                    break;
                }
            }
        }
        if (retoEncontrado != null) break;
    }

    if (retoEncontrado == null) {
        System.out.println("No se encontró el reto para: " + retador + " vs " + retado + " nivel " + nivel);
        return;
    }

    boolean esRetador = quienJugo.equals(retador);
    if (esRetador) {
        retoEncontrado.puntajeRetador   = puntaje;
        retoEncontrado.estrellasRetador = estrellas;
        retoEncontrado.tiempoRetador    = tiempoSeg;
        retoEncontrado.jugoRetador      = true;
    } else {
        retoEncontrado.puntajeRetado    = puntaje;
        retoEncontrado.estrellasRetado  = estrellas;
        retoEncontrado.tiempoRetado     = tiempoSeg;
        retoEncontrado.jugoRetado       = true;
    }

    if (retoEncontrado.jugoRetador && retoEncontrado.jugoRetado) {
        retoEncontrado.ganador = determinarGanador(retoEncontrado);
        retoEncontrado.estado  = Reto.Estado.COMPLETADO;
        String perdedor = retoEncontrado.ganador.equals(retador) ? retado : retador;

        Notificacion notifGanador = new Notificacion(
            Notificacion.Tipo.RETO_GANADO, perdedor, retoEncontrado.ganador, nivel);
        loginManager.guardarNotificacion(retoEncontrado.ganador, notifGanador);

        Notificacion notifPerdedor = new Notificacion(
            Notificacion.Tipo.RETO_PERDIDO, retoEncontrado.ganador, perdedor, nivel);
        loginManager.guardarNotificacion(perdedor, notifPerdedor);
    } else {
        retoEncontrado.estado = Reto.Estado.ESPERANDO_RIVAL;
    }

    loginManager.actualizarReto(usuarioDondeEsta, retoEncontrado);

    String otroUsuario = usuarioDondeEsta.equals(retador) ? retado : retador;
    List<Reto> retosOtro = loginManager.cargarRetos(otroUsuario);
    for (int i = 0; i < retosOtro.size(); i++) {
        Reto r = retosOtro.get(i);
        if (r.retador.equals(retador) && r.retado.equals(retado) && r.nivel == nivel) {
            retosOtro.set(i, retoEncontrado);
            loginManager.actualizarReto(otroUsuario, retoEncontrado);
            break;
        }
    }
}

    private String determinarGanador(Reto r) {
        if (r.estrellasRetador != r.estrellasRetado)
            return r.estrellasRetador > r.estrellasRetado ? r.retador : r.retado;
        return r.tiempoRetador <= r.tiempoRetado ? r.retador : r.retado;
    }

    
    public List<Reto> getRetosRecibidos(String usuario) {
        List<Reto> res = new ArrayList<>();
        if (loginManager == null) return res;

        for (Reto r : loginManager.cargarRetos(usuario)) {
            if (r.retado.equals(usuario) && r.estado == Reto.Estado.PENDIENTE) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Reto> getRetosEnviados(String usuario) {
        List<Reto> res = new ArrayList<>();
        if (loginManager == null) return res;

        String[] todosUsuarios = loginManager.listarUsuarios();
        for (String otroUsuario : todosUsuarios) {
            for (Reto r : loginManager.cargarRetos(otroUsuario)) {
                if (r.retador.equals(usuario) && r.estado == Reto.Estado.PENDIENTE) {
                    res.add(r);
                }
            }
        }
        return res;
    }

    public List<Reto> getRetosActivos(String usuario) {
        List<Reto> res = new ArrayList<>();
        if (loginManager == null) return res;

        String[] todosUsuarios = loginManager.listarUsuarios();
        for (String otroUsuario : todosUsuarios) {
            for (Reto r : loginManager.cargarRetos(otroUsuario)) {
                if (!(r.retador.equals(usuario) || r.retado.equals(usuario))) continue;
                if (r.estado != Reto.Estado.ACEPTADO && r.estado != Reto.Estado.ESPERANDO_RIVAL) continue;
                boolean esRetador = r.retador.equals(usuario);
                if (esRetador && r.jugoRetador) continue;
                if (!esRetador && r.jugoRetado) continue;
                res.add(r);
            }
        }
        return res;
    }

    public List<Reto> getHistorialRetos(String usuario) {
        List<Reto> res = new ArrayList<>();
        if (loginManager == null) return res;

        String[] todosUsuarios = loginManager.listarUsuarios();
        for (String otroUsuario : todosUsuarios) {
            for (Reto r : loginManager.cargarRetos(otroUsuario)) {
                if (r.retador.equals(usuario) || r.retado.equals(usuario)) {
                    res.add(r);
                }
            }
        }
        return res;
    }

    
    public List<Notificacion> getNotificaciones(String usuario) {
        List<Notificacion> res = new ArrayList<>();
        if (loginManager == null) return res;

        for (Notificacion n : loginManager.cargarNotificaciones(usuario)) {
            if (n.destino.equals(usuario)) res.add(n);
        }
        return res;
    }

    public int contarNoLeidas(String usuario) {
        int c = 0;
        if (loginManager == null) return 0;

        for (Notificacion n : loginManager.cargarNotificaciones(usuario)) {
            if (n.destino.equals(usuario) && !n.leida) c++;
        }
        return c;
    }

    public void marcarTodasLeidas(String usuario) {
        if (loginManager == null) return;

        List<Notificacion> notifs = loginManager.cargarNotificaciones(usuario);
        for (Notificacion n : notifs) {
            if (n.destino.equals(usuario) && !n.leida) {
                n.leida = true;
                loginManager.marcarNotificacionLeida(usuario, n.getId());
            }
        }
    }

    
    public String[][] getRankingAmigos(String usuario, String[] amigos) {
        List<String> jugadores = new ArrayList<>();
        jugadores.add(usuario);
        for (String a : amigos) jugadores.add(a);

        int n = jugadores.size();
        int[] ganados  = new int[n];
        int[] perdidos = new int[n];

        List<Reto> todosRetos = new ArrayList<>();
        if (loginManager != null) {
            String[] todosUsuarios = loginManager.listarUsuarios();
            for (String jugador : todosUsuarios) {
                todosRetos.addAll(loginManager.cargarRetos(jugador));
            }
        }

        for (Reto r : todosRetos) {
            if (r.estado != Reto.Estado.COMPLETADO || r.ganador == null) continue;
            String perdedor = r.ganador.equals(r.retador) ? r.retado : r.retador;
            for (int i = 0; i < n; i++) {
                if (jugadores.get(i).equals(r.ganador))  ganados[i]++;
                if (jugadores.get(i).equals(perdedor))   perdidos[i]++;
            }
        }

        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (ganados[j] < ganados[j+1]) {
                    int tg = ganados[j];  ganados[j]  = ganados[j+1];  ganados[j+1]  = tg;
                    int tp = perdidos[j]; perdidos[j] = perdidos[j+1]; perdidos[j+1] = tp;
                    String tu = jugadores.get(j);
                    jugadores.set(j, jugadores.get(j+1));
                    jugadores.set(j+1, tu);
                }

        int top = Math.min(5, n);
        String[][] res = new String[top][3];
        for (int i = 0; i < top; i++)
            res[i] = new String[]{ jugadores.get(i),
                String.valueOf(ganados[i]),
                String.valueOf(perdidos[i]) };
        return res;
    }
}