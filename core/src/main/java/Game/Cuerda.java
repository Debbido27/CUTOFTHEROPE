
package Game;
 
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
 
/**
 * Cuerda tipo Cut the Rope: una cadena recursiva de eslabones (bodies pequeños
 * y livianos) unidos por RevoluteJoint. Se dobla con naturalidad, no se
 * estira como elástico ni queda rígida como tabla.
 *
 * Los eslabones NO colisionan entre sí ni con la pelota (maskBits = 0),
 * así que pueden existir varias cuerdas sosteniendo la misma pelota sin
 * causar jitter/temblor.
 */
public class Cuerda {
 
    private static Texture texturaSoga;
    private static Texture texturaAncla;
 
    private Body body;
    private Joint joint;        // joint que une este eslabón con el anterior (null si cortado)
    private Cuerda siguiente;
    private final float largoSegmento;
 
    private static final float GROSOR = 0.1f;
    private static final int TEX_SIZE = 16;
 
    public Cuerda(World mundo, Body anclaBody, Vector2 anclaPos, int segmentos,
                   float largoSegmento, Body cuerpoFinal, boolean primerSegmento) {
 
        this.largoSegmento = largoSegmento;
 
        if (texturaSoga == null) texturaSoga = crearTexturaSoga();
 
        Vector2 posSegmento = new Vector2(anclaPos.x, anclaPos.y - largoSegmento / 2f);
 
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posSegmento);
        body = mundo.createBody(bodyDef);
        body.setLinearDamping(0.6f);
        body.setAngularDamping(0.8f);
 
        PolygonShape forma = new PolygonShape();
        forma.setAsBox(0.04f, largoSegmento / 2f);
 
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = forma;
        fixtureDef.density = 0.3f;   // muy liviano: la pelota domina el movimiento
        fixtureDef.friction = 0.1f;
        fixtureDef.filter.maskBits = 0; // no colisiona con NADA (evita jitter entre cuerdas/pelota)
        body.createFixture(fixtureDef);
        forma.dispose();
 
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = anclaBody;
        jointDef.bodyB = body;
        jointDef.localAnchorA.set(0, primerSegmento ? 0 : -largoSegmento / 2f);
        jointDef.localAnchorB.set(0, largoSegmento / 2f);
        joint = mundo.createJoint(jointDef);
 
        Vector2 finSegmento = new Vector2(posSegmento.x, posSegmento.y - largoSegmento / 2f);
 
