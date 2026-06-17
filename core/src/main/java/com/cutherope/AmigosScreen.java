        package com.cutherope;

        import LOGIC.*;
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
        import java.util.ArrayList;
        import LOGIC.RetosManager;
        import LOGIC.Reto;
        import LOGIC.Notificacion;
        import java.util.List;

        public class AmigosScreen implements Screen {

        private CutTheRope   juego;
        private String       usuario;
        private LoginManager gestor;
        private FriendsManager friendsManager;
        private Stage        escenario;
        private Skin         piel;
        private ArrayList<Texture> texturasDinamicas = new ArrayList<>();
        private Texture     bgTexture;
        private SpriteBatch batch;
        private RetosManager retosManager = new RetosManager();
        private final Color FONDO   = new Color(0.96f, 0.92f, 0.82f, 1f);
        private final Color VERDE   = new Color(0.33f, 0.59f, 0.31f, 1f);
        private final Color CAFE    = new Color(0.23f, 0.16f, 0.08f, 1f);
        private final Color NARANJA = new Color(0.78f, 0.63f, 0.31f, 1f);
        private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);
        private final Color GRIS    = new Color(0.60f, 0.60f, 0.60f, 1f);

        public AmigosScreen(CutTheRope juego, String usuario, LoginManager gestor) {
        this.juego         = juego;
        this.usuario       = usuario;
        this.gestor        = gestor;
        this.friendsManager = new FriendsManager();
        this.escenario     = new Stage(new FitViewport(640, 480));
        this.piel          = crearPiel();
        bgTexture = new Texture(Gdx.files.internal("images/mainmenu.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(escenario);
        construirMenu();
        }


        private void construirMenu() {
        escenario.clear();
            USER u = gestor.buscarUser(usuario);
            if (u != null) Idioma.setIngles(u.isIngles());

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

            Label titulo = new Label(Idioma.get(Idioma.Clave.AMIGOS), piel);
        titulo.setColor(VERDE);

        Label sub = new Label("@" + usuario, piel);
        sub.setColor(CAFE);


            int totalRetos = retosManager.getRetosRecibidos(usuario).size();
            int totalNotif = retosManager.contarNoLeidas(usuario);

            Label lblContador = new Label(
                Idioma.get(Idioma.Clave.AMIGOS) + ": " + totalAmigos
                    + (totalSolicitudes > 0 ? "  | Sol: " + totalSolicitudes : "")
                    + (totalNotif > 0       ? "  | Notif: " + totalNotif      : ""), piel);
            lblContador.setColor(NARANJA);

            TextButton btnVerAmigos    = crearBoton(Idioma.get(Idioma.Clave.VER_AMIGOS),      VERDE);
            TextButton btnAgregar      = crearBoton(Idioma.get(Idioma.Clave.AGREGAR_AMIGO),   VERDE);
            TextButton btnRetar        = crearBoton("Retar Amigo",                             CAFE.cpy());
            TextButton btnSolicitudes  = crearBoton(Idioma.get(Idioma.Clave.VER_SOLICITUDES), totalSolicitudes > 0 ? NARANJA : GRIS);
            TextButton btnNotificaciones = crearBoton("Notificaciones" + (totalNotif > 0 ? " (" + totalNotif + ")" : ""),
                totalNotif > 0 ? NARANJA : GRIS);
            TextButton btnHistorialRetos = crearBoton("Historial Retos", VERDE);
            TextButton btnRankingAmigos  = crearBoton("Ranking Amigos",  VERDE);
            TextButton btnVolver         = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);

            btnVerAmigos.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirVerAmigos(); }
            });
            btnAgregar.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirAgregarAmigo(); }
            });
            btnRetar.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirRetarAmigo(); }
            });
            btnSolicitudes.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirVerSolicitudes(); }
            });
            btnNotificaciones.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirNotificaciones(); }
            });
            btnHistorialRetos.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirHistorialRetos(); }
            });
            btnRankingAmigos.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirRankingAmigos(); }
            });
            btnVolver.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    Gdx.app.postRunnable(() ->
                        juego.setScreen(new MenuPrincipalScreen(juego, usuario, gestor)));
                }
            });

            tabla.add(titulo).padBottom(4).row();
            tabla.add(sub).padBottom(4).row();
            tabla.add(lblContador).padBottom(16).row();
            tabla.add(btnVerAmigos).width(280).height(44).padBottom(8).row();
            tabla.add(btnAgregar).width(280).height(44).padBottom(8).row();
            tabla.add(btnRetar).width(280).height(44).padBottom(8).row();
            tabla.add(btnSolicitudes).width(280).height(44).padBottom(8).row();
            tabla.add(btnNotificaciones).width(280).height(44).padBottom(8).row();
            tabla.add(btnHistorialRetos).width(280).height(44).padBottom(8).row();
            tabla.add(btnRankingAmigos).width(280).height(44).padBottom(8).row();
            tabla.add(btnVolver).width(280).height(44).row();

            escenario.addActor(tabla);

        }


        private void construirVerAmigos() {
        escenario.clear();

        Table contenido = new Table();
        contenido.top().pad(20);

            Label titulo = new Label(Idioma.get(Idioma.Clave.MIS_AMIGOS), piel);

        titulo.setColor(VERDE);
        contenido.add(titulo).colspan(2).center().padBottom(20).row();

        String[] amigos = friendsManager.getAmigos(usuario);

        if (amigos.length == 0) {
            Label lblVacio = new Label(Idioma.get(Idioma.Clave.NO_TIENES_AMIGOS), piel);
        lblVacio.setColor(GRIS);
        contenido.add(lblVacio).colspan(2).center().padBottom(20).row();
        } else {
        for (String amigo : amigos) {
            USER u = gestor.buscarUser(amigo);
            if (u == null) continue;

            Label lblNombre = new Label(u.getFullname(), piel);
            lblNombre.setColor(CAFE);
            Label lblUser = new Label("@" + u.getUsername(), piel);
            lblUser.setColor(NARANJA);

            TextButton btnEliminar = crearBoton(Idioma.get(Idioma.Clave.ELIMINAR),     ROJO);
            btnEliminar.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    friendsManager.eliminarAmigo(usuario, amigo);
                    construirVerAmigos();
                }
            });

            Table card = new Table();
            card.background(piel.newDrawable("white", new Color(0.9f, 0.87f, 0.78f, 1f)));
            card.pad(10);
            card.add(lblNombre).left().padRight(10);
            card.add(lblUser).left().expandX();
            card.add(btnEliminar).width(90).height(35);
            TextButton btnDetalles = crearBoton(Idioma.get(Idioma.Clave.VER_DETALLES), NARANJA);
        btnDetalles.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        construirDetallesAmigo(amigo);
        }
        });
        card.add(btnDetalles).width(110).height(35).padRight(6);



            contenido.add(card).width(400).padBottom(8).colspan(2).row();
        }
        }

            TextButton btnVolver   = crearBoton(Idioma.get(Idioma.Clave.VOLVER),       NARANJA);
        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirMenu(); }
        });
        contenido.add(btnVolver).width(280).height(50).padTop(20).colspan(2).center().row();

        ScrollPane.ScrollPaneStyle spStyle = new ScrollPane.ScrollPaneStyle();
        spStyle.background = null;
        ScrollPane scroll = new ScrollPane(contenido, spStyle);
        scroll.setFillParent(true);
        scroll.setScrollingDisabled(true, false);

        escenario.addActor(scroll);
        }


        private void construirAgregarAmigo() {
        escenario.clear();

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

            Label titulo   = new Label(Idioma.get(Idioma.Clave.AGREGAR_AMIGO_TITULO), piel);
        titulo.setColor(VERDE);

            Label lblInfo = new Label(Idioma.get(Idioma.Clave.INGRESA_USUARIO_SOLICITUD), piel);
            lblInfo.setColor(CAFE);

            TextField campoBuscar = crearCampo(Idioma.get(Idioma.Clave.USUARIO));

        Label mensaje = new Label("", piel);
        mensaje.setColor(ROJO);


        Table tablaResultado = new Table();

            TextButton btnVerJugadores = crearBoton(Idioma.get(Idioma.Clave.VER_JUGADORES), NARANJA);
        btnVerJugadores.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerJugadores(); }
        });


            TextButton btnBuscar = crearBoton(Idioma.get(Idioma.Clave.BUSCAR), VERDE);
            TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);

        btnBuscar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
            String busqueda = campoBuscar.getText().trim();
            tablaResultado.clear();

            if (busqueda.isEmpty()) {
                mensaje.setColor(ROJO);
                mensaje.setText(Idioma.get(Idioma.Clave.INGRESA_USUARIO_SOLICITUD));
                return;
            }
            if (busqueda.equals(usuario)) {
                mensaje.setColor(ROJO);
                mensaje.setText(Idioma.get(Idioma.Clave.NO_AUTO_AGREGAR));
                return;
            }

            USER encontrado = gestor.buscarUser(busqueda);
            if (encontrado == null) {
                mensaje.setColor(ROJO);
                mensaje.setText(Idioma.get(Idioma.Clave.USUARIO_NO_EXISTE));
                return;
            }

            mensaje.setText("");

            Label lblNombre = new Label(encontrado.getFullname(), piel);
            lblNombre.setColor(CAFE);
            Label lblUser = new Label("@" + encontrado.getUsername(), piel);
            lblUser.setColor(NARANJA);

            if (friendsManager.sonAmigos(usuario, busqueda)) {
                Label lblYaAmigo = new Label(Idioma.get(Idioma.Clave.YA_AMIGOS), piel);                lblYaAmigo.setColor(VERDE);
                tablaResultado.add(lblNombre).padRight(10);
                tablaResultado.add(lblUser).expandX();
                tablaResultado.add(lblYaAmigo).row();
            } else if (friendsManager.tieneSolicitudPendiente(usuario, busqueda)) {
                Label lblPendiente = new Label(Idioma.get(Idioma.Clave.SOLICITUD_ENVIADA), piel);                lblPendiente.setColor(GRIS);
                tablaResultado.add(lblNombre).padRight(10);
                tablaResultado.add(lblUser).expandX();
                tablaResultado.add(lblPendiente).row();
            } else {
                TextButton btnEnviar = crearBoton(Idioma.get(Idioma.Clave.ENVIAR_SOLICITUD), VERDE);                btnEnviar.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) {
                        boolean ok = friendsManager.enviarSolicitud(usuario, busqueda, gestor);
                        if (ok) {
                            mensaje.setColor(VERDE);
                            mensaje.setText(Idioma.get(Idioma.Clave.SOLICITUD_ENVIADA) + " @" + busqueda);
                            tablaResultado.clear();
                        } else {
                            mensaje.setColor(ROJO);
                            mensaje.setText(Idioma.get(Idioma.Clave.ERROR_SOLICITUD));
                        }
                    }
                });
                tablaResultado.add(lblNombre).padRight(10);
                tablaResultado.add(lblUser).expandX();
                tablaResultado.add(btnEnviar).width(160).height(40).row();
            }
        }
        });




        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirMenu(); }
        });

        tabla.add(titulo).padBottom(30).row();
        tabla.add(lblInfo).left().width(400).padBottom(8).row();
        tabla.add(campoBuscar).width(400).height(45).padBottom(10).row();
        tabla.add(btnBuscar).width(400).height(45).padBottom(10).row();
        tabla.add(tablaResultado).width(400).padBottom(10).row();
        tabla.add(mensaje).padBottom(10).row();
        tabla.add(btnVerJugadores).width(400).height(45).padBottom(15).row();
        tabla.add(btnVolver).width(280).height(50).row();

        escenario.addActor(tabla);
        }


        private void construirVerSolicitudes() {
        escenario.clear();

        Table contenido = new Table();
        contenido.top().pad(20);

            Label titulo   = new Label(Idioma.get(Idioma.Clave.SOLICITUDES_AMISTAD), piel);
        titulo.setFontScale(1.2f);
        titulo.setColor(VERDE);
        contenido.add(titulo).colspan(3).center().padBottom(20).row();

        String[] solicitudes = friendsManager.getSolicitudes(usuario);

        if (solicitudes.length == 0) {
            Label lblVacio = new Label(Idioma.get(Idioma.Clave.NO_SOLICITUDES), piel);
        lblVacio.setColor(GRIS);
        contenido.add(lblVacio).colspan(3).center().padBottom(20).row();
        } else {
        for (String solicitante : solicitudes) {
            USER u = gestor.buscarUser(solicitante);
            if (u == null) continue;

            Label lblNombre = new Label(u.getFullname(), piel);
            lblNombre.setColor(CAFE);
            Label lblUser = new Label("@" + u.getUsername(), piel);
            lblUser.setColor(NARANJA);

            TextButton btnAceptar  = crearBoton(Idioma.get(Idioma.Clave.ACEPTAR),  VERDE);
            TextButton btnRechazar = crearBoton(Idioma.get(Idioma.Clave.RECHAZAR), ROJO);

            btnAceptar.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    friendsManager.aceptarSolicitud(solicitante, usuario);
                    construirVerSolicitudes();
                }
            });
            btnRechazar.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    friendsManager.rechazarSolicitud(solicitante, usuario);
                    construirVerSolicitudes();
                }
            });

            Table card = new Table();
            card.background(piel.newDrawable("white", new Color(0.9f, 0.87f, 0.78f, 1f)));
            card.pad(10);
            card.add(lblNombre).left().padRight(10);
            card.add(lblUser).left().expandX();
            card.add(btnAceptar).width(90).height(35).padRight(6);
            card.add(btnRechazar).width(90).height(35);

            contenido.add(card).width(450).padBottom(8).colspan(3).row();
        }
        }

            TextButton btnVolver   = crearBoton(Idioma.get(Idioma.Clave.VOLVER),   NARANJA);
        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirMenu(); }
        });
        contenido.add(btnVolver).width(280).height(50).padTop(20).colspan(3).center().row();

        ScrollPane.ScrollPaneStyle spStyle = new ScrollPane.ScrollPaneStyle();
        spStyle.background = null;
        ScrollPane scroll = new ScrollPane(contenido, spStyle);
        scroll.setFillParent(true);
        scroll.setScrollingDisabled(true, false);

        escenario.addActor(scroll);
        }


        private void construirVerJugadores() {
        limpiarTexturas();
        escenario.clear();

        Table contenido = new Table();
        contenido.top().pad(20);

            Label titulo = new Label(Idioma.get(Idioma.Clave.JUGADORES), piel);
        titulo.setFontScale(1.2f);
        titulo.setColor(VERDE);
        contenido.add(titulo).colspan(3).center().padBottom(20).row();

        USER[] ranking = gestor.getRanking();

        for (USER u : ranking) {
        if (u == null) continue;
        if (u.getUsername().equals(usuario)) continue;


        String rutaAvatar = (u.getAvatarPath() != null && !u.getAvatarPath().isEmpty())
        ? u.getAvatarPath() : "AVATARS/X1.png";

        Table card = new Table();
        card.background(piel.newDrawable("white", new Color(0.9f, 0.87f, 0.78f, 1f)));
        card.pad(5);

        if (Gdx.files.internal(rutaAvatar).exists()) {
        Texture tex = new Texture(Gdx.files.internal(rutaAvatar));
        texturasDinamicas.add(tex);
        com.badlogic.gdx.scenes.scene2d.ui.Image imgAvatar =
            new com.badlogic.gdx.scenes.scene2d.ui.Image(tex);
        card.add(imgAvatar).width(40).height(40).padRight(8);
        }

        Label lblNombre = new Label(u.getFullname(), piel);
        lblNombre.setColor(CAFE);
        Label lblUser = new Label("@" + u.getUsername(), piel);
        lblUser.setColor(NARANJA);

        Table info = new Table();
        info.add(lblNombre).left().row();
        info.add(lblUser).left().row();
        card.add(info).expandX().left();

        if (friendsManager.sonAmigos(usuario, u.getUsername())) {
            Label lblYa = new Label(Idioma.get(Idioma.Clave.AMIGOS), piel);
            lblYa.setColor(VERDE);
        card.add(lblYa).width(100).center();
        } else if (friendsManager.tieneSolicitudPendiente(usuario, u.getUsername())) {
            Label lblPend = new Label(Idioma.get(Idioma.Clave.SOLICITUD_ENVIADA), piel);
            lblPend.setColor(GRIS);
        card.add(lblPend).width(100).center();
        } else {
            TextButton btnAgregar = crearBoton(Idioma.get(Idioma.Clave.AGREGAR), VERDE);
        final String usernameDestino = u.getUsername();
        btnAgregar.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                boolean ok = friendsManager.enviarSolicitud(usuario, usernameDestino, gestor);
                if (ok) construirVerJugadores();
            }
        });
        card.add(btnAgregar).width(100).height(34);
        }

        contenido.add(card).width(380).padBottom(6).colspan(3).row();
        }

            TextButton btnVolver  = crearBoton(Idioma.get(Idioma.Clave.VOLVER),  NARANJA);
        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirAgregarAmigo(); }
        });
        contenido.add(btnVolver).width(280).height(50).padTop(20).colspan(3).center().row();

        ScrollPane.ScrollPaneStyle spStyle = new ScrollPane.ScrollPaneStyle();
        spStyle.background = null;
        ScrollPane scroll = new ScrollPane(contenido, spStyle);
        scroll.setFillParent(true);
        scroll.setScrollingDisabled(true, false);


        escenario.addActor(scroll);
        }


        private void construirDetallesAmigo(String usernameAmigo) {
        limpiarTexturas();
        escenario.clear();

        USER u = gestor.buscarUser(usernameAmigo);

        Table contenido = new Table();
        contenido.top().pad(20);

            Label titulo = new Label(Idioma.get(Idioma.Clave.PERFIL_DE) + usernameAmigo, piel);
            titulo.setFontScale(1.2f);
        titulo.setColor(VERDE);
        contenido.add(titulo).colspan(2).center().padBottom(20).row();

        if (u != null) {

        String rutaAvatar = (u.getAvatarPath() != null && !u.getAvatarPath().isEmpty())
        ? u.getAvatarPath() : "AVATARS/X1.png";
        if (Gdx.files.internal(rutaAvatar).exists()) {
        Texture tex = new Texture(Gdx.files.internal(rutaAvatar));
        texturasDinamicas.add(tex);
        com.badlogic.gdx.scenes.scene2d.ui.Image imgAvatar =
            new com.badlogic.gdx.scenes.scene2d.ui.Image(tex);
        contenido.add(imgAvatar).width(80).height(80).padBottom(16).colspan(2).center().row();
        }

        long tiempoMs = u.getTiempoTotalJugado();
        String tiempo = String.format("%d min %d seg",
        (tiempoMs / 60000), (tiempoMs % 60000) / 1000);

            agregarFila(contenido, Idioma.get(Idioma.Clave.NOMBRE_COMPLETO_LABEL), u.getFullname());
            agregarFila(contenido, Idioma.get(Idioma.Clave.USUARIO),               "@" + u.getUsername());
            agregarFila(contenido, Idioma.get(Idioma.Clave.NIVELES_COMPLETADOS),   u.getNivelesCompletados() + " / 5");
            agregarFila(contenido, Idioma.get(Idioma.Clave.ESTRELLAS_TOTALES),     String.valueOf(u.getEstrellasTotal()));
            agregarFila(contenido, Idioma.get(Idioma.Clave.PUNTUACION_GENERAL),    String.valueOf(u.getPuntuacionGeneral()));
            agregarFila(contenido, Idioma.get(Idioma.Clave.PARTIDAS_JUGADAS),      String.valueOf(u.getPartidasJugadas()));
            agregarFila(contenido, Idioma.get(Idioma.Clave.TIEMPO_JUGADO),         tiempo);
            TextButton btnEliminar = crearBoton(Idioma.get(Idioma.Clave.ELIMINAR_AMIGO), ROJO);
            TextButton btnVolver   = crearBoton(Idioma.get(Idioma.Clave.VOLVER),         NARANJA);
        } else {
            Label lblError = new Label(Idioma.get(Idioma.Clave.ERROR_CARGAR_PERFIL), piel);
            lblError.setColor(ROJO);
        contenido.add(lblError).colspan(2).center().padBottom(10).row();
        }

            TextButton btnEliminar = crearBoton(Idioma.get(Idioma.Clave.ELIMINAR_AMIGO), ROJO);
        btnEliminar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        friendsManager.eliminarAmigo(usuario, usernameAmigo);
        construirVerAmigos();
        }
        });

            TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);
            btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirVerAmigos(); }
        });

        contenido.add(btnEliminar).width(280).height(50).padTop(20).colspan(2).center().row();
        contenido.add(btnVolver).width(280).height(50).padTop(10).colspan(2).center().row();

        ScrollPane.ScrollPaneStyle spStyle = new ScrollPane.ScrollPaneStyle();
        spStyle.background = null;
        ScrollPane scroll = new ScrollPane(contenido, spStyle);
        scroll.setFillParent(true);
        scroll.setScrollingDisabled(true, false);

        escenario.addActor(scroll);
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
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(Color.WHITE); px.fill();
        skin.add("white", new Texture(px)); px.dispose();
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GOODDC__.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float escala = Math.min(Gdx.graphics.getWidth() / 640f, Gdx.graphics.getHeight() / 480f);
        param.size = Math.round(16 * escala);
        param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont f = gen.generateFont(param);
        f.getData().setScale(1f / escala);
        skin.add("default-font", f);

        FreeTypeFontGenerator.FreeTypeFontParameter paramTitulo = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramTitulo.size = Math.round(32 * escala);
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
        TextField.TextFieldStyle ts = new TextField.TextFieldStyle();
        ts.font = f; ts.fontColor = CAFE; ts.messageFontColor = Color.GRAY;
        ts.cursor = skin.newDrawable("white", CAFE);
        ts.selection = skin.newDrawable("white", new Color(0.33f, 0.59f, 0.31f, 0.5f));
        ts.background = skin.newDrawable("white", new Color(0.95f, 0.92f, 0.85f, 1f));
        skin.add("default", ts);
        return skin;
        }


        private void agregarFila(Table t, String clave, String valor) {
        Label lblClave = new Label(clave + ":", piel);
        lblClave.setColor(GRIS);
        Label lblValor = new Label(valor, piel);
        lblValor.setColor(CAFE);
        t.add(lblClave).left().padRight(20).padBottom(6);
        t.add(lblValor).left().padBottom(6).row();
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
            // ── RETAR AMIGO ──────────────────────────────────────────────────
            private void construirRetarAmigo() {
                escenario.clear();
                Table tabla = new Table();
                tabla.setFillParent(true);
                tabla.center();

                Label titulo  = new Label("Retar a un Amigo", piel);
                titulo.setColor(VERDE);
                Label lblInfo = new Label("Usuario del amigo a retar:", piel);
                lblInfo.setColor(CAFE);

                TextField campoBuscar = crearCampo("@usuario");
                Label mensaje = new Label("", piel);
                Table tablaResultado = new Table();

                TextButton btnBuscar = crearBoton("Buscar", VERDE);
                TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);

                btnBuscar.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) {
                        String busqueda = campoBuscar.getText().trim();
                        tablaResultado.clear();
                        if (busqueda.isEmpty() || busqueda.equals(usuario)) {
                            mensaje.setColor(ROJO); mensaje.setText("Usuario inválido."); return;
                        }
                        if (!friendsManager.sonAmigos(usuario, busqueda)) {
                            mensaje.setColor(ROJO); mensaje.setText("Solo puedes retar a tus amigos."); return;
                        }
                        USER encontrado = gestor.buscarUser(busqueda);
                        if (encontrado == null) {
                            mensaje.setColor(ROJO); mensaje.setText("Usuario no encontrado."); return;
                        }
                        mensaje.setText("");
                        Label lblNombre = new Label(encontrado.getFullname() + " (@" + busqueda + ")", piel);
                        lblNombre.setColor(CAFE);
                        TextButton btnElegirNivel = crearBoton("Elegir Nivel", VERDE);
                        btnElegirNivel.addListener(new ClickListener() {
                            public void clicked(InputEvent e2, float x2, float y2) {
                                construirSelectorNivelReto(busqueda);
                            }
                        });
                        tablaResultado.add(lblNombre).padRight(16);
                        tablaResultado.add(btnElegirNivel).width(140).height(38).row();
                    }
                });
                btnVolver.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirMenu(); }
                });

                tabla.add(titulo).padBottom(20).row();
                tabla.add(lblInfo).left().width(360).padBottom(6).row();
                tabla.add(campoBuscar).width(360).height(40).padBottom(8).row();
                tabla.add(btnBuscar).width(360).height(40).padBottom(8).row();
                tabla.add(tablaResultado).padBottom(8).row();
                tabla.add(mensaje).padBottom(10).row();
                tabla.add(btnVolver).width(280).height(44).row();
                escenario.addActor(tabla);
            }

            // ── SELECTOR DE NIVEL PARA RETO ──────────────────────────────────
            private void construirSelectorNivelReto(String retado) {
                escenario.clear();
                Table tabla = new Table();
                tabla.setFillParent(true);
                tabla.center();

                Label titulo = new Label("Elige el Nivel del Reto", piel);
                titulo.setColor(VERDE);
                Label sub = new Label("Retando a @" + retado, piel);
                sub.setColor(NARANJA);
                Label mensaje = new Label("", piel);

                tabla.add(titulo).padBottom(8).row();
                tabla.add(sub).padBottom(20).row();

                for (int i = 1; i <= 5; i++) {
                    final int n = i;
                    TextButton btnNivel = crearBoton("Nivel " + i, VERDE);
                    btnNivel.addListener(new ClickListener() {
                        public void clicked(InputEvent e, float x, float y) {
                            boolean ok = retosManager.enviarReto(usuario, retado, n);
                            mensaje.setColor(ok ? VERDE : ROJO);
                            mensaje.setText(ok ? "¡Reto enviado al Nivel " + n + "!"
                                : "Ya existe un reto pendiente.");
                        }
                    });
                    tabla.add(btnNivel).width(260).height(40).padBottom(8).row();
                }

                tabla.add(mensaje).padBottom(10).row();
                TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);
                btnVolver.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirRetarAmigo(); }
                });
                tabla.add(btnVolver).width(260).height(44).row();
                escenario.addActor(tabla);
            }

            // ── NOTIFICACIONES ───────────────────────────────────────────────
            private void construirNotificaciones() {
                retosManager.marcarTodasLeidas(usuario);
                escenario.clear();

                Table contenido = new Table();
                contenido.top().pad(20);

                Label titulo = new Label("Notificaciones", piel);
                titulo.setColor(VERDE);
                contenido.add(titulo).padBottom(16).row();

                // Retos pendientes recibidos (con botones aceptar/rechazar)
                List<Reto> retosPend = retosManager.getRetosRecibidos(usuario);
                if (!retosPend.isEmpty()) {
                    Label lblSec = new Label("— Retos Pendientes —", piel);
                    lblSec.setColor(NARANJA);
                    contenido.add(lblSec).padBottom(8).row();
                    for (Reto r : retosPend) {
                        Label lbl = new Label("@" + r.retador + " te retó al Nivel " + r.nivel, piel);
                        lbl.setColor(CAFE);
                        TextButton btnAc = crearBoton("Aceptar", VERDE);
                        TextButton btnRe = crearBoton("Rechazar", ROJO);
                        btnAc.addListener(new ClickListener() {
                            public void clicked(InputEvent e, float x, float y) {
                                retosManager.aceptarReto(r.retador, usuario, r.nivel);
                                construirNotificaciones();
                            }
                        });
                        btnRe.addListener(new ClickListener() {
                            public void clicked(InputEvent e, float x, float y) {
                                retosManager.rechazarReto(r.retador, usuario, r.nivel);
                                construirNotificaciones();
                            }
                        });
                        Table fila = new Table();
                        fila.add(lbl).expandX().left().padRight(8);
                        fila.add(btnAc).width(90).height(34).padRight(6);
                        fila.add(btnRe).width(90).height(34);
                        contenido.add(fila).width(500).padBottom(6).row();
                    }
                }

                // Notificaciones generales
                List<Notificacion> notifs = retosManager.getNotificaciones(usuario);
                if (!notifs.isEmpty()) {
                    Label lblSec2 = new Label("— Eventos —", piel);
                    lblSec2.setColor(NARANJA);
                    contenido.add(lblSec2).padBottom(8).padTop(12).row();
                    // Mostrar las últimas 10, más recientes primero
                    int desde = Math.max(0, notifs.size() - 10);
                    for (int i = notifs.size() - 1; i >= desde; i--) {
                        Notificacion n = notifs.get(i);
                        Label lbl = new Label(textoNotificacion(n), piel);
                        lbl.setColor(n.leida ? GRIS : CAFE);
                        contenido.add(lbl).left().padBottom(4).row();
                    }
                }

                if (retosPend.isEmpty() && notifs.isEmpty()) {
                    Label lblVacio = new Label("Sin notificaciones.", piel);
                    lblVacio.setColor(GRIS);
                    contenido.add(lblVacio).padBottom(16).row();
                }

                TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);
                btnVolver.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirMenu(); }
                });
                contenido.add(btnVolver).width(280).height(44).padTop(20).row();

                ScrollPane scroll = new ScrollPane(contenido, new ScrollPane.ScrollPaneStyle());
                scroll.setFillParent(true);
                scroll.setScrollingDisabled(true, false);
                escenario.addActor(scroll);
            }

            private String textoNotificacion(Notificacion n) {
                switch (n.tipo) {
                    case SOLICITUD_RECIBIDA:  return "@" + n.origen + " te envió solicitud de amistad.";
                    case SOLICITUD_ACEPTADA:  return "@" + n.origen + " aceptó tu solicitud.";
                    case RETO_RECIBIDO:       return "@" + n.origen + " te retó al Nivel " + n.nivel + ".";
                    case RETO_ACEPTADO:       return "@" + n.origen + " aceptó tu reto (Nv." + n.nivel + ").";
                    case RETO_RECHAZADO:      return "@" + n.origen + " rechazó tu reto (Nv." + n.nivel + ").";
                    case RETO_GANADO:         return "¡Ganaste el reto contra @" + n.origen + " (Nv." + n.nivel + ")!";
                    case RETO_PERDIDO:        return "Perdiste el reto contra @" + n.origen + " (Nv." + n.nivel + ").";
                    default:                  return "Notificación de @" + n.origen;
                }
            }

            // ── HISTORIAL DE RETOS ───────────────────────────────────────────
            private void construirHistorialRetos() {
                escenario.clear();
                Table contenido = new Table();
                contenido.top().pad(20);

                Label titulo = new Label("Historial de Retos", piel);
                titulo.setColor(VERDE);
                contenido.add(titulo).padBottom(16).row();

                List<Reto> historial = retosManager.getHistorialRetos(usuario);

                if (historial.isEmpty()) {
                    Label lblVacio = new Label("Sin retos registrados.", piel);
                    lblVacio.setColor(GRIS);
                    contenido.add(lblVacio).padBottom(16).row();
                } else {
                    // Encabezado
                    Table enc = new Table();
                    for (String h : new String[]{"Retador","Retado","Nv.","Ganador","Estado"}) {
                        Label l = new Label(h, piel); l.setColor(NARANJA);
                        enc.add(l).width(h.equals("Estado") ? 110 : 90).center();
                    }
                    contenido.add(enc).padBottom(6).row();

                    for (Reto r : historial) {
                        Table fila = new Table();
                        String ganador = r.ganador != null ? "@" + r.ganador : "-";
                        String estado  = r.estado.name();
                        for (String v : new String[]{
                            "@"+r.retador, "@"+r.retado,
                            String.valueOf(r.nivel), ganador, estado}) {
                            Label l = new Label(v, piel); l.setColor(CAFE);
                            fila.add(l).width(v.equals(estado) ? 110 : 90).center();
                        }
                        contenido.add(fila).padBottom(4).row();
                    }
                }

                TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);
                btnVolver.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirMenu(); }
                });
                contenido.add(btnVolver).width(280).height(44).padTop(20).row();

                ScrollPane scroll = new ScrollPane(contenido, new ScrollPane.ScrollPaneStyle());
                scroll.setFillParent(true);
                scroll.setScrollingDisabled(true, false);
                escenario.addActor(scroll);
            }

            // ── RANKING DE AMIGOS ────────────────────────────────────────────
            private void construirRankingAmigos() {
                escenario.clear();
                Table tabla = new Table();
                tabla.setFillParent(true);
                tabla.center();

                Label titulo = new Label("Ranking de Amigos", piel);
                titulo.setColor(VERDE);
                tabla.add(titulo).padBottom(20).row();

                String[] amigos = friendsManager.getAmigos(usuario);
                String[][] ranking = retosManager.getRankingAmigos(usuario, amigos);

                if (ranking.length == 0) {
                    Label lblVacio = new Label("Sin datos de retos aún.", piel);
                    lblVacio.setColor(GRIS);
                    tabla.add(lblVacio).padBottom(16).row();
                } else {
                    for (int i = 0; i < ranking.length; i++) {
                        String[] entry = ranking[i];
                        boolean esMio  = entry[0].equals(usuario);

                        Label lblPos = new Label("TOP " + (i+1), piel);
                        lblPos.setColor(NARANJA);
                        Label lblUser = new Label("@" + entry[0], piel);
                        lblUser.setColor(esMio ? VERDE : CAFE);
                        Label lblStats = new Label(
                            "  Ganados: " + entry[1] + "  |  Perdidos: " + entry[2], piel);
                        lblStats.setColor(GRIS);

                        tabla.add(lblPos).left().padBottom(2).row();
                        tabla.add(lblUser).left().padLeft(16).padBottom(2).row();
                        tabla.add(lblStats).left().padLeft(16).padBottom(12).row();
                    }
                }

                TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);
                btnVolver.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirMenu(); }
                });
                tabla.add(btnVolver).width(280).height(44).padTop(10).row();
                escenario.addActor(tabla);
            }

        @Override public void resize(int w, int h) { escenario.getViewport().update(w, h, true); }
        @Override public void show()    { Gdx.input.setInputProcessor(escenario); }
        @Override public void pause()   {}
        @Override public void resume()  {}
        @Override public void hide()    {}
        @Override public void dispose() { escenario.dispose(); piel.dispose(); bgTexture.dispose(); batch.dispose(); limpiarTexturas(); }

        private void limpiarTexturas() { for (Texture t : texturasDinamicas) t.dispose(); texturasDinamicas.clear(); }
        }
