package LOGIC;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RetosManager {

    private static final String BASE  = "../assets/CTR_RAIZ";
    private static final String RETOS = BASE + "/retos.dat";
    private static final String NOTIF = BASE + "/notificaciones.dat";


    public boolean enviarReto(String retador, String retado, int nivel) {
        for (Reto r : getTodosRetos()) {
            if (r.retador.equals(retador) && r.retado.equals(retado)
                && r.nivel == nivel && r.estado == Reto.Estado.PENDIENTE)
                return false;
        }
        List<Reto> lista = getTodosRetos();
        lista.add(new Reto(retador, retado, nivel));
        guardarRetos(lista);

        agregarNotificacion(new Notificacion(
            Notificacion.Tipo.RETO_RECIBIDO, retador, retado, nivel));
        return true;
    }

    public boolean aceptarReto(String retador, String retado, int nivel) {
        return cambiarEstado(retador, retado, nivel,
            Reto.Estado.PENDIENTE, Reto.Estado.ACEPTADO, () ->
                agregarNotificacion(new Notificacion(
                    Notificacion.Tipo.RETO_ACEPTADO, retado, retador, nivel)));
    }

    public boolean rechazarReto(String retador, String retado, int nivel) {
        return cambiarEstado(retador, retado, nivel,
            Reto.Estado.PENDIENTE, Reto.Estado.RECHAZADO, () ->
                agregarNotificacion(new Notificacion(
                    Notificacion.Tipo.RETO_RECHAZADO, retado, retador, nivel)));
    }

    public void registrarResultado(String retador, String retado, int nivel,
                                   int puntajeRetador, int puntajeRetado) {
        List<Reto> lista = getTodosRetos();
        for (Reto r : lista) {
            if (r.retador.equals(retador) && r.retado.equals(retado)
                && r.nivel == nivel && r.estado == Reto.Estado.ACEPTADO) {
                r.puntajeRetador = puntajeRetador;
                r.puntajeRetado  = puntajeRetado;
                r.estado         = Reto.Estado.COMPLETADO;
                r.ganador        = puntajeRetador >= puntajeRetado ? retador : retado;
                String perdedor  = r.ganador.equals(retador) ? retado : retador;

                agregarNotificacion(new Notificacion(
                    Notificacion.Tipo.RETO_GANADO, perdedor, r.ganador, nivel));
                agregarNotificacion(new Notificacion(
                    Notificacion.Tipo.RETO_PERDIDO, r.ganador, perdedor, nivel));
                break;
            }
        }
        guardarRetos(lista);
    }

    public List<Reto> getRetosRecibidos(String usuario) {
        List<Reto> res = new ArrayList<>();
        for (Reto r : getTodosRetos())
            if (r.retado.equals(usuario) && r.estado == Reto.Estado.PENDIENTE)
                res.add(r);
        return res;
    }

    public List<Reto> getHistorialRetos(String usuario) {
        List<Reto> res = new ArrayList<>();
        for (Reto r : getTodosRetos())
            if (r.retador.equals(usuario) || r.retado.equals(usuario))
                res.add(r);
        return res;
    }

    public String[][] getRankingAmigos(String usuario, String[] amigos) {
        List<String> jugadores = new ArrayList<>();
        jugadores.add(usuario);
        for (String a : amigos) jugadores.add(a);

        int n = jugadores.size();
        int[] ganados  = new int[n];
        int[] perdidos = new int[n];

        for (Reto r : getTodosRetos()) {
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
        for (Notificacion n : todasNotificaciones())
            if (n.destino.equals(usuario)) res.add(n);
        return res;
    }

    public int contarNoLeidas(String usuario) {
        int c = 0;
        for (Notificacion n : todasNotificaciones())
            if (n.destino.equals(usuario) && !n.leida) c++;
        return c;
    }

    public void marcarTodasLeidas(String usuario) {
        List<Notificacion> lista = todasNotificaciones();
        for (Notificacion n : lista)
            if (n.destino.equals(usuario)) n.leida = true;
        guardarNotificaciones(lista);
    }


    @SuppressWarnings("unchecked")
    private List<Reto> getTodosRetos() {
        try (ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(RETOS))) {
            return (List<Reto>) in.readObject();
        } catch (Exception e) { return new ArrayList<>(); }
    }

    private void guardarRetos(List<Reto> lista) {
        new File(BASE).mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(
            new FileOutputStream(RETOS))) {
            out.writeObject(lista);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private List<Notificacion> todasNotificaciones() {
        try (ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(NOTIF))) {
            return (List<Notificacion>) in.readObject();
        } catch (Exception e) { return new ArrayList<>(); }
    }

    private void guardarNotificaciones(List<Notificacion> lista) {
        new File(BASE).mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(
            new FileOutputStream(NOTIF))) {
            out.writeObject(lista);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void agregarNotificacion(Notificacion n) {
        List<Notificacion> lista = todasNotificaciones();
        lista.add(n);
        guardarNotificaciones(lista);
    }

    private boolean cambiarEstado(String retador, String retado, int nivel,
                                  Reto.Estado de, Reto.Estado a, Runnable callback) {
        List<Reto> lista = getTodosRetos();
        for (Reto r : lista) {
            if (r.retador.equals(retador) && r.retado.equals(retado)
                && r.nivel == nivel && r.estado == de) {
                r.estado = a;
                guardarRetos(lista);
                callback.run();
                return true;
            }
        }
        return false;
    }
}
