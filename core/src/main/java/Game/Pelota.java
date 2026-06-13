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
        body.setUserData(this);
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

    private Texture crearTexturaPlaceHolder (float radio){
        int diametroPx = (int)(radio*100);
        if(diametroPx<2) diametroPx = 2;

        Pixmap pixmap = new Pixmap(diametroPx, diametroPx, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillCircle(diametroPx/2, diametroPx/2, diametroPx/2);

        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    public void dibujar(SpriteBatch batch){
        float diametro = radio*2;
        batch.draw(textura,
            body.getPosition().x-radio,
            body.getPosition().y-radio,
            diametro,diametro);
    }

    public Body getBody() {
        return body;
    }

    public void dispose(){
        textura.dispose();
    }
}
