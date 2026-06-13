
package NIVELES;

import Game.*;
import Game.Obstaculo.TipoObstaculo;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.math.Vector2;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;


public class Nivel3Screen extends NivelBaseScreen {

    public Nivel3Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 3);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl1.png"; // ← tu fondo específico
    }

    @Override
    protected void crearNivel() {
        float anclaX = 7.5f;
        float anclaY = 18f;
        int   segmentos = 16;
        float largoSeg  = 0.35f;

        // ── ancla ────────────────────────────────────────────────────
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(anclaX, anclaY);
        anclaBody = mundo.createBody(def);
        anclaPos  = new Vector2(anclaX, anclaY);

        float dist = segmentos * largoSeg;
        pelota = new Pelota(mundo, anclaX, anclaY - dist, 0.3f);
        cuerda = new Cuerda(mundo, anclaBody, anclaPos,
                            segmentos, largoSeg, pelota.getBody(), true);

        float base = anclaY - dist; // posición Y de la pelota al soltar

        // ── ZIGZAG de plataformas ─────────────────────────────────────
        // Pared izquierda alta — primer rebote
        obstaculos.add(new Obstaculo(mundo,
                anclaX - 3.0f, base - 0.8f,
                3.5f, 0.35f, TipoObstaculo.LARGO));

        // Pared derecha — segundo rebote
        obstaculos.add(new Obstaculo(mundo,
                anclaX + 3.0f, base - 2.2f,
                3.5f, 0.35f, TipoObstaculo.LARGO));

        // Pared izquierda baja — tercer rebote
        obstaculos.add(new Obstaculo(mundo,
                anclaX - 2.8f, base - 3.6f,
                3.2f, 0.35f, TipoObstaculo.LARGO));

        // Pared derecha baja — cuarto rebote
        obstaculos.add(new Obstaculo(mundo,
                anclaX + 2.8f, base - 5.0f,
                3.2f, 0.35f, TipoObstaculo.LARGO));

        // Bloques cuadrados que tapan el camino recto hacia abajo
        obstaculos.add(new Obstaculo(mundo,
                anclaX + 0.5f, base - 1.5f,
                0.7f, 0.7f, TipoObstaculo.CORTO));

        obstaculos.add(new Obstaculo(mundo,
                anclaX - 0.5f, base - 4.3f,
                0.7f, 0.7f, TipoObstaculo.CORTO));

        // ── BURBUJAS (trampa: te empujan contra pared) ───────────────
        burbujas.add(new Burbuja(mundo, anclaX + 3.8f, base - 1.2f, 0.38f, 5f));
        burbujas.add(new Burbuja(mundo, anclaX - 3.8f, base - 3.0f, 0.38f, 5f));

        // ── ESTRELLAS ────────────────────────────────────────────────
        // Estrella 1: en el primer hueco del zigzag (accesible si cortas bien)
        estrellas.add(new Estrella(mundo, anclaX - 4.5f, base - 1.5f, 0.22f));

        // Estrella 2: al final del zigzag derecho
        estrellas.add(new Estrella(mundo, anclaX + 4.5f, base - 4.0f, 0.22f));

        // Estrella 3: en el fondo, debajo de todo el zigzag (la más difícil)
        estrellas.add(new Estrella(mundo, anclaX, base - 6.5f, 0.22f));

        // ── cámara ───────────────────────────────────────────────────
        camaraFisica.setToOrtho(false, 20f, 26f);
        camaraFisica.position.set(anclaX, anclaY - 9f, 0);
        camaraFisica.update();
    }
}
