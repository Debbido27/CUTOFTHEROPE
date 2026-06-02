/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cutherope;

import LOGIC.LoginManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


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

     
}
