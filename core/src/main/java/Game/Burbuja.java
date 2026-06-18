package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

public class Burbuja extends ElementoNivel implements Interactuable {

    private static final float GRAVITY_BURBUJA = -1f;
    private static final float DAMPING_BURBUJA = 2f;
    private static final float DAMPING_NORMAL  = 0f;
    private static final float FPS             = 1f / 14f;

    private boolean activa       = true;
    private boolean pelotaDentro = false;
    private boolean popActivo    = false;

    private final World mundo;
    private final Body  body;
    private final float radio;
    private       Joint jointBurbuja = null;

    private static Texture[] fVuelo;//Texturas compartidas
    private static Texture[] fPop;
    private int   frameVuelo = 0;
    private int   framePop   = 0;
    private float tiempoAnim = 0f;
    private float popX, popY;

    public Burbuja(World mundo, float x, float y, float radio) {
        super(x, y, radio * 2, radio * 2, TipoElemento.BURBUJA, null);
        this.mundo = mundo;
        this.radio = radio;

        if (fVuelo == null) fVuelo = frames("bubble_flight", 14);
        if (fPop == null)   fPop   = frames("bubble_pop",    12);

        this.body = crearCuerpoFisico(x, y, radio);
    }

    public Burbuja(World mundo, float x, float y, float radio, float ignorado) {
        this(mundo, x, y, radio);
    }

    private static Texture[] frames(String prefijo, int n) {
        Texture[] arr = new Texture[n];
        for (int i = 0; i < n; i++)
            arr[i] = new Texture(Gdx.files.internal("images/" + prefijo + i + ".png"));
        return arr;
    }

    private Body crearCuerpoFisico(float cx, float cy, float r) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(cx, cy);

        Body b = mundo.createBody(def);
        b.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(r);

        FixtureDef fix = new FixtureDef();
        fix.shape               = shape;
        fix.isSensor            = true;
        fix.filter.categoryBits = 0x0004;
        fix.filter.maskBits     = 0x0001;
        b.createFixture(fix);
        shape.dispose();
        return b;
    }

    public void entrar(Pelota pelota) {
        if (!activa || pelotaDentro) return;
        pelotaDentro = true;

        body.setType(BodyDef.BodyType.DynamicBody);
        body.setGravityScale(GRAVITY_BURBUJA);
        body.setLinearDamping(DAMPING_BURBUJA);

        Body pb = pelota.getBody();
        pb.setGravityScale(GRAVITY_BURBUJA);
        pb.setLinearDamping(DAMPING_BURBUJA);

        Vector2 vel = pb.getLinearVelocity();
        pb.setLinearVelocity(vel.x * 0.3f, 0f);

        WeldJointDef weld = new WeldJointDef();
        weld.bodyA = body;
        weld.bodyB = pb;
        weld.localAnchorA.set(0, 0);
        weld.localAnchorB.set(0, 0);
        weld.referenceAngle = 0f;
        jointBurbuja = mundo.createJoint(weld);
    }

    public void salir(Pelota pelota) {}

    public void explotar(Pelota pelota) {
        if (!activa) return;

        popX = body.getPosition().x;
        popY = body.getPosition().y;

        if (jointBurbuja != null) {
            mundo.destroyJoint(jointBurbuja);
            jointBurbuja = null;
        }

        body.setType(BodyDef.BodyType.StaticBody);
        activa       = false;
        pelotaDentro = false;

        popActivo = true;
        framePop  = 0;
        tiempoAnim = 0f;

        if (pelota != null) {
            Body pb = pelota.getBody();
            pb.setGravityScale(1f);
            pb.setLinearDamping(DAMPING_NORMAL);
            pb.setLinearVelocity(pb.getLinearVelocity().x, 2f);
        }
    }

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

    @Override
    public void interactuar() {
        activa = false;
    }

    @Override
    public boolean estaActivo() { return activa; }

    @Override
    public void actualizar(float delta) {
        tiempoAnim += delta;
        if (tiempoAnim < FPS) return;
        tiempoAnim -= FPS;

        if (popActivo) {
            if (framePop < fPop.length - 1) {
                framePop++;
            } else {
                popActivo = false;
            }
        } else if (activa) {
            frameVuelo = (frameVuelo + 1) % fVuelo.length;
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (!activa && !popActivo) return;

        float cx, cy;
        Texture frame;
        float drawRadio;

        if (popActivo) {
            cx        = popX;
            cy        = popY;
            frame     = fPop[framePop];
            drawRadio = radio * 1.5f; // pop ring draws slightly larger
        } else {
            cx        = body.getPosition().x;
            cy        = body.getPosition().y;
            frame     = fVuelo[frameVuelo];
            drawRadio = radio;
        }

        batch.draw(frame, cx - drawRadio, cy - drawRadio, drawRadio * 2, drawRadio * 2);
    }

    public Body    getBody()        { return body; }
    public float   getRadio()       { return radio; }
    public boolean isPelotaDentro() { return pelotaDentro; }

    @Override
    public void dispose() {
        //No disparamos texturas aqui porque son estaticas y compartidas
    }

    public static void disposeTexturas() {
        if (fVuelo != null) {
            for (Texture t : fVuelo) if (t != null) t.dispose();
            fVuelo = null;
        }
        if (fPop != null) {
            for (Texture t : fPop) if (t != null) t.dispose();
            fPop = null;
        }
    }
}
