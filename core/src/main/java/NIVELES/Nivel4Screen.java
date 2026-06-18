
package NIVELES;

import Game.*;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel4Screen extends NivelBaseScreen {

    public Nivel4Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 4);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl45.png";
    }

    @Override
    protected void crearNivel() {
        float anclaX = 5f;
        float anclaY = 13f;
        int   segs   = 9;
        float largo  = 0.45f;
        float dist   = segs * largo;

        crearPelota(anclaX, anclaY - dist, 0.3f);
        anclarCuerda(anclaX, anclaY, segs, largo);

        // bubble blocks the path — player must tap to pop it
        burbujas.add(new Burbuja(mundo, anclaX, 6.5f, 0.55f));

        // stars: first two collected on the way down, third after the bubble pops
        estrellas.add(new Estrella(mundo, anclaX, 8.0f,  0.22f));
        estrellas.add(new Estrella(mundo, anclaX, 6.5f,  0.22f));
        estrellas.add(new Estrella(mundo, anclaX, 4.5f,  0.22f));

        colocarNomNom(anclaX, 2.5f, 0.6f);
        limiteInferior = 0f;

        camaraFisica.setToOrtho(false, 16f, 18f);
        camaraFisica.position.set(anclaX, 7.5f, 0);
        camaraFisica.update();
    }
}
