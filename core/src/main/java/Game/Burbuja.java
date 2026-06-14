package Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Burbuja extends ElementoNivel implements Interactuable {
    private boolean activa;
    private float fuerzaElevacion;
    private Body body;
    private World mundo;
    private float radio;
    private boolean pelotaDentro = false;
    public Burbuja(World mundo, float x, float y, float radio, float fuerzaElevacion) {
        super(x, y, radio * 2, radio * 2, TipoElemento.BURBUJA, crearTexturaBurbuja(radio));
        this.mundo = mundo;
        this.radio = radio;
        this.fuerzaElevacion = fuerzaElevacion;
        this.activa = true;
        crearCuerpoFisico();
    }
    
    private void crearCuerpoFisico() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        body = mundo.createBody(bodyDef);
        body.setUserData(this);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(radio);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true; 
// Sensor para solo detectar colisión sin rebote
fixtureDef.filter.categoryBits = 0x0002; // BURBUJA
fixtureDef.filter.maskBits = 0x0001;      // SOLO PELOTA
        body.createFixture(fixtureDef);
        
        shape.dispose();
    }
    
    private static Texture crearTexturaBurbuja(float radio) {
        int diametro = (int)(radio * 100);
        if(diametro < 4) diametro = 4;
        
        Pixmap pixmap = new Pixmap(diametro, diametro, Pixmap.Format.RGBA8888);
        
        // Fondo transparente
        pixmap.setColor(new Color(0, 0, 0, 0));
        pixmap.fill();
        
        // Círculo semitransparente
        pixmap.setColor(new Color(0.6f, 0.8f, 1f, 0.7f));
        pixmap.fillCircle(diametro / 2, diametro / 2, diametro / 2);
        
        // Brillo (reflejo)
        pixmap.setColor(new Color(1, 1, 1, 0.8f));
        int brilloX = diametro / 3;
        int brilloY = diametro / 3;
        int brilloRadio = diametro / 6;
        if(brilloRadio < 1) brilloRadio = 1;
        pixmap.fillCircle(brilloX, brilloY, brilloRadio);
        
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }
    
  
    
    @Override
    public void dibujar(SpriteBatch batch) {
        if (activa) {
            batch.draw(textura, x - radio, y - radio, ancho, alto);
        }
    }
    
    @Override
    public void interactuar() {
        if (activa) {
            activa = false;
            // Opcional: efecto visual o sonido aquí
        }
    }
    
    @Override
    public boolean estaActivo() {
        return activa;
    }
    
    public float getFuerzaElevacion() {
        return fuerzaElevacion;
    }
    
    public Body getBody() {
        return body;
    }
    
    public void aplicarElevacion(Pelota pelota) {
        if (activa) {
            Body cuerpoPelota = pelota.getBody();
            cuerpoPelota.setLinearVelocity(cuerpoPelota.getLinearVelocity().x, 0);
            cuerpoPelota.applyLinearImpulse(0, fuerzaElevacion, 
                cuerpoPelota.getPosition().x, cuerpoPelota.getPosition().y, true);
            interactuar(); // La burbuja desaparece
        }
    }
    public void entrar() {
    pelotaDentro = true;
}

public void salir() {
    pelotaDentro = false;
}

public void aplicarFlotacion(Pelota pelota) {
    if (pelotaDentro && activa) {
        Body b = pelota.getBody();

        b.applyForceToCenter(0, fuerzaElevacion, true);
    }
}
@Override
public void actualizar(float delta) {
    // flotación continua
}
}