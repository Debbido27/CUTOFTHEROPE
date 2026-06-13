
package NIVELES;

import Game.*;
import Game.Obstaculo.TipoObstaculo;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.math.Vector2;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

/**
 * NIVEL 5 — Caos Total
 * - Cuerda máxima (24 segmentos, segmentos cortos) → muy pesada, oscila mucho
 * - Laberinto complejo con obstáculos en diagonal (LARGO rotado ~30°)
 * - Bloques cuadrados dispersos en toda la zona de caída
 * - Burbujas en posiciones estratégicamente malas
 * - Estrellas en las tres esquinas más lejanas e inaccesibles
 * - El jugador tiene que hacer cortes múltiples y precisos
 */
public class Nivel5Screen extends NivelBaseScreen {

    public Nivel5Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 5);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl5.png";
    }

    @Override
    protected void crearNivel() {
        float anclaX = 7.5f;
        float anclaY = 22f;
        int   segmentos = 24;
        float largoSeg  = 0.28f;

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

        float base = anclaY - dist; // Y donde cuelga la pelota

        // ════════════════════════════════════════════════════════════
        // ZONA 1 — Embudo inicial (obliga a entrar por un pasillo)
        // ════════════════════════════════════════════════════════════
        obstaculos.add(new Obstaculo(mundo, anclaX - 4.0f, base - 0.6f, 6.0f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 4.5f, base - 0.6f, 4.5f, 0.3f, TipoObstaculo.LARGO));
        // hueco en anclaX + 1.5 a anclaX + 2.2 (muy estrecho)

        obstaculos.add(new Obstaculo(mundo, anclaX - 0.5f, base - 1.5f, 0.7f, 0.7f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 3.0f, base - 1.5f, 0.7f, 0.7f, TipoObstaculo.CORTO));

        // ════════════════════════════════════════════════════════════
        // ZONA 2 — Escalera invertida
        // ════════════════════════════════════════════════════════════
        obstaculos.add(new Obstaculo(mundo, anclaX - 5.0f, base - 2.4f, 4.0f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 1.5f, base - 3.0f, 4.5f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX - 3.5f, base - 3.8f, 4.0f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 3.5f, base - 4.5f, 4.0f, 0.3f, TipoObstaculo.LARGO));

        // Bloques sueltos en medio de los huecos (bloquean trayectorias obvias)
        obstaculos.add(new Obstaculo(mundo, anclaX + 2.5f, base - 2.0f, 0.65f, 0.65f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX - 2.0f, base - 3.3f, 0.65f, 0.65f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 0.5f, base - 4.2f, 0.65f, 0.65f, TipoObstaculo.CORTO));

        // ════════════════════════════════════════════════════════════
        // ZONA 3 — Sala del caos (muchos bloques aleatorios)
        // ════════════════════════════════════════════════════════════
        obstaculos.add(new Obstaculo(mundo, anclaX - 4.5f, base - 5.2f, 3.0f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 4.5f, base - 5.2f, 3.0f, 0.3f, TipoObstaculo.LARGO));

        obstaculos.add(new Obstaculo(mundo, anclaX - 1.5f, base - 5.8f, 0.65f, 0.65f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 1.5f, base - 5.8f, 0.65f, 0.65f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX,        base - 6.5f, 0.65f, 0.65f, TipoObstaculo.CORTO));

        obstaculos.add(new Obstaculo(mundo, anclaX - 3.0f, base - 6.8f, 3.5f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 3.0f, base - 7.5f, 3.5f, 0.3f, TipoObstaculo.LARGO));

        // ════════════════════════════════════════════════════════════
        // ZONA 4 — Pasillo final angustioso
        // ════════════════════════════════════════════════════════════
        obstaculos.add(new Obstaculo(mundo, anclaX - 4.2f, base - 8.5f, 5.8f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 4.2f, base - 8.5f, 5.8f, 0.3f, TipoObstaculo.LARGO));
        // hueco central de solo ~1.0u

        // ── BURBUJAS (estratégicamente molestas) ─────────────────────
        burbujas.add(new Burbuja(mundo, anclaX,        base - 2.8f, 0.4f, 6f));  // en el pasillo
        burbujas.add(new Burbuja(mundo, anclaX - 4.8f, base - 4.0f, 0.4f, 7f));  // te lanza a la pared
        burbujas.add(new Burbuja(mundo, anclaX + 4.8f, base - 6.0f, 0.4f, 7f));  // te lanza a la pared
        burbujas.add(new Burbuja(mundo, anclaX - 1.0f, base - 7.8f, 0.4f, 8f));  // justo antes del final
        burbujas.add(new Burbuja(mundo, anclaX + 1.0f, base - 9.2f, 0.4f, 8f));  // te saca del pasillo final

        // ── ESTRELLAS (todas en lugares difíciles) ───────────────────
        // Estrella 1: detrás del primer obstáculo izquierdo, zona 1
        estrellas.add(new Estrella(mundo, anclaX - 5.5f, base - 1.2f, 0.22f));

        // Estrella 2: en el corazón de la sala del caos
        estrellas.add(new Estrella(mundo, anclaX + 5.5f, base - 6.2f, 0.22f));

        // Estrella 3: al fondo de todo, después del pasillo final
        estrellas.add(new Estrella(mundo, anclaX, base - 10.0f, 0.22f));

        // ── cámara ───────────────────────────────────────────────────
        camaraFisica.setToOrtho(false, 24f, 32f);
        camaraFisica.position.set(anclaX, anclaY - 12f, 0);
        camaraFisica.update();
    }
}