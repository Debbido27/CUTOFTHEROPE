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
        private Texture     btnTexture;
        private Texture     btnOverTexture;
        private SpriteBatch batch;
        private RetosManager retosManager;
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
        this.friendsManager = new FriendsManager(gestor);
        this.retosManager = new RetosManager(gestor);
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

            int totalAmigos      = friendsManager.getAmigos(usuario).length;
            int totalSolicitudes = friendsManager.getSolicitudes(usuario).length;
            int totalRetos = retosManager.getRetosRecibidos(usuario).size();
            int totalNotif = retosManager.contarNoLeidas(usuario);


            Label lblContador = new Label(
                Idioma.get(Idioma.Clave.AMIGOS) + ": " + totalAmigos
                    + (totalSolicitudes > 0 ? "  | Sol: " + totalSolicitudes : "")
                    + (totalNotif > 0       ? "  | Notif: " + totalNotif      : ""), piel);
            lblContador.setColor(NARANJA);

            TextButton btnVerAmigos    = crearBoton(Idioma.get(Idioma.Clave.VER_AMIGOS),      VERDE);
            TextButton btnAgregar      = crearBoton(Idioma.get(Idioma.Clave.AGREGAR_AMIGO),   VERDE);
            TextButton btnRetos        = crearBoton(Idioma.get(Idioma.Clave.RETOS), CAFE.cpy());
            TextButton btnSolicitudes  = crearBoton(Idioma.get(Idioma.Clave.VER_SOLICITUDES), totalSolicitudes > 0 ? NARANJA : GRIS);
            TextButton btnVolver         = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);

            btnVerAmigos.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirVerAmigos(); }
            });
            btnAgregar.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirAgregarAmigo(); }
            });
            btnRetos.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirMenuRetos(); }
            });
            btnSolicitudes.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) { construirVerSolicitudes(); }
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
            tabla.add(btnRetos).width(280).height(44).padBottom(8).row();
            tabla.add(btnSolicitudes).width(280).height(44).padBottom(8).row();
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
                        boolean ok = friendsManager.enviarSolicitud(usuario, busqueda);
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
                boolean ok = friendsManager.enviarSolicitud(usuario, usernameDestino);
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
        // boton con estilo propio para evitar compartir estado
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
        skin.add("btn-up", new com.badlogic.gdx.graphics.g2d.NinePatch(btnTexture, 35, 35, 20, 20));
        btnOverTexture = new Texture(Gdx.files.internal("images/button_over.png"));
        skin.add("btn-over", new com.badlogic.gdx.graphics.g2d.NinePatch(btnOverTexture, 35, 35, 20, 20));

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
        
        
  private void construirRetarAmigo() {
    escenario.clear();
    
    Table contenido = new Table();
    contenido.top().pad(20);
    
    Label titulo = new Label(Idioma.get(Idioma.Clave.RETAR_AMIGO), piel);
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
            
         
            final String nombreAmigo = amigo;
            
            
            String rutaAvatar = (u.getAvatarPath() != null && !u.getAvatarPath().isEmpty())
                ? u.getAvatarPath() : "AVATARS/X1.png";
            
            Table card = new Table();
            card.background(piel.newDrawable("white", new Color(0.9f, 0.87f, 0.78f, 1f)));
            card.pad(8);
            
            if (Gdx.files.internal(rutaAvatar).exists()) {
                Texture tex = new Texture(Gdx.files.internal(rutaAvatar));
                texturasDinamicas.add(tex);
                Image imgAvatar = new Image(tex);
                imgAvatar.setSize(40, 40);
                card.add(imgAvatar).size(40, 40).padRight(10);
            }
            
            Table info = new Table();
            info.left();
            
            Label lblNombre = new Label(u.getFullname(), piel);
            lblNombre.setColor(CAFE);
            Label lblUser = new Label("@" + u.getUsername(), piel);
            lblUser.setColor(NARANJA);
            
            info.add(lblNombre).left().row();
            info.add(lblUser).left();
            
            card.add(info).expandX().left().padRight(10);
            
   
            TextButton btnRetar = crearBoton(Idioma.get(Idioma.Clave.RETAR), VERDE);
            btnRetar.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {
                    construirSelectorNivelReto(nombreAmigo);  
                }
            });
            card.add(btnRetar).width(100).height(38);
            
            contenido.add(card).width(450).padBottom(8).colspan(2).row();
        }
    }
    
    TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);
    btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirMenuRetos(); }
    });
    contenido.add(btnVolver).width(280).height(44).padTop(20).colspan(2).center().row();
    
    ScrollPane.ScrollPaneStyle spStyle = new ScrollPane.ScrollPaneStyle();
    spStyle.background = null;
    ScrollPane scroll = new ScrollPane(contenido, spStyle);
    scroll.setFillParent(true);
    scroll.setScrollingDisabled(true, false);
    
    escenario.addActor(scroll);
}

            
            
            private void construirSelectorNivelReto(String retado) {
                escenario.clear();
                Table tabla = new Table();
                tabla.setFillParent(true);
                tabla.center();

                Label titulo = new Label(Idioma.get(Idioma.Clave.ELEGIR_NIVEL), piel);
                titulo.setColor(VERDE);
                Label sub = new Label(Idioma.get(Idioma.Clave.RETANDO_A) + retado, piel);
                sub.setColor(NARANJA);
                Label mensaje = new Label("", piel);

                tabla.add(titulo).padBottom(8).row();
                tabla.add(sub).padBottom(20).row();

                for (int i = 1; i <= 5; i++) {
                    final int nivelFinal = i;
                    TextButton btnNivel = crearBoton(Idioma.get(Idioma.Clave.NIVEL) + " " + i, VERDE);
                    btnNivel.addListener(new ClickListener() {
                        public void clicked(InputEvent e, float x, float y) {
                            boolean ok = retosManager.enviarReto(usuario, retado, nivelFinal);
                            mensaje.setColor(ok ? VERDE : ROJO);
                            mensaje.setText(ok ? 
                                Idioma.get(Idioma.Clave.RETO_ENVIADO_NIVEL) + nivelFinal + "!" :
                                Idioma.get(Idioma.Clave.RETO_PENDIENTE_EXISTENTE));
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

            private void construirNotificaciones() {
                limpiarTexturas();
                retosManager.marcarTodasLeidas(usuario);
                escenario.clear();

                Table contenido = new Table();
                contenido.top().pad(20);

                Label titulo = new Label(Idioma.get(Idioma.Clave.NOTIFICACIONES), piel);
                titulo.setColor(VERDE);
                contenido.add(titulo).padBottom(16).row();

                String[] solicitudes = friendsManager.getSolicitudes(usuario);
                boolean haySolicitudes = solicitudes.length > 0;
                if (haySolicitudes) {
                    Label lblSec = new Label("— " + Idioma.get(Idioma.Clave.SOLICITUDES_AMISTAD) + " —", piel);
                    lblSec.setColor(NARANJA);
                    contenido.add(lblSec).padBottom(8).row();
                    for (String solicitante : solicitudes) {
                        USER u = gestor.buscarUser(solicitante);
                        if (u == null) continue;
                        Label lbl = new Label("@" + solicitante + " " + Idioma.get(Idioma.Clave.SOLICITUD_ENVIADA), piel);
                        lbl.setColor(CAFE);
                        TextButton btnAc = crearBoton(Idioma.get(Idioma.Clave.ACEPTAR), VERDE);
                        TextButton btnRe = crearBoton(Idioma.get(Idioma.Clave.RECHAZAR), ROJO);
                        btnAc.addListener(new ClickListener() {
                            public void clicked(InputEvent e, float x, float y) {
                                friendsManager.aceptarSolicitud(solicitante, usuario);
                                construirNotificaciones();
                            }
                        });
                        btnRe.addListener(new ClickListener() {
                            public void clicked(InputEvent e, float x, float y) {
                                friendsManager.rechazarSolicitud(solicitante, usuario);
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

                List<Notificacion> notifs = retosManager.getNotificaciones(usuario);
                List<Notificacion> notifsAmistad = new ArrayList<>();
                for (Notificacion n : notifs)
                    if (n.tipo == Notificacion.Tipo.SOLICITUD_RECIBIDA
                        || n.tipo == Notificacion.Tipo.SOLICITUD_ACEPTADA)
                        notifsAmistad.add(n);

                if (!notifsAmistad.isEmpty()) {
                    Label lblSec2 = new Label("— " + Idioma.get(Idioma.Clave.AMIGOS) + " —", piel);
                    lblSec2.setColor(NARANJA);
                    contenido.add(lblSec2).padBottom(8).padTop(12).row();
                    int desde = Math.max(0, notifsAmistad.size() - 10);
                    for (int i = notifsAmistad.size() - 1; i >= desde; i--) {
                        Notificacion n = notifsAmistad.get(i);
                        Label lbl = new Label(textoNotificacion(n), piel);
                        lbl.setColor(n.leida ? GRIS : CAFE);
                        contenido.add(lbl).left().padBottom(4).row();
                    }
                }

                if (!haySolicitudes && notifsAmistad.isEmpty()) {
                    Label lblVacio = new Label(Idioma.get(Idioma.Clave.SIN_NOTIFICACIONES), piel);
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
                    case RETO_RECIBIDO:  return "@" + n.origen + " " + Idioma.get(Idioma.Clave.TE_RETO) + n.nivel;
                    case RETO_ACEPTADO:  return "@" + n.origen + " " + Idioma.get(Idioma.Clave.RETO_ACEPTADO_MSG) + " (" + Idioma.get(Idioma.Clave.NIVEL_ABREV) + " " + n.nivel + ")";
                    case RETO_RECHAZADO: return "@" + n.origen + " " + Idioma.get(Idioma.Clave.RETO_RECHAZADO_MSG) + " (" + Idioma.get(Idioma.Clave.NIVEL_ABREV) + " " + n.nivel + ")";
                    case RETO_GANADO:    return Idioma.get(Idioma.Clave.RETO_GANADO_MSG) + " @" + n.origen + " (" + Idioma.get(Idioma.Clave.NIVEL_ABREV) + " " + n.nivel + ")!";
                    case RETO_PERDIDO:   return Idioma.get(Idioma.Clave.RETO_PERDIDO_MSG) + " @" + n.origen + " (" + Idioma.get(Idioma.Clave.NIVEL_ABREV) + " " + n.nivel + ")";
                    case SOLICITUD_RECIBIDA: return "@" + n.origen + " " + Idioma.get(Idioma.Clave.SOLICITUD_ENVIADA);
                    case SOLICITUD_ACEPTADA: return "@" + n.origen + " " + Idioma.get(Idioma.Clave.ACEPTO_SOLICITUD);
                    default: return Idioma.get(Idioma.Clave.NOTIFICACIONES) + " @" + n.origen;
                }
            }

            private void construirMenuRetos() {
                escenario.clear();
                Table tabla = new Table();
                tabla.setFillParent(true);
                tabla.center();

                Label titulo = new Label(Idioma.get(Idioma.Clave.RETOS), piel);
                titulo.setColor(VERDE);

                int pendRecibidos = retosManager.getRetosRecibidos(usuario).size();
                int pendEnviados  = retosManager.getRetosEnviados(usuario).size();
                int activos       = retosManager.getRetosActivos(usuario).size();

                TextButton btnRetar     = crearBoton(Idioma.get(Idioma.Clave.RETAR), VERDE);
                TextButton btnPend      = crearBoton(
                    Idioma.get(Idioma.Clave.RETOS_PENDIENTES) +
                        ((pendRecibidos + pendEnviados) > 0 ? " (" + (pendRecibidos + pendEnviados) + ")" : ""),
                    (pendRecibidos + pendEnviados) > 0 ? NARANJA : GRIS);
                TextButton btnActivos   = crearBoton(
                    Idioma.get(Idioma.Clave.RETOS_ACTIVOS) + (activos > 0 ? " (" + activos + ")" : ""),
                    activos > 0 ? NARANJA : VERDE);
                TextButton btnHistorial = crearBoton(Idioma.get(Idioma.Clave.HISTORIAL_RETOS), VERDE);
                TextButton btnRanking = crearBoton(Idioma.get(Idioma.Clave.RANKING_AMIGOS), VERDE);
                btnRanking.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirRankingAmigos(); }
                });

                TextButton btnVolver    = crearBoton(Idioma.get(Idioma.Clave.VOLVER), ROJO);

                btnRetar.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirRetarAmigo(); }
                });
                btnPend.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirRetosPendientes(); }
                });
                btnActivos.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirRetosActivos(); }
                });
                btnHistorial.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirHistorialRetos(); }
                });
                btnVolver.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirMenu(); }
                });

                tabla.add(titulo).padBottom(20).row();
                tabla.add(btnRetar).width(280).height(44).padBottom(8).row();
                tabla.add(btnPend).width(280).height(44).padBottom(8).row();
                tabla.add(btnActivos).width(280).height(44).padBottom(8).row();
                tabla.add(btnHistorial).width(280).height(44).padBottom(8).row();
                tabla.add(btnRanking).width(280).height(44).padBottom(8).row();
                tabla.add(btnVolver).width(280).height(44).padTop(10).row();

                escenario.addActor(tabla);
            }

            private void construirRetosPendientes() {
                escenario.clear();
                Table contenido = new Table();
                contenido.top().pad(20);

                Label titulo = new Label(Idioma.get(Idioma.Clave.RETOS_PENDIENTES), piel);
                titulo.setColor(VERDE);
                contenido.add(titulo).padBottom(16).row();

                List<Reto> recibidos = retosManager.getRetosRecibidos(usuario);
                List<Reto> enviados  = retosManager.getRetosEnviados(usuario);

                if (!recibidos.isEmpty()) {
                    Label lblSec = new Label("— " + Idioma.get(Idioma.Clave.RETOS_RECIBIDOS) + " —", piel);
                    lblSec.setColor(NARANJA);
                    contenido.add(lblSec).padBottom(8).row();
                    for (Reto r : recibidos) {
                        Label lbl = new Label("@" + r.retador + Idioma.get(Idioma.Clave.TE_RETO) + r.nivel, piel);
                        lbl.setColor(CAFE);
                        TextButton btnAc = crearBoton(Idioma.get(Idioma.Clave.ACEPTAR), VERDE);
                        TextButton btnRe = crearBoton(Idioma.get(Idioma.Clave.RECHAZAR), ROJO);
                        btnAc.addListener(new ClickListener() {
                            public void clicked(InputEvent e, float x, float y) {
                                retosManager.aceptarReto(r.retador, usuario, r.nivel);
                                construirRetosPendientes();
                            }
                        });
                        btnRe.addListener(new ClickListener() {
                            public void clicked(InputEvent e, float x, float y) {
                                retosManager.rechazarReto(r.retador, usuario, r.nivel);
                                construirRetosPendientes();
                            }
                        });
                        Table fila = new Table();
                        fila.add(lbl).expandX().left().padRight(8);
                        fila.add(btnAc).width(90).height(34).padRight(6);
                        fila.add(btnRe).width(90).height(34);
                        contenido.add(fila).width(500).padBottom(6).row();
                    }
                }

                if (!enviados.isEmpty()) {
                    Label lblSec2 = new Label("— " + Idioma.get(Idioma.Clave.RETOS_ENVIADOS) + " —", piel);
                    lblSec2.setColor(NARANJA);
                    contenido.add(lblSec2).padBottom(8).padTop(12).row();
                    for (Reto r : enviados) {
                        Label lbl = new Label(Idioma.get(Idioma.Clave.RETASTE_A) + r.retado
                            + " (" + Idioma.get(Idioma.Clave.NIVEL_ABREV) + " " + r.nivel + ") — "
                            + Idioma.get(Idioma.Clave.ESPERANDO_RESPUESTA), piel);
                        lbl.setColor(GRIS);
                        contenido.add(lbl).left().padBottom(6).row();
                    }
                }

                if (recibidos.isEmpty() && enviados.isEmpty()) {
                    Label lblVacio = new Label(Idioma.get(Idioma.Clave.SIN_RETOS_PENDIENTES), piel);
                    lblVacio.setColor(GRIS);
                    contenido.add(lblVacio).padBottom(16).row();
                }

                TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);
                btnVolver.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirMenuRetos(); }
                });
                contenido.add(btnVolver).width(280).height(44).padTop(20).row();

                ScrollPane scroll = new ScrollPane(contenido, new ScrollPane.ScrollPaneStyle());
                scroll.setFillParent(true);
                scroll.setScrollingDisabled(true, false);
                escenario.addActor(scroll);
            }

            private void construirRetosActivos() {
                escenario.clear();
                Table contenido = new Table();
                contenido.top().pad(20);

                Label titulo = new Label(Idioma.get(Idioma.Clave.RETOS_ACTIVOS), piel);
                titulo.setColor(VERDE);
                contenido.add(titulo).padBottom(16).row();

                List<Reto> activos = retosManager.getRetosActivos(usuario);

                if (activos.isEmpty()) {
                    Label lblVacio = new Label(Idioma.get(Idioma.Clave.SIN_RETOS_ACTIVOS), piel);
                    lblVacio.setColor(GRIS);
                    contenido.add(lblVacio).padBottom(16).row();
                } else {
                    for (Reto r : activos) {
                        String rival = r.retador.equals(usuario) ? r.retado : r.retador;
                        Label lbl = new Label("@" + rival + " — " + Idioma.get(Idioma.Clave.NIVEL_ABREV) + " " + r.nivel, piel);
                        lbl.setColor(CAFE);

                        TextButton btnEmpezar = crearBoton(Idioma.get(Idioma.Clave.EMPEZAR), VERDE);
                        final int    nivelReto   = r.nivel;
                        final String retadorReto = r.retador;
                        final String retadoReto  = r.retado;
                        btnEmpezar.addListener(new ClickListener() {
                            public void clicked(InputEvent e, float x, float y) {
                                Gdx.app.postRunnable(() -> {
                                    Screen pantallaNivel;
                                    switch (nivelReto) {
                                        case 1: pantallaNivel = new NIVELES.Nivel1Screen(juego, usuario, gestor, retadorReto, retadoReto); break;
                                        case 2: pantallaNivel = new NIVELES.Nivel2Screen(juego, usuario, gestor, retadorReto, retadoReto); break;
                                        case 3: pantallaNivel = new NIVELES.Nivel3Screen(juego, usuario, gestor, retadorReto, retadoReto); break;
                                        case 4: pantallaNivel = new NIVELES.Nivel4Screen(juego, usuario, gestor, retadorReto, retadoReto); break;
                                        case 5: pantallaNivel = new NIVELES.Nivel5Screen(juego, usuario, gestor, retadorReto, retadoReto); break;
                                        default: pantallaNivel = new SeleccionNivelScreen(juego, usuario, gestor); break;
                                    }
                                    juego.setScreen(pantallaNivel);
                                });
                            }
                        });

                        Table fila = new Table();
                        fila.add(lbl).expandX().left().padRight(8);
                        fila.add(btnEmpezar).width(100).height(34);
                        contenido.add(fila).width(450).padBottom(6).row();
                    }
                }

                TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);
                btnVolver.addListener(new ClickListener() {
                    public void clicked(InputEvent e, float x, float y) { construirMenuRetos(); }
                });
                contenido.add(btnVolver).width(280).height(44).padTop(20).row();

                ScrollPane scroll = new ScrollPane(contenido, new ScrollPane.ScrollPaneStyle());
                scroll.setFillParent(true);
                scroll.setScrollingDisabled(true, false);
                escenario.addActor(scroll);
            }

            private void construirHistorialRetos() {
    escenario.clear();
    Table contenido = new Table();
    contenido.top().pad(20);

    Label titulo = new Label(Idioma.get(Idioma.Clave.HISTORIAL_RETOS), piel);
    titulo.setColor(VERDE);
    contenido.add(titulo).padBottom(16).row();

    List<Reto> historial = retosManager.getHistorialRetos(usuario);

    if (historial.isEmpty()) {
        Label lblVacio = new Label(Idioma.get(Idioma.Clave.SIN_RETOS_HISTORIAL), piel);
        lblVacio.setColor(GRIS);
        contenido.add(lblVacio).padBottom(16).row();
    } else {
        Table encabezados = new Table();
        String[] headers = {
            Idioma.get(Idioma.Clave.RETADOR),
            Idioma.get(Idioma.Clave.RETADO),
            Idioma.get(Idioma.Clave.NIVEL_ABREV),
            Idioma.get(Idioma.Clave.GANADOR),
            Idioma.get(Idioma.Clave.RESULTADO)
        };
        for (String h : headers) {
            Label lblHeader = new Label(h, piel);
            lblHeader.setColor(Color.WHITE);
            lblHeader.setFontScale(0.9f);
            encabezados.add(lblHeader).width(h.equals(headers[4]) ? 100 : 90).center();
        }
        contenido.add(encabezados).padBottom(8).row();


        for (Reto r : historial) {
            Table fila = new Table();
            fila.background(piel.newDrawable("white", new Color(0.23f, 0.16f, 0.08f, 0.75f)));
            fila.pad(6);

            String ganador = r.ganador != null ? "@" + r.ganador : "-";
            

            String resultado;
            Color colorResultado;
            
            if (r.estado == Reto.Estado.COMPLETADO) {
                if (r.ganador != null) {
                    if (r.ganador.equals(usuario)) {
                        resultado = Idioma.get(Idioma.Clave.GANASTE);
                        colorResultado = new Color(0.33f, 0.59f, 0.31f, 1f); // Verde
                    } else if (r.ganador.equals(r.retador) || r.ganador.equals(r.retado)) {
                        if (r.retador.equals(usuario) || r.retado.equals(usuario)) {
                            resultado = Idioma.get(Idioma.Clave.PERDISTE);
                            colorResultado = new Color(0.70f, 0.27f, 0.20f, 1f); // Rojo
                        } else {
                            resultado = Idioma.get(Idioma.Clave.GANO) + " @" + r.ganador;
                            colorResultado = new Color(0.78f, 0.63f, 0.31f, 1f); // Naranja
                        }
                    } else {
                        resultado = Idioma.get(Idioma.Clave.GANO) + " @" + r.ganador;
                        colorResultado = new Color(0.78f, 0.63f, 0.31f, 1f);
                    }
                } else {
                    resultado = Idioma.get(Idioma.Clave.COMPLETADO);
                    colorResultado = Color.GRAY;
                }
            } else if (r.estado == Reto.Estado.RECHAZADO) {
                resultado = Idioma.get(Idioma.Clave.RECHAZADO);
                colorResultado = new Color(0.70f, 0.27f, 0.20f, 1f);
            } else if (r.estado == Reto.Estado.PENDIENTE) {
                resultado = Idioma.get(Idioma.Clave.PENDIENTE);
                colorResultado = new Color(0.78f, 0.63f, 0.31f, 1f);
           } else {
                resultado = Idioma.get(Idioma.Clave.PENDIENTE);
                colorResultado = new Color(0.78f, 0.63f, 0.31f, 1f);
            }

            String[] vals = {"@"+r.retador, "@"+r.retado, String.valueOf(r.nivel), ganador, resultado};
            
            for (int i = 0; i < vals.length; i++) {
                Label l = new Label(vals[i], piel);
                if (i == vals.length - 1) { 
                    l.setColor(colorResultado);
                } else {
                    l.setColor(Color.WHITE);
                }
                l.setFontScale(0.85f);
                float ancho = (i == vals.length - 1) ? 100 : 90;
                fila.add(l).width(ancho).center().padLeft(2).padRight(2);
            }
            contenido.add(fila).padBottom(4).row();
        }
    }

    TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);
    btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirMenuRetos(); } 
    });
    contenido.add(btnVolver).width(280).height(44).padTop(20).row();

    ScrollPane scroll = new ScrollPane(contenido, new ScrollPane.ScrollPaneStyle());
    scroll.setFillParent(true);
    scroll.setScrollingDisabled(true, false);
    escenario.addActor(scroll);
}

            private void construirRankingAmigos() {
                escenario.clear();
                Table tabla = new Table();
                tabla.setFillParent(true);
                tabla.center();

                Label titulo = new Label(Idioma.get(Idioma.Clave.RANKING_AMIGOS), piel);
                titulo.setColor(VERDE);
                tabla.add(titulo).padBottom(20).row();

                String[] amigos = friendsManager.getAmigos(usuario);
                String[][] ranking = retosManager.getRankingAmigos(usuario, amigos);

                if (ranking.length == 0) {
                    Label lblVacio = new Label(Idioma.get(Idioma.Clave.SIN_DATOS_RETOS), piel);
                    lblVacio.setColor(GRIS);
                    tabla.add(lblVacio).padBottom(16).row();
                } else {

                    for (int i = 0; i < ranking.length; i++) {
                        String[] entry = ranking[i];
                        boolean esMio  = entry[0].equals(usuario);
                        int pos = i + 1;

                        Color colorFondo;
                        Color colorTexto = CAFE;
                        String etiqueta;
                        switch (pos) {
                            case 1: colorFondo = new Color(1f, 0.84f, 0f, 1f);   etiqueta = Idioma.get(Idioma.Clave.ORO);   break;
                            case 2: colorFondo = new Color(0.75f, 0.75f, 0.75f, 1f); etiqueta = Idioma.get(Idioma.Clave.PLATA); break;
                            default: colorFondo = new Color(0.80f, 0.50f, 0.20f, 1f); etiqueta = Idioma.get(Idioma.Clave.BRONCE); colorTexto = Color.WHITE; break;
                        }

                        Table tarjeta = new Table();
                        tarjeta.background(piel.newDrawable("white", colorFondo));
                        tarjeta.pad(10, 14, 10, 14);

                        Label lblEtiqueta = new Label("#" + pos + " " + etiqueta, piel);
                        lblEtiqueta.setColor(colorTexto);
                        Label lblUser = new Label("@" + entry[0] + (esMio ? " (" + Idioma.get(Idioma.Clave.TU) + ")" : ""), piel);
                        lblUser.setColor(colorTexto);
                        Label lblStats = new Label(
                            Idioma.get(Idioma.Clave.GANADOS) + ": " + entry[1] +
                                "  |  " + Idioma.get(Idioma.Clave.PERDIDOS) + ": " + entry[2], piel);
                        lblStats.setColor(colorTexto);

                        Table izq = new Table();
                        izq.add(lblEtiqueta).left().row();
                        izq.add(lblUser).left();

                        tarjeta.add(izq).expandX().left();
                        tarjeta.add(lblStats).right();

                        tabla.add(tarjeta).width(400).padBottom(8).row();
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
        @Override public void dispose() { escenario.dispose(); piel.dispose(); bgTexture.dispose(); btnTexture.dispose(); btnOverTexture.dispose(); batch.dispose(); limpiarTexturas(); }

        private void limpiarTexturas() { for (Texture t : texturasDinamicas) t.dispose(); texturasDinamicas.clear(); }
}
