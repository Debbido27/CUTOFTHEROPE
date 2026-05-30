package com.cutherope;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import LOGIC.LoginManager;

public class MenuPrincipalScreen implements Screen {

    private CutTheRope game;
    private String username;
    private LoginManager manager;

    public MenuPrincipalScreen(CutTheRope game, String username, LoginManager manager) {
        this.game     = game;
        this.username = username;
        this.manager  = manager;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.96f, 0.92f, 0.82f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}