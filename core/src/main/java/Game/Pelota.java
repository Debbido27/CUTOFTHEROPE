package Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Pelota — cuerpo dinámico Box2D.
 *
 * Se expone setGravityScale() de forma implícita a través de getBody(),
 * que es lo que Burbuja necesita para controlar la flotación.
 *
 * Bits de filtro:
 *   categoryBits = 0x0001  (PELOTA)
 *   maskBits     = 0xFFFF  (choca con todo)
 */
public class Pelota {

    private final Body    body;
    private final float   radio;
    private final Texture textura;

    public Pelota(World mundo, float x, float y, float radio) {
        this.radio   = radio;
        this.textura = crearTextura(radio);

        BodyDef def = new BodyDef();
        def.type     = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        // gravityScale explícito = 1 por defecto; Burbuja lo cambia al entrar/salir
        def.gravityScale = 1f;

        body = mundo.createBody(def);
        body.setUserData(this);
        body.setLinearDamping(0f);
        body.setAngularDamping(0.5f);

        CircleShape shape = new CircleShape();
        shape.setRadius(radio);

        FixtureDef fix = new FixtureDef();
        fix.shape       = shape;
        fix.density     = 1f;
        fix.friction    = 0.3f;
        fix.restitution = 0.1f;          // rebote mínimo (más parecido al juego real)
        fix.filter.categoryBits = 0x0001; // PELOTA
        fix.filter.maskBits     = (short) 0xFFFF;

        body.createFixture(fix);
        shape.dispose();
    }

    // ── textura procedural ────────────────────────────────────────────────────
    private static Texture crearTextura(float radio) {
        int d = Math.max(2, (int)(radio * 100));
        Pixmap pm = new Pixmap(d, d, Pixmap.Format.RGBA8888);

        // base verde (color típico del dulce de Cut the Rope)
        pm.setColor(new Color(0.15f, 0.72f, 0.22f, 1f));
        pm.fillCircle(d / 2, d / 2, d / 2);

        // reflejo
        pm.setColor(new Color(1f, 1f, 1f, 0.45f));
        pm.fillCircle(d / 3, d / 3, Math.max(1, d / 5));

        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    // ── dibujo ────────────────────────────────────────────────────────────────
    public void dibujar(SpriteBatch batch) {
        float d = radio * 2;
        batch.draw(textura,
            body.getPosition().x - radio,
            body.getPosition().y - radio,
            d, d);
    }

    // ── getters ───────────────────────────────────────────────────────────────
    public Body  getBody()  { return body; }
    public float getRadio() { return radio; }

    public void dispose() {
        textura.dispose();
    }
}