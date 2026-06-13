package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;


public class NomNom extends ElementoNivel implements Interactuable {

    // ── spritesheet ───────────────────────────────────────────────────
    private static final int FRAME_PX   = 128;   // píxeles por frame
    private static final int COLS       = 16;    // columnas del sheet
    private static final int ROWS       = 16;    // filas del sheet

    // ── física ────────────────────────────────────────────────────────
    private Body  body;
    private World mundo;
    private float radio;        // radio del sensor de colisión

    // ── estado ────────────────────────────────────────────────────────
    public enum Estado { IDLE, COMIENDO, FELIZ }
    private Estado estado    = Estado.IDLE;
    private boolean comio    = false;   // true cuando la pelota lo tocó
    private float   timerAnim = 0f;     // para duración de animación "feliz"

    // ── animaciones ───────────────────────────────────────────────────
    private Animation<TextureRegion> animIdle;
    private Animation<TextureRegion> animComiendo;
    private Animation<TextureRegion> animFeliz;
    private float stateTime = 0f;

    // ─────────────────────────────────────────────────────────────────
    /**
     * @param mundo   mundo Box2D
     * @param x       posición X en unidades de mundo
     * @param y       posición Y en unidades de mundo
     * @param radio   radio del sensor (aprox 0.5f–0.8f)
     */
    public NomNom(World mundo, float x, float y, float radio) {
        super(x, y, radio * 2, radio * 2, TipoElemento.ESTRELLA, null);
        this.mundo = mundo;
        this.radio = radio;

        cargarAnimaciones();
        crearCuerpoFisico();
    }

    // ── carga de animaciones ──────────────────────────────────────────
    private void cargarAnimaciones() {
        Texture sheet = new Texture(
            Gdx.files.internal("images/char_animations_hd.png"));

        // Recortar todos los frames del sheet en una matriz
        TextureRegion[][] frames = TextureRegion.split(sheet, FRAME_PX, FRAME_PX);

        // IDLE: fila 0, columnas 0-3
        animIdle = new Animation<>(0.18f,
            frames[0][0], frames[0][1], frames[0][2], frames[0][3]);
        animIdle.setPlayMode(Animation.PlayMode.LOOP);

        // COMIENDO: fila 1 col 0-3 + fila 2 col 0-3  (boca abriéndose)
        animComiendo = new Animation<>(0.08f,
            frames[1][0], frames[1][1], frames[1][2], frames[1][3],
            frames[2][0], frames[2][1], frames[2][2], frames[2][3]);
        animComiendo.setPlayMode(Animation.PlayMode.NORMAL); // una sola vez

        // FELIZ: fila 3, columnas 0-5
        animFeliz = new Animation<>(0.14f,
            frames[3][0], frames[3][1], frames[3][2],
            frames[3][3], frames[3][4], frames[3][5]);
        animFeliz.setPlayMode(Animation.PlayMode.LOOP);

        // Guardamos la textura en el campo heredado para dispose()
        this.textura = sheet;
    }

    // ── cuerpo físico ─────────────────────────────────────────────────
    private void crearCuerpoFisico() {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(x, y);
        body = mundo.createBody(def);
        body.setUserData(this);

        CircleShape forma = new CircleShape();
        forma.setRadius(radio);

        FixtureDef fix = new FixtureDef();
        fix.shape    = forma;
        fix.isSensor = true;   // sensor: detecta sin empujar físicamente
        body.createFixture(fix);
        forma.dispose();
    }

    // ── actualizar ────────────────────────────────────────────────────
    @Override
    public void actualizar(float delta) {
        stateTime += delta;

        if (estado == Estado.FELIZ) {
            timerAnim -= delta;
            if (timerAnim <= 0) {
                // animación feliz terminó, NomNom vuelve a idle
                // (en la práctica el nivel ya habrá cambiado)
                estado = Estado.IDLE;
                stateTime = 0f;
            }
        }
    }

    // ── dibujar ───────────────────────────────────────────────────────
    @Override
    public void dibujar(SpriteBatch batch) {
        TextureRegion frame = obtenerFrameActual();
        if (frame == null) return;

        float tam = radio * 2.5f;   // un poco más grande que el sensor
        batch.draw(frame,
            x - tam / 2f,
            y - tam / 2f,
            tam, tam);
    }

    private TextureRegion obtenerFrameActual() {
        switch (estado) {
            case COMIENDO: return animComiendo.getKeyFrame(stateTime);
            case FELIZ:    return animFeliz.getKeyFrame(stateTime);
            default:       return animIdle.getKeyFrame(stateTime);
        }
    }


    @Override
    public void interactuar() {
        if (!comio) {
            comio     = true;
            estado    = Estado.COMIENDO;
            stateTime = 0f;
            timerAnim = 1.5f;   // después de 1.5s pasa a FELIZ
        }
    }

    public boolean comioLaPelota() {
        if (comio && estado == Estado.COMIENDO
                && animComiendo.isAnimationFinished(stateTime)) {
            estado    = Estado.FELIZ;
            stateTime = 0f;
            timerAnim = 2.0f;
            return true;
        }
        return false;
    }

    @Override
    public boolean estaActivo() { return !comio; }

    public Body getBody() { return body; }

    @Override
    public void dispose() {
        if (textura != null) { textura.dispose(); textura = null; }
    }
}