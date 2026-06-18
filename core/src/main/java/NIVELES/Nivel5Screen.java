package NIVELES;
import Game.*;
import Game.Obstaculo.TipoObstaculo;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel5Screen extends NivelBaseScreen {
    public Nivel5Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 4);
        construirUI();
    }
    public Nivel5Screen(CutTheRope juego, String usuario, LoginManager gestor,
                        String retoRetador, String retoRetado) {
        super(juego, usuario, gestor, 4, retoRetador, retoRetado);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl45.png";
    }

    @Override
    protected void crearNivel() {
        float pelotaX = 9f;
        float pelotaY = 13f;
        crearPelota(pelotaX, pelotaY, 0.35f);

        // Cuerda izquierda horizontal
        anclarCuerda(5.5f,  13f, 4, 0.8f);
        //Cuerda derecha horizontal
        anclarCuerda(12.5f, 13f, 4, 0.8f);
        //Cuerda abajo-centro
        anclarCuerda(9f,    10f, 6, 0.5f);

        //omNom abajo-centro
        colocarNomNom(9f, 4.5f, 0.6f);

        //Estrella suelta arriba-centro
        estrellas.add(new Estrella(mundo, 9f, 15f, 0.22f));

        //Burbujas con estrella adentro, izquierda y derecha
        burbujas.add(new Burbuja(mundo, 7.0f,  9.5f, 0.9f, 0f));
        burbujas.add(new Burbuja(mundo, 11.0f, 9.5f, 0.9f, 0f));

        //Estrellas dentro de las burbujas
        estrellas.add(new Estrella(mundo, 7.0f,  9.5f, 0.22f));
        estrellas.add(new Estrella(mundo, 11.0f, 9.5f, 0.22f));

        //Obstáculos horizontales a los lados de omnom
        obstaculos.add(new Obstaculo(mundo, 7.0f, 5.0f, 1.0f, 3.0f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, 11.0f, 5.0f, 1.0f, 3.0f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, 9.0f, 15.0f, 3.0f, 0.1f, TipoObstaculo.LARGO));

        limiteInferior = 0.5f;
        camaraFisica.setToOrtho(false, 18f, 22f);
        camaraFisica.position.set(9f, 11f, 0);
        camaraFisica.update();
    }
}