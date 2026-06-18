package LOGIC;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RetosManager {

    private LoginManager loginManager;

    // Constructor que recibe LoginManager
    public RetosManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    // Constructor vacío para compatibilidad (si se usa sin LoginManager)
    public RetosManager() {
        // Intenta obtener el LoginManager de la sesión
        // Si no está disponible, se usa sin persistencia
        this.loginManager = null;
    }

    public boolean enviarReto(String retador, String retado, int nivel) {
        // Verificar si ya existe un reto pendiente
        List<Reto> retosRetado = loginManager != null ?
            loginManager.cargarRetos(retado) : new ArrayList<>();

        for (Reto r : retosRetado) {
            if (r.retador.equals(retador) && r.retado.equals(retado)
                && r.nivel == nivel && r.estado == Reto.Estado.PENDIENTE)
                return false;
        }

        Reto nuevoReto = new Reto(retador, retado, nivel);

        if (loginManager != null) {
            // Guardar en carpeta del retado
            loginManager.guardarReto(retado, nuevoReto);

            // Crear notificación para el retado
            Notificacion notif = new Notificacion(
                Notificacion.Tipo.RETO_RECIBIDO, retador, retado, nivel);
            loginManager.guardarNotificacion(retado, notif);
        }
        return true;
    }

    public boolean aceptarReto(String retador, String retado, int nivel) {
        return cambiarEstado(retador, retado, nivel,
            Reto.Estado.PENDIENTE, Reto.Estado.ACEPTADO, () -> {
                if (loginManager != null) {
                    Notificacion notif = new Notificacion(
                        Notificacion.Tipo.RETO_ACEPTADO, retado, retador, nivel);
                    loginManager.guardarNotificacion(retador, notif);
                }
            });
    }

    public boolean rechazarReto(String retador, String retado, int nivel) {
        return cambiarEstado(retador, retado, nivel,
            Reto.Estado.PENDIENTE, Reto.Estado.RECHAZADO, () -> {
                if (loginManager != null) {
                    Notificacion notif = new Notificacion(
                        Notificacion.Tipo.RETO_RECHAZADO, retado, retador, nivel);
                    loginManager.guardarNotificacion(retador, notif);
                }
            });
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

    public List<Reto> getHistorialRetos(String usuario) {
        List<Reto> res = new ArrayList<>();
        if (loginManager == null) return res;

        for (Reto r : loginManager.cargarRetos(usuario)) {
            if (r.retador.equals(usuario) || r.retado.equals(usuario)) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Reto> getRetosEnviados(String usuario) {
        List<Reto> res = new ArrayList<>();
        if (loginManager == null) return res;

        for (Reto r : loginManager.cargarRetos(usuario)) {
            if (r.retador.equals(usuario) && r.estado == Reto.Estado.PENDIENTE) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Reto> getRetosActivos(String usuario) {
        List<Reto> res = new ArrayList<>();
        if (loginManager == null) return res;

        for (Reto r : loginManager.cargarRetos(usuario)) {
            if (!(r.retador.equals(usuario) || r.retado.equals(usuario))) continue;
            if (r.estado != Reto.Estado.ACEPTADO && r.estado != Reto.Estado.ESPERANDO_RIVAL) continue;
            boolean esRetador = r.retador.equals(usuario);
            if (esRetador && r.jugoRetador) continue;
            if (!esRetador && r.jugoRetado) continue;
            res.add(r);
        }
        return res;
    }

    public String[][] getRankingAmigos(String usuario, String[] amigos) {
        List<String> jugadores = new ArrayList<>();
        jugadores.add(usuario);
        for (String a : amigos) jugadores.add(a);

        int n = jugadores.size();
        int[] ganados  = new int[n];
        int[] perdidos = new int[n];

        // Recoger todos los retos de todos los amigos
        List<Reto> todosRetos = new ArrayList<>();
        if (loginManager != null) {
            for (String jugador : jugadores) {
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

    public void registrarResultadoJugador(String retador, String retado, int nivel,
                                          String quienJugo, int puntaje, int estrellas, int tiempoSeg) {
        if (loginManager == null) return;

        // Buscar el reto en el retador (o en el retado)
        List<Reto> retos = loginManager.cargarRetos(retador);
        Reto retoEncontrado = null;
        int indice = -1;

        for (int i = 0; i < retos.size(); i++) {
            Reto r = retos.get(i);
            if (r.retador.equals(retador) && r.retado.equals(retado) && r.nivel == nivel) {
                if (r.estado == Reto.Estado.ACEPTADO || r.estado == Reto.Estado.ESPERANDO_RIVAL) {
                    retoEncontrado = r;
                    indice = i;
                    break;
                }
            }
        }

        if (retoEncontrado == null) return;

        // Actualizar datos del jugador
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

        // Guardar reto actualizado (en la carpeta del retador)
        retos.set(indice, retoEncontrado);
        loginManager.actualizarReto(retador, retoEncontrado);

        // También guardar en la carpeta del retado (para sincronización)
        // Esto es un poco redundante pero asegura que ambos tengan el estado actualizado
        List<Reto> retosRetado = loginManager.cargarRetos(retado);
        boolean encontradoEnRetado = false;
        for (int i = 0; i < retosRetado.size(); i++) {
            Reto r = retosRetado.get(i);
            if (r.retador.equals(retador) && r.retado.equals(retado) && r.nivel == nivel) {
                retosRetado.set(i, retoEncontrado);
                encontradoEnRetado = true;
                break;
            }
        }
        if (encontradoEnRetado) {
            loginManager.actualizarReto(retado, retoEncontrado);
        }
    }

    private String determinarGanador(Reto r) {
        if (r.estrellasRetador != r.estrellasRetado)
            return r.estrellasRetador > r.estrellasRetado ? r.retador : r.retado;
        return r.tiempoRetador <= r.tiempoRetado ? r.retador : r.retado;
    }

    private boolean cambiarEstado(String retador, String retado, int nivel,
                                  Reto.Estado de, Reto.Estado a, Runnable callback) {
        if (loginManager == null) return false;

        List<Reto> retos = loginManager.cargarRetos(retado);
        for (int i = 0; i < retos.size(); i++) {
            Reto r = retos.get(i);
            if (r.retador.equals(retador) && r.retado.equals(retado)
                && r.nivel == nivel && r.estado == de) {
                r.estado = a;
                retos.set(i, r);
                loginManager.actualizarReto(retado, r);
                callback.run();
                return true;
            }
        }
        return false;
    }
}
