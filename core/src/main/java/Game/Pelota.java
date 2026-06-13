package Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
public class Pelota {


    private Body body;
    private float radio;
    private Texture textura;

    public Pelota(World mundo, float x, float y, float radio){
        this.radio=radio;
        this.textura= crearTexturaPlaceHolder(radio);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);

        body = mundo.createBody(bodyDef);

        CircleShape forma = new CircleShape();
        forma.setRadius(radio);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = forma;
        fixtureDef.density=1f;
        fixtureDef.friction=0.3f;
        fixtureDef.restitution=0.2f;

        body.createFixture(fixtureDef);
        forma.dispose();
    }
}
