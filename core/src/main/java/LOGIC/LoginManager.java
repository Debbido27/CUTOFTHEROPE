
        package LOGIC;

        import java.io.EOFException;
        import java.io.File;
        import java.io.IOException;
        import java.io.RandomAccessFile;
        import java.time.LocalDate;


        public class LoginManager {
        private static final String BASE_FOLDER = "../assets/CTR_RAIZ";
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
        long posInicio = f.getFilePointer();
        try {
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


        int totalAmigos  = f.readInt();
        String[] amigos  = new String[totalAmigos];
        for (int i = 0; i < totalAmigos; i++) amigos[i] = f.readUTF();

        USER u = new USER(username, password, fullname);
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

        } catch (EOFException e) {
        f.seek(f.length());
        throw e;
        }
        }


        private void escribirRegistro(RandomAccessFile f, USER u) throws IOException {
        f.writeUTF(u.getUsername());
        f.writeUTF(u.getPassword());
        f.writeUTF(u.getFullname());
        f.writeLong(u.getFechaRegistro().toEpochDay());
        f.writeLong(u.getUltimaSesion());
        f.writeUTF(u.getAvatarPath());
        f.writeInt(u.getNivelActual());

        boolean[] niveles = u.getNivelesDesbloqueados();
        for (boolean b : niveles) f.writeBoolean(b);

        int[] puntajes = u.getPuntajesPorNivel();
        for (int p : puntajes) f.writeInt(p);

        f.writeLong(u.getTiempoTotalJugado());
        f.writeInt(u.getPartidasJugadas());
        f.writeInt(u.getFallosTotales());
        f.writeInt(u.getEstrellasTotal());
        f.writeInt(u.getPuntuacionGeneral());
        f.writeFloat(u.getVolumen());

        String[] amigos = u.getAmigos();
        f.writeInt(amigos.length);
        for (String a : amigos) f.writeUTF(a);
        }

        private USER cargarUltimoUsuarioActivo(){
        USER lastActive = null;
        try(RandomAccessFile  f = new RandomAccessFile (USERS_FILE, "r")){
          while(f.getFilePointer()<f.length()){
                  USER u = leerRegistro(f);
            if (new File(BASE_FOLDER + "/" + u.getUsername()).exists())
                lastActive = u;
        }
        } catch (IOException e) {}
        return lastActive;
        }


        public boolean userExiste(String username) {
        try (RandomAccessFile f = new RandomAccessFile(USERS_FILE, "rw")) {
        while (f.getFilePointer() < f.length()) {
        USER u = leerRegistro(f);
        if (u.getUsername().equals(username)) return true;
        }
        } catch (IOException e) { 
        e.printStackTrace();
        }
        return false;
        }

        public USER buscarUser(String username){
        try (RandomAccessFile f = new RandomAccessFile(USERS_FILE, "r")) {
        while (f.getFilePointer() < f.length()) {
            USER u = leerRegistro(f);
            if (u.getUsername().equals(username)) return u;
        }
        } catch (IOException e) {}
        return null;
        }

        public boolean crearUser(String username, String password, String fullname, String avatar) {        if (userExiste(username)) return false;
        if (!crearCarpetaUsuario(username)) return false;
        try (RandomAccessFile f = new RandomAccessFile(USERS_FILE, "rw")) {
        f.seek(f.length());
        USER nuevo = new USER(username, password, fullname);
        nuevo.setAvatarPath(avatar);  
        escribirRegistro(f, nuevo);
        currentUser = nuevo;
        return true;
        } catch (IOException e) { System.out.println("Error creando usuario: " + e.getMessage()); }
        return false;
        }


        private boolean crearCarpetaUsuario(String username) {
        try {
        File userFolder = new File(BASE_FOLDER + "/" + username);
        if (userFolder.exists()) borrarCarpeta(userFolder);
        if (!userFolder.mkdir()) return false;
        new File(userFolder, "stats.ctr").createNewFile();
        new File(userFolder, "sessions.ctr").createNewFile();
        new File(userFolder, "preferences.ctr").createNewFile();
        new File(userFolder, "amigos.ctr").createNewFile();
        new File(userFolder, "avatar").mkdir();
        return true;
        } catch (IOException e) { System.out.println("Error creando carpeta: " + e.getMessage()); return false; }
        }


        public boolean login(String username, String password){
        USER u = buscarUser(username);
        if (u != null && u.getPassword().equals(password)) {
        if (!new File(BASE_FOLDER + "/" + username).exists()) return false;
        u.setUltimaSesion(System.currentTimeMillis());
        guardarCambios(u);
        currentUser = u;
        return true;
        }
        return false;
        }


        private interface RegistroModificador { void modificar(USER u); }

        private boolean reescribir(String usernameObjetivo, RegistroModificador mod) {
        File tempFile = new File(USERS_TEMP);
        try (RandomAccessFile original = new RandomAccessFile(USERS_FILE, "rw"); 
        RandomAccessFile temp    = new RandomAccessFile(tempFile, "rw")) {
        while (original.getFilePointer() < original.length()) {
        USER u = leerRegistro(original);
        if (u.getUsername().equals(usernameObjetivo)) mod.modificar(u);
        escribirRegistro(temp, u);
        }
        } catch (IOException e) { 
        System.out.println("Error reescribiendo: " + e.getMessage()); 
        e.printStackTrace();
        return false; 
        }
        new File(USERS_FILE).delete();
        return tempFile.renameTo(new File(USERS_FILE));
        }

        public void guardarCambios(USER u) {
        reescribir(u.getUsername(), existing -> {
        existing.setNivelActual(u.getNivelActual());
        existing.setPartidasJugadas(u.getPartidasJugadas());
        existing.setFallosTotales(u.getFallosTotales());
        existing.setEstrellasTotal(u.getEstrellasTotal());
        existing.setTiempoTotalJugado(u.getTiempoTotalJugado());
        existing.setPuntuacionGeneral(u.getPuntuacionGeneral());
        existing.setUltimaSesion(u.getUltimaSesion());
        existing.setVolumen(u.getVolumen());
        existing.setAmigos(u.getAmigos());
        existing.setAvatarPath(u.getAvatarPath());
        });
        }

        public boolean cambiarPassword(String username, String newPassword){
        boolean ok = reescribir(username, u -> u.setPassword(newPassword));
        if (ok && currentUser != null && currentUser.getUsername().equals(username))
        currentUser.setPassword(newPassword);
        return ok;
        }

        public boolean cambiarNombre(String username, String newFullname) {
        return reescribir(username, u -> u.setFullname(newFullname));
        }

        public boolean cambiarAvatar(String username, String rutaAvatar) {
        return reescribir(username, u -> u.setAvatarPath(rutaAvatar));
        }

        public boolean cambiarVolumen(String username, float volumen) {
        return reescribir(username, u -> u.setVolumen(volumen));
        }



        public void registrarPartida(String username, int nivel, int puntaje,
                          int estrellas, int fallos, long tiempoMs) {
        reescribir(username, u -> {
        u.setPartidasJugadas(u.getPartidasJugadas() + 1);
        u.setEstrellasTotal(u.getEstrellasTotal() + estrellas);
        u.setTiempoTotalJugado(u.getTiempoTotalJugado() + tiempoMs);
        u.setFallosTotales(u.getFallosTotales() + fallos);
        if (puntaje > u.getPuntajesPorNivel()[nivel])
            u.setPuntajeNivel(nivel, puntaje);
        if (nivel + 1 < 5) u.desbloquearNivel(nivel + 1);
        if (nivel + 1 > u.getNivelActual()) u.setNivelActual(nivel + 1);
        int total = 0;
        for (int p : u.getPuntajesPorNivel()) total += p;
        u.setPuntuacionGeneral(total);
        });
        }

        private int calcularPuntuacionGeneral(USER u) {
        int total = 0;
        for (int p : u.getPuntajesPorNivel()) total += p;
        return total;
        }



        public USER[] getRanking() {
        USER[] todos = new USER[50];
        int count = 0;
        try (RandomAccessFile f = new RandomAccessFile(USERS_FILE, "r")) {
        while (f.getFilePointer() < f.length() && count < 50)
            todos[count++] = leerRegistro(f);
        } catch (IOException e) {}

        for (int i = 0; i < count - 1; i++)
        for (int j = 0; j < count - i - 1; j++)
            if (todos[j].getPuntuacionGeneral() < todos[j+1].getPuntuacionGeneral()) {
                USER tmp = todos[j]; todos[j] = todos[j+1]; todos[j+1] = tmp;
            }
        USER[] resultado = new USER[count];
        for (int i = 0; i < count; i++) resultado[i] = todos[i];
        return resultado;
        }

        public boolean cambiarUsername(String usernameViejo, String usernameNuevo) {
        if (userExiste(usernameNuevo)) return false;

        File carpetaVieja = new File(BASE_FOLDER + "/" + usernameViejo);
        File carpetaNueva = new File(BASE_FOLDER + "/" + usernameNuevo);
        if (!carpetaVieja.renameTo(carpetaNueva)) return false;

        boolean ok = reescribir(usernameViejo, u -> u.setUsername(usernameNuevo));

        if (ok && currentUser != null && currentUser.getUsername().equals(usernameViejo))
         currentUser.setUsername(usernameNuevo);

        return ok;
        }

        public boolean eliminarUsuario(String username) {
        File tempFile = new File(USERS_TEMP);
        boolean encontrado = false;
        try (RandomAccessFile original = new RandomAccessFile(USERS_FILE, "r");
         RandomAccessFile temp    = new RandomAccessFile(tempFile, "rw")) {
        while (original.getFilePointer() < original.length()) {
            USER u = leerRegistro(original);
            if (!u.getUsername().equals(username)) {
                escribirRegistro(temp, u);
            } else {
                borrarCarpeta(new File(BASE_FOLDER + "/" + username));
                encontrado = true;
            }
        }
        } 
        catch (IOException e) {}
        new File(USERS_FILE).delete();
        tempFile.renameTo(new File(USERS_FILE));
        return encontrado;

        }


        private void limpiarUsuariosHuerfanos() {
        File tempFile = new File(USERS_TEMP);
        boolean ok = false;
        try (RandomAccessFile original = new RandomAccessFile(USERS_FILE, "rw");
        RandomAccessFile temp    = new RandomAccessFile(tempFile, "rw")) {
        while (original.getFilePointer() < original.length()) {
        USER u = leerRegistro(original);
        if (new File(BASE_FOLDER + "/" + u.getUsername()).exists()) {
            escribirRegistro(temp, u);
        } else {
        }
        }
        ok = true;
        } catch (IOException e) {
        e.printStackTrace();
        }
        if (ok) {
        new File(USERS_FILE).delete();
        tempFile.renameTo(new File(USERS_FILE));
        } else {
        tempFile.delete(); 
        }
        }

        private void borrarCarpeta(File folder) {
        if (folder.isDirectory()) {
        File[] files = folder.listFiles();
        if (files != null) for (File f : files) borrarCarpeta(f);
        }
        folder.delete();
        }

        public USER getCurrentUser() { return currentUser; }
        public void setCurrentUser(USER u) { this.currentUser = u; }



        }
