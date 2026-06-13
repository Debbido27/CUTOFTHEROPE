package NIVELES;

import Game.*;
import Game.Obstaculo.TipoObstaculo;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.math.Vector2;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;


public class Nivel2Screen extends NivelBaseScreen {

    public Nivel2Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 2);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl2.png";
    }

    @Override
    protected void crearNivel() {
        float anclaX = 7.5f;
        float anclaY = 16f;
        int   segmentos = 12;
        float largoSeg  = 0.35f;

        // ── ancla ────────────────────────────────────────────────────
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(anclaX, anclaY);
        anclaBody = mundo.createBody(def);
        anclaPos  = new Vector2(anclaX, anclaY);

        // ── pelota ───────────────────────────────────────────────────
        float dist = segmentos * largoSeg;
        pelota = new Pelota(mundo, anclaX, anclaY - dist, 0.3f);

        // ── cuerda ───────────────────────────────────────────────────
        cuerda = new Cuerda(mundo, anclaBody, anclaPos,
                            segmentos, largoSeg, pelota.getBody(), true);

        // ── obstáculos ───────────────────────────────────────────────
        // Plataforma larga izquierda — desvía la pelota hacia la derecha
        obstaculos.add(new Obstaculo(mundo,
                anclaX - 2.5f, anclaY - dist - 0.5f,
                3.0f, 0.35f, TipoObstaculo.LARGO));

        // Plataforma larga derecha más abajo — rebota de vuelta al centro
        obstaculos.add(new Obstaculo(mundo,
                anclaX + 2.5f, anclaY - dist - 2.2f,
                3.0f, 0.35f, TipoObstaculo.LARGO));

        // Bloque cuadrado en el centro, justo encima de la estrella principal
        obstaculos.add(new Obstaculo(mundo,
                anclaX, anclaY - dist - 3.8f,
                0.7f, 0.7f, TipoObstaculo.CORTO));

        // ── burbuja (trampolín hacia arriba si caes mal) ─────────────
        burbujas.add(new Burbuja(mundo,
                anclaX - 3.5f, anclaY - dist - 3.0f,
                0.4f, 6f));

        // ── estrellas ────────────────────────────────────────────────
        // Estrella 1: accesible, caída directa
        estrellas.add(new Estrella(mundo, anclaX, anclaY - dist - 5.2f, 0.22f));

        // Estrella 2: a la izquierda, detrás de la plataforma izquierda
        estrellas.add(new Estrella(mundo, anclaX - 3.8f, anclaY - dist - 1.5f, 0.22f));

        // Estrella 3: a la derecha abajo, hay que rebotar en plataforma derecha
        estrellas.add(new Estrella(mundo, anclaX + 3.5f, anclaY - dist - 4.5f, 0.22f));

        // ── cámara ───────────────────────────────────────────────────
        camaraFisica.setToOrtho(false, 18f, 22f);
        camaraFisica.position.set(anclaX, anclaY - 7f, 0);
        camaraFisica.update();
    }
}