package Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Cuerda {
    private Body body;
    private Joint joint;
    private Cuerda siguiente;
    private float largoSegmento;

    public Cuerda(World mundo, Body anclaBody, Vector2 anclaPos, int segmentos,
                  float largoSegmento, Body cuerpoFinal) {

        this.largoSegmento=largoSegmento;
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
        jointDef.localAnchorA.set(0, anclaBody == body ? 0 : -largoSegmento / 2f);
        jointDef.localAnchorB.set(0, largoSegmento / 2f);
        joint = mundo.createJoint(jointDef);

        Vector2 finSegmento = new Vector2(posSegmento.x, posSegmento.y - largoSegmento / 2f);

        if (segmentos > 1) {
            siguiente = new Cuerda(mundo, this.body, finSegmento, segmentos - 1, largoSegmento, cuerpoFinal);
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


}


