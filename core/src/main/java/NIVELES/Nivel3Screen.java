package NIVELES;
import Game.*;
import Game.Obstaculo.TipoObstaculo;
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
    float pelotaX = 4.5f;
    float pelotaY = 13f;
    crearPelota(pelotaX, pelotaY, 0.3f);
    anclarCuerda(pelotaX - 6.0f, pelotaY + 3.5f, 10, 0.6f);
    anclarCuerda(pelotaX, pelotaY + 3.5f, 9, 0.75f);
    anclarCuerda(pelotaX, pelotaY - 2.5f, 7, 0.65f);
    anclarCuerda(pelotaX, pelotaY - 6.0f, 9, 0.7f);
    obstaculos.add(new Obstaculo(mundo, pelotaX, pelotaY - 4.0f, 4.5f, 0.3f, TipoObstaculo.LARGO));
    obstaculos.add(new Obstaculo(mundo, pelotaX, pelotaY - 8.0f, 4.5f, 0.3f, TipoObstaculo.LARGO));
estrellas.add(new Estrella(mundo, pelotaX + 2.5f, pelotaY - 3.5f, 0.22f));

estrellas.add(new Estrella(mundo, pelotaX - 2f, pelotaY - 11.5f, 0.22f));
estrellas.add(new Estrella(mundo, pelotaX - 2f, pelotaY - 7f, 0.22f));
    colocarNomNom(pelotaX, pelotaY - 12.0f, 0.6f);
    limiteInferior = pelotaY - 14f;
    camaraFisica.setToOrtho(false, 18f, 24f);
    camaraFisica.position.set(pelotaX + 2f, pelotaY - 5f, 0);
    camaraFisica.update();
}
}