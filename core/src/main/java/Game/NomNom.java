package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class NomNom extends ElementoNivel implements Interactuable {

    private Body  body;
    private World mundo;
    private float radio;
    private boolean comio = false;

    public NomNom(World mundo, float x, float y, float radio) {
        super(x, y, radio * 2, radio * 2, TipoElemento.ESTRELLA, null);
        this.mundo = mundo;
        this.radio = radio;
        this.textura = new Texture(Gdx.files.internal("images/nomnomtemp.png"));
        crearCuerpoFisico();
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

    @Override
    public void actualizar(float delta) {}

    @Override
    public void dibujar(SpriteBatch batch) {
        float tam = radio * 2.5f;
        batch.draw(textura, x - tam / 2f, y - tam / 2f, tam, tam);
    }

    @Override
    public void interactuar() {
        comio = true;
    }

    // NivelBaseScreen llama esto cada frame para saber si ganó
    public boolean comioLaPelota() {
        return comio;
    }

    @Override
    public boolean estaActivo() { return !comio; }

    public Body getBody() { return body; }

    @Override
    public void dispose() {
        if (textura != null) { textura.dispose(); textura = null; }
    }
}