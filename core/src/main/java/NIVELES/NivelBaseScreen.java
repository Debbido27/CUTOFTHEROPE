        package NIVELES;

        import Game.*;
        import LOGIC.SesionJuego;
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
        protected NomNom             nomnom;

        protected List<Cuerda>   cuerdas          = new ArrayList<>();
        protected List<Body>     anclas           = new ArrayList<>();
        protected List<Vector2>  posicionesAnclas = new ArrayList<>();

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

        protected Stage   escenario;
        protected Skin    piel;
        protected Texture bgTexture;
        protected SpriteBatch batch;

        private Vector2 puntoAnterior       = new Vector2();
        private boolean puntoAnteriorValido = false;

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
        Gdx.input.setInputProcessor(escenario);
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

        switch (estadoNivel) {
        case JUGANDO:
            mundo.step(delta, 12, 8);
        if (burbujaActiva != null) burbujaActiva.entrar(pelota);
            if (nomnom != null) {
                nomnom.actualizar(delta);
                if (nomnom.comioLaPelota()) {
                    estadoNivel     = EstadoNivel.GANANDO;
                    timerTransicion = 1.8f;
                }
            }

            if (pelotaCayoFuera()) {
                SesionJuego.get().registrarFallo();
                estadoNivel     = EstadoNivel.PERDIENDO;
                timerTransicion = 1.0f;
            }

            procesarCorte();


            procesarToqueBurbujas();

            break;

        case GANANDO:
            mundo.step(delta, 6, 2);
            if (nomnom != null) nomnom.actualizar(delta);
            timerTransicion -= delta;
            if (timerTransicion <= 0) irAlSiguienteNivel();
            break;

        case PERDIENDO:
            timerTransicion -= delta;
            if (timerTransicion <= 0) reiniciarNivel();
            break;
        }

        batch.getProjectionMatrix().setToOrtho2D(
        0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        batch.draw(bgTexture, 0, 0,
        Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();


        batch.setProjectionMatrix(camaraFisica.combined);
        batch.begin();

        for (Cuerda c : cuerdas)          c.dibujar(batch);
        for (Vector2 pos : posicionesAnclas) Cuerda.dibujarAncla(batch, pos, 0.15f);

        pelota.dibujar(batch);
        if (nomnom != null) nomnom.dibujar(batch);

        for (Estrella  e : estrellas)  e.dibujar(batch);
        for (Obstaculo o : obstaculos) o.dibujar(batch);
        for (Burbuja   b : burbujas)   b.dibujar(batch);

        if (estadoNivel == EstadoNivel.GANANDO)
        dibujarBannerTexto("¡NomNom comió!", VERDE);
        if (estadoNivel == EstadoNivel.PERDIENDO)
        dibujarBannerTexto("¡Inténtalo de nuevo!", ROJO);

        batch.end();

        escenario.getViewport().apply(true);
        escenario.act(delta);
        escenario.draw();
        }

        private void procesarToqueBurbujas() {
        if (!Gdx.input.justTouched()) return;

        Vector3 raw = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camaraFisica.unproject(raw);

        for (Burbuja b : burbujas) {
        if (b.revisarToque(raw.x, raw.y, pelota)) {
            break;
        }
        }
        }

        private boolean pelotaCayoFuera() {
        if (pelota == null) return false;
        Vector2 pos = pelota.getBody().getPosition();
        return pos.y < limiteInferior || pos.x < -5f || pos.x > 25f;
        }

        private void procesarCorte() {
        if (Gdx.input.isTouched()) {
        Vector3 raw = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camaraFisica.unproject(raw);
        Vector2 actual = new Vector2(raw.x, raw.y);
        if (puntoAnteriorValido) {
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
        final int sig = nivel + 1;
        SesionJuego.get().finalizarNivel(true);
        Gdx.app.postRunnable(() -> juego.setScreen(crearPantallaNivel(sig)));
        }

        private void reiniciarNivel() {
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

        @Override
        public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        if (a instanceof Burbuja && b instanceof Pelota) burbujaActiva = (Burbuja) a;
        else if (b instanceof Burbuja && a instanceof Pelota) burbujaActiva = (Burbuja) b;
        if (a instanceof NomNom  && b instanceof Pelota) ((NomNom)  a).interactuar();
        else if (b instanceof NomNom  && a instanceof Pelota) ((NomNom)  b).interactuar();

            if (a instanceof Estrella && b instanceof Pelota) {
                SesionJuego.get().registrarEstrella();
                ((Estrella) a).interactuar();
            } else if (b instanceof Estrella && a instanceof Pelota) {
                SesionJuego.get().registrarEstrella();
                ((Estrella) b).interactuar();
            }
        if (a instanceof Obstaculo && b instanceof Pelota) ((Obstaculo) a).interactuar();
        else if (b instanceof Obstaculo && a instanceof Pelota) ((Obstaculo) b).interactuar();
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
        TextButton btn = new TextButton(texto, piel);
        btn.getStyle().up   = piel.newDrawable("blanco", color);
        btn.getStyle().down = piel.newDrawable("blanco", color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        btn.getStyle().over = piel.newDrawable("blanco", color.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        return btn;
        }

        private Skin crearPiel() {
        Skin skin = new Skin();
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(Color.WHITE); px.fill();
        skin.add("blanco", new Texture(px));
        px.dispose();

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
        gen.dispose();
        skin.add("fuente-defecto", fuente);

        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = fuente; ls.fontColor = CAFE;
        skin.add("default", ls);

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
        for (Cuerda    c : cuerdas)    c.dispose();
        for (Obstaculo o : obstaculos) o.dispose();
        for (Burbuja   b : burbujas)   b.dispose();
        escenario.dispose();
        piel.dispose();
        bgTexture.dispose();
        batch.dispose();
        }
        }
