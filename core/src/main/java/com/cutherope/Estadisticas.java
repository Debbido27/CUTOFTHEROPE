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

public class Estadisticas implements Screen {

    private CutTheRope   juego;
    private String       usuario;
    private LoginManager gestor;
    private Stage        escenario;
    private Skin         piel;
    private Texture      bgTexture;
    private SpriteBatch  batch;

    private final Color VERDE   = new Color(0.33f, 0.59f, 0.31f, 1f);
    private final Color CAFE    = new Color(0.23f, 0.16f, 0.08f, 1f);
    private final Color NARANJA = new Color(0.78f, 0.50f, 0.15f, 1f);
    private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);
    private final Color GRIS    = new Color(0.55f, 0.55f, 0.55f, 1f);
    private final Color DORADO  = new Color(0.85f, 0.65f, 0.10f, 1f);
    private final Color CREMA   = new Color(0.96f, 0.91f, 0.78f, 1f);
    private final Color GRIS_CLARO = new Color(0.75f, 0.75f, 0.75f, 1f);

    public Estadisticas(CutTheRope juego, String usuario, LoginManager gestor) {
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
        USER u = gestor.buscarUser(usuario);

        Table contenido = new Table();
        contenido.top().padTop(16).padLeft(20).padRight(20);

        // ── TITULO ──────────────────────────────────────────────
        Table cajaTitulo = crearCaja(VERDE, 560, -1);
        Label titulo = new Label(Idioma.get(Idioma.Clave.ESTADISTICAS), piel, "titulo");
        titulo.setColor(Color.WHITE);
        Label subTitulo = new Label("@" + usuario, piel);
        subTitulo.setColor(CREMA);
        cajaTitulo.add(titulo).padBottom(2).row();
        cajaTitulo.add(subTitulo).row();
        contenido.add(cajaTitulo).width(560).padBottom(12).row();

        if (u == null) {
            Label err = new Label(Idioma.get(Idioma.Clave.ERROR_CARGAR_DATOS), piel);
            err.setColor(ROJO);
            contenido.add(err).row();
        } else {

            // ── FILA 1: PROGRESO + ESTADISTICAS ─────────────────
            Table fila1 = new Table();

            // Caja Progreso
            Table cajaProgreso = crearCaja(NARANJA, 268, -1);
            Label tituloProgreso = new Label(Idioma.get(Idioma.Clave.PROGRESO_JUEGO), piel, "subtitulo");
            tituloProgreso.setColor(Color.WHITE);
            cajaProgreso.add(tituloProgreso).padBottom(10).row();

            int nivelesComp = u.getNivelesCompletados();
            float pct = (nivelesComp / 5f) * 100f;

            agregarFilaCaja(cajaProgreso, Idioma.get(Idioma.Clave.NIVELES_COMPLETADOS), nivelesComp + " de 5");
            agregarFilaCaja(cajaProgreso, Idioma.get(Idioma.Clave.PROGRESO_JUEGO), String.format("%.0f%%", pct));
            agregarFilaCaja(cajaProgreso, Idioma.get(Idioma.Clave.PUNTUACION_GENERAL), String.valueOf(u.getPuntuacionGeneral()));
            agregarFilaCaja(cajaProgreso, Idioma.get(Idioma.Clave.ESTRELLAS_TOTALES), String.valueOf(u.getEstrellasTotal()));

            // Barra de progreso
            cajaProgreso.add(new Label("", piel)).padTop(8).row();
            cajaProgreso.add(crearBarra(nivelesComp, 5)).width(220).height(14).padBottom(4).row();
            Label pctLabel = new Label(nivelesComp + " / 5 " + Idioma.get(Idioma.Clave.NIVELES), piel);
            pctLabel.setColor(CREMA);
            cajaProgreso.add(pctLabel).row();

            // Caja Estadisticas
            Table cajaStats = crearCaja(new Color(0.25f, 0.45f, 0.60f, 1f), 268, -1);
            Label tituloStats = new Label(Idioma.get(Idioma.Clave.ESTADISTICAS), piel, "subtitulo");
            tituloStats.setColor(Color.WHITE);
            cajaStats.add(tituloStats).padBottom(10).row();

            long tiempoMs = u.getTiempoTotalJugado();
            int  horas    = (int)(tiempoMs / 3600000);
            int  minutos  = (int)((tiempoMs % 3600000) / 60000);
            int  segundos = (int)((tiempoMs % 60000) / 1000);
            String tiempo = horas > 0
                ? horas + "h " + minutos + "m"
                : minutos + "m " + segundos + "s";

            agregarFilaCaja(cajaStats, Idioma.get(Idioma.Clave.PARTIDAS_JUGADAS), String.valueOf(u.getPartidasJugadas()));
            agregarFilaCaja(cajaStats, Idioma.get(Idioma.Clave.FALLOS_TOTALES), String.valueOf(u.getFallosTotales()));
            agregarFilaCaja(cajaStats, Idioma.get(Idioma.Clave.TIEMPO_JUGADO), tiempo);

            if (u.getPartidasJugadas() > 0) {
                int precision = (int)(100f * Math.max(0,
                    u.getPartidasJugadas() - u.getFallosTotales())
                    / u.getPartidasJugadas());
                agregarFilaCaja(cajaStats, Idioma.get(Idioma.Clave.PRECISION), precision + "%");
            }

            fila1.add(cajaProgreso).width(268).fillY().padRight(16);
            fila1.add(cajaStats).width(268).fillY();
            contenido.add(fila1).padBottom(12).row();

            // ── CAJA: ESTRELLAS POR NIVEL ────────────────────────
            Table cajaEstrellas = crearCaja(DORADO, 560, -1);
            Label titEstrellas = new Label(Idioma.get(Idioma.Clave.ESTRELLAS_POR_NIVEL), piel, "subtitulo");
            titEstrellas.setColor(Color.WHITE);
            cajaEstrellas.add(titEstrellas).padBottom(10).row();

            int[] epn = u.getEstrellasPorNivel();
            Table filaEstr = new Table();
            for (int i = 0; i < 5; i++) {
                Table celda = crearCaja(new Color(0.20f, 0.15f, 0.05f, 0.35f), 90, -1);
                Label lblNv = new Label(Idioma.get(Idioma.Clave.NIVEL) + " " + (i + 1), piel);
                lblNv.setColor(Color.WHITE);

                // Estrellas como texto "llenas/vacias" sin simbolos raros
                int cant = epn[i];
                Label lblCant = new Label(cant + " " + Idioma.get(Idioma.Clave.DE) + " 3", piel);
                lblCant.setColor(cant == 3 ? CREMA : new Color(1f,1f,1f,0.6f));

                // Barra pequeña de estrellas
                Table barraNv = new Table();
                for (int s = 0; s < 3; s++) {
                    Pixmap px2 = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                    px2.setColor(s < cant ? Color.WHITE : new Color(1f,1f,1f,0.25f));
                    px2.fill();
                    Image seg = new Image(new Texture(px2));
                    px2.dispose();
                    barraNv.add(seg).width(20).height(8).pad(2);
                }

                celda.add(lblNv).padBottom(4).row();
                celda.add(barraNv).padBottom(4).row();
                celda.add(lblCant).row();
                filaEstr.add(celda).width(90).padRight(8);
            }
            cajaEstrellas.add(filaEstr).row();
            contenido.add(cajaEstrellas).width(560).padBottom(12).row();

            // ── CAJA: HISTORIAL ──────────────────────────────────
            Table cajaHist = crearCaja(new Color(0.55f, 0.40f, 0.18f, 1f), 560, -1);
            Label titHist = new Label(Idioma.get(Idioma.Clave.HISTORIAL_PARTIDAS), piel, "subtitulo");
            titHist.setColor(Color.WHITE);
            cajaHist.add(titHist).padBottom(10).row();

            PartidaHistorial[] historial = gestor.getHistorialMemoria();
            if (historial.length == 0) {
                Label lblSin = new Label(Idioma.get(Idioma.Clave.SIN_HISTORIAL), piel);
                lblSin.setColor(CREMA);
                cajaHist.add(lblSin).padBottom(6).row();
            } else {
                // Encabezado
                Table enc = new Table();
                String[] heads  = {
                    Idioma.get(Idioma.Clave.NIVEL),
                    Idioma.get(Idioma.Clave.RESULTADO),
                    Idioma.get(Idioma.Clave.ESTRELLAS),
                    Idioma.get(Idioma.Clave.PUNTUACION),
                    Idioma.get(Idioma.Clave.TIEMPO)
                };                int[]    aenchs = { 70,      110,         90,          80,       80 };
                for (int i = 0; i < heads.length; i++) {
                    Label l = new Label(heads[i], piel);
                    l.setColor(DORADO);
                    enc.add(l).width(aenchs[i]).center();
                }
                cajaHist.add(enc).padBottom(6).row();

                // Separador
                Pixmap pxSep = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                pxSep.setColor(new Color(1f,1f,1f,0.2f)); pxSep.fill();
                cajaHist.add(new Image(new Texture(pxSep))).width(520).height(1).padBottom(6).row();
                pxSep.dispose();

                int desde = Math.max(0, historial.length - 5);
                for (int i = historial.length - 1; i >= desde; i--) {
                    PartidaHistorial p = historial[i];
                    Table fila = new Table();

                    Label lNv  = new Label("Nivel " + p.nivel, piel);
                    Label lRes = new Label(p.gano ? Idioma.get(Idioma.Clave.GANO) : Idioma.get(Idioma.Clave.PERDIO), piel);
                    Label lEst = new Label(p.estrellas + " " + Idioma.get(Idioma.Clave.DE) + " 3", piel);
                    Label lPts = new Label(String.valueOf(p.puntuacion), piel);
                    Label lTmp = new Label((p.tiempoMs / 1000) + " " + Idioma.get(Idioma.Clave.SEGUNDOS), piel);

                    lNv.setColor(CREMA);
                    lRes.setColor(p.gano ? VERDE : ROJO);
                    lRes.setColor(p.gano ? new Color(0.6f,1f,0.5f,1f) : new Color(1f,0.5f,0.4f,1f));
                    lEst.setColor(DORADO);
                    lPts.setColor(CREMA);
                    lTmp.setColor(GRIS_CLARO);

                    fila.add(lNv).width(70).center();
                    fila.add(lRes).width(110).center();
                    fila.add(lEst).width(90).center();
                    fila.add(lPts).width(80).center();
                    fila.add(lTmp).width(80).center();
                    cajaHist.add(fila).padBottom(5).row();
                }
            }
            contenido.add(cajaHist).width(560).padBottom(16).row();
        }

        // ── BOTON VOLVER ─────────────────────────────────────────
        TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);
        btnVolver.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.postRunnable(() ->
                    juego.setScreen(new MenuPrincipalScreen(juego, usuario, gestor)));
            }
        });
        contenido.add(btnVolver).width(260).height(42).row();

        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFillParent(true);
        scroll.setScrollingDisabled(true, false);
        escenario.addActor(scroll);
    }

    // ── HELPERS ──────────────────────────────────────────────────

    /** Crea una tabla con fondo de color y padding interno */
    private Table crearCaja(Color color, float ancho, float alto) {
        Table caja = new Table();
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(color); px.fill();
        caja.setBackground(new Image(new Texture(px)).getDrawable());
        px.dispose();
        caja.pad(12);
        return caja;
    }

    /** Fila "clave : valor" dentro de una caja */
    private void agregarFilaCaja(Table t, String clave, String valor) {
        Label lClave = new Label(clave + ":", piel);
        lClave.setColor(new Color(1f, 1f, 1f, 0.75f));
        Label lValor = new Label(valor, piel);
        lValor.setColor(Color.WHITE);
        Table fila = new Table();
        fila.add(lClave).width(110).left();
        fila.add(lValor).width(100).left();
        t.add(fila).padBottom(4).row();
    }

    /** Barra de progreso segmentada */
    private Table crearBarra(int llenos, int total) {
        Table barra = new Table();
        for (int i = 0; i < total; i++) {
            Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            px.setColor(i < llenos ? Color.WHITE : new Color(1f, 1f, 1f, 0.25f));
            px.fill();
            Image seg = new Image(new Texture(px));
            px.dispose();
            barra.add(seg).expandX().fillX().height(14).pad(1);
        }
        return barra;
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

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/GOODDC__.TTF"));
        float escala = Math.min(Gdx.graphics.getWidth() / 640f,
            Gdx.graphics.getHeight() / 480f);

        // Fuente normal
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.size = Math.round(14 * escala);
        p.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuente = gen.generateFont(p);
        fuente.getData().setScale(1f / escala);

        // Fuente subtitulo (para headers de caja)
        FreeTypeFontGenerator.FreeTypeFontParameter pS = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pS.size = Math.round(16 * escala);
        pS.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuenteSub = gen.generateFont(pS);
        fuenteSub.getData().setScale(1f / escala);

        // Fuente titulo
        FreeTypeFontGenerator.FreeTypeFontParameter pT = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pT.size = Math.round(36 * escala);
        pT.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuenteTit = gen.generateFont(pT);
        fuenteTit.getData().setScale(1f / escala);
        gen.dispose();

        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = fuente; ls.fontColor = CAFE;
        skin.add("default", ls);

        Label.LabelStyle lsSub = new Label.LabelStyle();
        lsSub.font = fuenteSub; lsSub.fontColor = Color.WHITE;
        skin.add("subtitulo", lsSub);

        Label.LabelStyle lsTit = new Label.LabelStyle();
        lsTit.font = fuenteTit; lsTit.fontColor = Color.WHITE;
        skin.add("titulo", lsTit);

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
        batch.getProjectionMatrix().setToOrtho2D(0,0,
            Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.begin();
        batch.draw(bgTexture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
        escenario.getViewport().apply(true);
        escenario.act(delta);
        escenario.draw();
    }

    @Override public void resize(int w,int h){ escenario.getViewport().update(w,h,true); }
    @Override public void show()   { Gdx.input.setInputProcessor(escenario); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose(){ escenario.dispose(); piel.dispose(); bgTexture.dispose(); batch.dispose(); }
}
