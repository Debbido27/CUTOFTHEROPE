
    package NIVELES;


    import Game.*;
    import Game.Obstaculo.TipoObstaculo;
    import com.badlogic.gdx.physics.box2d.BodyDef;
    import com.badlogic.gdx.math.Vector2;
    import LOGIC.LoginManager;
    import com.cutherope.CutTheRope;


    public class Nivel4Screen extends NivelBaseScreen {

    public Nivel4Screen(CutTheRope juego, String usuario, LoginManager gestor) {
    super(juego, usuario, gestor, 4);
    }

    @Override
    protected String rutaFondo() {
    return "images/lvl1.png"; 
    }

    @Override
    protected void crearNivel() {
     }
    }