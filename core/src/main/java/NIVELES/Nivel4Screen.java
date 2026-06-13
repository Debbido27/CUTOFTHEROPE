
package NIVELES;


import Game.*;
import Game.Obstaculo.TipoObstaculo;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.math.Vector2;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

/**
 * NIVEL 4 — Laberinto
 * - Cuerda muy larga (20 segmentos) con segmentos cortos → más pesada y difícil
 * - Pasillo estrecho de obstáculos: la pelota debe caer por un hueco de ~1.2u
 * - Paredes laterales casi completas → muy poco margen de error
 * - Burbujas al fondo que interfieren con la trayectoria final
 * - Estrellas en las esquinas más lejanas del laberinto
 * - 2 cuerdas (el ancla tiene una cuerda secundaria decorativa que confunde)
 */
public class Nivel4Screen extends NivelBaseScreen {

    public Nivel4Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 4);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl1.png"; // ← tu fondo específico
    }

    @Override
    protected void crearNivel() {
        float anclaX = 7.5f;
        float anclaY = 20f;
        int   segmentos = 20;
        float largoSeg  = 0.30f;

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

        float base = anclaY - dist;

        // ── LABERINTO: paredes casi completas con un hueco ───────────
        // Nivel 1 del laberinto: hueco en el centro
        obstaculos.add(new Obstaculo(mundo,
                anclaX - 3.8f, base - 1.0f,
                5.5f, 0.3f, TipoObstaculo.LARGO));   // pared izquierda
        obstaculos.add(new Obstaculo(mundo,
                anclaX + 3.8f, base - 1.0f,
                5.5f, 0.3f, TipoObstaculo.LARGO));   // pared derecha
        // hueco de ~1.2u justo en anclaX (el jugador debe cortar para caer ahí)

        // Nivel 2: hueco desplazado a la derecha
        obstaculos.add(new Obstaculo(mundo,
                anclaX - 2.0f, base - 2.5f,
                7.5f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo,
                anclaX + 4.5f, base - 2.5f,
                2.5f, 0.3f, TipoObstaculo.LARGO));
        // hueco a la derecha (anclaX + 2.5 a anclaX + 3.5)

        // Nivel 3: hueco desplazado a la izquierda
        obstaculos.add(new Obstaculo(mundo,
                anclaX - 4.5f, base - 4.0f,
                2.5f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo,
                anclaX + 2.0f, base - 4.0f,
                7.5f, 0.3f, TipoObstaculo.LARGO));
        // hueco a la izquierda

        // Nivel 4: pasillo final muy estrecho en el centro
        obstaculos.add(new Obstaculo(mundo,
                anclaX - 3.5f, base - 5.5f,
                5.3f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo,
                anclaX + 3.5f, base - 5.5f,
                5.3f, 0.3f, TipoObstaculo.LARGO));

        // Bloques cuadrados sueltos que bloquean trayectorias obvias
        obstaculos.add(new Obstaculo(mundo, anclaX + 2.0f, base - 1.8f, 0.65f, 0.65f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX - 2.0f, base - 3.2f, 0.65f, 0.65f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 0.3f, base - 4.8f, 0.65f, 0.65f, TipoObstaculo.CORTO));

        // ── BURBUJAS (al fondo, empujan en dirección mala) ───────────
        burbujas.add(new Burbuja(mundo, anclaX - 4.5f, base - 6.5f, 0.45f, 7f));
        burbujas.add(new Burbuja(mundo, anclaX + 4.5f, base - 6.5f, 0.45f, 7f));
        burbujas.add(new Burbuja(mundo, anclaX,        base - 7.2f, 0.45f, 7f));

        // ── ESTRELLAS ────────────────────────────────────────────────
        // Estrella 1: dentro del primer hueco (hay que caer exacto)
        estrellas.add(new Estrella(mundo, anclaX, base - 1.8f, 0.22f));

        // Estrella 2: en la esquina izquierda del laberinto nivel 2
        estrellas.add(new Estrella(mundo, anclaX - 5.5f, base - 3.2f, 0.22f));

        // Estrella 3: al fondo de todo, debajo del pasillo final
        estrellas.add(new Estrella(mundo, anclaX, base - 7.8f, 0.22f));

        // ── cámara ───────────────────────────────────────────────────
        camaraFisica.setToOrtho(false, 22f, 28f);
        camaraFisica.position.set(anclaX, anclaY - 10f, 0);
        camaraFisica.update();
    }
}