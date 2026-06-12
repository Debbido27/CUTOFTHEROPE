package com.cutherope;

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

public class NivelScreen implements Screen {

    private CutTheRope   juego;
    private String       usuario;
    private LoginManager gestor;
    private int          nivel;
    private Stage        escenario;
    private Skin         piel;
    private Texture      bgTexture;
    private SpriteBatch  batch;

    private final Color VERDE = new Color(0.33f, 0.59f, 0.31f, 1f);
    private final Color CAFE  = new Color(0.23f, 0.16f, 0.08f, 1f);
    private final Color ROJO  = new Color(0.70f, 0.27f, 0.20f, 1f);

    public NivelScreen(CutTheRope juego, String usuario, LoginManager gestor, int nivel) {
        this.juego   = juego;
        this.usuario = usuario;
        this.gestor  = gestor;
        this.nivel   = nivel;

        bgTexture = cargarFondo(nivel);
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch = new SpriteBatch();

        escenario = new Stage(new FitViewport(640, 480));
        piel = crearPiel();
        Gdx.input.setInputProcessor(escenario);
        construir();
    }

    private Texture cargarFondo(int nivel) {
        //niveles 1-2: lvl1.png, 3-4: lvl2.png (pendiente), 5: lvl5.png (pendiente)
        String ruta;
        if (nivel <= 2) {
            ruta = "images/lvl1.png";
        } else if (nivel <= 4) {
            ruta = "images/lvl2.png";
        } else {
            ruta = "images/lvl5.png";
        }
        if (Gdx.files.internal(ruta).exists()) return new Texture(Gdx.files.internal(ruta));
        //fallback mientras no llegue el asset del nivel
        return new Texture(Gdx.files.internal("images/mainmenu.png"));
    }

    private void construir() {
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
        gen.dispose();
        skin.add("fuente-defecto", fuente);

        Label.LabelStyle estiloLabel = new Label.LabelStyle();
        estiloLabel.font = fuente;
        estiloLabel.fontColor = CAFE;
        skin.add("default", estiloLabel);

        TextButton.TextButtonStyle estiloBoton = new TextButton.TextButtonStyle();
        estiloBoton.font = fuente;
        estiloBoton.fontColor = Color.WHITE;
        estiloBoton.up   = skin.newDrawable("blanco", VERDE);
        estiloBoton.down = skin.newDrawable("blanco", VERDE.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        estiloBoton.over = skin.newDrawable("blanco", VERDE.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
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
    @Override public void dispose() { escenario.dispose(); piel.dispose(); bgTexture.dispose(); batch.dispose(); }
}
