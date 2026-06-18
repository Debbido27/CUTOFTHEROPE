package com.cutherope;

import LOGIC.Idioma;
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
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import LOGIC.LoginManager;

public class AjustesScreen implements Screen {

        private CutTheRope   juego;
        private String       usuario;
        private LoginManager gestor;
        private Stage        escenario;
        private Skin         piel;
        private Texture      bgTexture;
        private Texture      btnTexture;
        private Texture      btnOverTexture;
        private SpriteBatch  batch;

        private final Color FONDO   = new Color(0.96f, 0.92f, 0.82f, 1f);
        private final Color VERDE   = new Color(0.33f, 0.59f, 0.31f, 1f);
        private final Color CAFE    = new Color(0.23f, 0.16f, 0.08f, 1f);
        private final Color NARANJA = new Color(0.78f, 0.63f, 0.31f, 1f);
        private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);

        public AjustesScreen(CutTheRope juego, String usuario, LoginManager gestor) {
        this.juego     = juego;
        this.usuario   = usuario;
        this.gestor    = gestor;
        this.escenario = new Stage(new FitViewport(640, 480));
        this.piel      = crearPiel();
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

        Label titulo = new Label(Idioma.get(Idioma.Clave.AJUSTES), piel, "titulo");

        TextButton btnPerfil       = crearBoton(Idioma.get(Idioma.Clave.PERFIL),       VERDE);
        TextButton btnPreferencias = crearBoton(Idioma.get(Idioma.Clave.PREFERENCIAS), NARANJA);
        TextButton btnVolver       = crearBoton(Idioma.get(Idioma.Clave.VOLVER),       ROJO);

        btnPerfil.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
            Gdx.app.postRunnable(() -> juego.setScreen(new PerfilScreen(juego, usuario, gestor)));
        }
        });
            btnPreferencias.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    Gdx.app.postRunnable(() ->
                        juego.setScreen(new PreferenciasScreen(juego, usuario, gestor)));
                }
            });
        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
            Gdx.app.postRunnable(() -> juego.setScreen(new MenuPrincipalScreen(juego, usuario, gestor)));
        }
        });

        tabla.add(titulo).padBottom(30).row();
        tabla.add(btnPerfil).width(280).height(50).padBottom(12).row();
        tabla.add(btnPreferencias).width(280).height(50).padBottom(12).row();
        tabla.add(btnVolver).width(280).height(50).row();
        escenario.addActor(tabla);
        }

        private TextButton crearBoton(String texto, Color color) {
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
        skin.add("white", new Texture(px)); px.dispose();

        btnTexture = new Texture(Gdx.files.internal("images/button.png"));
        skin.add("btn-up", new NinePatch(btnTexture, 35, 35, 20, 20));
        btnOverTexture = new Texture(Gdx.files.internal("images/button_over.png"));
        skin.add("btn-over", new NinePatch(btnOverTexture, 35, 35, 20, 20));

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GOODDC__.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float escala = Math.min(Gdx.graphics.getWidth() / 640f, Gdx.graphics.getHeight() / 480f);
        param.size = Math.round(18 * escala);
        param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont f = gen.generateFont(param);
        f.getData().setScale(1f / escala);
        skin.add("default-font", f);
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

        Label.LabelStyle ls = new Label.LabelStyle(); ls.font = f; ls.fontColor = CAFE;
        skin.add("default", ls);
        TextButton.TextButtonStyle bs = new TextButton.TextButtonStyle();
        bs.font = f; bs.fontColor = Color.WHITE;
        bs.up   = skin.newDrawable("white", VERDE);
        bs.down = skin.newDrawable("white", VERDE.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        bs.over = skin.newDrawable("white", VERDE.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        skin.add("default", bs);
        return skin;
        }

        @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        batch.draw(bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        escenario.getViewport().apply(true);
        escenario.act(delta); escenario.draw();
        }
        @Override public void resize(int w, int h) { escenario.getViewport().update(w, h, true); }
        @Override public void show()    { Gdx.input.setInputProcessor(escenario); }
        @Override public void pause()   {}
        @Override public void resume()  {}
        @Override public void hide()    {}
        @Override public void dispose() { escenario.dispose(); piel.dispose(); bgTexture.dispose(); btnTexture.dispose(); btnOverTexture.dispose(); batch.dispose(); }
        }
