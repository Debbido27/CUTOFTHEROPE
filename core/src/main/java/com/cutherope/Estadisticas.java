package com.cutherope;

import LOGIC.Idioma;
import LOGIC.SesionJuego;
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
import LOGIC.PartidaHistorial;

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
    private final Color NARANJA = new Color(0.78f, 0.63f, 0.31f, 1f);
    private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);
    private final Color GRIS    = new Color(0.60f, 0.60f, 0.60f, 1f);
    private final Color DORADO  = new Color(1.00f, 0.80f, 0.00f, 1f);

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
        contenido.top().pad(20);

        // ── TÍTULO ──────────────────────────────────────────────
        Label titulo = new Label(Idioma.get(Idioma.Clave.ESTADISTICAS), piel);
        titulo.setColor(VERDE);
        contenido.add(titulo).padBottom(6).row();

        Label subTitulo = new Label("@" + usuario, piel);
        subTitulo.setColor(NARANJA);
        contenido.add(subTitulo).padBottom(20).row();

        if (u != null) {

            // ── SECCIÓN: PROGRESO ────────────────────────────────
            agregarSeccion(contenido, "— Progreso —");

            int nivelesComp = u.getNivelesCompletados();
            float porcentaje = (nivelesComp / 5f) * 100f;

            agregarFila(contenido, Idioma.get(Idioma.Clave.NIVELES_COMPLETADOS),
                nivelesComp + " / 5");
            agregarFila(contenido, "Progreso",
                String.format("%.0f%%", porcentaje));
            agregarFila(contenido, Idioma.get(Idioma.Clave.PUNTUACION_GENERAL),
                String.valueOf(u.getPuntuacionGeneral()));
            agregarFila(contenido, Idioma.get(Idioma.Clave.ESTRELLAS_TOTALES),
                "★ " + u.getEstrellasTotal());

            // Barra de progreso visual
            contenido.add(crearBarraProgreso(nivelesComp, 5)).width(400).height(18).padTop(6).padBottom(16).row();

            // ── SECCIÓN: ESTRELLAS POR NIVEL ─────────────────────
            agregarSeccion(contenido, "— Estrellas por Nivel —");

            int[] estrellasPorNivel = u.getEstrellasPorNivel();
            Table filaEstrellas = new Table();
            for (int i = 0; i < 5; i++) {
                Table celda = new Table();
                Label lblNv = new Label("Nv." + (i + 1), piel);
                lblNv.setColor(CAFE);
                celda.add(lblNv).row();
                StringBuilder stars = new StringBuilder();
                for (int s = 0; s < 3; s++)
                    stars.append(s < estrellasPorNivel[i] ? "★" : "☆");
                Label lblStars = new Label(stars.toString(), piel);
                lblStars.setColor(DORADO);
                celda.add(lblStars).row();
                filaEstrellas.add(celda).width(72).center().padRight(8);
            }
            contenido.add(filaEstrellas).padBottom(16).row();

            // ── SECCIÓN: ESTADÍSTICAS ────────────────────────────
            agregarSeccion(contenido, "— Estadísticas —");

            long tiempoMs = u.getTiempoTotalJugado();
            String tiempo = String.format("%d h  %d min  %d seg",
                tiempoMs / 3600000,
                (tiempoMs % 3600000) / 60000,
                (tiempoMs % 60000) / 1000);

            agregarFila(contenido, Idioma.get(Idioma.Clave.PARTIDAS_JUGADAS),
                String.valueOf(u.getPartidasJugadas()));
            agregarFila(contenido, Idioma.get(Idioma.Clave.FALLOS_TOTALES),
                String.valueOf(u.getFallosTotales()));
            agregarFila(contenido, Idioma.get(Idioma.Clave.TIEMPO_JUGADO), tiempo);

            if (u.getPartidasJugadas() > 0) {
                int precision = (int)(((float)(u.getPartidasJugadas() - u.getFallosTotales())
                    / u.getPartidasJugadas()) * 100);
                agregarFila(contenido, "Precisión", Math.max(0, precision) + "%");
            }

            // ── SECCIÓN: HISTORIAL DE PARTIDAS ───────────────────
            agregarSeccion(contenido, "— Últimas Partidas —");

            PartidaHistorial[] historial = gestor.getHistorialMemoria();
            if (historial.length == 0) {
                Label lblSin = new Label("Sin partidas en esta sesión.", piel);
                lblSin.setColor(GRIS);
                contenido.add(lblSin).padBottom(8).row();
            } else {
                // Encabezado tabla
                Table enc = new Table();
                for (String h : new String[]{"Nv.", "Result.", "★", "Pts", "Tiempo", "Fecha"}) {
                    Label l = new Label(h, piel);
                    l.setColor(NARANJA);
                    enc.add(l).width(h.equals("Fecha") ? 120 : 60).center();
                }
                contenido.add(enc).padBottom(4).row();

                int desde = Math.max(0, historial.length - 6);
                for (int i = historial.length - 1; i >= desde; i--) {
                    PartidaHistorial p = historial[i];
                    Table fila = new Table();
                    String[] vals = {
                        String.valueOf(p.nivel),
                        p.gano ? "✓ Ganó" : "✗ Perdió",
                        String.valueOf(p.estrellas),
                        String.valueOf(p.puntuacion),
                        (p.tiempoMs / 1000) + "s",
                        p.fecha.toString()
                    };
                    Color[] colores = {
                        CAFE, p.gano ? VERDE : ROJO, DORADO, CAFE, GRIS, GRIS
                    };
                    int[] anchos = {60, 60, 60, 60, 60, 120};
                    for (int j = 0; j < vals.length; j++) {
                        Label l = new Label(vals[j], piel);
                        l.setColor(colores[j]);
                        fila.add(l).width(anchos[j]).center();
                    }
                    contenido.add(fila).padBottom(4).row();
                }
            }

        } else {
            Label lblError = new Label("No se pudieron cargar los datos.", piel);
            lblError.setColor(ROJO);
            contenido.add(lblError).padBottom(16).row();
        }

        // ── BOTÓN VOLVER ─────────────────────────────────────────
        TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);
        btnVolver.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.postRunnable(() ->
                    juego.setScreen(new MenuPrincipalScreen(juego, usuario, gestor)));
            }
        });
        contenido.add(btnVolver).width(260).height(40).padTop(20).row();

        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFillParent(true);
        scroll.setScrollingDisabled(true, false);
        escenario.addActor(scroll);
    }

    // ── Barra de progreso visual ──────────────────────────────────
    private Table crearBarraProgreso(int completados, int total) {
        Table barra = new Table();
        for (int i = 0; i < total; i++) {
            Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            Color c = i < completados ? VERDE : new Color(0.8f, 0.8f, 0.8f, 1f);
            px.setColor(c); px.fill();
            Image seg = new Image(new Texture(px));
            px.dispose();
            barra.add(seg).expandX().fillX().height(18).pad(1);
        }
        return barra;
    }

    private void agregarSeccion(Table t, String texto) {
        Label lbl = new Label(texto, piel);
        lbl.setColor(NARANJA);
        t.add(lbl).padTop(10).padBottom(6).row();
    }

    private void agregarFila(Table t, String clave, String valor) {
        Label lClave = new Label(clave + ":", piel); lClave.setColor(GRIS);
        Label lValor = new Label(valor, piel);       lValor.setColor(CAFE);
        Table fila = new Table();
        fila.add(lClave).width(200).left();
        fila.add(lValor).width(160).left();
        t.add(fila).padBottom(5).row();
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
        p.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿★☆✓✗";
        BitmapFont fuente = gen.generateFont(p);
        fuente.getData().setScale(1f / escala);

        FreeTypeFontGenerator.FreeTypeFontParameter pT = new FreeTypeFontGenerator.FreeTypeFontParameter();
        pT.size = Math.round(40 * escala);
        pT.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuenteTitulo = gen.generateFont(pT);
        fuenteTitulo.getData().setScale(1f / escala);
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
