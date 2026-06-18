package NIVELES;

import Game.*;
import Game.Obstaculo.TipoObstaculo;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel4Screen extends NivelBaseScreen {

    public Nivel4Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 4);
        construirUI();
    }

    public Nivel4Screen(CutTheRope juego, String usuario, LoginManager gestor,
            String retoRetador, String retoRetado) {
        super(juego, usuario, gestor, 4, retoRetador, retoRetado);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl45.png";
    }

    @Override
    protected void crearNivel() {
        float pelotaX = 3.5f;
        float pelotaY = 14.0f;
        crearPelota(pelotaX, pelotaY, 0.35f);

        anclarCuerda(2.0f, 16.5f, 3, 1.05f);
        anclarCuerda(6.5f, 18.0f, 5, 1.0f);
        anclarCuerda(11.5f, 18.0f, 13, 0.88f);
        anclarCuerda(3.5f, 11.0f, 3, 1.0f);

        colocarNomNom(11.0f, 5.0f, 0.6f);

        estrellas.add(new Estrella(mundo, 5.0f, 13.5f, 0.22f));
        estrellas.add(new Estrella(mundo, 9.5f, 12.5f, 0.22f));
        estrellas.add(new Estrella(mundo, 9.5f, 7.0f, 0.22f)); // top-right de NomNom (11.0, 5.0)

        obstaculos.add(new Obstaculo(mundo, 12.5f, 8.0f, 1.0f, 5.0f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, 3.0f, 8.5f, 1.0f, 5.0f, TipoObstaculo.LARGO));

        limiteInferior = 0.5f;
        camaraFisica.setToOrtho(false, 14f, 18f);
        camaraFisica.position.set(7.5f, 12.0f, 0);
        camaraFisica.update();
    }
}
