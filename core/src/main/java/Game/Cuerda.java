package Game;

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

}

}
