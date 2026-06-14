package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Burbuja — mecánica idéntica a Cut the Rope.
 *
 * Principio clave: en lugar de aplicar fuerzas repetidas (que generan jitter),
 * se cambia el gravityScale del cuerpo de la pelota:
 *   - Al entrar: gravityScale = -FACTOR  → la pelota "flota" (gravedad invertida suave)
 *   - Al salir o explotar: gravityScale = 1f → gravedad normal
 *
 * La burbuja es un sensor (isSensor = true), así que NO produce rebotes físicos.
 * La detección de tap/clic se hace en actualizar() comparando la posición del toque
 * con el centro de la burbuja.
 */
public class Burbuja extends ElementoNivel implements Interactuable {

    // ── constantes ──────────────────────────────────────────────────────────
    /**
     * Escala de gravedad negativa que se aplica a la pelota mientras flota.
     * -0.35f → sube suavemente (35% de la gravedad invertida).
     * Ajusta este valor para cambiar la "potencia" de la burbuja.
     */
    private static final float GRAVITY_SCALE_FLOTANDO = -0.35f;

    /**
     * Amortiguación lineal extra que se aplica a la pelota dentro de la burbuja
     * para suavizar los movimientos y evitar que acelere indefinidamente.
     */
    private static final float DAMPING_DENTRO = 1.8f;

    /** Amortiguación normal de la pelota fuera de la burbuja. */
    private static final float DAMPING_NORMAL  = 0.0f;

    // ── estado ──────────────────────────────────────────────────────────────
    private boolean activa        = true;
    private boolean pelotaDentro  = false;

    // ── física ──────────────────────────────────────────────────────────────
    private final World   mundo;
    private final Body    body;
    private final float   radio;

    // ── constructor ─────────────────────────────────────────────────────────
    public Burbuja(World mundo, float x, float y, float radio) {
        super(x, y, radio * 2, radio * 2, TipoElemento.BURBUJA,
              crearTexturaBurbuja(radio));
        this.mundo = mundo;
        this.radio = radio;
        this.body  = crearCuerpoFisico(x, y, radio);
    }

    // ── creación del cuerpo Box2D ────────────────────────────────────────────
    private Body crearCuerpoFisico(float cx, float cy, float r) {
        BodyDef def = new BodyDef();
        def.type     = BodyDef.BodyType.StaticBody;
        def.position.set(cx, cy);

        Body b = mundo.createBody(def);
        b.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(r);

        FixtureDef fix = new FixtureDef();
        fix.shape    = shape;
        fix.isSensor = true;                // sin rebote, solo detección
        fix.filter.categoryBits = 0x0002;   // categoría BURBUJA
        fix.filter.maskBits     = 0x0001;   // solo colisiona con PELOTA

        b.createFixture(fix);
        shape.dispose();
        return b;
    }

    // ── textura procedural ───────────────────────────────────────────────────
    private static Texture crearTexturaBurbuja(float radio) {
        int d = Math.max(4, (int)(radio * 100));
        Pixmap pm = new Pixmap(d, d, Pixmap.Format.RGBA8888);

        // fondo transparente
        pm.setColor(0, 0, 0, 0);
        pm.fill();

        // círculo principal semitransparente (azul claro)
        pm.setColor(new Color(0.55f, 0.80f, 1f, 0.55f));
        pm.fillCircle(d / 2, d / 2, d / 2 - 1);

        // borde sutil
        pm.setColor(new Color(0.7f, 0.9f, 1f, 0.85f));
        pm.drawCircle(d / 2, d / 2, d / 2 - 1);

        // reflejo superior izquierdo
        int br = Math.max(1, d / 6);
        pm.setColor(new Color(1, 1, 1, 0.75f));
        pm.fillCircle(d / 3, d / 3, br);

        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    // ── API pública (llamada desde NivelBaseScreen) ──────────────────────────

    /**
     * Llamar en beginContact cuando la pelota entra en la burbuja.
     * Activa la gravedad invertida y mayor amortiguación.
     */
    public void entrar(Pelota pelota) {
        if (!activa || pelotaDentro) return;
        pelotaDentro = true;

        Body pb = pelota.getBody();
        pb.setGravityScale(GRAVITY_SCALE_FLOTANDO);
        pb.setLinearDamping(DAMPING_DENTRO);

        // cancelar velocidad vertical brusca para arranque suave
        Vector2 vel = pb.getLinearVelocity();
        pb.setLinearVelocity(vel.x * 0.3f, 0f);
    }

    /**
     * Llamar en endContact cuando la pelota sale de la burbuja.
     * Restaura la gravedad normal.
     */
    public void salir(Pelota pelota) {
        if (!pelotaDentro) return;
        pelotaDentro = false;
        restaurarGravedad(pelota);
    }

    /**
     * Llamar cada frame desde render() para detectar si el usuario
     * toca/hace clic sobre la burbuja y hacerla explotar.
     */
    @Override
    public void actualizar(float delta) {
        if (!activa) return;

        if (Gdx.input.justTouched()) {
            // La detección en coordenadas de mundo se hace en NivelBaseScreen
            // con camaraFisica.unproject(). Aquí solo exponemos el método
            // revisarToque() para que NivelBaseScreen lo llame.
        }
    }

    /**
     * Llama a esto desde NivelBaseScreen.procesarToqueBurbujas()
     * con las coordenadas ya desprojectadas al mundo físico.
     *
     * @return true si la burbuja explotó
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

    /** Hace explotar la burbuja (la desactiva y restaura gravedad). */
    public void explotar(Pelota pelota) {
        if (!activa) return;
        activa       = false;
        pelotaDentro = false;
        if (pelota != null) restaurarGravedad(pelota);
    }

    // ── Interactuable (legacy: explotar sin referencia a pelota) ────────────
    @Override
    public void interactuar() {
        // No usar — usar explotar(pelota) para restaurar gravedad correctamente
        activa = false;
    }

    @Override
    public boolean estaActivo() {
        return activa;
    }

    // ── dibujo ───────────────────────────────────────────────────────────────
    @Override
    public void dibujar(SpriteBatch batch) {
        if (!activa) return;
        float cx = body.getPosition().x;
        float cy = body.getPosition().y;
        batch.draw(textura, cx - radio, cy - radio, radio * 2, radio * 2);
    }

    // ── helpers privados ─────────────────────────────────────────────────────
    private void restaurarGravedad(Pelota pelota) {
        Body pb = pelota.getBody();
        pb.setGravityScale(1f);
        pb.setLinearDamping(DAMPING_NORMAL);
    }

    // ── getters ───────────────────────────────────────────────────────────────
    public Body    getBody()         { return body; }
    public float   getRadio()        { return radio; }
    public boolean isPelotaDentro()  { return pelotaDentro; }
}