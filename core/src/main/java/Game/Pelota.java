package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Pelota {

    private final Body    body;
    private final float   radio;
    private final Texture textura;
    private final Texture texIzq;
    private final Texture texDer;

    private boolean rota   = false;
    private boolean oculta = false;
    private float izqX, izqY, izqVX, izqVY;
    private float derX, derY, derVX, derVY;

    private static final float GRAVEDAD = -14f;

    public Pelota(World mundo, float x, float y, float radio) {
        this.radio   = radio;
        this.textura = new Texture(Gdx.files.internal("images/candy.png"));
        this.texIzq  = new Texture(Gdx.files.internal("images/candy_left.png"));
        this.texDer  = new Texture(Gdx.files.internal("images/candy_right.png"));

        BodyDef def = new BodyDef();
        def.type         = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
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
        fix.restitution = 0.1f;
        fix.filter.categoryBits = 0x0001;
        fix.filter.maskBits     = (short) 0xFFFF;

        body.createFixture(fix);
        shape.dispose();
    }

    //llama esto desde fuera del paso de fisicas (no desde beginContact)
    public void romper() {
        if (rota) return;
        rota = true;
        float cx = body.getPosition().x;
        float cy = body.getPosition().y;
        body.setActive(false);

        //mitad izquierda sale hacia arriba-izquierda, derecha hacia arriba-derecha
        izqX = cx;  izqY = cy;  izqVX = -3.5f;  izqVY = 2.5f;
        derX = cx;  derY = cy;  derVX =  3.5f;  derVY = 2.5f;
    }

    public void actualizar(float delta) {
        if (!rota) return;
        izqVY += GRAVEDAD * delta;
        derVY += GRAVEDAD * delta;
        izqX  += izqVX * delta;
        izqY  += izqVY * delta;
        derX  += derVX * delta;
        derY  += derVY * delta;
    }

    public void ocultar() { oculta = true; }

    public void dibujar(SpriteBatch batch) {
        if (oculta) return;
        if (rota) {
            float r = radio;
            batch.draw(texIzq, izqX - r, izqY - r, r * 2f, r * 2f);
            batch.draw(texDer, derX - r, derY - r, r * 2f, r * 2f);
        } else {
            float d = radio * 2f;
            batch.draw(textura,
                body.getPosition().x - radio,
                body.getPosition().y - radio,
                d, d);
        }
    }

    public boolean estaRota() { return rota; }
    public Body    getBody()  { return body; }
    public float   getRadio() { return radio; }

    public void dispose() {
        textura.dispose();
        texIzq.dispose();
        texDer.dispose();
    }
}
