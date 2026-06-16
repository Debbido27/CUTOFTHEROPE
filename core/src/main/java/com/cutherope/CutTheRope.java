package com.cutherope;

import LOGIC.MusicaManager;
import com.badlogic.gdx.Game;

public class CutTheRope extends Game {

    @Override
    public void create() {
        setScreen(new LoginScreen(this));
        MusicaManager musica = MusicaManager.getInstance();
        LOGIC.LoginManager lm = new LOGIC.LoginManager();
        LOGIC.USER u = lm.getCurrentUser();
        if (u != null) {
            LOGIC.Idioma.setIngles(u.isIngles());
            musica.aplicarPreferencias(u.isMusicaActiva(), u.getVolumen());
        } else {
            musica.reproducir(); // defaults: ON, volumen 1.0
        }
    }
}
