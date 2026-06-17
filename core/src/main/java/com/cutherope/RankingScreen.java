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

public class RankingScreen implements Screen {

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
    private final Color GRIS    = new Color(0.60f, 0.60f, 0.60f, 1f);
    private final Color DORADO  = new Color(1.00f, 0.80f, 0.00f, 1f);
    private final Color PLATA   = new Color(0.75f, 0.75f, 0.75f, 1f);
    private final Color BRONCE  = new Color(0.80f, 0.50f, 0.20f, 1f);

    public RankingScreen(CutTheRope juego, String usuario, LoginManager gestor) {
        this.juego   = juego;
        this.usuario = usuario;
        this.gestor  = gestor;
        this.escenario = new Stage(new FitViewport(640, 480));
        this.piel      = crearPiel();
        bgTexture = new Texture(Gdx.files.internal("images/mainmenu.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(escenario);
        construir();
    }

    private void construir() {
        escenario.clear();

        Table contenido = new Table();
        contenido.top().pad(20);

        Label titulo = new Label(Idioma.get(Idioma.Clave.RANKING_GENERAL), piel);
        titulo.setColor(VERDE);
        contenido.add(titulo).padBottom(6).row();

        Label sub = new Label("Clasificación global de jugadores", piel);
        sub.setColor(GRIS);
        contenido.add(sub).padBottom(20).row();

        Table encabezado = new Table();
        String[] headers = {"#", "Jugador", "Pts", "★", "Nv."};
        int[]    anchos  = {40, 200, 80, 60, 60};
        for (int i = 0; i < headers.length; i++) {
            Label l = new Label(headers[i], piel);
            l.setColor(NARANJA);
            encabezado.add(l).width(anchos[i]).center();
        }
        contenido.add(encabezado).padBottom(6).row();

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(NARANJA); px.fill();
        Image linea = new Image(new Texture(px));
        px.dispose();
        contenido.add(linea).width(440).height(2).padBottom(8).row();

        USER[] ranking = gestor.getRanking();

        if (ranking == null || ranking.length == 0) {
            Label lblVacio = new Label(Idioma.get(Idioma.Clave.SIN_RANKING), piel);
            lblVacio.setColor(GRIS);
            contenido.add(lblVacio).padBottom(16).row();
        } else {
            for (int i = 0; i < ranking.length; i++) {
                USER u = ranking[i];
                if (u == null) continue;

                boolean esMio = u.getUsername().equals(usuario);
                int pos = i + 1;

                Color colorPos;
                String medalla;
                if      (pos == 1) { colorPos = DORADO;  medalla = "🥇"; }
                else if (pos == 2) { colorPos = PLATA;   medalla = "🥈"; }
                else if (pos == 3) { colorPos = BRONCE;  medalla = "🥉"; }
                else               { colorPos = GRIS;    medalla = String.valueOf(pos); }

                Table fila = new Table();

                if (esMio) {
                    Pixmap pxFondo = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                    pxFondo.setColor(new Color(0.33f, 0.59f, 0.31f, 0.15f));
                    pxFondo.fill();
                    fila.setBackground(new Image(new Texture(pxFondo)).getDrawable());
                    pxFondo.dispose();
                }

                Label lblPos    = new Label(medalla, piel);
                Label lblNombre = new Label(u.getUsername() + (esMio ? " ◄" : ""), piel);
                Label lblPts    = new Label(String.valueOf(u.getPuntuacionGeneral()), piel);
                Label lblStars  = new Label("★ " + u.getEstrellasTotal(), piel);
                Label lblNvs    = new Label(u.getNivelesCompletados() + "/5", piel);

                lblPos.setColor(colorPos);
                lblNombre.setColor(esMio ? VERDE : CAFE);
                lblPts.setColor(esMio ? VERDE : CAFE);
                lblStars.setColor(DORADO);
                lblNvs.setColor(esMio ? VERDE : GRIS);

                fila.add(lblPos).width(40).center();
                fila.add(lblNombre).width(200).left();
                fila.add(lblPts).width(80).center();
                fila.add(lblStars).width(60).center();
                fila.add(lblNvs).width(60).center();

                contenido.add(fila).padBottom(6).row();

                if (pos % 3 == 0 && pos < ranking.length) {
                    Pixmap pxSep = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                    pxSep.setColor(new Color(0.8f, 0.8f, 0.8f, 0.4f)); pxSep.fill();
                    Image sep = new Image(new Texture(pxSep));
                    pxSep.dispose();
                    contenido.add(sep).width(440).height(1).padBottom(6).row();
                }
            }

            Label lblMiPos = new Label("", piel);
            for (int i = 0; i < ranking.length; i++) {
                if (ranking[i] != null && ranking[i].getUsername().equals(usuario)) {
                    lblMiPos.setText("Tu posición: #" + (i + 1) + " de " + ranking.length);
                    break;
                }
            }
            lblMiPos.setColor(VERDE);
            contenido.add(lblMiPos).padTop(12).padBottom(4).row();
        }

        TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);
        btnVolver.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.postRunnable(() ->
                    juego.setScreen(new MenuPrincipalScreen(juego, usuario, gestor)));
            }
        });
        contenido.add(btnVolver).width(260).height(40).padTop(16).row();

        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFillParent(true);
        scroll.setScrollingDisabled(true, false);
        escenario.addActor(scroll);
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
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(Color.WHITE); px.fill();
        skin.add("blanco", new Texture(px)); px.dispose();

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GOODDC__.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float escala = Math.min(Gdx.graphics.getWidth() / 640f, Gdx.graphics.getHeight() / 480f);
        p.size = Math.round(15 * escala);
        p.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿★◄";
        BitmapFont fuente = gen.generateFont(p);
        fuente.getData().setScale(1f / escala);

        FreeTypeFontGenerator.FreeTypeFontParameter pT = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pT.size = Math.round(40 * escala);
        pT.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fT = gen.generateFont(pT);
        fT.getData().setScale(1f / escala);
        gen.dispose();

        Label.LabelStyle ls = new Label.LabelStyle(); ls.font = fuente; ls.fontColor = CAFE;
        skin.add("default", ls);
        TextButton.TextButtonStyle bs = new TextButton.TextButtonStyle();
        bs.font = fuente; bs.fontColor = Color.WHITE;
        bs.up   = skin.newDrawable("blanco", VERDE);
        bs.down = skin.newDrawable("blanco", VERDE.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        bs.over = skin.newDrawable("blanco", VERDE.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        skin.add("default", bs);
        return skin;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.getProjectionMatrix().setToOrtho2D(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.begin();
        batch.draw(bgTexture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
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
