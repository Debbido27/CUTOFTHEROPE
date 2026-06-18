        package LOGIC;

        import java.io.*;
        import java.time.LocalDate;
        import java.util.*;

        public class LoginManager {

        private static final String BASE_FOLDER = "../CTR_RAIZ";
        private USER currentUser;
        private final UserDataManager dataManager;

        public LoginManager() {
        File base = new File(BASE_FOLDER);
        if (!base.exists()) base.mkdirs();
        this.dataManager = new UserDataManager();
        asegurarAdministrador(); //Asegurar que la cuenta admin exista siempre
        this.currentUser = cargarUltimoUsuarioActivo();
        }

        private void asegurarAdministrador() {
            String adminUser = "administrador";
            if (!userExiste(adminUser)) {
                crearUser(adminUser, "Admin1", "Administrador del Sistema", "AVATARS/X4.png");
            }
            USER admin = buscarUser(adminUser);
            if (admin != null) {
                //Asegurar que tenga los 5 niveles desbloqueados
                for (int i = 0; i < 5; i++) admin.desbloquearNivel(i);
                admin.setNivelActual(5);
                guardarCambios(admin);
            }
        }




        public synchronized boolean userExiste(String username) {
        return dataManager.usuarioExiste(username);
        }

        public synchronized USER buscarUser(String username) {
        return dataManager.cargarUsuarioCompleto(username);
        }

        public synchronized String[] listarUsuarios() {
        return dataManager.listarUsuarios();
        }


        public synchronized boolean crearUser(String username, String password, String fullname, String avatar) {
        if (userExiste(username)) return false;
        if (!dataManager.crearCarpetaUsuario(username)) return false;

        USER nuevo = new USER(username, password, fullname);
        nuevo.setAvatarPath(avatar);
        nuevo.setFechaRegistro(LocalDate.now());
        nuevo.setUltimaSesion(System.currentTimeMillis());
        nuevo.setAmigos(new String[0]);
        nuevo.setVolumen(1.0f);
        nuevo.setIngles(false);
        nuevo.setMusicaActiva(true);

        dataManager.guardarUsuarioCompleto(nuevo);
        currentUser = nuevo;
        return true;
        }

        public synchronized boolean login(String username, String password) {
        USER u = dataManager.cargarUsuarioCompleto(username);
        if (u != null && u.getPassword().equals(password)) {
        if (!dataManager.usuarioExiste(username)) return false;
        u.setUltimaSesion(System.currentTimeMillis());
        dataManager.guardarUsuarioCompleto(u);
        currentUser = u;
        return true;
        }
        return false;
        }

        public synchronized void guardarCambios(USER u) {
        dataManager.guardarUsuarioCompleto(u);
        }

        public synchronized boolean cambiarPassword(String username, String newPassword) {
        USER u = dataManager.cargarUsuarioCompleto(username);
        if (u == null) return false;
        u.setPassword(newPassword);
        dataManager.guardarUsuarioCompleto(u);
        if (currentUser != null && currentUser.getUsername().equals(username)) {
        currentUser.setPassword(newPassword);
        }
        return true;
        }

        public synchronized boolean cambiarNombre(String username, String newFullname) {
        USER u = dataManager.cargarUsuarioCompleto(username);
        if (u == null) return false;
        u.setFullname(newFullname);
        dataManager.guardarUsuarioCompleto(u);
        return true;
        }

        public synchronized boolean cambiarAvatar(String username, String rutaAvatar) {
        USER u = dataManager.cargarUsuarioCompleto(username);
        if (u == null) return false;
        u.setAvatarPath(rutaAvatar);
        dataManager.guardarUsuarioCompleto(u);
        return true;
        }

        public synchronized boolean cambiarVolumen(String username, float volumen) {
        USER u = dataManager.cargarUsuarioCompleto(username);
        if (u == null) return false;
        u.setVolumen(volumen);
        dataManager.guardarUsuarioCompleto(u);
        return true;
        }

        public synchronized boolean cambiarIdioma(String username, boolean ingles) {
        USER u = dataManager.cargarUsuarioCompleto(username);
        if (u == null) return false;
        u.setIngles(ingles);
        dataManager.guardarUsuarioCompleto(u);
        if (currentUser != null && currentUser.getUsername().equals(username)) {
        currentUser.setIngles(ingles);
        }
        return true;
        }

        public synchronized boolean cambiarMusica(String username, boolean activa) {
        USER u = dataManager.cargarUsuarioCompleto(username);
        if (u == null) return false;
        u.setMusicaActiva(activa);
        dataManager.guardarUsuarioCompleto(u);
        if (currentUser != null && currentUser.getUsername().equals(username)) {
        currentUser.setMusicaActiva(activa);
        }
        return true;
        }

        public synchronized boolean cambiarUsername(String usernameViejo, String usernameNuevo) {
        if (userExiste(usernameNuevo)) return false;

        File carpetaVieja = new File(BASE_FOLDER + "/" + usernameViejo);
        File carpetaNueva = new File(BASE_FOLDER + "/" + usernameNuevo);
        if (!carpetaVieja.renameTo(carpetaNueva)) return false;

        USER u = dataManager.cargarUsuarioCompleto(usernameNuevo);
        if (u == null) return false;
        u.setUsername(usernameNuevo);
        dataManager.guardarUsuarioCompleto(u);

        if (currentUser != null && currentUser.getUsername().equals(usernameViejo)) {
        currentUser.setUsername(usernameNuevo);
        }

        return true;
        }

        public synchronized boolean eliminarUsuario(String username) {
        if (!dataManager.usuarioExiste(username)) return false;
        dataManager.borrarCarpetaUsuario(username);
        if (currentUser != null && currentUser.getUsername().equals(username)) {
        currentUser = null;
        }
        return true;
        }

        public synchronized void registrarPartida(String username, int nivel, int puntaje,
                                  int estrellas, int fallos, long tiempoMs) {
        USER u = dataManager.cargarUsuarioCompleto(username);
        if (u == null) return;

        u.setPartidasJugadas(u.getPartidasJugadas() + 1);

        if (puntaje > 0) { // Solo si gano la partida
            int estrellasViejas = u.getEstrellasPorNivel()[nivel];
            if (estrellas > estrellasViejas) {
                u.setEstrellasTotal(u.getEstrellasTotal() + (estrellas - estrellasViejas));
                u.setEstrellasNivel(nivel, estrellas);
            }

            if (puntaje > u.getPuntajesPorNivel()[nivel]) {
                u.setPuntajeNivel(nivel, puntaje);
            }

            if (nivel + 1 < 5) u.desbloquearNivel(nivel + 1);
            if (nivel + 1 > u.getNivelActual()) u.setNivelActual(nivel + 1);
        }

        u.setTiempoTotalJugado(u.getTiempoTotalJugado() + tiempoMs);
        u.setFallosTotales(u.getFallosTotales() + fallos);

        int total = 0;
        for (int p : u.getPuntajesPorNivel()) total += p;
        u.setPuntuacionGeneral(total);

        dataManager.guardarUsuarioCompleto(u);
        }

        public synchronized void guardarPartidaHistorial(String username, PartidaHistorial partida) {
        dataManager.guardarPartidaHistorial(username, partida);
        }

        public synchronized List<PartidaHistorial> cargarHistorial(String username) {
        return dataManager.cargarHistorial(username, 10);
        }

        public synchronized void guardarSolicitud(String receptor, String emisor) {
        dataManager.agregarSolicitud(receptor, emisor);
        }

        public synchronized List<String> cargarSolicitudes(String usuario) {
        return dataManager.cargarSolicitudes(usuario);
        }

        public synchronized boolean eliminarSolicitud(String receptor, String emisor) {
        return dataManager.eliminarSolicitud(receptor, emisor);
        }

        public synchronized boolean tieneSolicitudPendiente(String solicitante, String receptor) {
        return dataManager.tieneSolicitudPendiente(solicitante, receptor);
        }
        public synchronized USER[] getRanking() {
        String[] usuarios = dataManager.listarUsuarios();
        List<USER> ranking = new ArrayList<>();

        for (String username : usuarios) {
        USER u = dataManager.cargarUsuarioCompleto(username);
        if (u != null) {
        ranking.add(u);
        }
        }

        ranking.sort((a, b) -> Integer.compare(b.getPuntuacionGeneral(), a.getPuntuacionGeneral()));

        return ranking.toArray(new USER[0]);
        }


        public synchronized void guardarReto(String username, Reto reto) {
        dataManager.agregarReto(username, reto);
        }

        public synchronized void actualizarReto(String username, Reto reto) {
        dataManager.actualizarReto(username, reto);
        }

        public synchronized List<Reto> cargarRetos(String username) {
        return dataManager.cargarRetos(username);
        }

        // ==================== MÉTODOS PARA NOTIFICACIONES ====================

        public synchronized void guardarNotificacion(String username, Notificacion notificacion) {
        dataManager.agregarNotificacion(username, notificacion);
        }

        public synchronized void marcarNotificacionLeida(String username, String idNotificacion) {
        dataManager.marcarNotificacionLeida(username, idNotificacion);
        }

        public synchronized List<Notificacion> cargarNotificaciones(String username) {
        return dataManager.cargarNotificaciones(username);
        }


        public synchronized void agregarAmigo(String username, String amigo) {
        dataManager.agregarAmigo(username, amigo);
        }

        public synchronized void eliminarAmigo(String username, String amigo) {
        dataManager.eliminarAmigo(username, amigo);
        }

        public synchronized String[] cargarAmigos(String username) {
        return dataManager.cargarAmigos(username);
        }

        public USER getCurrentUser() {
        return currentUser;
        }

        public void setCurrentUser(USER u) {
        this.currentUser = u;
        }

        public PartidaHistorial[] getHistorialMemoria() {
        return SesionJuego.get().getHistorial().toArray(new PartidaHistorial[0]);
        }


        private USER cargarUltimoUsuarioActivo() {
        USER lastActive = null;
        long maxSesion = 0;
        String[] usuarios = dataManager.listarUsuarios();

        for (String username : usuarios) {
        USER u = dataManager.cargarUsuarioCompleto(username);
        if (u != null && u.getUltimaSesion() > maxSesion) {
        maxSesion = u.getUltimaSesion();
        lastActive = u;
        }
        }
        return lastActive;
        }


        private static class UserDataManager {

        private static final String BASE_FOLDER = "../CTR_RAIZ";


        public synchronized void guardarPerfil(String username, USER u) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/perfil.ctr");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
        oos.writeObject(u);
        }
        } catch (IOException e) {
        System.err.println("Error guardando perfil: " + e.getMessage());
        }
        }

        public synchronized USER cargarPerfil(String username) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/perfil.ctr");
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        return (USER) ois.readObject();
        }
        } catch (IOException | ClassNotFoundException e) {
        System.err.println("Error cargando perfil: " + e.getMessage());
        return null;
        }
        }

        public synchronized void guardarStats(String username, USER u) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/stats.ctr");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
        oos.writeObject(u);
        }
        } catch (IOException e) {
        System.err.println("Error guardando stats: " + e.getMessage());
        }
        }

        public synchronized USER cargarStats(String username) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/stats.ctr");
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        return (USER) ois.readObject();
        }
        } catch (IOException | ClassNotFoundException e) {
        System.err.println("Error cargando stats: " + e.getMessage());
        return null;
        }
        }
        // ==================== SOLICITUDES ====================

        public synchronized void agregarSolicitud(String usuario, String emisor) {
        try {
            File file = new File(BASE_FOLDER + "/" + usuario + "/solicitudes.ctr");
            file.getParentFile().mkdirs();
            try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
                f.seek(f.length());
                f.writeUTF(emisor);
            }
        } catch (IOException e) {
            System.err.println("Error guardando solicitud: " + e.getMessage());
        }
        }

        public synchronized List<String> cargarSolicitudes(String usuario) {
        List<String> solicitudes = new ArrayList<>();
        try {
            File file = new File(BASE_FOLDER + "/" + usuario + "/solicitudes.ctr");
            if (!file.exists()) return solicitudes;
            try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
                while (f.getFilePointer() < f.length()) {
                    solicitudes.add(f.readUTF());
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando solicitudes: " + e.getMessage());
        }
        return solicitudes;
        }

        public synchronized boolean eliminarSolicitud(String usuario, String emisor) {
        try {
        File file = new File(BASE_FOLDER + "/" + usuario + "/solicitudes.ctr");
        if (!file.exists()) return false;

        List<String> restantes = new ArrayList<>();
        boolean encontrado = false;
        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
        while (f.getFilePointer() < f.length()) {
        String soli = f.readUTF();
        if (!soli.equals(emisor)) {
            restantes.add(soli);
        } else {
            encontrado = true;
        }
        }
        }

        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
        f.setLength(0);
        for (String soli : restantes) {
        f.writeUTF(soli);
        }
        }
        return encontrado;
        } catch (IOException e) {
        System.err.println("Error eliminando solicitud: " + e.getMessage());
        return false;
        }
        }

        public synchronized boolean tieneSolicitudPendiente(String solicitante, String receptor) {
        File file = new File(BASE_FOLDER + "/" + receptor + "/solicitudes.ctr");
        if (!file.exists()) return false;
        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
            while (f.getFilePointer() < f.length()) {
                if (f.readUTF().equals(solicitante)) return true;
            }
        } catch (IOException e) {
            System.err.println("Error verificando solicitud: " + e.getMessage());
        }
        return false;
        }

        public synchronized void guardarPreferencias(String username, USER u) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/preferencias.ctr");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
        oos.writeObject(u);
        }
        } catch (IOException e) {
        System.err.println("Error guardando preferencias: " + e.getMessage());
        }
        }

        public synchronized USER cargarPreferencias(String username) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/preferencias.ctr");
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        return (USER) ois.readObject();
        }
        } catch (IOException | ClassNotFoundException e) {
        System.err.println("Error cargando preferencias: " + e.getMessage());
        return null;
        }
        }

        public synchronized void guardarUsuarioCompleto(USER u) {
        String username = u.getUsername();
        guardarPerfil(username, u);
        guardarStats(username, u);
        guardarPreferencias(username, u);
        guardarAmigos(username, u.getAmigos());
        guardarSesion(username, u.getFechaRegistro().toEpochDay(), u.getUltimaSesion());
        }

        public synchronized USER cargarUsuarioCompleto(String username) {
        USER u = cargarPerfil(username);
        if (u == null) return null;

        USER stats = cargarStats(username);
        if (stats != null) {
        u.setPuntuacionGeneral(stats.getPuntuacionGeneral());
        u.setPartidasJugadas(stats.getPartidasJugadas());
        u.setFallosTotales(stats.getFallosTotales());
        u.setEstrellasTotal(stats.getEstrellasTotal());
        u.setTiempoTotalJugado(stats.getTiempoTotalJugado());
        u.setPuntajesPorNivel(stats.getPuntajesPorNivel());
        u.setEstrellasPorNivel(stats.getEstrellasPorNivel());
        u.setNivelesDesbloqueados(stats.getNivelesDesbloqueados());
        u.setNivelActual(stats.getNivelActual());
        }

        USER pref = cargarPreferencias(username);
        if (pref != null) {
        u.setVolumen(pref.getVolumen());
        u.setIngles(pref.isIngles());
        u.setMusicaActiva(pref.isMusicaActiva());
        u.setAvatarPath(pref.getAvatarPath());
        }

        u.setAmigos(cargarAmigos(username));

        long[] sesion = cargarSesion(username);
        if (sesion[0] > 0) {
        u.setFechaRegistro(LocalDate.ofEpochDay(sesion[0]));
        }
        if (sesion[1] > 0) {
        u.setUltimaSesion(sesion[1]);
        }

        return u;
        }


        public synchronized void guardarAmigos(String username, String[] amigos) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/amigos.ctr");
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
        f.setLength(0);
        for (String amigo : amigos) {
            f.writeUTF(amigo);
        }
        }
        } catch (IOException e) {
        System.err.println("Error guardando amigos: " + e.getMessage());
        }
        }

        public synchronized void agregarAmigo(String username, String amigo) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/amigos.ctr");
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
        f.seek(f.length());
        f.writeUTF(amigo);
        }
        } catch (IOException e) {
        System.err.println("Error agregando amigo: " + e.getMessage());
        }
        }

        public synchronized void eliminarAmigo(String username, String amigo) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/amigos.ctr");
        File tempFile = new File(BASE_FOLDER + "/" + username + "/amigos_temp.ctr");

        try (RandomAccessFile f = new RandomAccessFile(file, "r");
         RandomAccessFile temp = new RandomAccessFile(tempFile, "rw")) {

        while (f.getFilePointer() < f.length()) {
            String a = f.readUTF();
            if (!a.equals(amigo)) {
                temp.writeUTF(a);
            }
        }
        }

        file.delete();
        tempFile.renameTo(file);

        } catch (IOException e) {
        System.err.println("Error eliminando amigo: " + e.getMessage());
        }
        }

        public synchronized String[] cargarAmigos(String username) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/amigos.ctr");
        if (!file.exists()) return new String[0];

        List<String> amigos = new ArrayList<>();
        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
        while (f.getFilePointer() < f.length()) {
            amigos.add(f.readUTF());
        }
        }
        return amigos.toArray(new String[0]);

        } catch (IOException e) {
        System.err.println("Error cargando amigos: " + e.getMessage());
        return new String[0];
        }
        }

        public synchronized void guardarSesion(String username, long fechaRegistro, long ultimaSesion) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/sesiones.ctr");
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
        f.seek(0);
        f.writeLong(fechaRegistro);
        f.writeLong(ultimaSesion);
        }
        } catch (IOException e) {
        System.err.println("Error guardando sesion: " + e.getMessage());
        }
        }

        public synchronized long[] cargarSesion(String username) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/sesiones.ctr");
        if (!file.exists()) return new long[]{0, 0};

        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
        long fechaRegistro = f.readLong();
        long ultimaSesion = f.readLong();
        return new long[]{fechaRegistro, ultimaSesion};
        }

        } catch (IOException e) {
        System.err.println("Error cargando sesion: " + e.getMessage());
        return new long[]{0, 0};
        }
        }

        public synchronized void guardarPartidaHistorial(String username, PartidaHistorial p) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/historial.ctr");
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
        f.seek(f.length());
        f.writeInt(p.getNivel());
        f.writeInt(p.getPuntaje());
        f.writeInt(p.getEstrellas());
        f.writeInt(p.getFallos());
        f.writeLong(p.getTiempoMs());
        f.writeLong(p.getFecha().toEpochDay());
        f.writeBoolean(p.isGano());
        }
        } catch (IOException e) {
        System.err.println("Error guardando historial: " + e.getMessage());
        }
        }

        public synchronized List<PartidaHistorial> cargarHistorial(String username, int maxPartidas) {
        List<PartidaHistorial> partidas = new ArrayList<>();
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/historial.ctr");
        if (!file.exists()) return partidas;

        int registroSize = 4 + 4 + 4 + 4 + 8 + 8 + 1;
        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
        long fileLength = f.length();
        int totalRegistros = (int) (fileLength / registroSize);
        int startIndex = Math.max(0, totalRegistros - maxPartidas);

        f.seek(startIndex * registroSize);

        while (f.getFilePointer() < fileLength) {
            int nivel = f.readInt();
            int puntaje = f.readInt();
            int estrellas = f.readInt();
            int fallos = f.readInt();
            long tiempoMs = f.readLong();
            long fechaEpoch = f.readLong();
            boolean gano = f.readBoolean();

            PartidaHistorial p = new PartidaHistorial(
                nivel, puntaje, estrellas, fallos,
                tiempoMs, LocalDate.ofEpochDay(fechaEpoch), gano
            );
            partidas.add(p);
        }
        }

        } catch (IOException e) {
        System.err.println("Error cargando historial: " + e.getMessage());
        }
        return partidas;
        }


        public synchronized void agregarReto(String username, Reto reto) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/retos.ctr");
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
        f.seek(f.length());
        escribirReto(f, reto);
        }
        } catch (IOException e) {
        System.err.println("Error agregando reto: " + e.getMessage());
        }
        }

        public synchronized void actualizarReto(String username, Reto retoActualizado) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/retos.ctr");
        File tempFile = new File(BASE_FOLDER + "/" + username + "/retos_temp.ctr");

        try (RandomAccessFile f = new RandomAccessFile(file, "r");
         RandomAccessFile temp = new RandomAccessFile(tempFile, "rw")) {

        while (f.getFilePointer() < f.length()) {
            Reto r = leerReto(f);
            if (r.getId().equals(retoActualizado.getId())) {
                escribirReto(temp, retoActualizado);
            } else {
                escribirReto(temp, r);
            }
        }
        }

        file.delete();
        tempFile.renameTo(file);

        } catch (IOException e) {
        System.err.println("Error actualizando reto: " + e.getMessage());
        }
        }

        public synchronized List<Reto> cargarRetos(String username) {
        List<Reto> retos = new ArrayList<>();
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/retos.ctr");
        if (!file.exists()) return retos;

        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
        while (f.getFilePointer() < f.length()) {
            retos.add(leerReto(f));
        }
        }

        } catch (IOException e) {
        System.err.println("Error cargando retos: " + e.getMessage());
        }
        return retos;
        }

        private void escribirReto(RandomAccessFile f, Reto r) throws IOException {
        f.writeUTF(r.retador);
        f.writeUTF(r.retado);
        f.writeInt(r.nivel);
        f.writeUTF(r.estado.name());
        f.writeUTF(r.fecha.toString());
        f.writeInt(r.puntajeRetador);
        f.writeInt(r.puntajeRetado);
        f.writeInt(r.estrellasRetador);
        f.writeInt(r.estrellasRetado);
        f.writeInt(r.tiempoRetador);
        f.writeInt(r.tiempoRetado);
        f.writeBoolean(r.jugoRetador);
        f.writeBoolean(r.jugoRetado);
        f.writeUTF(r.ganador != null ? r.ganador : "");
        }

        private Reto leerReto(RandomAccessFile f) throws IOException {
        String retador = f.readUTF();
        String retado = f.readUTF();
        int nivel = f.readInt();
        String estadoStr = f.readUTF();
        String fechaStr = f.readUTF();

        Reto r = new Reto(retador, retado, nivel);
        r.estado = Reto.Estado.valueOf(estadoStr);
        r.fecha = java.time.LocalDateTime.parse(fechaStr);
        r.puntajeRetador = f.readInt();
        r.puntajeRetado = f.readInt();
        r.estrellasRetador = f.readInt();
        r.estrellasRetado = f.readInt();
        r.tiempoRetador = f.readInt();
        r.tiempoRetado = f.readInt();
        r.jugoRetador = f.readBoolean();
        r.jugoRetado = f.readBoolean();
        String ganador = f.readUTF();
        r.ganador = ganador.isEmpty() ? null : ganador;
        return r;
        }


        public synchronized void agregarNotificacion(String username, Notificacion n) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/notificaciones.ctr");
        try (RandomAccessFile f = new RandomAccessFile(file, "rw")) {
        f.seek(f.length());
        escribirNotificacion(f, n);
        }
        } catch (IOException e) {
        System.err.println("Error agregando notificacion: " + e.getMessage());
        }
        }

        public synchronized void marcarNotificacionLeida(String username, String idNotificacion) {
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/notificaciones.ctr");
        File tempFile = new File(BASE_FOLDER + "/" + username + "/notificaciones_temp.ctr");

        try (RandomAccessFile f = new RandomAccessFile(file, "r");
         RandomAccessFile temp = new RandomAccessFile(tempFile, "rw")) {

        while (f.getFilePointer() < f.length()) {
            Notificacion n = leerNotificacion(f);
            if (n.getId().equals(idNotificacion)) {
                n.leida = true;
                escribirNotificacion(temp, n);
            } else {
                escribirNotificacion(temp, n);
            }
        }
        }

        file.delete();
        tempFile.renameTo(file);

        } catch (IOException e) {
        System.err.println("Error marcando notificacion leida: " + e.getMessage());
        }
        }

        public synchronized List<Notificacion> cargarNotificaciones(String username) {
        List<Notificacion> notificaciones = new ArrayList<>();
        try {
        File file = new File(BASE_FOLDER + "/" + username + "/notificaciones.ctr");
        if (!file.exists()) return notificaciones;

        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
        while (f.getFilePointer() < f.length()) {
            notificaciones.add(leerNotificacion(f));
        }
        }

        } catch (IOException e) {
        System.err.println("Error cargando notificaciones: " + e.getMessage());
        }
        return notificaciones;
        }

        private void escribirNotificacion(RandomAccessFile f, Notificacion n) throws IOException {
        f.writeUTF(n.tipo.name());
        f.writeUTF(n.origen);
        f.writeUTF(n.destino);
        f.writeInt(n.nivel);
        f.writeUTF(n.fecha.toString());
        f.writeBoolean(n.leida);
        f.writeUTF(n.getId());//Guardar el ID persistente
        }

        private Notificacion leerNotificacion(RandomAccessFile f) throws IOException {
        String tipoStr = f.readUTF();
        String origen = f.readUTF();
        String destino = f.readUTF();
        int nivel = f.readInt();
        String fechaStr = f.readUTF();
        boolean leida = f.readBoolean();
        String id = f.readUTF();//Leer el ID guardado

        Notificacion n = new Notificacion(
        Notificacion.Tipo.valueOf(tipoStr),
        origen, destino, nivel
        );
        n.fecha = java.time.LocalDateTime.parse(fechaStr);
        n.leida = leida;
        n.setId(id);//Asignar el ID recuperado
        return n;
        }


        public synchronized boolean usuarioExiste(String username) {
        File userFolder = new File(BASE_FOLDER + "/" + username);
        return userFolder.exists() && userFolder.isDirectory();
        }

        public synchronized boolean crearCarpetaUsuario(String username) {
        try {
            File userFolder = new File(BASE_FOLDER + "/" + username);
            if (userFolder.exists()) {
                borrarCarpeta(userFolder);
            }
            if (!userFolder.mkdirs()) return false;

            new File(userFolder, "perfil.ctr").createNewFile();
            new File(userFolder, "stats.ctr").createNewFile();
            new File(userFolder, "preferencias.ctr").createNewFile();
            new File(userFolder, "amigos.ctr").createNewFile();
            new File(userFolder, "solicitudes.ctr").createNewFile();  // ← NUEVO
            new File(userFolder, "sesiones.ctr").createNewFile();
            new File(userFolder, "historial.ctr").createNewFile();
            new File(userFolder, "retos.ctr").createNewFile();
            new File(userFolder, "notificaciones.ctr").createNewFile();
            new File(userFolder, "avatar").mkdir();

            return true;
        } catch (IOException e) {
            System.err.println("Error creando carpeta: " + e.getMessage());
            return false;
        }
        }
        public synchronized void borrarCarpetaUsuario(String username) {
        File userFolder = new File(BASE_FOLDER + "/" + username);
        if (userFolder.exists()) {
        borrarCarpeta(userFolder);
        }
        }

        private void borrarCarpeta(File folder) {
        if (folder.isDirectory()) {
        File[] files = folder.listFiles();
        if (files != null) {
        for (File f : files) {
            borrarCarpeta(f);
        }
        }
        }
        folder.delete();
        }

        public synchronized String[] listarUsuarios() {
        File base = new File(BASE_FOLDER);
        if (!base.exists()) return new String[0];

        File[] carpetas = base.listFiles(File::isDirectory);
        if (carpetas == null) return new String[0];

        String[] usuarios = new String[carpetas.length];
        for (int i = 0; i < carpetas.length; i++) {
        usuarios[i] = carpetas[i].getName();
        }
        return usuarios;
        }
        }
        }
