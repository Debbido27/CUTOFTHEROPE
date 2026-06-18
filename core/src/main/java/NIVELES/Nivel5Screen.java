package NIVELES;

import Game.*;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel5Screen extends NivelBaseScreen {

    public Nivel5Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 5);
        construirUI();
    }

    public Nivel5Screen(CutTheRope juego, String usuario, LoginManager gestor,
                        String retoRetador, String retoRetado) {
        super(juego, usuario, gestor, 5, retoRetador, retoRetado);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl45.png";
    }

    @Override
    protected void crearNivel() {
        float candyX = 9f;
        float candyY = 12f;

        crearPelota(candyX, candyY, 0.3f);

        // two ropes from upper-left and upper-right — both must be cut
        anclarCuerda(candyX - 3f, candyY + 4f, 8, 0.65f);
        anclarCuerda(candyX + 3f, candyY + 4f, 8, 0.65f);

        // bubble below candy — player must tap to pop it
        burbujas.add(new Burbuja(mundo, candyX, 7.5f, 0.55f));

        // stars: first on the way down, second at bubble, third after bubble pops
        estrellas.add(new Estrella(mundo, candyX, 10.5f, 0.22f));
        estrellas.add(new Estrella(mundo, candyX,  7.5f, 0.22f));
        estrellas.add(new Estrella(mundo, candyX,  4.5f, 0.22f));

        colocarNomNom(candyX, 2.5f, 0.6f);
        limiteInferior = 0f;

        camaraFisica.setToOrtho(false, 20f, 22f);
        camaraFisica.position.set(candyX, 9f, 0);
        camaraFisica.update();
    }
}
