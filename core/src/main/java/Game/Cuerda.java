
    package Game;

    import com.badlogic.gdx.Gdx;
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
    private final float largoSegmento;

    private static final float GROSOR = 0.1f;
    private static final int TEX_SIZE = 16;

    public Cuerda(World mundo, Body anclaBody, Vector2 anclaPos, int segmentos,
           float largoSegmento, Body cuerpoFinal, boolean primerSegmento) {

    this.largoSegmento = largoSegmento;

    if (texturaSoga == null) texturaSoga = crearTexturaSoga();

    Vector2 posSegmento = new Vector2(anclaPos.x, anclaPos.y - largoSegmento / 2f);

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set(posSegmento);
    body = mundo.createBody(bodyDef);
    body.setLinearDamping(0.6f);
    body.setAngularDamping(0.8f);

    PolygonShape forma = new PolygonShape();
    forma.setAsBox(0.04f, largoSegmento / 2f);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = forma;
    fixtureDef.density = 0.3f;  
    fixtureDef.friction = 0.1f;
    fixtureDef.filter.maskBits = 0; 
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

    public void dibujar(SpriteBatch batch) {
    Vector2 pos = body.getPosition();
    float angulo = (float) Math.toDegrees(body.getAngle());

    float repeticiones = (largoSegmento * 1.15f) / (GROSOR * 1.5f);
    int srcHeight = Math.max(TEX_SIZE, (int) (TEX_SIZE * repeticiones));

    batch.draw(texturaSoga,
    pos.x - GROSOR / 2f, pos.y - largoSegmento / 2f,
    GROSOR / 2f, largoSegmento / 2f,
    GROSOR, largoSegmento * 1.15f,
    1f, 1f,
    angulo,
    0, 0, TEX_SIZE, srcHeight,
    false, false);

    if (siguiente != null) {
    siguiente.dibujar(batch);
    }
    }

    public boolean revisarCorte(Vector2 p1, Vector2 p2, World mundo) {
    if (joint == null) {
    return siguiente != null && siguiente.revisarCorte(p1, p2, mundo);
    }

    Vector2 pos = body.getPosition();
    float angulo = body.getAngle();
    float mitad = largoSegmento / 2f;
    float dx = (float) Math.sin(angulo) * mitad;
    float dy = (float) Math.cos(angulo) * mitad;

    Vector2 extremoA = new Vector2(pos.x - dx, pos.y - dy);
    Vector2 extremoB = new Vector2(pos.x + dx, pos.y + dy);

    if (segmentosSeCruzan(p1, p2, extremoA, extremoB)
    || distanciaPuntoASegmento(p1,      extremoA, extremoB) < 0.3f
    || distanciaPuntoASegmento(p2,      extremoA, extremoB) < 0.3f
    || distanciaPuntoASegmento(extremoA, p1,      p2)       < 0.3f
    || distanciaPuntoASegmento(extremoB, p1,      p2)       < 0.3f) {
    mundo.destroyJoint(joint);
    joint = null;
    return true;
    }

    if (siguiente != null) {
    return siguiente.revisarCorte(p1, p2, mundo);
    }
    return false;
    }

    private boolean segmentosSeCruzan(Vector2 a1, Vector2 a2, Vector2 b1, Vector2 b2) {
    float d1 = dir(b1, b2, a1);
    float d2 = dir(b1, b2, a2);
    float d3 = dir(a1, a2, b1);
    float d4 = dir(a1, a2, b2);

    if (Math.abs(d1) < 0.001f && puntoEnSegmento(b1, b2, a1)) return true;
    if (Math.abs(d2) < 0.001f && puntoEnSegmento(b1, b2, a2)) return true;
    if (Math.abs(d3) < 0.001f && puntoEnSegmento(a1, a2, b1)) return true;
    if (Math.abs(d4) < 0.001f && puntoEnSegmento(a1, a2, b2)) return true;

    return ((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0))
    && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0));
    }

    private boolean puntoEnSegmento(Vector2 a, Vector2 b, Vector2 p) {
    float minX = Math.min(a.x, b.x);
    float maxX = Math.max(a.x, b.x);
    float minY = Math.min(a.y, b.y);
    float maxY = Math.max(a.y, b.y);
    return p.x >= minX - 0.01f && p.x <= maxX + 0.01f && 
    p.y >= minY - 0.01f && p.y <= maxY + 0.01f;
    }

    private float dir(Vector2 p, Vector2 q, Vector2 r) {
    return (q.x - p.x) * (r.y - p.y) - (q.y - p.y) * (r.x - p.x);
    }



    private static Texture crearTexturaSoga() {
    int s = TEX_SIZE;
    Pixmap pm = new Pixmap(s, s, Pixmap.Format.RGBA8888);

    Color base   = new Color(0.55f, 0.35f, 0.18f, 1f);
    Color oscuro = new Color(0.35f, 0.20f, 0.08f, 1f);
    Color claro  = new Color(0.75f, 0.55f, 0.30f, 1f);

    pm.setColor(base);
    pm.fill();

    pm.setColor(claro);
    pm.fillRectangle(s / 2 - 2, 0, 4, s);

    pm.setColor(oscuro);
    pm.fillRectangle(0, 0, s, 2);
    pm.fillRectangle(0, s / 2, s, 2);

    Texture t = new Texture(pm);
    t.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    pm.dispose();
    return t;
    }

    private static Texture crearTexturaAncla() {
    return new Texture(Gdx.files.internal("images/pin.png"));
    }

    public static void dibujarAncla(SpriteBatch batch, Vector2 pos, float radio) {
    if (texturaAncla == null) texturaAncla = crearTexturaAncla();
    float r = radio * 2f;
    batch.draw(texturaAncla, pos.x - r, pos.y - r, r * 2f, r * 2f);
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

    public void dispose() {
    if (siguiente != null) siguiente.dispose();
    }

    public static void disposeTextura() {
    if (texturaSoga != null) { texturaSoga.dispose(); texturaSoga = null; }
    if (texturaAncla != null) { texturaAncla.dispose(); texturaAncla = null; }
    }

    private boolean mouseTocaEslabon(Vector2 mouse) {

    Vector2 pos = body.getPosition();

    float angulo = body.getAngle();
    float mitad = largoSegmento / 2f;

    float dx = (float) Math.sin(angulo) * mitad;
    float dy = (float) Math.cos(angulo) * mitad;

    Vector2 a = new Vector2(pos.x - dx, pos.y - dy);
    Vector2 b = new Vector2(pos.x + dx, pos.y + dy);

    return distanciaPuntoASegmento(mouse, a, b) < 0.25f;
    }

    private float distanciaPuntoASegmento(Vector2 p, Vector2 a, Vector2 b) {

    Vector2 ab = new Vector2(b).sub(a);
    Vector2 ap = new Vector2(p).sub(a);

    float t = ap.dot(ab) / ab.len2();
    t = Math.max(0f, Math.min(1f, t));

    Vector2 proyeccion = new Vector2(a).mulAdd(ab, t);

    return p.dst(proyeccion);
    }
    }