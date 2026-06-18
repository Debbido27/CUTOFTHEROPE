package Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Obstaculo extends ElementoNivel implements Interactuable {

    private boolean activo;
    private Body body;
    private World mundo;
    private TipoObstaculo tipoObstaculo;
    private float altoDibujo;

    public enum TipoObstaculo {
        CORTO,
        LARGO
    }

    public Obstaculo(World mundo, float x, float y, float ancho, float alto, TipoObstaculo tipo) {
        super(x, y, ancho, alto, TipoElemento.ESTRELLA, null);
        this.mundo         = mundo;
        this.tipoObstaculo = tipo;
        this.activo        = true;

        if (tipo == TipoObstaculo.CORTO) {
            this.ancho = ancho;
            this.alto  = ancho;
        }

        int n = numEspinas(this.ancho);
        this.textura   = new Texture(Gdx.files.internal(rutaEspinas(this.ancho)));
        this.altoDibujo = Math.max(this.ancho / n, 0.5f);

        crearCuerpoFisico();
    }

    private static int numEspinas(float ancho) {
        if (ancho < 1.5f) return 3;
        if (ancho < 3.0f) return 4;
        if (ancho < 5.0f) return 5;
        return 7;
    }

    private static String rutaEspinas(float ancho) {
        if (ancho < 1.5f) return "images/spikes_1.png";
        if (ancho < 3.0f) return "images/spikes_2.png";
        if (ancho < 5.0f) return "images/spikes_3.png";
        return                   "images/spikes_4.png";
    }

    private void crearCuerpoFisico() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        body = mundo.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(ancho / 2f, alto / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape       = shape;
        fixtureDef.density     = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction    = 0.1f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    @Override
    public void actualizar(float delta) {}

    @Override
    public void dibujar(SpriteBatch batch) {
        if (!activo) return;
        
        float xPos = x - ancho / 2f;
        float yPos = y - alto / 2f;
        batch.draw(textura, xPos, yPos, ancho, alto);
    }

    @Override
    public void interactuar() {}

    @Override
    public boolean estaActivo() { return activo; }

    public Body getBody()                { return body; }
    public TipoObstaculo getTipoObstaculo() { return tipoObstaculo; }
}