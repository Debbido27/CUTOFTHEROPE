package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class NomNom extends ElementoNivel implements Interactuable {

    private enum Estado { NORMAL, ABRIENDO, BOCA_ABIERTA, CERRANDO, COMIENDO, TRISTE }

    private Body  body;
    private World mundo;
    private float radio;
    private boolean comio = false;

    private Estado    estado     = Estado.NORMAL;
    private Texture[] fNormal;
    private Texture[] fAbrirBoca;
    private Texture[] fCerrarBoca;
    private Texture[] fComiendo;
    private Texture[] fTriste;
    private int   frameActual = 0;
    private float tiempoFrame = 0f;
    private static final float FPS  = 1f / 12f;
    // candy must be this close (world units) to trigger mouth-open
    private static final float PROX = 2.5f;

    public NomNom(World mundo, float x, float y, float radio) {
        super(x, y, radio * 2, radio * 2, TipoElemento.ESTRELLA, null);
        this.mundo = mundo;
        this.radio = radio;

        fNormal    = frames("pet_normal",     19);
        fAbrirBoca = frames("pet_openMouth",   4);
        fCerrarBoca= frames("pet_closeMouth",  4);
        fComiendo  = frames("pet_chew",       34);
        fTriste    = frames("pet_sad",        14);

        crearCuerpoFisico();
    }

    private static Texture[] frames(String prefijo, int n) {
        Texture[] arr = new Texture[n];
        for (int i = 0; i < n; i++)
            arr[i] = new Texture(Gdx.files.internal("images/" + prefijo + i + ".png"));
        return arr;
    }

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
        fix.isSensor = true;
        body.createFixture(fix);
        forma.dispose();
    }

    // called every frame from NivelBaseScreen with the candy's current position
    public void setPelotaCerca(boolean cerca) {
        if (estado == Estado.COMIENDO || estado == Estado.TRISTE) return;

        if (cerca && (estado == Estado.NORMAL || estado == Estado.CERRANDO)) {
            estado = Estado.ABRIENDO;
            frameActual = 0;
            tiempoFrame = 0f;
        } else if (!cerca && (estado == Estado.BOCA_ABIERTA || estado == Estado.ABRIENDO)) {
            // don't interrupt a mid-open sequence — wait until BOCA_ABIERTA
            if (estado == Estado.BOCA_ABIERTA) {
                estado = Estado.CERRANDO;
                frameActual = 0;
                tiempoFrame = 0f;
            }
        }
    }

    @Override
    public void actualizar(float delta) {
        tiempoFrame += delta;
        if (tiempoFrame < FPS) return;
        tiempoFrame -= FPS;

        switch (estado) {
            case NORMAL:
                frameActual = (frameActual + 1) % fNormal.length;
                break;
            case ABRIENDO:
                if (frameActual < fAbrirBoca.length - 1) {
                    frameActual++;
                } else {
                    estado = Estado.BOCA_ABIERTA;
                }
                break;
            case BOCA_ABIERTA:
                break; // hold last frame of openMouth
            case CERRANDO:
                if (frameActual < fCerrarBoca.length - 1) {
                    frameActual++;
                } else {
                    estado = Estado.NORMAL;
                    frameActual = 0;
                }
                break;
            case COMIENDO:
                if (frameActual < fComiendo.length - 1) frameActual++;
                break;
            case TRISTE:
                if (frameActual < fTriste.length - 1) frameActual++;
                break;
        }
    }

    public void ponerTriste() {
        if (estado == Estado.NORMAL || estado == Estado.ABRIENDO
                || estado == Estado.BOCA_ABIERTA || estado == Estado.CERRANDO) {
            estado = Estado.TRISTE;
            frameActual = 0;
            tiempoFrame = 0f;
        }
    }

    @Override
    public void interactuar() {
        comio = true;
        estado = Estado.COMIENDO;
        frameActual = 0;
        tiempoFrame = 0f;
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        Texture[] f;
        switch (estado) {
            case ABRIENDO:
            case BOCA_ABIERTA: f = fAbrirBoca;  break;
            case CERRANDO:     f = fCerrarBoca; break;
            case COMIENDO:     f = fComiendo;   break;
            case TRISTE:       f = fTriste;     break;
            default:           f = fNormal;     break;
        }
        float tam = radio * 2.5f;
        batch.draw(f[frameActual], x - tam / 2f, y - tam / 2f, tam, tam);
    }

    public boolean comioLaPelota() { return comio; }

    @Override
    public boolean estaActivo() { return !comio; }

    public Body getBody() { return body; }

    @Override
    public void dispose() {
        for (Texture t : fNormal)    if (t != null) t.dispose();
        for (Texture t : fAbrirBoca) if (t != null) t.dispose();
        for (Texture t : fCerrarBoca)if (t != null) t.dispose();
        for (Texture t : fComiendo)  if (t != null) t.dispose();
        for (Texture t : fTriste)    if (t != null) t.dispose();
    }
}
