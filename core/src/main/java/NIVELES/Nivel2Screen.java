        package NIVELES;

        import Game.*;
        import com.badlogic.gdx.math.Vector2;
        import LOGIC.LoginManager;
        import com.cutherope.CutTheRope;

        public class Nivel2Screen extends NivelBaseScreen {

        public Nivel2Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 2);
            construirUI();
        }
            public Nivel2Screen(CutTheRope juego, String usuario, LoginManager gestor,
                                String retoRetador, String retoRetado) {
                super(juego, usuario, gestor, 2, retoRetador, retoRetado);
            }

        @Override
        protected String rutaFondo() {
        return "images/lvl23.png";
        }


        @Override
        protected void crearNivel() {
        float bajar = 2.5f;
        float pelotaX = 7.5f;
        float pelotaY = 15f - bajar; //12.5f

        crearPelota(pelotaX, pelotaY, 0.3f);

        anclarCuerda(pelotaX,         18f - bajar, 3,   1.3f);
        anclarCuerda(pelotaX + 5f,   15f - bajar, 10,  0.85f);
        anclarCuerda(pelotaX - 3.5f,  5f,          5,   1.1f);
        colocarNomNom(pelotaX, 19f, 0.6f);

        burbujas.add(new Burbuja(mundo, pelotaX + 0.5f, 7f - bajar, 0.6f, 9f));

        estrellas.add(new Estrella(mundo, pelotaX + 0.5f, 7f - bajar, 0.22f));


        estrellas.add(new Estrella(mundo, pelotaX - 2.2f, 10f, 0.22f));

        estrellas.add(new Estrella(mundo, pelotaX - 2.5f, 16f - bajar, 0.22f));

        limiteInferior = 1f;
        camaraFisica.setToOrtho(false, 14f, 18f);
        camaraFisica.position.set(7.5f, 11f, 0);
        camaraFisica.update();
        }
        }
