package NIVELES;

import Game.*;
import LOGIC.RetosManager;
import LOGIC.SesionJuego;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    protected NomNom             nomnom;

    protected List<Cuerda>   cuerdas          = new ArrayList<>();
    protected List<Body>     anclas           = new ArrayList<>();
    protected List<Vector2>  posicionesAnclas = new ArrayList<>();
    protected boolean modoReto          = false;
    protected String  retoRetador       = null;
    protected String  retoRetado        = null;
    protected String  retoJugadorActual = null;
    protected List<Estrella>  estrellas  = new ArrayList<>();
    protected List<Obstaculo> obstaculos = new ArrayList<>();
    protected List<Burbuja>   burbujas   = new ArrayList<>();

    private enum EstadoNivel { JUGANDO, GANANDO, PERDIENDO }
    private EstadoNivel estadoNivel     = EstadoNivel.JUGANDO;
    private float       timerTransicion = 0f;
    protected float     limiteInferior  = -5f;
    private Burbuja burbujaActiva = null;
    protected CutTheRope   juego;
    protected String       usuario;
    protected LoginManager gestor;
    protected int          nivel;

    private boolean pausado = false;
    private Texture pauseBtnTex;
    private Texture playBtnTex;
    private Texture selectLvlBtnTex;
    private Texture overlayBgTex;
    private Table overlayTable;
    private Table overlayVictoriaTable;
    private Table filaEstrellasVictoria;
    private Texture sheetResultado;
    private TextureRegion regStarLlena, regStarVacia;

    protected Stage   escenario;
    protected Skin    piel;
    protected Texture bgTexture;
    protected Texture btnTexture;
    protected Texture btnOverTexture;
    protected SpriteBatch batch;

    private Vector2 puntoAnterior       = new Vector2();
    private boolean puntoAnteriorValido = false;
    private boolean pelotaCayoEnEspina  = false;

    //viewport del mundo fisico — se recalcula cada frame para mantener la proporcion de la camara
    private int vpX, vpY, vpW, vpH;

    protected final Color VERDE = new Color(0.33f, 0.59f, 0.31f, 1f);
    protected final Color CAFE  = new Color(0.23f, 0.16f, 0.08f, 1f);
    protected final Color ROJO  = new Color(0.70f, 0.27f, 0.20f, 1f);

    public NivelBaseScreen(CutTheRope juego, String usuario,
                           LoginManager gestor, int nivel) {
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

        crearNivel();
        SesionJuego.get().iniciarNivel(nivel);
        escenario = new Stage(new FitViewport(640, 480));
        piel = crearPiel();
        sheetResultado = new Texture(Gdx.files.internal("images/menu_result_hd.png"));
        sheetResultado.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        regStarLlena = new TextureRegion(sheetResultado, 4, 0, 133, 129);
        regStarVacia = new TextureRegion(sheetResultado, 163, 13, 121, 116);
        Gdx.input.setInputProcessor(escenario);
    }
    public NivelBaseScreen(CutTheRope juego, String usuario, LoginManager gestor, int nivel,
                           String retoRetador, String retoRetado) {
        this(juego, usuario, gestor, nivel);
        this.modoReto          = true;
        this.retoRetador       = retoRetador;
        this.retoRetado        = retoRetado;
        this.retoJugadorActual = usuario;
        construirUI();

    }


    protected abstract String rutaFondo();
    protected abstract void   crearNivel();

    protected Body crearAncla(float x, float y) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(x, y);
        Body ancla = mundo.createBody(def);
        anclas.add(ancla);
        posicionesAnclas.add(new Vector2(x, y));
        return ancla;
    }

    protected Cuerda crearCuerda(Body ancla, Vector2 posAncla,
                                 int segmentos, float largoSeg) {
        Cuerda c = new Cuerda(mundo, ancla, posAncla,
            segmentos, largoSeg, pelota.getBody(), true);
        cuerdas.add(c);
        return c;
    }

    protected void crearPelota(float x, float y, float radio) {
        pelota = new Pelota(mundo, x, y, radio);
    }

    protected void anclarCuerda(float anclaX, float anclaY,
                                int segmentos, float largoSeg) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(anclaX, anclaY);
        Body anclaBody = mundo.createBody(def);
        posicionesAnclas.add(new Vector2(anclaX, anclaY));
        Cuerda c = new Cuerda(mundo, anclaBody,
            new Vector2(anclaX, anclaY),
            segmentos, largoSeg,
            pelota.getBody(), true);
        cuerdas.add(c);
    }

    protected void colocarNomNom(float x, float y, float radio) {
        nomnom = new NomNom(mundo, x, y, radio);
    }

    protected void construirUI() {
        Table raiz = new Table();
        raiz.setFillParent(true);
        raiz.top().left();

        Pixmap px = new Pixmap(24, 24, Pixmap.Format.RGBA8888);
        px.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        px.fillRectangle(4, 3, 6, 18);
        px.fillRectangle(14, 3, 6, 18);
        pauseBtnTex = new Texture(px);
        px.dispose();
        Image pauseImg = new Image(pauseBtnTex);
        pauseImg.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                pausar();
            }
        });
        raiz.add(pauseImg).size(32, 32).padLeft(8).padTop(8);

        if (modoReto) {
            TextButton btnRendir = crearBoton("Rendirse", ROJO);
            btnRendir.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    finalizarReto(false);
                }
            });
            raiz.add(btnRendir).width(120).height(38).padLeft(8).padTop(4);
        }

        escenario.addActor(raiz);
        construirPauseOverlay();
    }

    private void construirPauseOverlay() {
        playBtnTex = new Texture(Gdx.files.internal("images/play.png"));
        selectLvlBtnTex = new Texture(Gdx.files.internal("images/selectLevel.png"));

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(0, 0, 0, 0.65f);
        px.fill();
        overlayBgTex = new Texture(px);
        px.dispose();

        overlayTable = new Table();
        overlayTable.setFillParent(true);

        Label lblPaused = new Label("PAUSED", piel, "pause-titulo");

        Image imgPlay = new Image(playBtnTex);
        imgPlay.setScaling(com.badlogic.gdx.utils.Scaling.fit);
        imgPlay.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                reanudar();
            }
        });

        Image imgSelect = new Image(selectLvlBtnTex);
        imgSelect.setScaling(com.badlogic.gdx.utils.Scaling.fit);
        imgSelect.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                irASeleccionNiveles();
            }
        });

        Table contenido = new Table();
        contenido.add(lblPaused).padBottom(25).row();
        contenido.add(imgPlay).width(160).height(65).padBottom(8).row();
        contenido.add(imgSelect).width(160).height(65).row();
        overlayTable.add(contenido).expand().center();

        overlayTable.setVisible(false);
        escenario.addActor(overlayTable);
        construirOverlayVictoria();
    }

    private void construirOverlayVictoria() {
        overlayVictoriaTable = new Table();
        overlayVictoriaTable.setFillParent(true);

        Label lblTitulo = new Label("NIVEL COMPLETADO", piel, "pause-titulo");
        Label lblNivel  = new Label("Nivel " + nivel, piel, "titulo");

        filaEstrellasVictoria = new Table();

        Image imgPlay = new Image(playBtnTex);
        imgPlay.setScaling(com.badlogic.gdx.utils.Scaling.fit);
        imgPlay.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                overlayVictoriaTable.setVisible(false);
                irAlSiguienteNivel();
            }
        });

        Image imgSelect = new Image(selectLvlBtnTex);
        imgSelect.setScaling(com.badlogic.gdx.utils.Scaling.fit);
        imgSelect.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                overlayVictoriaTable.setVisible(false);
                irASeleccionNiveles();
            }
        });

        Table contenido = new Table();
        contenido.add(lblTitulo).padBottom(12).row();
        contenido.add(lblNivel).padBottom(10).row();
        contenido.add(filaEstrellasVictoria).padBottom(20).row();
        contenido.add(imgPlay).width(160).height(65).padBottom(8).row();
        contenido.add(imgSelect).width(160).height(65).row();
        overlayVictoriaTable.add(contenido).expand().center();

        overlayVictoriaTable.setVisible(false);
        escenario.addActor(overlayVictoriaTable);
    }

    private void pausar() {
        pausado = true;
        overlayTable.setVisible(true);
    }

    private void reanudar() {
        pausado = false;
        overlayTable.setVisible(false);
    }

    private void irASeleccionNiveles() {
        pausado = false;
        if (modoReto) {
            finalizarReto(false);
        } else {
            SesionJuego.get().finalizarNivel(false, false);
            Gdx.app.postRunnable(() ->
                juego.setScreen(new SeleccionNivelScreen(juego, usuario, gestor)));
        }
    }

    //calcula el viewport que mantiene la proporcion de la camara sin estirar
    private void recalcularViewportFisico() {
        if (camaraFisica.viewportWidth == 0 || camaraFisica.viewportHeight == 0) return;
        float camAspect    = camaraFisica.viewportWidth / camaraFisica.viewportHeight;
        int   screenW      = Gdx.graphics.getWidth();
        int   screenH      = Gdx.graphics.getHeight();
        float screenAspect = (float) screenW / screenH;
        if (screenAspect > camAspect) {
            vpH = screenH;
            vpW = (int) (screenH * camAspect);
        } else {
            vpW = screenW;
            vpH = (int) (screenW / camAspect);
        }
        vpX = (screenW - vpW) / 2;
        vpY = (screenH - vpH) / 2;
    }

    @Override
    public void render(float delta) {
        recalcularViewportFisico();
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (estadoNivel) {
            case JUGANDO:
                if (!pausado) {
                    mundo.step(delta, 12, 8);
                    if (burbujaActiva != null) burbujaActiva.entrar(pelota);
                    if (nomnom != null) {
                        if (pelota != null) {
                            Vector2 pp = pelota.getBody().getPosition();
                            Vector2 np = nomnom.getBody().getPosition();
                            nomnom.setPelotaCerca(pp.dst2(np) < 6.25f);
                        }
                        nomnom.actualizar(delta);
                        if (nomnom.comioLaPelota()) {
                            if (burbujaActiva != null) {
                                burbujaActiva.explotar(pelota);
                                burbujaActiva = null;
                            }
                            pelota.ocultar();
                            estadoNivel     = EstadoNivel.GANANDO;
                            timerTransicion = 1.8f;
                        }
                    }

                    if (pelotaCayoEnEspina && pelota != null && !pelota.estaRota()) {
                        pelotaCayoEnEspina = false;
                        SesionJuego.get().registrarFallo();
                        pelota.romper();
                        if (nomnom != null) nomnom.ponerTriste();
                        estadoNivel     = EstadoNivel.PERDIENDO;
                        timerTransicion = 2.0f;
                    } else if (pelotaCayoFuera()) {
                        SesionJuego.get().registrarFallo();
                        estadoNivel     = EstadoNivel.PERDIENDO;
                        timerTransicion = 1.0f;
                        if (nomnom != null) nomnom.ponerTriste();
                    }

                    procesarCorte();
                    procesarToqueBurbujas();
                }
                break;

            case GANANDO:
                mundo.step(delta, 6, 2);
                if (nomnom != null) nomnom.actualizar(delta);
                filaEstrellasVictoria.clearChildren();
                {
                    int estrellas = SesionJuego.get().getEstrellasNivel();
                    for (int i = 0; i < 3; i++) {
                        Image star = new Image(new TextureRegionDrawable(
                            i < estrellas ? regStarLlena : regStarVacia));
                        filaEstrellasVictoria.add(star).size(44, 44).pad(6);
                    }
                }
                overlayVictoriaTable.setVisible(true);
                break;

            case PERDIENDO:
                if (pelota != null) pelota.actualizar(delta);
                if (nomnom != null) nomnom.actualizar(delta);
                timerTransicion -= delta;
                if (timerTransicion <= 0) {
                    timerTransicion = Float.MAX_VALUE;
                    reiniciarNivel();
                }
                break;
        }

        //fondo en pantalla completa
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.getProjectionMatrix().setToOrtho2D(
            0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        batch.draw(bgTexture, 0, 0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if (!pausado) {
            for (Estrella e : estrellas) e.actualizar(delta);
            for (Burbuja  b : burbujas)  b.actualizar(delta);
        }

        //objetos fisicos con viewport que respeta la proporcion de la camara
        Gdx.gl.glViewport(vpX, vpY, vpW, vpH);
        batch.setProjectionMatrix(camaraFisica.combined);
        batch.begin();

        for (Cuerda c : cuerdas)          c.dibujar(batch);
        for (Vector2 pos : posicionesAnclas) Cuerda.dibujarAncla(batch, pos, 0.15f);

        pelota.dibujar(batch);
        if (nomnom != null) nomnom.dibujar(batch);

        for (Estrella  e : estrellas)  e.dibujar(batch);
        for (Obstaculo o : obstaculos) o.dibujar(batch);
        for (Burbuja   b : burbujas)   b.dibujar(batch);

        //banners removed — replaced by overlays

        batch.end();

        if (pausado || overlayVictoriaTable.isVisible()) {
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            batch.getProjectionMatrix().setToOrtho2D(
                0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.begin();
            batch.setColor(0, 0, 0, 0.65f);
            batch.draw(overlayBgTex, 0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
            batch.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        escenario.getViewport().apply(true);
        escenario.act(delta);
        escenario.draw();
    }

    //convierte coordenadas de pantalla a coordenadas del mundo fisico
    private Vector2 desdePixel(int x, int y) {
        Vector3 raw = new Vector3(x, y, 0);
        camaraFisica.unproject(raw, vpX, vpY, vpW, vpH);
        return new Vector2(raw.x, raw.y);
    }

    private void procesarToqueBurbujas() {
        if (!Gdx.input.justTouched()) return;
        Vector2 pos = desdePixel(Gdx.input.getX(), Gdx.input.getY());
        for (Burbuja b : burbujas) {
            if (b.revisarToque(pos.x, pos.y, pelota)) break;
        }
    }

    private boolean pelotaCayoFuera() {
        if (pelota == null || pelota.estaRota()) return false;
        Vector2 pos = pelota.getBody().getPosition();
        return pos.y < limiteInferior || pos.x < -5f || pos.x > 25f;
    }

    private void procesarCorte() {
        if (Gdx.input.isTouched()) {
            Vector2 actual = desdePixel(Gdx.input.getX(), Gdx.input.getY());
            if (puntoAnteriorValido && actual.dst2(puntoAnterior) > 0.01f) {
                for (Cuerda c : cuerdas) {
                    c.revisarCorte(puntoAnterior, actual, mundo);
                }
            }
            puntoAnterior.set(actual);
            puntoAnteriorValido = true;
        } else {
            puntoAnteriorValido = false;
        }
    }

    private void irAlSiguienteNivel() {
        if (modoReto) {
            finalizarReto(true);
            return;
        }
        final int sig = nivel + 1;
        SesionJuego.get().finalizarNivel(true, false);
        Gdx.app.postRunnable(() -> juego.setScreen(crearPantallaNivel(sig)));
    }

    private void reiniciarNivel() {
        if (modoReto) {
            finalizarReto(false);
            return;
        }
        final int n = nivel;
        Gdx.app.postRunnable(() -> juego.setScreen(crearPantallaNivel(n)));
    }

    private Screen crearPantallaNivel(int n) {
        switch (n) {
            case 1:  return new Nivel1Screen(juego, usuario, gestor);
            case 2:  return new Nivel2Screen(juego, usuario, gestor);
            case 3:  return new Nivel3Screen(juego, usuario, gestor);
            case 4:  return new Nivel4Screen(juego, usuario, gestor);
            case 5:  return new Nivel5Screen(juego, usuario, gestor);
            default: return new SeleccionNivelScreen(juego, usuario, gestor);
        }
    }

    private void dibujarBannerTexto(String texto, Color color) {
        float cx = camaraFisica.position.x;
        float cy = camaraFisica.position.y;
        float w  = camaraFisica.viewportWidth;
        float h  = 2.2f;

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(color.r, color.g, color.b, 0.65f);
        px.fill();
        Texture overlay = new Texture(px);
        px.dispose();

        batch.setColor(1, 1, 1, 1);
        batch.draw(overlay, cx - w / 2f, cy - h / 2f, w, h);
        overlay.dispose();
    }
    void finalizarReto(boolean gano) {
        long tiempoMs = SesionJuego.get().getTiempoTranscurridoMs();
        int  segundos = (int) (tiempoMs / 1000);
        int  estrellas = SesionJuego.get().getEstrellasNivel();

        int puntaje = gano ? (1000 + (200 * estrellas) - (5 * segundos)) : 0;
        if (puntaje < 0) puntaje = 0;

        SesionJuego.get().finalizarNivel(gano, true);

        new RetosManager(gestor).registrarResultadoJugador(
            retoRetador, retoRetado, nivel, retoJugadorActual,
            puntaje, estrellas, segundos);

        Gdx.app.postRunnable(() ->
            juego.setScreen(new com.cutherope.AmigosScreen(juego, usuario, gestor)));
    }

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        if (a instanceof Burbuja && b instanceof Pelota) burbujaActiva = (Burbuja) a;
        else if (b instanceof Burbuja && a instanceof Pelota) burbujaActiva = (Burbuja) b;
        if (a instanceof NomNom  && b instanceof Pelota) ((NomNom)  a).interactuar();
        else if (b instanceof NomNom  && a instanceof Pelota) ((NomNom)  b).interactuar();

        if (a instanceof Estrella && b instanceof Pelota) {
            Estrella est = (Estrella) a;
            if (!est.yaFueContada()) {          // ← AÑADIR
                SesionJuego.get().registrarEstrella();
            }
            est.interactuar();
        } else if (b instanceof Estrella && a instanceof Pelota) {
            Estrella est = (Estrella) b;
            if (!est.yaFueContada()) {          // ← AÑADIR
                SesionJuego.get().registrarEstrella();
            }
            est.interactuar();
        }
        if ((a instanceof Obstaculo && b instanceof Pelota)
                || (b instanceof Obstaculo && a instanceof Pelota)) {
            pelotaCayoEnEspina = true;
        }
    }


    @Override
    public void endContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        if (a instanceof Burbuja && b instanceof Pelota) { burbujaActiva = null; ((Burbuja) a).salir(pelota); }
        else if (b instanceof Burbuja && a instanceof Pelota) { burbujaActiva = null; ((Burbuja) b).salir(pelota); }
    }

    @Override public void preSolve(Contact c, Manifold m) {}
    @Override public void postSolve(Contact c, ContactImpulse i) {}


    protected TextButton crearBoton(String texto, Color color) {
        // boton con estilo propio para evitar compartir estado
        TextButton.TextButtonStyle estilo = new TextButton.TextButtonStyle(piel.get(TextButton.TextButtonStyle.class));
        estilo.up   = piel.newDrawable("btn-up", color);
        estilo.down = piel.newDrawable("btn-up", color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        estilo.over = piel.newDrawable("btn-over", color.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        TextButton btn = new TextButton(texto, estilo);
        btn.pad(5, 15, 5, 15);
        return btn;
    }

    private Skin crearPiel() {
        Skin skin = new Skin();
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(Color.WHITE); px.fill();
        skin.add("blanco", new Texture(px));
        px.dispose();

        // texturas del boton como ninepatch para evitar estiramiento
        btnTexture = new Texture(Gdx.files.internal("images/button.png"));
        skin.add("btn-up", new com.badlogic.gdx.graphics.g2d.NinePatch(btnTexture, 35, 35, 20, 20));
        btnOverTexture = new Texture(Gdx.files.internal("images/button_over.png"));
        skin.add("btn-over", new com.badlogic.gdx.graphics.g2d.NinePatch(btnOverTexture, 35, 35, 20, 20));

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/GOODDC__.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter p =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        float escala = Math.min(
            Gdx.graphics.getWidth()  / 640f,
            Gdx.graphics.getHeight() / 480f);
        p.size = Math.round(14 * escala);
        p.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuente = gen.generateFont(p);
        fuente.getData().setScale(1f / escala);

        FreeTypeFontGenerator.FreeTypeFontParameter pPausa =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        pPausa.size = Math.round(48 * escala);
        pPausa.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuentePausa = gen.generateFont(pPausa);
        fuentePausa.getData().setScale(1f / escala);

        gen.dispose();
        skin.add("fuente-defecto", fuente);

        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = fuente; ls.fontColor = CAFE;
        skin.add("default", ls);

        Label.LabelStyle lsTitulo = new Label.LabelStyle();
        lsTitulo.font = fuente; lsTitulo.fontColor = CAFE;
        skin.add("titulo", lsTitulo);

        Label.LabelStyle lsPausa = new Label.LabelStyle();
        lsPausa.font = fuentePausa; lsPausa.fontColor = Color.WHITE;
        skin.add("pause-titulo", lsPausa);

        TextButton.TextButtonStyle bs = new TextButton.TextButtonStyle();
        bs.font = fuente; bs.fontColor = Color.WHITE;
        bs.up   = skin.newDrawable("blanco", VERDE);
        bs.down = skin.newDrawable("blanco", VERDE.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        bs.over = skin.newDrawable("blanco", VERDE.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        skin.add("default", bs);
        return skin;
    }

    @Override public void resize(int w, int h) { escenario.getViewport().update(w, h, true); }
    @Override public void show()   { Gdx.input.setInputProcessor(escenario); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        mundo.dispose();
        if (pelota != null) pelota.dispose();
        if (nomnom != null) nomnom.dispose();
        Cuerda.disposeTextura();
        Burbuja.disposeTexturas();
        for (Cuerda    c : cuerdas)    c.dispose();
        for (Estrella  e : estrellas)  e.dispose();
        for (Obstaculo o : obstaculos) o.dispose();
        for (Burbuja   b : burbujas)   b.dispose();
        escenario.dispose();
        piel.dispose();
        bgTexture.dispose();
        if (btnTexture != null) btnTexture.dispose();
        if (btnOverTexture != null) btnOverTexture.dispose();
        if (pauseBtnTex != null) pauseBtnTex.dispose();
        if (playBtnTex != null) playBtnTex.dispose();
        if (selectLvlBtnTex != null) selectLvlBtnTex.dispose();
        if (overlayBgTex != null) overlayBgTex.dispose();
        if (sheetResultado != null) sheetResultado.dispose();
        batch.dispose();
    }
}
