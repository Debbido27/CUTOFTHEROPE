package Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Cuerda {
    private static Texture texturaSoga;
    private static Texture texturaAncla;
    private Body body;
    private Joint joint;
    private Cuerda siguiente;
    private float largoSegmento;
    private float grosor;
    
public Cuerda(World mundo, Body anclaBody, Vector2 anclaPos, int segmentos,
              float largoSegmento, Body cuerpoFinal, boolean primerSegmento) {

        this.largoSegmento=largoSegmento;
        this.grosor = 0.05f;
        if (texturaSoga == null) {
            texturaSoga = crearTexturaSoga();
        }
        Vector2 posSegmento = new Vector2(anclaPos.x, anclaPos.y - largoSegmento / 2f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posSegmento);
        body = mundo.createBody(bodyDef);

        PolygonShape forma = new PolygonShape();
        forma.setAsBox(0.05f, largoSegmento / 2f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = forma;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.2f;
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
    }else {
            siguiente = null;
            RevoluteJointDef ultimoJoint = new RevoluteJointDef();
            ultimoJoint.bodyA = this.body;
            ultimoJoint.bodyB = cuerpoFinal;
            ultimoJoint.localAnchorA.set(0, -largoSegmento / 2f);
            ultimoJoint.localAnchorB.set(0, 0);
            mundo.createJoint(ultimoJoint);
        }
    }


    private static Texture crearTexturaSoga() {
        int ancho = 32;
        int alto = 32;
        Pixmap pixmap = new Pixmap(ancho, alto, Pixmap.Format.RGBA8888);

        Color base = new Color(0.55f, 0.35f, 0.18f, 1f);
        Color oscuro = new Color(0.35f, 0.20f, 0.08f, 1f);
        Color claro = new Color(0.75f, 0.55f, 0.30f, 1f);

        pixmap.setColor(base);
        pixmap.fill();

        pixmap.setColor(claro);
        pixmap.fillRectangle(0, alto / 2 - 3, ancho, 4);

        pixmap.setColor(oscuro);
        int paso = 8;
        for (int offset = -alto; offset < ancho; offset += paso) {
            for (int x = 0; x < ancho; x++) {
                int y = x - offset;
                if (y >= 0 && y < alto) {
                    pixmap.drawPixel(x, y);
                    if (y + 1 < alto) pixmap.drawPixel(x, y + 1);
                }
            }
        }

        pixmap.setColor(oscuro);
        pixmap.drawLine(0, 0, ancho, 0);
        pixmap.drawLine(0, alto - 1, ancho, alto - 1);

        Texture textura = new Texture(pixmap);
        textura.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        pixmap.dispose();
        return textura;
    }

    
    

   public void dibujar(SpriteBatch batch) {
    Vector2 pos = body.getPosition();
    float angulo = (float) Math.toDegrees(body.getAngle());

    batch.draw(texturaSoga,
        pos.x - grosor / 2f, pos.y - largoSegmento / 2f,
        grosor / 2f, largoSegmento / 2f,
        grosor, largoSegmento * 1.15f,
        1f, 1f,
        angulo,
        0, 0, texturaSoga.getWidth(), texturaSoga.getHeight(),
        false, false);

    if (siguiente != null) {
        siguiente.dibujar(batch);
    }
}
   
   
   private static Texture crearTexturaAncla() {
    int diametro = 24;
    Pixmap pixmap = new Pixmap(diametro, diametro, Pixmap.Format.RGBA8888);
    pixmap.setColor(new Color(0.5f, 0.5f, 0.5f, 1f));
    pixmap.fillCircle(diametro / 2, diametro / 2, diametro / 2);
    Texture tex = new Texture(pixmap);
    pixmap.dispose();
    return tex;
}

public static void dibujarAncla(SpriteBatch batch, Vector2 posicion, float radio) {
    if (texturaAncla == null) texturaAncla = crearTexturaAncla();
    batch.draw(texturaAncla, posicion.x - radio, posicion.y - radio, radio * 2, radio * 2);
}

/**
 * Revisa recursivamente si el segmento (p1->p2, trazo del dedo) cruza este
 * eslabón de cuerda. Si cruza, destruye el joint que conecta este eslabón
 * con el anterior y corta la cadena aquí (siguiente pasa a ser una cuerda
 * independiente, ya no conectada con esta mitad).
 *
 * Devuelve true si se realizó un corte (para detener más chequeos ese frame).
 */
public boolean revisarCorte(Vector2 p1, Vector2 p2, World mundo) {
    Vector2 pos = body.getPosition();
    float angulo = body.getAngle();

    // extremos del segmento de cuerda en el mundo
    float mitad = largoSegmento / 2f;
    float dx = (float) Math.sin(angulo) * mitad;
    float dy = (float) Math.cos(angulo) * mitad;

    Vector2 extremoA = new Vector2(pos.x - dx, pos.y - dy); // arriba
    Vector2 extremoB = new Vector2(pos.x + dx, pos.y + dy); // abajo

    if (segmentosSeCruzan(p1, p2, extremoA, extremoB)) {
        // destruye el joint que conecta este eslabón con el anterior
        mundo.destroyJoint(joint);
        joint = null;
        return true;
    }

    if (siguiente != null) {
        return siguiente.revisarCorte(p1, p2, mundo);
    }
    return false;
}

/** Determina si el segmento (a1-a2) cruza al segmento (b1-b2). */
    private boolean segmentosSeCruzan(Vector2 a1, Vector2 a2, Vector2 b1, Vector2 b2) {
        float d1 = direccion(b1, b2, a1);
        float d2 = direccion(b1, b2, a2);
        float d3 = direccion(a1, a2, b1);
        float d4 = direccion(a1, a2, b2);

        return ((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0))
            && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0));
    }

    private float direccion(Vector2 p, Vector2 q, Vector2 r) {
        return (q.x - p.x) * (r.y - p.y) - (q.y - p.y) * (r.x - p.x);
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
    
    /** Libera el body de este segmento y los siguientes recursivamente. */
public void dispose() {
    if (siguiente != null) siguiente.dispose();
}

    /** Libera la textura compartida. Llamar solo una vez al cerrar el nivel. */
    public static void disposeTextura() {
        if (texturaSoga != null) {
            texturaSoga.dispose();
            texturaSoga = null;
            if (texturaAncla != null) { texturaAncla.dispose(); texturaAncla = null; }
        }
    }


}


