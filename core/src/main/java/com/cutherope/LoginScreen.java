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
        import com.badlogic.gdx.scenes.scene2d.Actor;
        import com.badlogic.gdx.scenes.scene2d.InputEvent;
        import com.badlogic.gdx.scenes.scene2d.Stage;
        import com.badlogic.gdx.scenes.scene2d.ui.*;
        import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
        import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
        import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
        import com.badlogic.gdx.utils.viewport.FitViewport;
        import LOGIC.LoginManager;

        public class LoginScreen implements Screen {

        private CutTheRope game;
        private Stage stage;
        private LoginManager manager;
        private Skin skin;
        private Texture logoTexture;
        private Texture bgTexture;
        private Texture btnTexture;
        private Texture btnOverTexture;
        private SpriteBatch batch;
        private int categoriaActual = 0;
        private final Color FONDO   = new Color(0.96f, 0.92f, 0.82f, 1f);
        private final Color VERDE   = new Color(0.33f, 0.59f, 0.31f, 1f);
        private final Color CAFE    = new Color(0.23f, 0.16f, 0.08f, 1f);
        private final Color NARANJA = new Color(0.78f, 0.63f, 0.31f, 1f);
        private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);
        private static final String DEFAULT_AVATAR = "AVATARS/X1.png";
        private String avatarTemporal = DEFAULT_AVATAR;
        private static final String[][] CATEGORIAS = {
        { "Dragon Ball",       "AVATARS/DRAGON",      "D1","D2","D3","D4" },
        { "Rapidos y Furiosos","AVATARS/FNR",          "R1","R2","R3","R4","R5" },
        { "Invincible",        "AVATARS/INVINCIBLE",   "I1","I2","I3","I4" },
        { "Transformers",      "AVATARS/TRANSFORMER",  "T1","T2","T3","T4" },
        { "Futbol",            "AVATARS/futbol",        "F1","F2","F3","F4" }
        };

        private Label mensajeLabel;
        private Label checksLabel;
        private TextField campoUser, campoPass;
        private TextField campoNombre, campoUserCrear, campoPassCrear;

        public LoginScreen(CutTheRope game) {
        this.game    = game;
        this.manager = new LoginManager();
        this.stage   = new Stage(new FitViewport(640, 480));
        this.skin    = crearSkin();
        this.logoTexture = new Texture(Gdx.files.internal("images/Cut_the_Rope_Logo.png"));
        this.bgTexture   = new Texture(Gdx.files.internal("images/mainmenu.png"));
        this.bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.batch       = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        construirMenu();
        }


        private void construirMenu() {
        stage.clear();
        LOGIC.USER ultimo = manager.getCurrentUser();
        if (ultimo != null) Idioma.setIngles(ultimo.isIngles());
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Image logo = new Image(logoTexture);


        Label sub = new Label(Idioma.get(Idioma.Clave.ALIMENTA), skin);
        TextButton btnLogin = crearBoton(Idioma.get(Idioma.Clave.INICIAR_SESION), VERDE);
        TextButton btnCrear = crearBoton(Idioma.get(Idioma.Clave.CREAR_JUGADOR),  NARANJA);
        TextButton btnSalir = crearBoton(Idioma.get(Idioma.Clave.SALIR),          ROJO);

        btnLogin.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirLogin(); }
        });
        btnCrear.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirCrear(); }
        });
        btnSalir.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { Gdx.app.exit(); }
        });

        table.add(logo).width(300).height(110).padBottom(10).row();
        table.add(sub).padBottom(50).row();
        table.add(btnLogin).width(280).height(50).padBottom(15).row();
        table.add(btnCrear).width(280).height(50).padBottom(15).row();
        table.add(btnSalir).width(280).height(50).row();

        stage.addActor(table);
        }


        private void construirLogin() {
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label titulo = new Label(Idioma.get(Idioma.Clave.INICIAR_SESION), skin);
        campoUser = crearCampo(Idioma.get(Idioma.Clave.USUARIO));
        campoPass = crearCampo(Idioma.get(Idioma.Clave.CONTRASENA));
        campoPass.setPasswordMode(true);
        campoPass.setPasswordCharacter('*');

        CheckBox chkVerPass = new CheckBox(Idioma.get(Idioma.Clave.VER_CONTRASENA), skin);
        chkVerPass.addListener(new ChangeListener() {
        public void changed(ChangeEvent event, Actor actor) {
            campoPass.setPasswordMode(!chkVerPass.isChecked());
        }
        });

        mensajeLabel = new Label("", skin);
        mensajeLabel.setColor(ROJO);

        TextButton btnAceptar = crearBoton(Idioma.get(Idioma.Clave.INGRESAR), VERDE);
        TextButton btnVolver  = crearBoton(Idioma.get(Idioma.Clave.VOLVER),   NARANJA);

        btnAceptar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
            String user = campoUser.getText().trim();
            String pass = campoPass.getText().trim();
            if (user.isEmpty() || pass.isEmpty()) {
                mensajeLabel.setText(Idioma.get(Idioma.Clave.CAMPOS_VACIOS));
            } else if (!manager.userExiste(user)) {
                mensajeLabel.setText(Idioma.get(Idioma.Clave.USUARIO_NO_EXISTE));
            } else if (manager.login(user, pass)) {
                final String u = user;
                LOGIC.SesionJuego.get().iniciar(u, manager);
                Gdx.app.postRunnable(() -> {
                    game.setScreen(new MenuPrincipalScreen(game, u, manager));
                });
            } else {
                mensajeLabel.setText(Idioma.get(Idioma.Clave.CONTRASENA_INCORRECTA));
            }
        }
        });

        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
            campoUser.setText("");
            campoPass.setText("");
            mensajeLabel.setText("");
            construirMenu();
        }
        });

        table.add(titulo).padBottom(30).row();
        table.add(campoUser).width(300).height(45).padBottom(15).row();
        table.add(campoPass).width(300).height(45).padBottom(5).row();
        table.add(chkVerPass).left().width(300).padBottom(15).row();
        table.add(mensajeLabel).padBottom(10).row();
        table.add(btnAceptar).width(300).height(45).padBottom(10).row();
        table.add(btnVolver).width(300).height(45).row();

        stage.addActor(table);
        }


        private void construirCrear() {
        stage.clear();

        final String[] avatarSeleccionado = { avatarTemporal };

        final Table[] tableRef = { new Table() };
        Table table = tableRef[0];
        table.setFillParent(true);
        table.center();

        Label titulo = new Label(Idioma.get(Idioma.Clave.CREAR_JUGADOR), skin);
        campoNombre    = crearCampo(Idioma.get(Idioma.Clave.NOMBRE_COMPLETO));
        campoUserCrear = crearCampo(Idioma.get(Idioma.Clave.USUARIO));
        campoPassCrear = crearCampo(Idioma.get(Idioma.Clave.CONTRASENA));
        campoPassCrear.setPasswordMode(true);
        campoPassCrear.setPasswordCharacter('*');

        CheckBox chkVerPassCrear = new CheckBox(Idioma.get(Idioma.Clave.VER_CONTRASENA), skin);
        chkVerPassCrear.addListener(new ChangeListener() {
        public void changed(ChangeEvent event, Actor actor) {
        campoPassCrear.setPasswordMode(!chkVerPassCrear.isChecked());
        }
        });

        checksLabel = new Label(
            "X  " + Idioma.get(Idioma.Clave.CHECK_MIN6) + "\n" +
                "X  " + Idioma.get(Idioma.Clave.CHECK_MAYUS) + "\n" +
                "X  " + Idioma.get(Idioma.Clave.CHECK_MINUS) + "\n" +
                "X  " + Idioma.get(Idioma.Clave.CHECK_NUM), skin);
        checksLabel.setColor(ROJO);

        campoPassCrear.addListener(new ChangeListener() {
        public void changed(ChangeEvent event, Actor actor) {
        actualizarChecks(campoPassCrear.getText());
        }
        });

        Label lblAvatarElegido = new Label("Avatar: default", skin);
        lblAvatarElegido.setColor(CAFE);

        TextButton btnAvatar = crearBoton(Idioma.get(Idioma.Clave.ELEGIR_AVATAR), NARANJA);
        btnAvatar.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        construirPanelAvatares(avatarSeleccionado, () -> {
        avatarTemporal = avatarSeleccionado[0];
        String nombre = avatarTemporal;
        int slash = nombre.lastIndexOf('/'); int dot = nombre.lastIndexOf('.');
        if (dot > slash + 1) nombre = nombre.substring(slash + 1, dot);
        lblAvatarElegido.setText("Avatar: " + nombre);
        stage.clear();
        stage.addActor(table);
        });
        }
        });

        mensajeLabel = new Label("", skin);
        mensajeLabel.setColor(ROJO);

        TextButton btnCrear  = crearBoton(Idioma.get(Idioma.Clave.CREAR),  VERDE);
        TextButton btnVolver = crearBoton(Idioma.get(Idioma.Clave.VOLVER), NARANJA);

        btnCrear.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        String nombre = campoNombre.getText().trim();
        String user   = campoUserCrear.getText().trim();
        String pass   = campoPassCrear.getText().trim();
        if (nombre.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            mostrarError(Idioma.get(Idioma.Clave.CAMPOS_VACIOS));
        } else if (manager.userExiste(user)) {
            mostrarError(Idioma.get(Idioma.Clave.USUARIO_YA_EXISTE));
        } else if (!validarPass(pass)) {
            mostrarError(Idioma.get(Idioma.Clave.PASS_NO_CUMPLE));
        } else if (manager.crearUser(user, pass, nombre, avatarSeleccionado[0])) {
            final String u = user;
            LOGIC.SesionJuego.get().iniciar(u, manager);

            Gdx.app.postRunnable(() ->
                game.setScreen(new MenuPrincipalScreen(game, u, manager)));
        } else {
            mostrarError(Idioma.get(Idioma.Clave.ERROR_CREAR));
        }

        }
        });

        btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
        avatarTemporal = DEFAULT_AVATAR;
        construirMenu();
        }
        });

        table.add(titulo).padBottom(6).row();
        table.add(campoNombre).width(260).height(32).padBottom(4).row();
        table.add(campoUserCrear).width(260).height(32).padBottom(4).row();
        table.add(campoPassCrear).width(260).height(32).padBottom(2).row();
        table.add(chkVerPassCrear).left().width(260).padBottom(2).row();
        table.add(checksLabel).left().width(260).padBottom(4).row();
        table.add(lblAvatarElegido).center().padBottom(2).row();
        table.add(btnAvatar).width(260).height(32).padBottom(4).row();
        table.add(mensajeLabel).padBottom(2).row();
        table.add(btnCrear).width(260).height(32).padBottom(4).row();
        table.add(btnVolver).width(260).height(32).row();

        stage.addActor(table);
        }

        private void construirPanelAvatares(String[] avatarSeleccionado, Runnable alVolver) {
        stage.clear();

        Table tabla = new Table();
        tabla.setFillParent(true);
        tabla.center();

        String[] cat     = CATEGORIAS[categoriaActual];
        String nombreCat = cat[0];
        String carpeta   = cat[1];

        Label titulo = new Label(Idioma.get(Idioma.Clave.ELIGE_AVATAR), skin);
        titulo.setColor(VERDE);

        Label lblCat = new Label(nombreCat, skin);
        lblCat.setColor(NARANJA);

        Label lblPagina = new Label((categoriaActual + 1) + " / " + CATEGORIAS.length, skin);
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

        stage.addActor(tabla);
        }


        private void actualizarChecks(String pass) {
        boolean lon = pass.length() >= 6;
        boolean may = pass.matches(".*[A-Z].*");
        boolean min = pass.matches(".*[a-z].*");
        boolean num = pass.matches(".*[0-9].*");
        checksLabel.setText(
            (lon ? "OK " : "X  ") + Idioma.get(Idioma.Clave.CHECK_MIN6) + "\n" +
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

        private void mostrarError(String msg) {
        mensajeLabel.setColor(ROJO);
        mensajeLabel.setText(msg);
        }

        private void mostrarExito(String msg) {
        mensajeLabel.setColor(VERDE);
        mensajeLabel.setText(msg);
        }

        private TextField crearCampo(String placeholder) {
        TextField campo = new TextField("", skin);
        campo.setMessageText(placeholder);
        return campo;
        }

        private TextButton crearBoton(String texto, Color color) {
        // boton con estilo propio para evitar compartir estado
        TextButton.TextButtonStyle estilo = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        estilo.up   = skin.newDrawable("btn-up", color);
        estilo.down = skin.newDrawable("btn-up", color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        estilo.over = skin.newDrawable("btn-over", color.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        TextButton btn = new TextButton(texto, estilo);
        btn.pad(5, 15, 5, 15);
        return btn;
        }


        private Skin crearSkin() {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        // texturas del boton como ninepatch para evitar estiramiento
        btnTexture = new Texture(Gdx.files.internal("images/button.png"));
        skin.add("btn-up", new com.badlogic.gdx.graphics.g2d.NinePatch(btnTexture, 35, 35, 20, 20));
        btnOverTexture = new Texture(Gdx.files.internal("images/button_over.png"));
        skin.add("btn-over", new com.badlogic.gdx.graphics.g2d.NinePatch(btnOverTexture, 35, 35, 20, 20));

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GOODDC__.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float escala = Math.min(Gdx.graphics.getWidth() / 640f, Gdx.graphics.getHeight() / 480f);
        param.size = Math.round(14 * escala);
        param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont font = gen.generateFont(param);
        font.getData().setScale(1f / escala);
        FreeTypeFontGenerator.FreeTypeFontParameter paramTitulo = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramTitulo.size = Math.round(200 * escala);
        paramTitulo.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áéíóúÁÉÍÓÚñÑüÜ¡¿";
        BitmapFont fuenteTitulo = gen.generateFont(paramTitulo);
        fuenteTitulo.getData().setScale(1f / escala);
        gen.dispose();

        skin.add("font-titulo", fuenteTitulo);

        Label.LabelStyle estiloTitulo = new Label.LabelStyle();
        estiloTitulo.font = fuenteTitulo;
        estiloTitulo.fontColor = CAFE;
        skin.add("titulo", estiloTitulo);
        skin.add("default-font", font);




        Label.LabelStyle lblStyle = new Label.LabelStyle();
        lblStyle.font      = font;
        lblStyle.fontColor = CAFE;
        skin.add("default", lblStyle);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font      = font;
        btnStyle.fontColor = Color.WHITE;
        btnStyle.up        = skin.newDrawable("white", VERDE);
        btnStyle.down      = skin.newDrawable("white", VERDE.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        btnStyle.over      = skin.newDrawable("white", VERDE.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        skin.add("default", btnStyle);

        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font             = font;
        tfStyle.fontColor        = CAFE;
        tfStyle.messageFontColor = Color.GRAY;
        tfStyle.cursor           = skin.newDrawable("white", CAFE);
        tfStyle.selection        = skin.newDrawable("white", new Color(0.33f, 0.59f, 0.31f, 0.5f));
        tfStyle.background       = skin.newDrawable("white", new Color(0.95f, 0.92f, 0.85f, 1f));
        skin.add("default", tfStyle);

        CheckBox.CheckBoxStyle chkStyle = new CheckBox.CheckBoxStyle();
        chkStyle.font        = font;
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
        stage.getViewport().apply(true);
        stage.act(delta);
        stage.draw();
        }

        @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
        @Override public void show() { Gdx.input.setInputProcessor(stage); }
        @Override public void pause() {}
        @Override public void resume() {}
        @Override public void hide() {}
        @Override public void dispose() { stage.dispose(); skin.dispose(); logoTexture.dispose(); bgTexture.dispose(); btnTexture.dispose(); btnOverTexture.dispose(); batch.dispose(); }
        }
