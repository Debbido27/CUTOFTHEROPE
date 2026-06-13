/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NIVELES;
import Game.Cuerda;
import Game.Estrella;
import Game.Pelota;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;
import com.cutherope.SeleccionNivelScreen;
import java.util.ArrayList;
import java.util.List;


public abstract class NivelBaseScreen implements Screen, ContactListener {

    protected World              mundo;
    protected OrthographicCamera camaraFisica;
    protected Pelota             pelota;
    protected Cuerda             cuerda;
    protected Body               anclaBody;
    protected Vector2            anclaPos;
    protected List<Estrella>     estrellas = new ArrayList<>();

    protected CutTheRope   juego;
    protected String       usuario;
    protected LoginManager gestor;
    protected int          nivel;

    protected Stage      escenario;
    protected Skin       piel;
    protected Texture    bgTexture;
    protected SpriteBatch batch;

    private Vector2 puntoAnterior      = new Vector2();
    private boolean puntoAnteriorValido = false;

    protected final Color VERDE = new Color(0.33f, 0.59f, 0.31f, 1f);
    protected final Color CAFE  = new Color(0.23f, 0.16f, 0.08f, 1f);
    protected final Color ROJO  = new Color(0.70f, 0.27f, 0.20f, 1f);

    public NivelBaseScreen(CutTheRope juego, String usuario, LoginManager gestor, int nivel) {
        this.juego   = juego;
        this.usuario = usuario;
        this.gestor  = gestor;
        this.nivel   = nivel;

        bgTexture = new Texture(Gdx.files.internal(rutaFondo()));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch = new SpriteBatch();

        mundo = new World(new Vector2(0, -10), true);
        mundo.setContactListener(this);
        camaraFisica = new OrthographicCamera();

        crearNivel(); // ← cada subclase implementa esto

        escenario = new Stage(new FitViewport(640, 480));
        piel = crearPiel();
        Gdx.input.setInputProcessor(escenario);
        construirUI();
    }


    protected abstract String rutaFondo();

    protected abstract void crearNivel();

    private void construirUI() {
        Table raiz = new Table();
        raiz.setFillParent(true);
        raiz.top().left();

        TextButton btnVolver = crearBoton("< Volver", ROJO);
        btnVolver.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.postRunnable(() ->
                    juego.setScreen(new SeleccionNivelScreen(juego, usuario, gestor)));
            }
        });
        raiz.add(btnVolver).width(120).height(38).pad(12);
        escenario.addActor(raiz);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mundo.step(delta, 6, 2);

        // fondo
        batch.getProjectionMatrix().setToOrtho2D(0, 0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        batch.draw(bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // física
        batch.setProjectionMatrix(camaraFisica.combined);
        batch.begin();

        if (Gdx.input.isTouched()) {
            Vector3 actual3 = camaraFisica.unproject(
                new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            Vector2 actual = new Vector2(actual3.x, actual3.y);
            if (puntoAnteriorValido) cuerda.revisarCorte(puntoAnterior, actual, mundo);
            puntoAnterior = actual;
            puntoAnteriorValido = true;
        } else {
            puntoAnteriorValido = false;
        }

        cuerda.dibujar(batch);
        Cuerda.dibujarAncla(batch, anclaPos, 0.15f);
        pelota.dibujar(batch);
        for (Estrella e : estrellas) e.dibujar(batch);

        batch.end();

        escenario.getViewport().apply(true);
        escenario.act(delta);
        escenario.draw();
    }

    // ── contactos ───────────────────────────────────────────────────
    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();
        if      (a instanceof Estrella && b instanceof Pelota) ((Estrella) a).interactuar();
        else if (b instanceof Estrella && a instanceof Pelota) ((Estrella) b).interactuar();
    }
    @Override public void endContact(Contact c) {}
    @Override public void preSolve(Contact c, Manifold m) {}
    @Override public void postSolve(Contact c, ContactImpulse i) {}

    // ── helpers ─────────────────────────────────────────────────────
    protected TextButton crearBoton(String texto, Color color) {
        TextButton btn = new TextButton(texto, piel);
        btn.getStyle().up   = piel.newDrawable("blanco", color);
        btn.getStyle().down = piel.newDrawable("blanco", color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        btn.getStyle().over = piel.newDrawable("blanco", color.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        return btn;
    }

    private Skin crearPiel() {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE); pixmap.fill();
        skin.add("blanco", new Texture(pixmap));
        pixmap.dispose();

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/GOODDC__.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float escala = Math.min(Gdx.graphics.getWidth() / 640f, Gdx.graphics.getHeight() / 480f);
        p.size = Math.round(14 * escala);
        p.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuente = gen.generateFont(p);
        fuente.getData().setScale(1f / escala);
        gen.dispose();
        skin.add("fuente-defecto", fuente);

        Label.LabelStyle lStyle = new Label.LabelStyle();
        lStyle.font = fuente; lStyle.fontColor = CAFE;
        skin.add("default", lStyle);

        TextButton.TextButtonStyle bStyle = new TextButton.TextButtonStyle();
        bStyle.font = fuente; bStyle.fontColor = Color.WHITE;
        bStyle.up   = skin.newDrawable("blanco", VERDE);
        bStyle.down = skin.newDrawable("blanco", VERDE.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        bStyle.over = skin.newDrawable("blanco", VERDE.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        skin.add("default", bStyle);
        return skin;
    }

    @Override public void resize(int w, int h) { escenario.getViewport().update(w, h, true); }
    @Override public void show()    { Gdx.input.setInputProcessor(escenario); }
    @Override public void pause()   {}
    @Override public void resume()  {}
    @Override public void hide()    {}
    @Override public void dispose() {
        mundo.dispose(); pelota.dispose();
        Cuerda.disposeTextura(); escenario.dispose();
        piel.dispose(); bgTexture.dispose(); batch.dispose();
    }
}
