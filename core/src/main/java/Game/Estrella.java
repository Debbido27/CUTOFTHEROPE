package Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Estrella extends ElementoNivel implements Interactuable {

    private boolean activa;
    private Body body;
    private float radio;
    private boolean yaContada = false;

    public Estrella(World mundo, float x, float y, float radio) {
        super(x, y, radio * 2, radio * 2, TipoElemento.ESTRELLA, crearTexturaPlaceholder(radio));
        this.activa = true;
        this.radio = radio;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        body = mundo.createBody(bodyDef);
        body.setUserData(this);
        body.setUserData(this); // para identificarla en el ContactListener

        CircleShape forma = new CircleShape();
        forma.setRadius(radio);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = forma;
        fixtureDef.isSensor = true; // no choca físicamente, solo detecta
        body.createFixture(fixtureDef);
        forma.dispose();
    }

    private static Texture crearTexturaPlaceholder(float radio) {
        int diametroPx = Math.max((int) (radio * 100), 2);
        Pixmap pixmap = new Pixmap(diametroPx, diametroPx, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillCircle(diametroPx / 2, diametroPx / 2, diametroPx / 2);
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }
    public boolean yaFueContada() { return yaContada; }


    @Override
    public void actualizar(float delta) {
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (!activa) return;
        batch.draw(textura, x - radio, y - radio, radio * 2, radio * 2);
    }

    @Override
    public void interactuar() {
        yaContada=true;
        activa = false;
    }

    @Override
    public boolean estaActivo() {
        return activa;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
