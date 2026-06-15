    package Game;

    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.Pixmap;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.physics.box2d.*;

    public class Obstaculo extends ElementoNivel implements Interactuable {
    private boolean activo;
    private Body body;
    private World mundo;
    private TipoObstaculo tipoObstaculo;
    private float restitution;
    private float friction;

    public enum TipoObstaculo {
    CORTO,   
    LARGO   
    }

    public Obstaculo(World mundo, float x, float y, float ancho, float alto, TipoObstaculo tipo) {
    super(x, y, ancho, alto, TipoElemento.ESTRELLA, null); 
    this.mundo = mundo;
    this.tipoObstaculo = tipo;
    this.activo = true;
    this.restitution = 0.5f; 
    this.friction = 0.3f;

    if (tipo == TipoObstaculo.CORTO) {
    this.ancho = ancho;
    this.alto = ancho; 
    }

    this.textura = crearTexturaRedondeada(this.ancho, this.alto, tipo);
    crearCuerpoFisico();
    }

    private void crearCuerpoFisico() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    bodyDef.position.set(x, y);
    body = mundo.createBody(bodyDef);
    body.setUserData(this);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(ancho / 2, alto / 2);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = 1f;
    fixtureDef.restitution = restitution;
    fixtureDef.friction = friction;
    body.createFixture(fixtureDef);

    shape.dispose();
    }

    private Texture crearTexturaRedondeada(float ancho, float alto, TipoObstaculo tipo) {
    int anchoPx = Math.max(1, (int)(ancho * 50));
    int altoPx = Math.max(1, (int)(alto * 50));

    Pixmap pixmap = new Pixmap(anchoPx, altoPx, Pixmap.Format.RGBA8888);

    Color colorBase;
    Color colorBorde;

    if (tipo == TipoObstaculo.CORTO) {
    colorBase = new Color(0.8f, 0.3f, 0.2f, 1f); 
    colorBorde = new Color(0.9f, 0.5f, 0.3f, 1f);
    } else {
    colorBase = new Color(0.3f, 0.4f, 0.7f, 1f); 
    colorBorde = new Color(0.5f, 0.6f, 0.9f, 1f);
    }

    pixmap.setColor(colorBase);
    pixmap.fill();

    int radioBorde = Math.min(anchoPx, altoPx) / 6;
    if(radioBorde < 2) radioBorde = 2;

    pixmap.setColor(colorBorde);

  
    for(int i = 0; i < radioBorde; i++) {
    for(int j = 0; j < radioBorde; j++) {
        if(Math.sqrt(Math.pow(i - radioBorde, 2) + Math.pow(j - radioBorde, 2)) > radioBorde) {
            pixmap.drawPixel(i, j);
            pixmap.drawPixel(anchoPx - 1 - i, j);
            pixmap.drawPixel(i, altoPx - 1 - j);
            pixmap.drawPixel(anchoPx - 1 - i, altoPx - 1 - j);
        }
    }
    }

    pixmap.setColor(colorBorde);
    for(int i = 0; i < anchoPx; i++) {
    pixmap.drawPixel(i, 0);
    pixmap.drawPixel(i, altoPx - 1);
    }
    for(int i = 0; i < altoPx; i++) {
    pixmap.drawPixel(0, i);
    pixmap.drawPixel(anchoPx - 1, i);
    }

    Texture tex = new Texture(pixmap);
    pixmap.dispose();
    return tex;
    }

    @Override
    public void actualizar(float delta) {
    }

    @Override
    public void dibujar(SpriteBatch batch) {
    if (activo) {
    batch.draw(textura, x - ancho/2, y - alto/2, ancho, alto);
    }
    }

    @Override
    public void interactuar() {
    }

    @Override
    public boolean estaActivo() {
    return activo;
    }

    public Body getBody() {
    return body;
    }

    public TipoObstaculo getTipoObstaculo() {
    return tipoObstaculo;
    }

    public void setRebote(float restitution) {
    this.restitution = restitution;
   
    }
    }