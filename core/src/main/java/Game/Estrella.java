package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Estrella extends ElementoNivel implements Interactuable {

    private boolean activa    = true;
    private boolean yaContada = false;
    private boolean desapareciendo = false;

    private final Body  body;
    private final float radio;

    private final Texture[] fIdle;
    private final Texture[] fDesaparecer;
    private int   frameActual = 0;
    private float tiempoAnim  = 0f;
    private static final float FPS = 1f / 12f;

    public Estrella(World mundo, float x, float y, float radio) {
        super(x, y, radio * 2, radio * 2, TipoElemento.ESTRELLA, null);
        this.radio = radio;

        fIdle        = frames("star_idle",      18);
        fDesaparecer = frames("star_disappear", 11);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        body = mundo.createBody(bodyDef);
        body.setUserData(this);

        CircleShape forma = new CircleShape();
        forma.setRadius(radio);
        FixtureDef fix = new FixtureDef();
        fix.shape     = forma;
        fix.isSensor  = true;
        body.createFixture(fix);
        forma.dispose();
    }

    private static Texture[] frames(String prefijo, int n) {
        Texture[] arr = new Texture[n];
        for (int i = 0; i < n; i++)
            arr[i] = new Texture(Gdx.files.internal("images/" + prefijo + i + ".png"));
        return arr;
    }

    public boolean yaFueContada() { return yaContada; }

    @Override
    public void actualizar(float delta) {
        if (!activa) return;
        tiempoAnim += delta;
        if (tiempoAnim < FPS) return;
        tiempoAnim -= FPS;

        if (desapareciendo) {
            if (frameActual < fDesaparecer.length - 1) {
                frameActual++;
            } else {
                activa = false;
            }
        } else {
            frameActual = (frameActual + 1) % fIdle.length;
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (!activa) return;
        Texture frame = desapareciendo ? fDesaparecer[frameActual] : fIdle[frameActual];
        batch.draw(frame, x - radio, y - radio, radio * 2, radio * 2);
    }

    @Override
    public void interactuar() {
        yaContada      = true;
        desapareciendo = true;
        frameActual    = 0;
        tiempoAnim     = 0f;
    }

    @Override
    public boolean estaActivo() { return activa; }

    public Body getBody() { return body; }

    @Override
    public void dispose() {
        for (Texture t : fIdle)        if (t != null) t.dispose();
        for (Texture t : fDesaparecer) if (t != null) t.dispose();
    }
}
