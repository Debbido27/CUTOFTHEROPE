
        package LOGIC;

        import java.time.LocalDate;

        public class USER {

        private String username;
        private String password;
        private String fullname;
        private LocalDate fechaRegistro;
        private long ultimaSesion;
        private String avatarPath;



        private int nivelActual;
        private boolean[] nivelesDesbloqueados;
        private int[] puntajesPorNivel;
        private long tiempoTotalJugado;
        private int partidasJugadas;
        private int fallosTotales;
        private int estrellasTotal;
        private int puntuacionGeneral;

        private float volumen;
            private boolean ingles;
            private boolean musicaActiva;
        private String[] amigos;

        public USER(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.fechaRegistro = LocalDate.now();
        this.ultimaSesion = System.currentTimeMillis();
        this.avatarPath = "";
        this.nivelActual = 1;
        this.nivelesDesbloqueados = new boolean[]{true, true, true, true, true};
        this.puntajesPorNivel = new int[5];
        this.tiempoTotalJugado = 0;
        this.partidasJugadas = 0;
        this.fallosTotales = 0;
        this.estrellasTotal = 0;
        this.puntuacionGeneral = 0;
        this.volumen = 1.0f;
        this.amigos = new String[0];
        }

        public String getUsername() {
        return username;
        }

        public void setUsername(String username) {
        this.username = username;
        }

        public String getPassword() {
        return password;
        }

        public void setPassword(String password) {
        this.password = password;
        }

        public String getFullname() {
        return fullname;
        }

        public void setFullname(String fullname) {
        this.fullname = fullname;
        }

        public LocalDate getFechaRegistro() {
        return fechaRegistro;
        }

        public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        }

        public long getUltimaSesion() {
        return ultimaSesion;
        }

        public void setUltimaSesion(long ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
        }

        public String getAvatarPath() {
        return avatarPath;
        }

        public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
        }

        public int getNivelActual() {
        return nivelActual;
        }

        public void setNivelActual(int nivelActual) {
        this.nivelActual = nivelActual;
        }

        public void setNivelesDesbloqueados(boolean[] nivelesDesbloqueados) {
        this.nivelesDesbloqueados = nivelesDesbloqueados;
        }

        public int[] getPuntajesPorNivel() {
        return puntajesPorNivel;
        }

        public void setPuntajesPorNivel(int[] puntajesPorNivel) {
        this.puntajesPorNivel = puntajesPorNivel;
        }

        public long getTiempoTotalJugado() {
        return tiempoTotalJugado;
        }

        public void setTiempoTotalJugado(long tiempoTotalJugado) {
        this.tiempoTotalJugado = tiempoTotalJugado;
        }

        public int getPartidasJugadas() {
        return partidasJugadas;
        }

        public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
        }

        public int getFallosTotales() {
        return fallosTotales;
        }

        public void setFallosTotales(int fallosTotales) {
        this.fallosTotales = fallosTotales;
        }

        public int getEstrellasTotal() {
        return estrellasTotal;
        }

        public void setEstrellasTotal(int estrellasTotal) {
        this.estrellasTotal = estrellasTotal;
        }

        public int getPuntuacionGeneral() {
        return puntuacionGeneral;
        }

        public void setPuntuacionGeneral(int puntuacionGeneral) {
        this.puntuacionGeneral = puntuacionGeneral;
        }

        public float getVolumen() {
        return volumen;
        }

        public void setVolumen(float volumen) {
        this.volumen = volumen;
        }

        public String[] getAmigos() {
        return amigos;
        }

        public void setAmigos(String[] amigos) {
        this.amigos = amigos;
        }

        public boolean[] getNivelesDesbloqueados(){return nivelesDesbloqueados;}

        public void desbloquearNivel(int nivel){
        if(nivel>=0 && nivel < nivelesDesbloqueados.length)
        nivelesDesbloqueados[nivel]=true;
        }



        public void setPuntajeNivel(int nivel, int puntaje){
        if(nivel >= 0 && nivel < puntajesPorNivel.length)
        puntajesPorNivel[nivel] = puntaje;
        }


        @Override
        public String toString() {
        return "Usuario: " + username + "\nNombre: " + fullname +
           "\nFecha de registro: " + fechaRegistro +
           "\nNivel actual: " + nivelActual +
           "\nPartidas jugadas: " + partidasJugadas + "\nPuntuacion: " + puntuacionGeneral;
        }


        public int getNivelesCompletados() {
        int completados = 0;
        for (int puntaje : puntajesPorNivel) {
        if (puntaje > 0) completados++;
        }
        return completados;
        }

        public double getTiempoPromedioPorNivel() {
        if (getNivelesCompletados() == 0) return 0;
        return (double) tiempoTotalJugado / getNivelesCompletados();
        }

            public boolean isIngles()            { return ingles; }
            public void    setIngles(boolean v)  { this.ingles = v; }

            public boolean isMusicaActiva()           { return musicaActiva; }
            public void    setMusicaActiva(boolean v) { this.musicaActiva = v; }

        }
