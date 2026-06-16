package com.cutherope;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import LOGIC.Idioma;
import LOGIC.LoginManager;
import LOGIC.USER;

public class PreferenciasScreen implements Screen {

    private CutTheRope   juego;
    private String       usuario;
    private LoginManager gestor;
    private Stage        escenario;
    private Skin         piel;
    private Texture      bgTexture;
    private SpriteBatch  batch;

    private final Color VERDE   = new Color(0.33f, 0.59f, 0.31f, 1f);
    private final Color CAFE    = new Color(0.23f, 0.16f, 0.08f, 1f);
    private final Color NARANJA = new Color(0.78f, 0.63f, 0.31f, 1f);
    private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);

    public PreferenciasScreen(CutTheRope juego, String usuario, LoginManager gestor) {
        this.juego   = juego;
        this.usuario = usuario;
        this.gestor  = gestor;
        escenario = new Stage(new FitViewport(640, 480));
        piel      = crearPiel();
        bgTexture = new Texture(Gdx.files.internal("images/mainmenu.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(escenario);
        construirUI();
    }

    private void construirUI() {
        USER u = gestor.buscarUser(usuario);
        if (u == null) return;

        // Sincronizar idioma global con el del jugador actual
        Idioma.setIngles(u.isIngles());

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        // ── Título ──────────────────────────────────────────────
        Label titulo = new Label(Idioma.get("preferencias"), piel, "titulo");
        titulo.setColor(VERDE);

        // ── IDIOMA ──────────────────────────────────────────────
        Label lblIdioma = new Label(Idioma.get("idioma") + ":", piel);
        lblIdioma.setColor(CAFE);

        final TextButton[] btnIdioma = { null };
        btnIdioma[0] = crearBoton(u.isIngles() ? "EN" : "ES", NARANJA);
        btnIdioma[0].addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                USER usr = gestor.buscarUser(usuario);
                if (usr == null) return;
                boolean nuevoIngles = !usr.isIngles();
                gestor.cambiarIdioma(usuario, nuevoIngles);
                Idioma.setIngles(nuevoIngles);
                btnIdioma[0].setText(nuevoIngles ? "EN" : "ES");
            }
        });

        Table filaIdioma = new Table();
        filaIdioma.add(lblIdioma).width(160).left();
        filaIdioma.add(btnIdioma[0]).width(90).height(40);

        // ── MÚSICA ──────────────────────────────────────────────
        Label lblMusica = new Label(Idioma.get("musica") + ":", piel);
        lblMusica.setColor(CAFE);

        final TextButton[] btnMusica = { null };
        boolean musicaOn = u.isMusicaActiva();
        btnMusica[0] = crearBoton(musicaOn ? "ON" : "OFF", musicaOn ? VERDE : ROJO);
        btnMusica[0].addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                USER usr = gestor.buscarUser(usuario);
                if (usr == null) return;
                boolean nuevoEstado = !usr.isMusicaActiva();
                gestor.cambiarMusica(usuario, nuevoEstado);
                MusicaManager.getInstance().aplicarPreferencias(nuevoEstado, usr.getVolumen());
                btnMusica[0].setText(nuevoEstado ? "ON" : "OFF");
                Color c = nuevoEstado ? VERDE : ROJO;
                btnMusica[0].getStyle().up   = piel.newDrawable("white", c);
                btnMusica[0].getStyle().down = piel.newDrawable("white", c.cpy().mul(0.8f,0.8f,0.8f,1f));
                btnMusica[0].getStyle().over = piel.newDrawable("white", c.cpy().mul(1.1f,1.1f,1.1f,1f));
            }
        });

        Table filaMusica = new Table();
        filaMusica.add(lblMusica).width(160).left();
        filaMusica.add(btnMusica[0]).width(90).height(40);

        // ── VOLUMEN ─────────────────────────────────────────────
        Label lblVolumen = new Label(Idioma.get("volumen") + ":", piel);
        lblVolumen.setColor(CAFE);

        Slider slider = new Slider(0f, 1f, 0.01f, false, piel);
        slider.setValue(u.getVolumen());

        Label lblPorcentaje = new Label(Math.round(u.getVolumen() * 100) + "%", piel);
        lblPorcentaje.setColor(CAFE);

        slider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                float val = slider.getValue();
                lblPorcentaje.setText(Math.round(val * 100) + "%");
                MusicaManager.getInstance().setVolumen(val);
                gestor.cambiarVolumen(usuario, val);
            }
        });

        Table filaVolumen = new Table();
        filaVolumen.add(lblVolumen).width(160).left();
        filaVolumen.add(slider).width(140).height(20).padRight(10);
        filaVolumen.add(lblPorcentaje).width(50).left();

        // ── VOLVER ──────────────────────────────────────────────
        TextButton btnVolver = crearBoton(Idioma.get("volver"), ROJO);
        btnVolver.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.postRunnable(() ->
                    juego.setScreen(new AjustesScreen(juego, usuario, gestor)));
            }
        });

        // ── Ensamblar tabla ─────────────────────────────────────
        tabla.add(titulo).padBottom(30).row();
        tabla.add(filaIdioma).padBottom(20).row();
        tabla.add(filaMusica).padBottom(20).row();
        tabla.add(filaVolumen).padBottom(30).row();
        tabla.add(btnVolver).width(200).height(45).row();

        escenario.addActor(tabla);
    }

    private TextButton crearBoton(String texto, Color color) {
        TextButton btn = new TextButton(texto, piel);
        btn.getStyle().up   = piel.newDrawable("white", color);
        btn.getStyle().down = piel.newDrawable("white", color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        btn.getStyle().over = piel.newDrawable("white", color.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        return btn;
    }

    private Skin crearPiel() {
        Skin skin = new Skin();
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(Color.WHITE); px.fill();
        skin.add("white", new Texture(px)); px.dispose();

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/GOODDC__.TTF"));
        float escala = Math.min(Gdx.graphics.getWidth() / 640f,
            Gdx.graphics.getHeight() / 480f);

        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = Math.round(16 * escala);
        p.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont font = gen.generateFont(p);
        font.getData().setScale(1f / escala);

        FreeTypeFontGenerator.FreeTypeFontParameter pT = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pT.size = Math.round(46 * escala);
        pT.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fontTitulo = gen.generateFont(pT);
        fontTitulo.getData().setScale(1f / escala);
        gen.dispose();

        skin.add("default-font", font);
        skin.add("font-titulo",  fontTitulo);

        Label.LabelStyle lsTitulo = new Label.LabelStyle();
        lsTitulo.font = fontTitulo; lsTitulo.fontColor = CAFE;
        skin.add("titulo", lsTitulo);

        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = font; ls.fontColor = CAFE;
        skin.add("default", ls);

        TextButton.TextButtonStyle bs = new TextButton.TextButtonStyle();
        bs.font = font; bs.fontColor = Color.WHITE;
        bs.up   = skin.newDrawable("white", VERDE);
        bs.down = skin.newDrawable("white", VERDE.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        bs.over = skin.newDrawable("white", VERDE.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        skin.add("default", bs);

        // Slider style
        Slider.SliderStyle ss = new Slider.SliderStyle();
        ss.background = skin.newDrawable("white", new Color(0.7f, 0.7f, 0.7f, 1f));
        ss.knob       = skin.newDrawable("white", NARANJA);
        skin.add("default-horizontal", ss);

        return skin;
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.getProjectionMatrix().setToOrtho2D(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.begin();
        batch.draw(bgTexture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
        escenario.getViewport().apply(true);
        escenario.act(delta); escenario.draw();
    }
    @Override public void resize(int w,int h) { escenario.getViewport().update(w,h,true); }
    @Override public void show()    { Gdx.input.setInputProcessor(escenario); }
    @Override public void pause()   {}
    @Override public void resume()  {}
    @Override public void hide()    {}
    @Override public void dispose() { escenario.dispose(); piel.dispose(); bgTexture.dispose(); batch.dispose(); }
}
