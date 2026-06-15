
        package NIVELES;

        import Game.*;
        import Game.Obstaculo.TipoObstaculo;
        import LOGIC.LoginManager;
        import com.cutherope.CutTheRope;

        public class Nivel3Screen extends NivelBaseScreen {

        public Nivel3Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 3);
        }

        @Override
        protected String rutaFondo() {
        return "images/lvl1.png"; // ← tu fondo específico
        }

        @Override
        protected void crearNivel() {
        float pelotaX = 4.5f;
        float pelotaY = 13f;

        // Pelota (candy)
        crearPelota(pelotaX, pelotaY, 0.3f);

        // Cuerda 1: arriba-izquierda (más a la izquierda, corta pero con más segmentos para suavidad)
        anclarCuerda(pelotaX - 6.0f, pelotaY + 3.5f, 10, 0.6f);

        // Cuerda 2: arriba-centro (misma altura, floja con holgura moderada)
        anclarCuerda(pelotaX, pelotaY + 3.5f, 9, 0.75f);

        // Cuerda 3: centro, más abajo (más corta pero con segmentos suficientes)
        anclarCuerda(pelotaX, pelotaY - 2.5f, 7, 0.65f);

        // Cuerda 4: centro, todavía más abajo (larga pero equilibrada)
        anclarCuerda(pelotaX, pelotaY - 6.0f, 9, 0.7f);

        // Obstáculos
        obstaculos.add(new Obstaculo(mundo, pelotaX, pelotaY - 4.0f, 4.5f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, pelotaX, pelotaY - 8.0f, 4.5f, 0.3f, TipoObstaculo.LARGO));

        // Estrellas
        estrellas.add(new Estrella(mundo, pelotaX + 2.0f, pelotaY - 2.0f, 0.22f));
        estrellas.add(new Estrella(mundo, pelotaX, pelotaY - 5.0f, 0.22f));
        estrellas.add(new Estrella(mundo, pelotaX - 2.0f, pelotaY - 7.0f, 0.22f));

        // Om Nom
        colocarNomNom(pelotaX, pelotaY - 11f, 0.6f);

        // Límite inferior
        limiteInferior = pelotaY - 14f;

        // Cámara
        camaraFisica.setToOrtho(false, 18f, 24f);
        camaraFisica.position.set(pelotaX + 2f, pelotaY - 5f, 0);
        camaraFisica.update();
        }


        }
