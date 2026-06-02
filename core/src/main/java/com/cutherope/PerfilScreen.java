/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cutherope;

import LOGIC.LoginManager;
import LOGIC.USER;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.time.format.DateTimeFormatter;


public class PerfilScreen implements Screen{
    
    private CutTheRope   juego;
    private String       usuario;
    private LoginManager gestor;
    private Stage        escenario;
    private Skin         piel;

    private final Color FONDO   = new Color(0.96f, 0.92f, 0.82f, 1f);
    private final Color VERDE   = new Color(0.33f, 0.59f, 0.31f, 1f);
    private final Color CAFE    = new Color(0.23f, 0.16f, 0.08f, 1f);
    private final Color NARANJA = new Color(0.78f, 0.63f, 0.31f, 1f);
    private final Color ROJO    = new Color(0.70f, 0.27f, 0.20f, 1f);
    private final Color GRIS    = new Color(0.60f, 0.60f, 0.60f, 1f);
   
    private static final String[] AVATARES = {
        "avatares/avatar1.png",
        "avatares/avatar2.png",
        "avatares/avatar3.png",
        "avatares/avatar4.png"
    };
    
    
     public PerfilScreen(CutTheRope juego, String usuario, LoginManager gestor) {
        this.juego    = juego;
        this.usuario  = usuario;
        this.gestor   = gestor;
        this.escenario = new Stage(new ScreenViewport());
        this.piel      = crearPiel();
        Gdx.input.setInputProcessor(escenario);
        construirMenuPerfil();
    }
     
     private void construirMenuPerfil(){
         escenario.clear();
         
         Table tabla = new Table();
         tabla.setFillParent(true);
         tabla.center();
         
         Label titulo = new Label("Mi perfil", piel);
         titulo.setFontScale(2f);
         titulo.setColor(VERDE);
         
          USER u = gestor.buscarUser(usuario);
        String avatarInfo = (u != null && !u.getAvatarPath().isEmpty())
            ? u.getAvatarPath() : "Sin avatar";

        Label subAvatar = new Label("Avatar: " + avatarInfo, piel);
        subAvatar.setColor(CAFE);

        Label subUsuario = new Label("@" + usuario, piel);
        subUsuario.setColor(NARANJA);

        TextButton btnVerInfo      = crearBoton("Ver Informacion",      NARANJA);
        TextButton btnCambiarUser  = crearBoton("Cambiar Usuario",      VERDE);
        TextButton btnCambiarPass  = crearBoton("Cambiar Contrasena",   VERDE);
        TextButton btnCambiarAvatar= crearBoton("Cambiar Avatar",       VERDE);
        TextButton btnVolver       = crearBoton("Volver",               ROJO);

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
        
        tabla.add(titulo).padBottom(4).row();
        tabla.add(subUsuario).padBottom(4).row();
        tabla.add(subAvatar).padBottom(40).row();
        tabla.add(btnVerInfo).width(280).height(50).padBottom(12).row();
        tabla.add(btnCambiarUser).width(280).height(50).padBottom(12).row();
        tabla.add(btnCambiarPass).width(280).height(50).padBottom(12).row();
        tabla.add(btnCambiarAvatar).width(280).height(50).padBottom(12).row();
        tabla.add(btnVolver).width(280).height(50).row();

        escenario.addActor(tabla);
     
}
     
     
     
     
      private void construirVerInfo() {
        escenario.clear();

        USER u = gestor.buscarUser(usuario);

        ScrollPane.ScrollPaneStyle spStyle = new ScrollPane.ScrollPaneStyle();
        spStyle.background = piel.newDrawable("white", FONDO);

        Table contenido = new Table();
        contenido.center().pad(20);

        Label titulo = new Label("Informacion de Perfil", piel);
        titulo.setFontScale(1.8f);
        titulo.setColor(VERDE);

        String fechaReg = (u != null)
            ? u.getFechaRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
        long ms = (u != null) ? u.getUltimaSesion() : 0;
        String ultimaSesion = (ms > 0)
            ? new java.util.Date(ms).toString() : "-";
        String avatarActual = (u != null && !u.getAvatarPath().isEmpty())
            ? u.getAvatarPath() : "Sin avatar";
        String nombreCompleto = (u != null) ? u.getFullname() : "-";

        int nivelesComp  = (u != null) ? u.getNivelesCompletados() : 0;
        int puntuacion   = (u != null) ? u.getPuntuacionGeneral()  : 0;
        int partidas     = (u != null) ? u.getPartidasJugadas()    : 0;
        int fallos       = (u != null) ? u.getFallosTotales()      : 0;
        int estrellas    = (u != null) ? u.getEstrellasTotal()      : 0;
        long tiempoMs    = (u != null) ? u.getTiempoTotalJugado()  : 0;
        String tiempo    = String.format("%d min %d seg",
            (tiempoMs / 60000), (tiempoMs % 60000) / 1000);

        contenido.add(titulo).padBottom(20).colspan(2).center().row();

        // separador de sección
        agregarSeccion(contenido, "DATOS PERSONALES");
        agregarFila(contenido, "Nombre completo",  nombreCompleto);
        agregarFila(contenido, "Usuario",          "@" + usuario);
        agregarFila(contenido, "Avatar",           avatarActual);
        agregarFila(contenido, "Fecha de registro",fechaReg);
        agregarFila(contenido, "Ultima sesion",    ultimaSesion);

        agregarSeccion(contenido, "PROGRESO DEL JUEGO");
        agregarFila(contenido, "Niveles completados", nivelesComp + " / 5");
        agregarFila(contenido, "Puntuacion general",  String.valueOf(puntuacion));
        agregarFila(contenido, "Estrellas totales",   String.valueOf(estrellas));

        agregarSeccion(contenido, "ESTADISTICAS");
        agregarFila(contenido, "Partidas jugadas",    String.valueOf(partidas));
        agregarFila(contenido, "Fallos totales",      String.valueOf(fallos));
        agregarFila(contenido, "Tiempo total jugado", tiempo);

        // ── secciones pendientes de logica ─────────────────────────────────
        agregarSeccion(contenido, "HISTORIAL DE PARTIDAS");
        Label lblHistorial = new Label("[ Proximamente ]", piel);
        lblHistorial.setColor(GRIS);
        contenido.add(lblHistorial).colspan(2).padBottom(10).center().row();

        agregarSeccion(contenido, "RANKING GENERAL");
        Label lblRanking = new Label("[ Proximamente ]", piel);
        lblRanking.setColor(GRIS);
        contenido.add(lblRanking).colspan(2).padBottom(10).center().row();

        agregarSeccion(contenido, "AMIGOS / RIVALES");
        if (u != null && u.getAmigos().length > 0) {
            for (String amigo : u.getAmigos()) {
                Label lblAmigo = new Label("• " + amigo, piel);
                lblAmigo.setColor(CAFE);
                contenido.add(lblAmigo).colspan(2).left().padLeft(20).padBottom(4).row();
            }
        } else {
            Label lblSinAmigos = new Label("Sin amigos agregados aun.", piel);
            lblSinAmigos.setColor(GRIS);
            contenido.add(lblSinAmigos).colspan(2).padBottom(10).center().row();
        }

        TextButton btnVolver = crearBoton("Volver al Perfil", NARANJA);
        btnVolver.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) { construirMenuPerfil(); }
        });
        contenido.add(btnVolver).width(280).height(50).padTop(20).colspan(2).center().row();

        ScrollPane scroll = new ScrollPane(contenido, spStyle);
        scroll.setFillParent(true);
        scroll.setScrollingDisabled(true, false);

        escenario.addActor(scroll);
    }

     
}

