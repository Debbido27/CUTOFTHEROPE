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
    private final Color DORADO  = new Color(0.95f, 0.78f, 0.20f, 1f);
    private final Color PLATA   = new Color(0.75f, 0.76f, 0.78f, 1f);
    private final Color BRONCE  = new Color(0.72f, 0.47f, 0.26f, 1f);
    private final Color BLANCO  = new Color(1f, 1f, 1f, 1f);

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

        Label sub = new Label(Idioma.get(Idioma.Clave.CLASIFICACION_GLOBAL), piel);
        sub.setColor(GRIS);
        contenido.add(sub).padBottom(20).row();

        USER[] ranking = gestor.getRanking();

        if (ranking == null || ranking.length == 0) {
            Label lblVacio = new Label(Idioma.get(Idioma.Clave.SIN_RANKING), piel);
            lblVacio.setColor(GRIS);
            contenido.add(lblVacio).padBottom(16).row();
        } else {
            // ---- Tarjetas Top 3 (oro / plata / bronce) ----
            int topCount = Math.min(3, ranking.length);
            for (int i = 0; i < topCount; i++) {
                USER u = ranking[i];
                if (u == null) continue;
                contenido.add(crearTarjetaTop(u, i + 1)).width(460).padBottom(10).row();
            }

            // ---- Separador antes del resto del ranking ----
            if (ranking.length > 3) {
                Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                px.setColor(NARANJA); px.fill();
                Image linea = new Image(new Texture(px));
                px.dispose();
                contenido.add(linea).width(460).height(2).padTop(6).padBottom(10).row();

                Table encabezado = new Table();
                String[] headers = {
                    "#",
                    Idioma.get(Idioma.Clave.JUGADOR),
                    Idioma.get(Idioma.Clave.PTS),
                    Idioma.get(Idioma.Clave.ESTRELLAS),
                    Idioma.get(Idioma.Clave.NV)
                };                int[]    anchos  = {40, 170, 70, 110, 60};
                for (int i = 0; i < headers.length; i++) {
                    Label l = new Label(headers[i], piel);
                    l.setColor(NARANJA);
                    encabezado.add(l).width(anchos[i]).center();
                }
                contenido.add(encabezado).padBottom(8).row();
            }

            // ---- Resto del ranking (4to en adelante) ----
            for (int i = 3; i < ranking.length; i++) {
                USER u = ranking[i];
                if (u == null) continue;

                boolean esMio = u.getUsername().equals(usuario);
                int pos = i + 1;

                Table fila = new Table();

                if (esMio) {
                    Pixmap pxFondo = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                    pxFondo.setColor(new Color(0.33f, 0.59f, 0.31f, 0.15f));
                    pxFondo.fill();
                    fila.setBackground(new Image(new Texture(pxFondo)).getDrawable());
                    pxFondo.dispose();
                }

                Label lblPos    = new Label(String.valueOf(pos), piel);
                Label lblNombre = new Label(u.getUsername() + (esMio ? "  (" + Idioma.get(Idioma.Clave.TU) + ")" : ""), piel);                Label lblPts    = new Label(String.valueOf(u.getPuntuacionGeneral()), piel);
                Label lblStars  = new Label(u.getEstrellasTotal() + " " + Idioma.get(Idioma.Clave.ESTRELLAS), piel);
                Label lblNvs    = new Label(u.getNivelesCompletados() + "/5 " + Idioma.get(Idioma.Clave.NIVELES), piel);

                lblPos.setColor(GRIS);
                lblNombre.setColor(esMio ? VERDE : CAFE);
                lblPts.setColor(esMio ? VERDE : CAFE);
                lblStars.setColor(esMio ? VERDE : CAFE);
                lblNvs.setColor(esMio ? VERDE : GRIS);

                fila.add(lblPos).width(40).center();
                fila.add(lblNombre).width(170).left();
                fila.add(lblPts).width(70).center();
                fila.add(lblStars).width(110).center();
                fila.add(lblNvs).width(60).center();

                contenido.add(fila).padBottom(6).row();

                if (pos % 5 == 0 && pos < ranking.length) {
                    Pixmap pxSep = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                    pxSep.setColor(new Color(0.8f, 0.8f, 0.8f, 0.4f)); pxSep.fill();
                    Image sep = new Image(new Texture(pxSep));
                    pxSep.dispose();
                    contenido.add(sep).width(450).height(1).padBottom(6).row();
                }
            }

            Label lblMiPos = new Label("", piel);
            for (int i = 0; i < ranking.length; i++) {
                if (ranking[i] != null && ranking[i].getUsername().equals(usuario)) {
                    lblMiPos.setText(Idioma.get(Idioma.Clave.TU_POSICION) + ": #" + (i + 1) + " " + Idioma.get(Idioma.Clave.DE) + " " + ranking.length);
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

    /**
     * Crea una tarjeta ancha para los puestos 1, 2 y 3 con fondo de color
     * (oro, plata o bronce) y la etiqueta correspondiente en texto.
     */
    private Table crearTarjetaTop(USER u, int pos) {
        Color colorFondo;
        String etiqueta;
        Color colorTexto;

        switch (pos) {
            case 1:
                colorFondo = DORADO;
                etiqueta = Idioma.get(Idioma.Clave.ORO);
                colorTexto = CAFE;
                break;
            case 2:
                colorFondo = PLATA;
                etiqueta = Idioma.get(Idioma.Clave.PLATA);
                colorTexto = CAFE;
                break;
            default:
                colorFondo = BRONCE;
                etiqueta = Idioma.get(Idioma.Clave.BRONCE);
                colorTexto = BLANCO;
                break;
        }

        boolean esMio = u.getUsername().equals(usuario);

        Table tarjeta = new Table();
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(colorFondo); px.fill();
        tarjeta.setBackground(new Image(new Texture(px)).getDrawable());
        px.dispose();
        tarjeta.pad(10, 14, 10, 14);

        Label lblEtiqueta = new Label(etiqueta, piel);
        lblEtiqueta.setColor(colorTexto);

        Label lblPos = new Label("#" + pos, piel);
        lblPos.setColor(colorTexto);

        Label lblNombre = new Label(u.getUsername() + (esMio ? "  (" + Idioma.get(Idioma.Clave.TU) + ")" : ""), piel);
        lblNombre.setColor(colorTexto);

        Label lblPts = new Label(u.getPuntuacionGeneral() + " " + Idioma.get(Idioma.Clave.PTS), piel);
        lblPts.setColor(colorTexto);

        Label lblStars = new Label(u.getEstrellasTotal() + " " + Idioma.get(Idioma.Clave.ESTRELLAS), piel);
        lblStars.setColor(colorTexto);

        Label lblNvs = new Label(u.getNivelesCompletados() + "/5 " + Idioma.get(Idioma.Clave.NIVELES), piel);
        lblNvs.setColor(colorTexto);

        Table izquierda = new Table();
        izquierda.add(lblEtiqueta).left().row();
        izquierda.add(lblNombre).left();

        Table derecha = new Table();
        derecha.add(lblPts).right().padBottom(2).row();
        derecha.add(lblStars).right().padBottom(2).row();
        derecha.add(lblNvs).right();

        tarjeta.add(lblPos).width(50).left();
        tarjeta.add(izquierda).expandX().left();
        tarjeta.add(derecha).right();

        return tarjeta;
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
        // Solo caracteres que la fuente realmente soporta: letras latinas + acentos.
        // Se eliminan simbolos como estrella/corazon/flecha que no estan en la fuente
        // y se ven como cuadros vacios.
        p.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
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