        if (segmentos > 1) {
            siguiente = new Cuerda(mundo, this.body, finSegmento, segmentos - 1, largoSegmento, cuerpoFinal, false);
        } else {
            siguiente = null;
            RevoluteJointDef ultimoJoint = new RevoluteJointDef();
            ultimoJoint.bodyA = this.body;
            ultimoJoint.bodyB = cuerpoFinal;
            ultimoJoint.localAnchorA.set(0, -largoSegmento / 2f);
            ultimoJoint.localAnchorB.set(0, 0);
            mundo.createJoint(ultimoJoint);
        }
    }
 
    /** Dibuja recursivamente este eslabón y los siguientes, usando textura tileada. */
    public void dibujar(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        float angulo = (float) Math.toDegrees(body.getAngle());
 
        float repeticiones = (largoSegmento * 1.15f) / (GROSOR * 1.5f);
        int srcHeight = Math.max(TEX_SIZE, (int) (TEX_SIZE * repeticiones));
 
        batch.draw(texturaSoga,
            pos.x - GROSOR / 2f, pos.y - largoSegmento / 2f,
            GROSOR / 2f, largoSegmento / 2f,
            GROSOR, largoSegmento * 1.15f,
            1f, 1f,
            angulo,
            0, 0, TEX_SIZE, srcHeight,
            false, false);
 
        if (siguiente != null) {
            siguiente.dibujar(batch);
        }
    }
 
    /**
     * Revisa recursivamente si el trazo (p1->p2) cruza este eslabón.
     * Si cruza, destruye el joint que lo une al eslabón anterior, separando
     * la cadena en dos mitades independientes.
     */
    public boolean revisarCorte(Vector2 p1, Vector2 p2, World mundo) {
        if (joint == null) {
            return siguiente != null && siguiente.revisarCorte(p1, p2, mundo);
        }
 
        Vector2 pos = body.getPosition();
        float angulo = body.getAngle();
        float mitad = largoSegmento / 2f;
        float dx = (float) Math.sin(angulo) * mitad;
        float dy = (float) Math.cos(angulo) * mitad;
 
        Vector2 extremoA = new Vector2(pos.x - dx, pos.y - dy);
        Vector2 extremoB = new Vector2(pos.x + dx, pos.y + dy);
 
        if (segmentosSeCruzan(p1, p2, extremoA, extremoB)) {
            mundo.destroyJoint(joint);
            joint = null;
            return true;
        }
 
        if (siguiente != null) {
            return siguiente.revisarCorte(p1, p2, mundo);
        }
        return false;
    }
 
    private boolean segmentosSeCruzan(Vector2 a1, Vector2 a2, Vector2 b1, Vector2 b2) {
        float d1 = dir(b1, b2, a1), d2 = dir(b1, b2, a2);
        float d3 = dir(a1, a2, b1), d4 = dir(a1, a2, b2);
        return ((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0))
            && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0));
    }
 
    private float dir(Vector2 p, Vector2 q, Vector2 r) {
        return (q.x - p.x) * (r.y - p.y) - (q.y - p.y) * (r.x - p.x);
    }
 
    /** Textura tileable tipo cuerda: franja clara central + marcas de trenzado. */
    private static Texture crearTexturaSoga() {
        int s = TEX_SIZE;
        Pixmap pm = new Pixmap(s, s, Pixmap.Format.RGBA8888);
 
        Color base   = new Color(0.55f, 0.35f, 0.18f, 1f);
        Color oscuro = new Color(0.35f, 0.20f, 0.08f, 1f);
        Color claro  = new Color(0.75f, 0.55f, 0.30f, 1f);
 
        pm.setColor(base);
        pm.fill();
 
        pm.setColor(claro);
        pm.fillRectangle(s / 2 - 2, 0, 4, s);
 
        pm.setColor(oscuro);
        pm.fillRectangle(0, 0, s, 2);
        pm.fillRectangle(0, s / 2, s, 2);
 
        Texture t = new Texture(pm);
        t.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        pm.dispose();
        return t;
    }
 
    private static Texture crearTexturaAncla() {
    int d = 70; // antes 24, ahora más grande
    Pixmap px = new Pixmap(d, d, Pixmap.Format.RGBA8888);
    
    // Círculo gris claro exterior
    px.setColor(new Color(0.75f, 0.75f, 0.75f, 1f)); // gris claro
px.fillCircle(d / 2, d / 2, d / 4); // antes d/6, ahora d/4 - más grande
    
    // Círculo gris oscuro en el centro
    px.setColor(new Color(0.35f, 0.35f, 0.35f, 1f)); // gris oscuro
    px.fillCircle(d / 2, d / 2, d / 6); // círculo pequeño central
    
    Texture t = new Texture(px);
    px.dispose();
    return t;
}
 
   public static void dibujarAncla(SpriteBatch batch, Vector2 pos, float radio) {
    if (texturaAncla == null) texturaAncla = crearTexturaAncla();
    float radioVisual = radio * 2.5f; // antes radio * 2, ahora más grande
    batch.draw(texturaAncla, pos.x - radioVisual, pos.y - radioVisual, radioVisual * 2, radioVisual * 2);
}
    public int contarSegmentos() {
        return 1 + (siguiente != null ? siguiente.contarSegmentos() : 0);
    }
 
    public Body getBody() {
        return body;
    }
 
    public Cuerda getSiguiente() {
        return siguiente;
    }
 
    /** Libera este eslabón y los siguientes recursivamente (no destruye bodies del World). */
    public void dispose() {
        if (siguiente != null) siguiente.dispose();
    }
 
    public static void disposeTextura() {
        if (texturaSoga != null) { texturaSoga.dispose(); texturaSoga = null; }
        if (texturaAncla != null) { texturaAncla.dispose(); texturaAncla = null; }
    }
}