package com.cutherope;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import LOGIC.LoginManager;

public class LoginScreen implements Screen {

    private CutTheRope game;
    private Stage stage;
    private LoginManager manager;
    private Skin skin;

    private final Color FONDO   = new Color(0.96f, 0.92f, 0.82f, 1f);
    private final Color VERDE   = new Color(0.33f, 0.59f, 0.31f, 1f);
    private final Color CAFE    = new Color(0.23f, 0.16f, 0.08f, 1f);
    private final Color NARANJA = new Color(0.78f, 0.63f, 0.31f, 1f);
    private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);

    private static final String[] AVATARES = {
    "AVATARES/X1.png",
    "AVATARES/X2.png",
    "AVATARES/X3.png",
    "AVATARES/X4.png"
};
    
    private Label mensajeLabel;
    private Label checksLabel;
    private TextField campoUser, campoPass;
    private TextField campoNombre, campoUserCrear, campoPassCrear;

    public LoginScreen(CutTheRope game) {
        this.game    = game;
        this.manager = new LoginManager();
        this.stage   = new Stage(new ScreenViewport());
        this.skin    = crearSkin();
        Gdx.input.setInputProcessor(stage);
        construirMenu();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  PANEL 1 — MENU
    // ══════════════════════════════════════════════════════════════════════════
    private void construirMenu() {
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label titulo = new Label("Cut the Rope", skin);
        titulo.setFontScale(2f);
        titulo.setColor(VERDE);

        Label sub = new Label("Alimenta a Om Nom!", skin);
        sub.setColor(CAFE);

        TextButton btnLogin = crearBoton("Iniciar Sesion", VERDE);
        TextButton btnCrear = crearBoton("Crear Jugador",  NARANJA);
        TextButton btnSalir = crearBoton("Salir",          ROJO);

        btnLogin.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) { construirLogin(); }
        });
        btnCrear.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) { construirCrear(); }
        });
        btnSalir.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) { Gdx.app.exit(); }
        });

        table.add(titulo).padBottom(10).row();
        table.add(sub).padBottom(50).row();
        table.add(btnLogin).width(280).height(50).padBottom(15).row();
        table.add(btnCrear).width(280).height(50).padBottom(15).row();
        table.add(btnSalir).width(280).height(50).row();

        stage.addActor(table);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  PANEL 2 — LOGIN
    // ══════════════════════════════════════════════════════════════════════════
    private void construirLogin() {
        stage.clear();

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label titulo = new Label("Iniciar Sesion", skin);
        titulo.setFontScale(1.8f);
        titulo.setColor(VERDE);

        campoUser = crearCampo("Usuario");
        campoPass = crearCampo("Contrasena");
        campoPass.setPasswordMode(true);
        campoPass.setPasswordCharacter('*');

        CheckBox chkVerPass = new CheckBox(" Ver contrasena", skin);
        chkVerPass.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                campoPass.setPasswordMode(!chkVerPass.isChecked());
            }
        });

        mensajeLabel = new Label("", skin);
        mensajeLabel.setColor(ROJO);

        TextButton btnAceptar = crearBoton("Ingresar", VERDE);
        TextButton btnVolver  = crearBoton("Volver",   NARANJA);

        btnAceptar.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                String user = campoUser.getText().trim();
                String pass = campoPass.getText().trim();
                if (user.isEmpty() || pass.isEmpty()) {
                    mensajeLabel.setText("Completa todos los campos.");
                } else if (!manager.userExiste(user)) {
                    mensajeLabel.setText("Usuario no existe.");
                } else if (manager.login(user, pass)) {
                    final String u = user;
                    Gdx.app.postRunnable(() -> {
                        game.setScreen(new MenuPrincipalScreen(game, u, manager));
                    });
                } else {
                    mensajeLabel.setText("Contrasena incorrecta.");
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

    // ══════════════════════════════════════════════════════════════════════════
    //  PANEL 3 — CREAR JUGADOR
    // ══════════════════════════════════════════════════════════════════════════
    private void construirCrear() {
    stage.clear();

    Table table = new Table();
    table.setFillParent(true);
    table.center();

    Label titulo = new Label("Crear Jugador", skin);
    titulo.setFontScale(1.8f);
    titulo.setColor(VERDE);

    campoNombre    = crearCampo("Nombre Completo");
    campoUserCrear = crearCampo("Usuario");
    campoPassCrear = crearCampo("Contrasena");
    campoPassCrear.setPasswordMode(true);
    campoPassCrear.setPasswordCharacter('*');

    CheckBox chkVerPassCrear = new CheckBox(" Ver contrasena", skin);
    chkVerPassCrear.addListener(new ChangeListener() {
        public void changed(ChangeEvent event, Actor actor) {
            campoPassCrear.setPasswordMode(!chkVerPassCrear.isChecked());
        }
    });

    checksLabel = new Label(
        "X  Min 6 caracteres\nX  Una mayuscula\nX  Una minuscula\nX  Un numero", skin);
    checksLabel.setColor(ROJO);

    campoPassCrear.addListener(new ChangeListener() {
        public void changed(ChangeEvent event, Actor actor) {
            actualizarChecks(campoPassCrear.getText());
        }
    });

    // ── avatares ──────────────────────────────────────────────────────────
    final String[] avatarSeleccionado = { "" };

    Label lblAvatar = new Label("Selecciona tu avatar:", skin);
    lblAvatar.setColor(CAFE);

    Table tablaAvatares = new Table();
    for (String ruta : AVATARES) {
        String nombre = ruta.substring(ruta.lastIndexOf('/') + 1, ruta.lastIndexOf('.'));
        TextButton btnAvatar = crearBoton(nombre, CAFE);
        btnAvatar.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                avatarSeleccionado[0] = ruta;
                mensajeLabel.setColor(VERDE);
                mensajeLabel.setText("Avatar seleccionado: " + nombre);
            }
        });
        tablaAvatares.add(btnAvatar).width(60).height(35).pad(3);
    }

    mensajeLabel = new Label("", skin);
    mensajeLabel.setColor(ROJO);

    TextButton btnCrear  = crearBoton("Crear",  VERDE);
    TextButton btnVolver = crearBoton("Volver", NARANJA);

    btnCrear.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) {
            String nombre = campoNombre.getText().trim();
            String user   = campoUserCrear.getText().trim();
            String pass   = campoPassCrear.getText().trim();
            if (nombre.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                mostrarError("Completa todos los campos.");
            } else if (manager.userExiste(user)) {
                mostrarError("Usuario ya existe.");
            } else if (!validarPass(pass)) {
                mostrarError("La contrasena no cumple los requisitos.");
            } else if (manager.crearUser(user, pass, nombre, avatarSeleccionado[0])) {
                final String u = user;
                Gdx.app.postRunnable(() -> {
                    game.setScreen(new MenuPrincipalScreen(game, u, manager));
                });
            } else {
                mostrarError("Error al crear jugador.");
            }
        }
    });

    btnVolver.addListener(new ClickListener() {
        public void clicked(InputEvent e, float x, float y) { construirMenu(); }
    });

    table.add(titulo).padBottom(20).row();
    table.add(campoNombre).width(300).height(45).padBottom(12).row();
    table.add(campoUserCrear).width(300).height(45).padBottom(12).row();
    table.add(campoPassCrear).width(300).height(45).padBottom(5).row();
    table.add(chkVerPassCrear).left().width(300).padBottom(8).row();
    table.add(checksLabel).left().width(300).padBottom(12).row();
    table.add(lblAvatar).left().width(300).padBottom(6).row();
    table.add(tablaAvatares).width(300).padBottom(12).row();
    table.add(mensajeLabel).padBottom(10).row();
    table.add(btnCrear).width(300).height(45).padBottom(10).row();
    table.add(btnVolver).width(300).height(45).row();

    stage.addActor(table);
}
    
    
    // ══════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ══════════════════════════════════════════════════════════════════════════
    private void actualizarChecks(String pass) {
        boolean lon = pass.length() >= 6;
        boolean may = pass.matches(".*[A-Z].*");
        boolean min = pass.matches(".*[a-z].*");
        boolean num = pass.matches(".*[0-9].*");
        checksLabel.setText(
            (lon ? "OK " : "X  ") + "Min 6 caracteres\n" +
            (may ? "OK " : "X  ") + "Una mayuscula\n"    +
            (min ? "OK " : "X  ") + "Una minuscula\n"    +
            (num ? "OK " : "X  ") + "Un numero"
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
        TextButton btn = new TextButton(texto, skin);
        btn.getStyle().up   = skin.newDrawable("white", color);
        btn.getStyle().down = skin.newDrawable("white", color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
        btn.getStyle().over = skin.newDrawable("white", color.cpy().mul(1.1f, 1.1f, 1.1f, 1f));
        return btn;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SKIN
    // ══════════════════════════════════════════════════════════════════════════
    private Skin crearSkin() {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        BitmapFont font = new BitmapFont();
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

    // ══════════════════════════════════════════════════════════════════════════
    //  SCREEN
    // ══════════════════════════════════════════════════════════════════════════
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(FONDO.r, FONDO.g, FONDO.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void show() { Gdx.input.setInputProcessor(stage); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}