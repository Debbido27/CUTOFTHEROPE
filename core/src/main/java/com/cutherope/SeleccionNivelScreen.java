        package com.cutherope;

        import LOGIC.Idioma;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Screen;
        import com.badlogic.gdx.graphics.Color;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.Pixmap;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
        import com.badlogic.gdx.graphics.g2d.TextureRegion;
        import com.badlogic.gdx.scenes.scene2d.InputEvent;
        import com.badlogic.gdx.scenes.scene2d.Stage;
        import com.badlogic.gdx.scenes.scene2d.ui.*;
        import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
        import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
        import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
        import com.badlogic.gdx.utils.viewport.FitViewport;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import LOGIC.LoginManager;
        import LOGIC.USER;

        public class SeleccionNivelScreen implements Screen {

        private CutTheRope   juego;
        private String       usuario;
        private LoginManager gestor;
        private Stage        escenario;
        private Skin         piel;
        private Texture      sheetNiveles;
        private Texture      sheetResultado;
        private Texture      bgTexture;
        private SpriteBatch  batch;

        private static final int[] R_BLOQUEADO   = {  0,   0, 128, 128 };
        private static final int[] R_DESBLOQUEAD = {  0, 130, 128, 128 };

        private static final int[] R_STAR_LLENA  = {   0,  0, 137, 129 };
        private static final int[] R_STAR_VACIA  = { 163, 13, 121, 116 };

        private final Color FONDO   = new Color(0.96f, 0.92f, 0.82f, 1f);
        private final Color VERDE   = new Color(0.33f, 0.59f, 0.31f, 1f);
        private final Color CAFE    = new Color(0.23f, 0.16f, 0.08f, 1f);
        private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);

        public SeleccionNivelScreen(CutTheRope juego, String usuario, LoginManager gestor) {
        this.juego   = juego;
        this.usuario = usuario;
        this.gestor  = gestor;

        bgTexture = new Texture(Gdx.files.internal("images/mainmenu.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch     = new SpriteBatch();
        sheetNiveles    = new Texture(Gdx.files.internal("images/menu_level_selection_hd.png"));
        sheetNiveles.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sheetResultado  = new Texture(Gdx.files.internal("images/menu_result_hd.png"));
        sheetResultado.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        escenario = new Stage(new FitViewport(640, 480));
        piel      = crearPiel();
        Gdx.input.setInputProcessor(escenario);
        construir();
        }

        private void construir() {
        USER user = gestor.buscarUser(usuario);
        boolean[] desbloqueados = (user != null)
        ? user.getNivelesDesbloqueados()
        : new boolean[]{true, false, false, false, false};
        int[] puntajes = (user != null) ? user.getPuntajesPorNivel() : new int[5];
        int[] estrellasPorNivel = (user != null) ? user.getEstrellasPorNivel() : new int[5];
        TextureRegion regBloqueado   = region(sheetNiveles,   R_BLOQUEADO);
        TextureRegion regDesbloquead = region(sheetNiveles,   R_DESBLOQUEAD);
        TextureRegion regStarLlena   = region(sheetResultado, R_STAR_LLENA);
        TextureRegion regStarVacia   = region(sheetResultado, R_STAR_VACIA);

        Table raiz = new Table();
        raiz.setFillParent(true);
        raiz.center();

        Table filaNiveles = new Table();

        for (int i = 0; i < 5; i++) {
        final int nivel = i;
        boolean bloqueado = !desbloqueados[i];
        int estrellas = estrellasPorNivel[i];
        Table celda = new Table();
        celda.center();

            Label numLabel = new Label(Idioma.get(Idioma.Clave.NIVEL) + " " + (i + 1), piel);        numLabel.setColor(bloqueado ? Color.GRAY : CAFE);

        Image icono = new Image(new TextureRegionDrawable(
            bloqueado ? regBloqueado : regDesbloquead));

        Table filaEstrellas = new Table();
        for (int s = 0; s < 3; s++) {
            Image star = new Image(new TextureRegionDrawable(
                s < estrellas ? regStarLlena : regStarVacia));
            filaEstrellas.add(star).size(22, 22).pad(2);
        }

        celda.add(numLabel).padBottom(4).row();
        celda.add(icono).size(90, 90).padBottom(6).row();
        celda.add(filaEstrellas).row();


        if (!bloqueado) {
        icono.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
            Gdx.app.postRunnable(() -> {

                switch(nivel + 1) {
                    case 1:
                        juego.setScreen(new NIVELES.Nivel1Screen(juego, usuario, gestor));
                        break;
                    case 2:
                        juego.setScreen(new NIVELES.Nivel2Screen(juego, usuario, gestor));
                        break;
                    case 3:
                        juego.setScreen(new NIVELES.Nivel3Screen(juego, usuario, gestor));
                        break;
                    case 4:
                        juego.setScreen(new NIVELES.Nivel4Screen(juego, usuario, gestor));
                        break;
                    case 5:
                        juego.setScreen(new NIVELES.Nivel5Screen(juego, usuario, gestor));
                        break;
                    default:
                        juego.setScreen(new SeleccionNivelScreen(juego, usuario, gestor));
                        break;
                }
            });
        }
        });
        }

        filaNiveles.add(celda).top().padLeft(10).padRight(10);
        }

            TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
            Gdx.app.postRunnable(() ->
                juego.setScreen(new MenuPrincipalScreen(juego, usuario, gestor)));
        }
        });

        raiz.add(filaNiveles).padBottom(50).row();
        raiz.add(btnVolver).width(200).height(45).padBottom(20).row();

        escenario.addActor(raiz);
        }



        private TextureRegion region(Texture tex, int[] r) {
        return new TextureRegion(tex, r[0], r[1], r[2], r[3]);
        }

        private int calcularEstrellas(int puntaje) {
        if (puntaje <= 0)   return 0;
        if (puntaje >= 200) return 3;
        if (puntaje >= 100) return 2;
        return 1;
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
        param.size = Math.round(18 * escala);
        param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuente = gen.generateFont(param);
        fuente.getData().setScale(1f / escala);
        skin.add("fuente-defecto", fuente);

        FreeTypeFontGenerator.FreeTypeFontParameter paramTitulo = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramTitulo.size = Math.round(60 * escala);
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
        @Override public void dispose() { escenario.dispose(); piel.dispose(); sheetNiveles.dispose(); sheetResultado.dispose(); bgTexture.dispose(); batch.dispose(); }
        }
