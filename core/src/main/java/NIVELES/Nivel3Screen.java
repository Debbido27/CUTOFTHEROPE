package NIVELES;
import Game.*;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel3Screen extends NivelBaseScreen {

    public Nivel3Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 3);
    }

    public Nivel3Screen(CutTheRope juego, String usuario, LoginManager gestor,
                        String retoRetador, String retoRetado) {
        super(juego, usuario, gestor, 3, retoRetador, retoRetado);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl23.png";
    }

    @Override
    protected void crearNivel() {
        float pelotaX = 9f, pelotaY = 6f;
        crearPelota(pelotaX, pelotaY, 0.3f);

        anclarCuerda(3.5f, 12f, 10, 0.83f);
        anclarCuerda(13f, 15f, 11, 0.95f);

        estrellas.add(new Estrella(mundo,  6f,  9f,   0.22f));
        estrellas.add(new Estrella(mundo, 13f,  9f,   0.22f));
        estrellas.add(new Estrella(mundo,  9f,  4.5f, 0.22f));

        burbujas.add(new Burbuja(mundo,  3.5f, 3.5f, 1.0f));
        burbujas.add(new Burbuja(mundo,  9f,   3.5f, 1.0f));
        burbujas.add(new Burbuja(mundo, 13f,   3.5f, 1.0f));

        colocarNomNom(2.5f, 17f, 0.6f);

        limiteInferior = 0f;
        camaraFisica.setToOrtho(false, 18f, 24f);
        camaraFisica.position.set(9f, 12f, 0);
        camaraFisica.update();
    }
}