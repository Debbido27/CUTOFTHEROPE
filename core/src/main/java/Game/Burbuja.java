package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

public class Burbuja extends ElementoNivel implements Interactuable {

    // ── constantes ───────────────────────────────────────────────────
    private static final float GRAVITY_BURBUJA  = -1f;
    private static final float DAMPING_BURBUJA  = 2f;
    private static final float DAMPING_NORMAL   = 0f;

    // ── estado ───────────────────────────────────────────────────────
    private boolean activa       = true;
    private boolean pelotaDentro = false;

    // ── física ───────────────────────────────────────────────────────
    private final World mundo;
    private final Body  body;
    private final float radio;
    private       Joint jointBurbuja = null;

    // ── constructores ────────────────────────────────────────────────
    public Burbuja(World mundo, float x, float y, float radio) {
        super(x, y, radio * 2, radio * 2, TipoElemento.BURBUJA,
              crearTexturaBurbuja(radio));
        this.mundo = mundo;
        this.radio = radio;
        this.body  = crearCuerpoFisico(x, y, radio);
    }

    public Burbuja(World mundo, float x, float y, float radio, float ignorado) {
        this(mundo, x, y, radio);
    }

    // ── cuerpo físico ────────────────────────────────────────────────
    private Body crearCuerpoFisico(float cx, float cy, float r) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody; // empieza estática
        def.position.set(cx, cy);

        Body b = mundo.createBody(def);
        b.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(r);

        FixtureDef fix = new FixtureDef();
        fix.shape           = shape;
        fix.isSensor        = true;
        fix.filter.categoryBits = 0x0004;
        fix.filter.maskBits     = 0x0001;
        b.createFixture(fix);
        shape.dispose();
        return b;
    }

    // ── API pública ──────────────────────────────────────────────────

    /**
     * Pelota entra a la burbuja:
     * - Burbuja se vuelve dinámica y empieza a flotar
     * - Pelota se pega a la burbuja con WeldJoint
     * - Ambas suben juntas
     */
    public void entrar(Pelota pelota) {
        if (!activa || pelotaDentro) return;
        pelotaDentro = true;

        // Burbuja pasa a dinámica y flota
        body.setType(BodyDef.BodyType.DynamicBody);
        body.setGravityScale(GRAVITY_BURBUJA);
        body.setLinearDamping(DAMPING_BURBUJA);

        // Pelota también flota
        Body pb = pelota.getBody();
        pb.setGravityScale(GRAVITY_BURBUJA);
        pb.setLinearDamping(DAMPING_BURBUJA);

        // Cancelar velocidad brusca para arranque suave
        Vector2 vel = pb.getLinearVelocity();
        pb.setLinearVelocity(vel.x * 0.3f, 0f);

        // Pegar burbuja y pelota
       WeldJointDef weld = new WeldJointDef();
weld.bodyA = body;
weld.bodyB = pb;
weld.localAnchorA.set(0, 0);
weld.localAnchorB.set(0, 0);
weld.referenceAngle = 0f;
jointBurbuja = mundo.createJoint(weld);
    }

    /**
     * No se usa — la pelota solo se separa al explotar la burbuja.
     */
    public void salir(Pelota pelota) {
        // intencional vacío
    }

    /**
     * Explotar la burbuja:
     * - Destruye el joint
     * - Burbuja vuelve a estática y desaparece
     * - Pelota recupera gravedad normal
     */
    public void explotar(Pelota pelota) {
        if (!activa) return;

        // Destruir joint
        if (jointBurbuja != null) {
            mundo.destroyJoint(jointBurbuja);
            jointBurbuja = null;
        }

        // Burbuja desaparece
        body.setType(BodyDef.BodyType.StaticBody);
        activa       = false;
        pelotaDentro = false;

        // Pelota recupera física normal
        if (pelota != null) {
            Body pb = pelota.getBody();
            pb.setGravityScale(1f);
            pb.setLinearDamping(DAMPING_NORMAL);
            // Dar pequeño impulso hacia arriba para que no caiga brutal
            pb.setLinearVelocity(pb.getLinearVelocity().x, 2f);
        }
    }

    /**
     * Revisar si el toque del jugador cayó sobre la burbuja.
     * Llamar desde NivelBaseScreen con coordenadas ya desprojectadas.
     */
    public boolean revisarToque(float mundoX, float mundoY, Pelota pelota) {
        if (!activa) return false;
        float dx = mundoX - body.getPosition().x;
        float dy = mundoY - body.getPosition().y;
        if (dx * dx + dy * dy <= radio * radio) {
            explotar(pelota);
            return true;
        }
        return false;
    }

    // ── Interactuable ────────────────────────────────────────────────
    @Override
    public void interactuar() {
        activa = false; // legacy, no restaura gravedad
    }

    @Override
    public boolean estaActivo() { return activa; }

    // ── actualizar ───────────────────────────────────────────────────
    @Override
    public void actualizar(float delta) {}

    // ── dibujar ──────────────────────────────────────────────────────
    @Override
    public void dibujar(SpriteBatch batch) {
        if (!activa) return;
        float cx = body.getPosition().x;
        float cy = body.getPosition().y;
        batch.draw(textura, cx - radio, cy - radio, radio * 2, radio * 2);
    }

    // ── textura ──────────────────────────────────────────────────────
    private static Texture crearTexturaBurbuja(float radio) {
        int d = Math.max(4, (int)(radio * 100));
        Pixmap pm = new Pixmap(d, d, Pixmap.Format.RGBA8888);

        pm.setColor(0, 0, 0, 0);
        pm.fill();

        pm.setColor(new Color(0.55f, 0.80f, 1f, 0.55f));
        pm.fillCircle(d / 2, d / 2, d / 2 - 1);

        pm.setColor(new Color(0.7f, 0.9f, 1f, 0.85f));
        pm.drawCircle(d / 2, d / 2, d / 2 - 1);

        int br = Math.max(1, d / 6);
        pm.setColor(new Color(1, 1, 1, 0.75f));
        pm.fillCircle(d / 3, d / 3, br);

        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    // ── getters ──────────────────────────────────────────────────────
    public Body    getBody()        { return body; }
    public float   getRadio()       { return radio; }
    public boolean isPelotaDentro() { return pelotaDentro; }
}