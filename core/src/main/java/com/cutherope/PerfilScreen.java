
        package com.cutherope;

        import LOGIC.LoginManager;
        import LOGIC.USER;
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
        import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
        import com.badlogic.gdx.scenes.scene2d.ui.Label;
        import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
        import com.badlogic.gdx.scenes.scene2d.ui.Skin;
        import com.badlogic.gdx.scenes.scene2d.ui.Table;
        import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
        import com.badlogic.gdx.scenes.scene2d.ui.TextField;
        import LOGIC.Idioma;
        import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
        import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
        import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
        import com.badlogic.gdx.utils.viewport.FitViewport;
        import java.time.format.DateTimeFormatter;


        public class PerfilScreen implements Screen{

        private CutTheRope   juego;
        private String       usuario;
        private LoginManager gestor;
        private Stage        escenario;
        private Skin         piel;
        private Texture      bgTexture;
        private SpriteBatch  batch;
        private int categoriaActual = 0;

        private final Color FONDO   = new Color(0.96f, 0.92f, 0.82f, 1f);
        private final Color VERDE   = new Color(0.33f, 0.59f, 0.31f, 1f);
        private final Color CAFE    = new Color(0.23f, 0.16f, 0.08f, 1f);
        private final Color NARANJA = new Color(0.78f, 0.63f, 0.31f, 1f);
        private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);
        private final Color GRIS    = new Color(0.60f, 0.60f, 0.60f, 1f);
        private String avatarTemporalPerfil = null;
        private static final String DEFAULT_AVATAR = "AVATARS/X1.png";

        private static final String[][] CATEGORIAS = {
        { "Dragon Ball",       "AVATARS/DRAGON",      "D1","D2","D3","D4" },
        { "Rapidos y Furiosos","AVATARS/FNR",          "R1","R2","R3","R4","R5" },
        { "Invincible",        "AVATARS/INVINCIBLE",   "I1","I2","I3","I4" },
        { "Transformers",      "AVATARS/TRANSFORMER",  "T1","T2","T3","T4" },
        { "Futbol",            "AVATARS/futbol",        "F1","F2","F3","F4" }
        };


        public PerfilScreen(CutTheRope juego, String usuario, LoginManager gestor) {
        this.juego    = juego;
        this.usuario  = usuario;
        this.gestor   = gestor;
        this.escenario = new Stage(new FitViewport(640, 480));
        this.piel      = crearPiel();
        bgTexture = new Texture(Gdx.files.internal("images/mainmenu.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(escenario);
        construirMenuPerfil();
        }

        private void construirMenuPerfil(){
        escenario.clear();

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Label titulo = new Label(Idioma.get(Idioma.Clave.MI_PERFIL), piel);

        titulo.setColor(VERDE);

        USER u = gestor.buscarUser(usuario);
        String avatarRuta = (u != null && !u.getAvatarPath().isEmpty())
        ? u.getAvatarPath() : DEFAULT_AVATAR;

        com.badlogic.gdx.scenes.scene2d.ui.Image imgAvatarPerfil = null;
        if (Gdx.files.internal(avatarRuta).exists()) {
        Texture texAvatar = new Texture(Gdx.files.internal(avatarRuta));
        imgAvatarPerfil = new com.badlogic.gdx.scenes.scene2d.ui.Image(texAvatar);
        }

        Label subUsuario = new Label("@" + usuario, piel);
        subUsuario.setColor(NARANJA);

        TextButton btnVerInfo       = crearBoton(Idioma.get(Idioma.Clave.VER_INFORMACION),    NARANJA);
        TextButton btnCambiarUser   = crearBoton(Idioma.get(Idioma.Clave.CAMBIAR_USUARIO),    VERDE);
        TextButton btnCambiarPass   = crearBoton(Idioma.get(Idioma.Clave.CAMBIAR_CONTRASENA), VERDE);
        TextButton btnCambiarAvatar = crearBoton(Idioma.get(Idioma.Clave.CAMBIAR_AVATAR),     VERDE);
        TextButton btnVolver        = crearBoton(Idioma.get(Idioma.Clave.VOLVER),               ROJO);

        btnVerInfo.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent e, float x, float y) { construirVerInfo(); }
        });
        btnCambiarUser.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirCambiarUsuario(); }
        });
        btnCambiarPass.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirCambiarPassword(); }
        });
        btnCambiarAvatar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirCambiarAvatar(); }
        });
        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        Gdx.app.postRunnable(() ->
            juego.setScreen(new MenuPrincipalScreen(juego, usuario, gestor)));
        }
        });

        tabla.add(titulo).padBottom(2).row();
        tabla.add(subUsuario).padBottom(2).row();
        if (imgAvatarPerfil != null)
        tabla.add(imgAvatarPerfil).width(60).height(60).padBottom(6).row();
        tabla.add(btnVerInfo).width(260).height(38).padBottom(8).row();
        tabla.add(btnCambiarUser).width(260).height(38).padBottom(8).row();
        tabla.add(btnCambiarPass).width(260).height(38).padBottom(8).row();
        tabla.add(btnCambiarAvatar).width(260).height(38).padBottom(8).row();
        tabla.add(btnVolver).width(260).height(38).row();

        escenario.addActor(tabla);

        }




        private void construirVerInfo() {
        escenario.clear();

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Label titulo = new Label(Idioma.get(Idioma.Clave.VER_INFORMACION), piel);
        titulo.setColor(VERDE);

        TextButton btnDatos     = crearBoton(Idioma.get(Idioma.Clave.DATOS_PERSONALES),  NARANJA);
        TextButton btnProgreso  = crearBoton(Idioma.get(Idioma.Clave.PROGRESO_JUEGO),    VERDE);
        TextButton btnEstadist  = crearBoton(Idioma.get(Idioma.Clave.ESTADISTICAS),      VERDE);
        TextButton btnHistorial = crearBoton(Idioma.get(Idioma.Clave.HISTORIAL),         VERDE);
        TextButton btnRanking   = crearBoton(Idioma.get(Idioma.Clave.RANKING),           VERDE);
        TextButton btnAmigos    = crearBoton(Idioma.get(Idioma.Clave.VER_AMIGOS_RIVALES),VERDE);
        TextButton btnVolver    = crearBoton(Idioma.get(Idioma.Clave.VOLVER_AL_PERFIL),  ROJO);

        btnDatos.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerDatos(); }
        });
        btnProgreso.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerProgreso(); }
        });
        btnEstadist.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerEstadisticas(); }
        });
        btnHistorial.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerHistorial(); }
        });
        btnRanking.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerRanking(); }
        });
        btnAmigos.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerAmigos(); }
        });
        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirMenuPerfil(); }
        });

        tabla.add(titulo).padBottom(20).row();
        tabla.add(btnDatos).width(260).height(38).padBottom(8).row();
        tabla.add(btnProgreso).width(260).height(38).padBottom(8).row();
        tabla.add(btnEstadist).width(260).height(38).padBottom(8).row();
        tabla.add(btnHistorial).width(260).height(38).padBottom(8).row();
        tabla.add(btnRanking).width(260).height(38).padBottom(8).row();
        tabla.add(btnAmigos).width(260).height(38).padBottom(20).row();
        tabla.add(btnVolver).width(260).height(38).row();

        escenario.addActor(tabla);
        }

        private void construirVerDatos() {
        escenario.clear();
        USER u = gestor.buscarUser(usuario);
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Label titulo = new Label(Idioma.get(Idioma.Clave.DATOS_PERSONALES), piel);
        titulo.setColor(VERDE);

        String fechaReg = (u != null)
        ? u.getFechaRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
        long ms = (u != null) ? u.getUltimaSesion() : 0;
        String ultimaSesion = (ms > 0) ? new java.util.Date(ms).toString() : "-";
        String avatarActual = (u != null && !u.getAvatarPath().isEmpty()) ? u.getAvatarPath() : "Sin avatar";
        String nombreCompleto = (u != null) ? u.getFullname() : "-";

        TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);
        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerInfo(); }
        });

        tabla.add(titulo).padBottom(16).colspan(2).center().row();
        agregarFila(tabla, Idioma.get(Idioma.Clave.NOMBRE_COMPLETO_LABEL), nombreCompleto);
        agregarFila(tabla, Idioma.get(Idioma.Clave.USUARIO),               "@" + usuario);
        agregarFila(tabla, Idioma.get(Idioma.Clave.AVATAR),                avatarActual);
        agregarFila(tabla, Idioma.get(Idioma.Clave.FECHA_REGISTRO),        fechaReg);
        agregarFila(tabla, Idioma.get(Idioma.Clave.ULTIMA_SESION),         ultimaSesion);
        tabla.add(btnVolver).width(240).height(36).padTop(16).colspan(2).center().row();

        escenario.addActor(tabla);
        }

        private void construirVerProgreso() {
        escenario.clear();
        USER u = gestor.buscarUser(usuario);
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Label titulo = new Label(Idioma.get(Idioma.Clave.PROGRESO_JUEGO), piel);
        titulo.setColor(VERDE);

        int nivelesComp = (u != null) ? u.getNivelesCompletados() : 0;
        int puntuacion  = (u != null) ? u.getPuntuacionGeneral()  : 0;
        int estrellas   = (u != null) ? u.getEstrellasTotal()     : 0;

        TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);
        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerInfo(); }
        });

        tabla.add(titulo).padBottom(16).colspan(2).center().row();
        agregarFila(tabla, Idioma.get(Idioma.Clave.NIVELES_COMPLETADOS), nivelesComp + " / 5");
        agregarFila(tabla, Idioma.get(Idioma.Clave.PUNTUACION_GENERAL),  String.valueOf(puntuacion));
        agregarFila(tabla, Idioma.get(Idioma.Clave.ESTRELLAS_TOTALES),   String.valueOf(estrellas));
        tabla.add(btnVolver).width(240).height(36).padTop(16).colspan(2).center().row();

        escenario.addActor(tabla);
        }

        private void construirVerEstadisticas() {
        escenario.clear();
        USER u = gestor.buscarUser(usuario);
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Label titulo = new Label(Idioma.get(Idioma.Clave.ESTADISTICAS), piel);
        titulo.setColor(VERDE);

        int partidas  = (u != null) ? u.getPartidasJugadas()   : 0;
        int fallos    = (u != null) ? u.getFallosTotales()      : 0;
        long tiempoMs = (u != null) ? u.getTiempoTotalJugado()  : 0;
        String tiempo = String.format("%d min %d seg", (tiempoMs / 60000), (tiempoMs % 60000) / 1000);

        TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);
        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerInfo(); }
        });

        tabla.add(titulo).padBottom(16).colspan(2).center().row();
        agregarFila(tabla, Idioma.get(Idioma.Clave.PARTIDAS_JUGADAS), String.valueOf(partidas));
        agregarFila(tabla, Idioma.get(Idioma.Clave.FALLOS_TOTALES),   String.valueOf(fallos));
        agregarFila(tabla, Idioma.get(Idioma.Clave.TIEMPO_JUGADO),    tiempo);
        tabla.add(btnVolver).width(240).height(36).padTop(16).colspan(2).center().row();

        escenario.addActor(tabla);
        }

            private void construirVerHistorial() {
                escenario.clear();
                Table tabla = new Table();
                tabla.setFillParent(true);
                tabla.top().padTop(20);

                Label titulo = new Label(Idioma.get(Idioma.Clave.HISTORIAL_PARTIDAS), piel);
                titulo.setColor(VERDE);
                tabla.add(titulo).padBottom(12).row();

                LOGIC.PartidaHistorial[] partidas = gestor.getHistorialMemoria();

                if (partidas.length == 0) {
                    Label lblSin = new Label(Idioma.get(Idioma.Clave.SIN_HISTORIAL), piel);
                    lblSin.setColor(GRIS);
                    tabla.add(lblSin).padBottom(16).row();
                } else {
                    Table contenido = new Table();
                    for (LOGIC.PartidaHistorial p : partidas) {
                        String linea = "Nv." + p.nivel
                            + "  " + (p.gano ? "v/" : "X")
                            + "  *" + p.estrellas
                            + "  " + p.puntuacion + "pts"
                            + "  " + (p.tiempoMs / 1000) + "s"
                            + "  " + p.fecha;
                        Label lbl = new Label(linea, piel);
                        lbl.setColor(CAFE);
                        contenido.add(lbl).left().padBottom(4).row();
                    }
                    ScrollPane scroll = new ScrollPane(contenido, piel);
                    tabla.add(scroll).width(580).height(280).row();
                }

                TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);
                btnVolver.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirVerInfo(); }
                });
                tabla.add(btnVolver).width(240).height(36).padTop(12).row();
                escenario.addActor(tabla);
            }

            private void construirVerRanking() {
                escenario.clear();
                Table tabla = new Table();
                tabla.setFillParent(true);
                tabla.top().padTop(20);

                Label titulo = new Label(Idioma.get(Idioma.Clave.RANKING_GENERAL), piel);
                titulo.setColor(VERDE);
                tabla.add(titulo).padBottom(12).row();

                USER[] ranking = gestor.getRanking();

                if (ranking == null || ranking.length == 0) {
                    Label lblSin = new Label(Idioma.get(Idioma.Clave.SIN_RANKING), piel);
                    lblSin.setColor(GRIS);
                    tabla.add(lblSin).padBottom(16).row();
                } else {
                    Table contenido = new Table();
                    // Encabezado
                    Label h1 = new Label("#  Usuario", piel);   h1.setColor(NARANJA);
                    Label h2 = new Label("Pts",        piel);   h2.setColor(NARANJA);
                    Label h3 = new Label("★",          piel);   h3.setColor(NARANJA);
                    Label h4 = new Label("Nv.",        piel);   h4.setColor(NARANJA);
                    contenido.add(h1).width(200).left();
                    contenido.add(h2).width(80).center();
                    contenido.add(h3).width(50).center();
                    contenido.add(h4).width(50).center().row();

                    int pos = 1;
                    for (USER u : ranking) {
                        Label l1 = new Label(pos + ". " + u.getUsername(), piel); l1.setColor(CAFE);
                        Label l2 = new Label(String.valueOf(u.getPuntuacionGeneral()), piel); l2.setColor(CAFE);
                        Label l3 = new Label(String.valueOf(u.getEstrellasTotal()), piel);    l3.setColor(CAFE);
                        Label l4 = new Label(String.valueOf(u.getNivelesCompletados()), piel);l4.setColor(CAFE);
                        // Destacar usuario actual
                        if (u.getUsername().equals(usuario)) {
                            l1.setColor(VERDE); l2.setColor(VERDE);
                            l3.setColor(VERDE); l4.setColor(VERDE);
                        }
                        contenido.add(l1).width(200).left().padBottom(4);
                        contenido.add(l2).width(80).center().padBottom(4);
                        contenido.add(l3).width(50).center().padBottom(4);
                        contenido.add(l4).width(50).center().padBottom(4).row();
                        pos++;
                    }
                    ScrollPane scroll = new ScrollPane(contenido, piel);
                    tabla.add(scroll).width(400).height(280).row();
                }

                TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);
                btnVolver.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirVerInfo(); }
                });
                tabla.add(btnVolver).width(240).height(36).padTop(12).row();
                escenario.addActor(tabla);
            }

        private void construirVerAmigos() {
        escenario.clear();
        USER u = gestor.buscarUser(usuario);
        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Label titulo = new Label(Idioma.get(Idioma.Clave.AMIGOS_RIVALES), piel);
        titulo.setColor(VERDE);

        TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);
        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerInfo(); }
        });

        tabla.add(titulo).padBottom(16).row();

        if (u != null && u.getAmigos().length > 0) {
        for (String amigo : u.getAmigos()) {
           Label lblAmigo = new Label("• " + amigo, piel);
           lblAmigo.setColor(CAFE);
           tabla.add(lblAmigo).left().padBottom(4).row();
        }
        } else {
        Label lblSin = new Label(Idioma.get(Idioma.Clave.SIN_AMIGOS), piel);
        lblSin.setColor(GRIS);
        tabla.add(lblSin).padBottom(16).row();
        }

        tabla.add(btnVolver).width(240).height(36).padTop(16).row();

        escenario.addActor(tabla);
        }

        private void construirCambiarUsuario() {
        escenario.clear();

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Label titulo  = new Label(Idioma.get(Idioma.Clave.CAMBIAR_USUARIO), piel);
        titulo.setColor(VERDE);

        Label lblPass = crearLabel(Idioma.get(Idioma.Clave.CONFIRMA_CONTRASENA));
        TextField campoPass = crearCampo(Idioma.get(Idioma.Clave.CONTRASENA_ACTUAL));
        campoPass.setPasswordMode(true);
        campoPass.setPasswordCharacter('*');CheckBox chkVer = new CheckBox(Idioma.get(Idioma.Clave.VER_CONTRASENA), piel);
        chkVer.addListener(new ChangeListener() {
        public void changed(ChangeEvent event, Actor actor) {
        campoPass.setPasswordMode(!chkVer.isChecked());
        }
        });

        Label lblNuevo = crearLabel(Idioma.get(Idioma.Clave.NUEVO_USUARIO));
        lblNuevo.setVisible(false);
        TextField campoNuevoUser = crearCampo(Idioma.get(Idioma.Clave.USUARIO));
        campoNuevoUser.setVisible(false);

        Label mensaje = new Label("", piel);
        mensaje.setColor(ROJO);

        TextButton btnValidar = crearBoton(Idioma.get(Idioma.Clave.VALIDAR), VERDE);
        TextButton btnGuardar = crearBoton(Idioma.get(Idioma.Clave.GUARDAR), VERDE);
        TextButton btnVolver  = crearBoton(Idioma.get(Idioma.Clave.VOLVER),  NARANJA);
        btnGuardar.setVisible(false);

        btnValidar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        USER u = gestor.buscarUser(usuario);
        if (u == null || !u.getPassword().equals(campoPass.getText().trim())) {
            mensaje.setColor(ROJO);
            mensaje.setText(Idioma.get(Idioma.Clave.CONTRASENA_INCORRECTA));
        } else {
            mensaje.setColor(VERDE);
            mensaje.setText(Idioma.get(Idioma.Clave.CONTRASENA_VALIDADA));
            lblNuevo.setVisible(true);
            campoNuevoUser.setVisible(true);
            btnGuardar.setVisible(true);
            btnValidar.setVisible(false);
            campoPass.setDisabled(true);
        }
        }
        });

        btnGuardar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        String nuevoUser = campoNuevoUser.getText().trim();
        if (nuevoUser.isEmpty()) {
            mensaje.setColor(ROJO);
            mensaje.setText(Idioma.get(Idioma.Clave.INGRESA_USUARIO));
        } else if (gestor.userExiste(nuevoUser)) {
            mensaje.setColor(ROJO);
            mensaje.setText(Idioma.get(Idioma.Clave.USUARIO_YA_EXISTE));
        } else if (gestor.cambiarUsername(usuario, nuevoUser)) {
            usuario = nuevoUser;
            mensaje.setColor(VERDE);
            mensaje.setText(Idioma.get(Idioma.Clave.USUARIO_CAMBIADO));
            btnGuardar.setVisible(false);
        } else {
            mensaje.setColor(ROJO);
            mensaje.setText(Idioma.get(Idioma.Clave.ERROR_CAMBIAR_USUARIO));
        }
        }
        });

        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirMenuPerfil(); }
        });

        tabla.add(titulo).padBottom(16).row();
        tabla.add(lblPass).left().width(280).padBottom(2).row();
        tabla.add(campoPass).width(280).height(36).padBottom(3).row();
        tabla.add(chkVer).left().width(280).padBottom(8).row();
        tabla.add(lblNuevo).left().width(280).padBottom(2).row();
        tabla.add(campoNuevoUser).width(280).height(36).padBottom(8).row();
        tabla.add(mensaje).padBottom(6).row();
        tabla.add(btnValidar).width(280).height(36).padBottom(6).row();
        tabla.add(btnGuardar).width(280).height(36).padBottom(6).row();
        tabla.add(btnVolver).width(280).height(36).row();

        escenario.addActor(tabla);
        }



        private void construirCambiarPassword() {
        escenario.clear();

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Label titulo    = new Label(Idioma.get(Idioma.Clave.CAMBIAR_CONTRASENA), piel);
        titulo.setColor(VERDE);

        Label lblActual = crearLabel(Idioma.get(Idioma.Clave.CONTRASENA_ACTUAL));
        TextField campoActual = crearCampo(Idioma.get(Idioma.Clave.CONTRASENA_ACTUAL));
        campoActual.setPasswordMode(true);
        campoActual.setPasswordCharacter('*');

        CheckBox chkVer = new CheckBox(Idioma.get(Idioma.Clave.VER_CONTRASENA), piel);
        chkVer.addListener(new ChangeListener() {
        public void changed(ChangeEvent event, Actor actor) {
        campoActual.setPasswordMode(!chkVer.isChecked());
        }
        });

        Label lblNueva = crearLabel(Idioma.get(Idioma.Clave.NUEVA_CONTRASENA));
        lblNueva.setVisible(false);
        TextField campoNueva = crearCampo(Idioma.get(Idioma.Clave.NUEVA_CONTRASENA));
        campoNueva.setPasswordMode(true);
        campoNueva.setPasswordCharacter('*');
        campoNueva.setVisible(false);

        Label checksLabel = new Label(
            "X  " + Idioma.get(Idioma.Clave.CHECK_MIN6) + "\n" +
                "X  " + Idioma.get(Idioma.Clave.CHECK_MAYUS) + "\n" +
                "X  " + Idioma.get(Idioma.Clave.CHECK_MINUS) + "\n" +
                "X  " + Idioma.get(Idioma.Clave.CHECK_NUM),
            piel
        );
        checksLabel.setColor(ROJO);
        checksLabel.setVisible(false);

        campoNueva.addListener(new ChangeListener() {
        public void changed(ChangeEvent event, Actor actor) {
        if (campoNueva.isVisible()) actualizarChecks(checksLabel, campoNueva.getText());
        }
        });

        Label mensaje = new Label("", piel);
        mensaje.setColor(ROJO);

        TextButton btnValidar = crearBoton(Idioma.get(Idioma.Clave.VALIDAR), VERDE);
        TextButton btnGuardar = crearBoton(Idioma.get(Idioma.Clave.GUARDAR), VERDE);
        TextButton btnVolver  = crearBoton(Idioma.get(Idioma.Clave.VOLVER),  NARANJA);
        btnGuardar.setVisible(false);

        btnValidar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        USER u = gestor.buscarUser(usuario);
        if (u == null || !u.getPassword().equals(campoActual.getText().trim())) {
            mensaje.setColor(ROJO);
            mensaje.setText(Idioma.get(Idioma.Clave.CONTRASENA_INCORRECTA));
        } else {
            mensaje.setColor(VERDE);
            mensaje.setText(Idioma.get(Idioma.Clave.CONTRASENA_VALIDADA_NUEVA));
            lblNueva.setVisible(true);
            campoNueva.setVisible(true);
            checksLabel.setVisible(true);
            btnGuardar.setVisible(true);
            btnValidar.setVisible(false);
            campoActual.setDisabled(true);
        }
        }
        });

        btnGuardar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        String nueva = campoNueva.getText().trim();
        if (!validarPass(nueva)) {
            mensaje.setColor(ROJO);
            mensaje.setText(Idioma.get(Idioma.Clave.PASS_NO_CUMPLE));
        } else if (gestor.cambiarPassword(usuario, nueva)) {
            mensaje.setColor(VERDE);
            mensaje.setText(Idioma.get(Idioma.Clave.CONTRASENA_CAMBIADA));
            btnGuardar.setVisible(false);
        } else {
            mensaje.setColor(ROJO);
            mensaje.setText(Idioma.get(Idioma.Clave.ERROR_CAMBIAR_PASS));
        }
        }
        });

        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirMenuPerfil(); }
        });

        tabla.add(titulo).padBottom(16).row();
        tabla.add(lblActual).left().width(280).padBottom(2).row();
        tabla.add(campoActual).width(280).height(36).padBottom(3).row();
        tabla.add(chkVer).left().width(280).padBottom(8).row();
        tabla.add(lblNueva).left().width(280).padBottom(2).row();
        tabla.add(campoNueva).width(280).height(36).padBottom(3).row();
        tabla.add(checksLabel).left().width(280).padBottom(8).row();
        tabla.add(mensaje).padBottom(6).row();
        tabla.add(btnValidar).width(280).height(36).padBottom(6).row();
        tabla.add(btnGuardar).width(280).height(36).padBottom(6).row();
        tabla.add(btnVolver).width(280).height(36).row();

        escenario.addActor(tabla);
        }



        private void construirCambiarAvatar() {
        escenario.clear();

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        Label titulo  = new Label(Idioma.get(Idioma.Clave.CAMBIAR_AVATAR), piel);
        titulo.setColor(VERDE);

        Label lblPass = crearLabel(Idioma.get(Idioma.Clave.CONFIRMA_CONTRASENA));
        TextField campoPass = crearCampo(Idioma.get(Idioma.Clave.CONTRASENA));
        campoPass.setPasswordMode(true);
        campoPass.setPasswordCharacter('*');

        CheckBox chkVer = new CheckBox(Idioma.get(Idioma.Clave.VER_CONTRASENA), piel);
        chkVer.addListener(new ChangeListener() {
        public void changed(ChangeEvent event, Actor actor) {
        campoPass.setPasswordMode(!chkVer.isChecked());
        }
        });

        Label mensaje = new Label("", piel);
        mensaje.setColor(ROJO);

        if (avatarTemporalPerfil == null) avatarTemporalPerfil = DEFAULT_AVATAR;
        final String[] avatarSeleccionado = { avatarTemporalPerfil };

        TextButton btnValidar = crearBoton(Idioma.get(Idioma.Clave.VALIDAR), VERDE);
        TextButton btnVolver  = crearBoton(Idioma.get(Idioma.Clave.VOLVER),  NARANJA);

        btnValidar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        USER u = gestor.buscarUser(usuario);
        if (u == null || !u.getPassword().equals(campoPass.getText().trim())) {
        mensaje.setColor(ROJO);
        mensaje.setText(Idioma.get(Idioma.Clave.CONTRASENA_INCORRECTA));
        } else {
        construirPanelAvatares(avatarSeleccionado, () -> {
            avatarTemporalPerfil = avatarSeleccionado[0];
            gestor.cambiarAvatar(usuario, avatarTemporalPerfil);
            avatarTemporalPerfil = null;
            construirMenuPerfil();
        });
        }
        }
        });

        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        avatarTemporalPerfil = null;
        construirMenuPerfil();
        }
        });

        tabla.add(titulo).padBottom(16).row();
        tabla.add(lblPass).left().width(280).padBottom(2).row();
        tabla.add(campoPass).width(280).height(36).padBottom(3).row();
        tabla.add(chkVer).left().width(280).padBottom(8).row();
        tabla.add(mensaje).padBottom(6).row();
        tabla.add(btnValidar).width(280).height(36).padBottom(6).row();
        tabla.add(btnVolver).width(280).height(36).row();

        escenario.addActor(tabla);
        }



        private void construirPanelAvatares(String[] avatarSeleccionado, Runnable alVolver) {
        escenario.clear();

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        String[] cat     = CATEGORIAS[categoriaActual];
        String nombreCat = cat[0];
        String carpeta   = cat[1];

        Label titulo = new Label(Idioma.get(Idioma.Clave.ELIGE_AVATAR), piel);
        titulo.setColor(VERDE);

        Label lblCat = new Label(nombreCat, piel);
        lblCat.setColor(NARANJA);

        Label lblPagina = new Label((categoriaActual + 1) + " / " + CATEGORIAS.length, piel);
        lblPagina.setColor(CAFE);

        Table filaAvatares = new Table();
        for (int i = 2; i < cat.length; i++) {
        String ruta = carpeta + "/" + cat[i] + ".png";
        if (!Gdx.files.internal(ruta).exists()) continue;

        Texture tex = new Texture(Gdx.files.internal(ruta));
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        com.badlogic.gdx.scenes.scene2d.ui.Image img =
        new com.badlogic.gdx.scenes.scene2d.ui.Image(tex);

        img.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        avatarSeleccionado[0] = ruta;
        categoriaActual = 0;
        alVolver.run();
        }
        });
        filaAvatares.add(img).width(70).height(70).pad(8);
        }

        TextButton btnAnterior  = crearBoton(Idioma.get(Idioma.Clave.ANTERIOR),  NARANJA);
        TextButton btnSiguiente = crearBoton(Idioma.get(Idioma.Clave.SIGUIENTE), NARANJA);
        TextButton btnCancelar  = crearBoton(Idioma.get(Idioma.Clave.CANCELAR),  ROJO);

        btnAnterior.setVisible(categoriaActual > 0);
        btnSiguiente.setVisible(categoriaActual < CATEGORIAS.length - 1);

        btnAnterior.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        categoriaActual--;
        construirPanelAvatares(avatarSeleccionado, alVolver);
        }
        });
        btnSiguiente.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        categoriaActual++;
        construirPanelAvatares(avatarSeleccionado, alVolver);
        }
        });
        btnCancelar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        categoriaActual = 0;
        alVolver.run();
        }
        });

        Table filaBotones = new Table();
        filaBotones.add(btnAnterior).width(140).height(36).padRight(10);
        filaBotones.add(btnSiguiente).width(140).height(36);

        tabla.add(titulo).padBottom(10).row();
        tabla.add(lblCat).padBottom(10).row();
        tabla.add(filaAvatares).padBottom(10).row();
        tabla.add(lblPagina).padBottom(10).row();
        tabla.add(filaBotones).padBottom(16).row();
        tabla.add(btnCancelar).width(200).height(36).row();

        escenario.addActor(tabla);
        }
        private void agregarSeccion(Table t, String titulo) {
        Label lbl = new Label("— " + titulo + " —", piel);
        lbl.setColor(NARANJA);
        t.add(lbl).colspan(2).center().padTop(16).padBottom(6).row();
        }

        private void agregarFila(Table t, String clave, String valor) {
        Label lblClave = new Label(clave + ":", piel);
        lblClave.setColor(GRIS);
        Label lblValor = new Label(valor, piel);
        lblValor.setColor(CAFE);
        t.add(lblClave).left().padRight(20).padBottom(6);
        t.add(lblValor).left().padBottom(6).row();
        }

        private void actualizarChecks(Label checksLabel, String pass) {
        boolean lon = pass.length() >= 6;
        boolean may = pass.matches(".*[A-Z].*");
        boolean min = pass.matches(".*[a-z].*");
        boolean num = pass.matches(".*[0-9].*");
        checksLabel.setText(
            (lon ? "OK " : "X  ") + Idioma.get(Idioma.Clave.CHECK_MIN6)  + "\n" +
                (may ? "OK " : "X  ") + Idioma.get(Idioma.Clave.CHECK_MAYUS) + "\n" +
                (min ? "OK " : "X  ") + Idioma.get(Idioma.Clave.CHECK_MINUS) + "\n" +
                (num ? "OK " : "X  ") + Idioma.get(Idioma.Clave.CHECK_NUM)
        );
        checksLabel.setColor(lon && may && min && num ? VERDE : ROJO);
        }

        private boolean validarPass(String pass) {
        return pass.length() >= 6
        && pass.matches(".*[A-Z].*")
        && pass.matches(".*[a-z].*")
        && pass.matches(".*[0-9].*");
        }

        private Label crearLabel(String texto) {
        Label lbl = new Label(texto, piel);
        lbl.setColor(CAFE);
        return lbl;
        }



        private TextField crearCampo(String placeholder) {
        TextField campo = new TextField("", piel);
        campo.setMessageText(placeholder);
        return campo;
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

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GOODDC__.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float escala = Math.min(Gdx.graphics.getWidth() / 640f, Gdx.graphics.getHeight() / 480f);
        param.size = Math.round(18 * escala);
        param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuente = gen.generateFont(param);
        fuente.getData().setScale(1f / escala);

        FreeTypeFontGenerator.FreeTypeFontParameter paramTitulo = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramTitulo.size = Math.round(55 * escala);
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
        estiloBoton.up        = skin.newDrawable("white", VERDE);
        estiloBoton.down      = skin.newDrawable("white", VERDE.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        estiloBoton.over      = skin.newDrawable("white", VERDE.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        skin.add("default", estiloBoton);

        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font             = fuente;
        tfStyle.fontColor        = CAFE;
        tfStyle.messageFontColor = Color.GRAY;
        tfStyle.cursor           = skin.newDrawable("white", CAFE);
        tfStyle.selection        = skin.newDrawable("white", new Color(0.33f, 0.59f, 0.31f, 0.5f));
        tfStyle.background       = skin.newDrawable("white", new Color(0.95f, 0.92f, 0.85f, 1f));
        skin.add("default", tfStyle);

        CheckBox.CheckBoxStyle chkStyle = new CheckBox.CheckBoxStyle();
        chkStyle.font        = fuente;
        chkStyle.fontColor   = CAFE;
        chkStyle.checkboxOn  = skin.newDrawable("white", VERDE);
        chkStyle.checkboxOff = skin.newDrawable("white", new Color(0.7f, 0.7f, 0.7f, 1f));
        skin.add("default", chkStyle);

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

