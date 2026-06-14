package NIVELES;

import Game.*;
import Game.Obstaculo.TipoObstaculo;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.math.Vector2;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel2Screen extends NivelBaseScreen {

    public Nivel2Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 2);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl2_background.png";
    }

    @Override
    protected void crearNivel() {
        float pelotaX = 7.5f;
        float pelotaY = 16f;
        
        pelota = new Pelota(mundo, pelotaX, pelotaY, 0.3f);
        
   
        
        limiteInferior = -2f;
        
        camaraFisica.setToOrtho(false, 18f, 20f);
        camaraFisica.position.set(7.5f, 9f, 0);
        camaraFisica.update();
    }
}