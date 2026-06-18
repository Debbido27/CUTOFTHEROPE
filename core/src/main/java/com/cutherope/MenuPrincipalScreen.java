        package com.cutherope;

        import LOGIC.Idioma;
        import LOGIC.USER;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Screen;
        import com.badlogic.gdx.graphics.Color;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.Pixmap;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.scenes.scene2d.InputEvent;
        import com.badlogic.gdx.scenes.scene2d.Stage;
        import com.badlogic.gdx.scenes.scene2d.ui.*;
        import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
        import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
        import com.badlogic.gdx.utils.viewport.FitViewport;
        import LOGIC.LoginManager;

        public class MenuPrincipalScreen implements Screen {

        private CutTheRope   juego;
        private String       usuario;
        private LoginManager gestor;
        private Stage        escenario;
        private Skin         piel;
        private Texture      logoTexture;
        private Texture      bgTexture;
        private SpriteBatch  batch;

        private final Color FONDO   = new Color(0.96f, 0.92f, 0.82f, 1f);
        private final Color VERDE   = new Color(0.33f, 0.59f, 0.31f, 1f);
        private final Color CAFE    = new Color(0.23f, 0.16f, 0.08f, 1f);
        private final Color NARANJA = new Color(0.78f, 0.63f, 0.31f, 1f);
        private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);

        public MenuPrincipalScreen(CutTheRope juego, String usuario, LoginManager gestor) {
        this.juego    = juego;
        this.usuario  = usuario;
        this.gestor   = gestor;
        this.escenario = new Stage(new FitViewport(640, 480));
        this.piel      = crearPiel();
        this.logoTexture = new Texture(Gdx.files.internal("images/Cut_the_Rope_Logo.png"));
        bgTexture = new Texture(Gdx.files.internal("images/mainmenu.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(escenario);
        construirMenu();
        }


        private void construirMenu() {
        escenario.clear();

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Image logo = new Image(logoTexture);

        USER u = gestor.buscarUser(usuario);
        if (u != null) Idioma.setIngles(u.isIngles());


        Label bienvenida = new Label(Idioma.get(Idioma.Clave.BIENVENIDO) + ", " + usuario + "!", piel);

        TextButton btnJugar        = crearBoton(Idioma.get(Idioma.Clave.JUGAR),         VERDE);
        TextButton btnEstadisticas = crearBoton(Idioma.get(Idioma.Clave.ESTADISTICAS),  NARANJA);
        TextButton btnRanking      = crearBoton(Idioma.get(Idioma.Clave.RANKING),       NARANJA);
        TextButton btnAmigos       = crearBoton(Idioma.get(Idioma.Clave.AMIGOS),        NARANJA);
        TextButton btnAjustes      = crearBoton(Idioma.get(Idioma.Clave.AJUSTES),       NARANJA);
        TextButton btnCerrarSesion = crearBoton(Idioma.get(Idioma.Clave.CERRAR_SESION), ROJO);
        btnJugar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        Gdx.app.postRunnable(() ->
        juego.setScreen(new SeleccionNivelScreen(juego, usuario, gestor)));
        }
        });
            btnEstadisticas.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    Gdx.app.postRunnable(() ->
                        juego.setScreen(new Estadisticas(juego, usuario, gestor)));
                }
            });


            btnRanking.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    Gdx.app.postRunnable(() ->
                        juego.setScreen(new RankingScreen(juego, usuario, gestor)));
                }
            });
        btnAmigos.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        Gdx.app.postRunnable(() ->
        juego.setScreen(new AmigosScreen(juego, usuario, gestor)));
        }
        });

        btnAjustes.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        Gdx.app.postRunnable(() -> juego.setScreen(new AjustesScreen(juego, usuario, gestor)));
        }
        });
        btnCerrarSesion.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        Gdx.app.postRunnable(() -> juego.setScreen(new LoginScreen(juego)));
        }
        });

        tabla.add(logo).width(300).height(110).padBottom(4).row();
        tabla.add(bienvenida).padBottom(20).row();
        tabla.add(btnJugar).width(260).height(38).padBottom(8).row();
        tabla.add(btnEstadisticas).width(260).height(38).padBottom(8).row();
        tabla.add(btnRanking).width(260).height(38).padBottom(8).row();
        tabla.add(btnAmigos).width(260).height(38).padBottom(8).row();
        tabla.add(btnAjustes).width(260).height(38).padBottom(8).row();
        tabla.add(btnCerrarSesion).width(260).height(38).row();

        escenario.addActor(tabla);
        }


        private TextButton crearBoton(String texto, Color color) {
        TextButton btn = new TextButton(texto, piel);
        btn.getStyle().up   = piel.newDrawable("blanco", color);
        btn.getStyle().down = piel.newDrawable("blanco", color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        btn.getStyle().over = piel.newDrawable("blanco", color.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        return btn;
        }


        private Skin crearPiel() {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("blanco", new Texture(pixmap));
        pixmap.dispose();

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GOODDC__.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float escala = Math.min(Gdx.graphics.getWidth() / 640f, Gdx.graphics.getHeight() / 480f);
        param.size = Math.round(14 * escala);
        param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuente = gen.generateFont(param);
        fuente.getData().setScale(1f / escala);
        skin.add("fuente-defecto", fuente);

        FreeTypeFontGenerator.FreeTypeFontParameter paramTitulo = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramTitulo.size = Math.round(50 * escala);
        paramTitulo.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuenteTitulo = gen.generateFont(paramTitulo);
        fuenteTitulo.getData().setScale(1f / escala);
        gen.dispose();

        skin.add("font-titulo", fuenteTitulo);

        Label.LabelStyle estiloTitulo = new Label.LabelStyle();
        estiloTitulo.font = fuenteTitulo;
        estiloTitulo.fontColor = CAFE;
        skin.add("titulo", estiloTitulo);


        Label.LabelStyle estiloLabel = new Label.LabelStyle();
        estiloLabel.font      = fuente;
        estiloLabel.fontColor = CAFE;
        skin.add("default", estiloLabel);

        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.font      = fuente;
        estiloBoton.fontColor = Color.WHITE;
        estiloBoton.up        = skin.newDrawable("blanco", VERDE);
        estiloBoton.down      = skin.newDrawable("blanco", VERDE.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        estiloBoton.over      = skin.newDrawable("blanco", VERDE.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        skin.add("default", estiloBoton);

        return skin;
        }


        @Override
        public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        batch.draw(bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        escenario.getViewport().apply(true);
        escenario.act(delta);
        escenario.draw();
        }

        @Override public void resize(int w, int h) { escenario.getViewport().update(w, h, true); }
        @Override public void show()    { Gdx.input.setInputProcessor(escenario); }
        @Override public void pause()   {}
        @Override public void resume()  {}
        @Override public void hide()    {}
        @Override public void dispose() { escenario.dispose(); piel.dispose(); logoTexture.dispose(); bgTexture.dispose(); batch.dispose(); }
        }
