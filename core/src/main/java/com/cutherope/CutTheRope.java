package com.cutherope;

import LOGIC.MusicaManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CutTheRope extends Game {

    private SpriteBatch transitionBatch;
    private Texture whitePixel;
    private Screen targetScreen;
    private boolean transitioning = false;
    private boolean switched = false;
    private float transitionTime = 0f;
    private static final float TRANSITION_DURATION = 0.35f;

    @Override
    public void create() {
        transitionBatch = new SpriteBatch();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();

        setScreen(new LoginScreen(this));
        MusicaManager musica = MusicaManager.getInstance();
        LOGIC.LoginManager lm = new LOGIC.LoginManager();
        LOGIC.USER u = lm.getCurrentUser();
        if (u != null) {
            LOGIC.Idioma.setIngles(u.isIngles());
            musica.aplicarPreferencias(u.isMusicaActiva(), u.getVolumen());
        } else {
            musica.reproducir();
        }
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null || screen == this.screen) return;
        targetScreen = screen;
        if (this.screen == null) {
            Screen old = this.screen;
            this.screen = targetScreen;
            if (old != null) old.hide();
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            targetScreen = null;
            return;
        }
        transitioning = true;
        switched = false;
        transitionTime = 0f;
    }

    @Override
    public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f);

        if (transitioning) {
            transitionTime += delta;
            float t = Math.min(transitionTime / TRANSITION_DURATION, 1f);

            if (t < 0.5f) {
                float alpha = t / 0.5f;
                if (screen != null) screen.render(delta);
                drawOverlay(alpha);
            } else if (t >= 0.5f && !switched) {
                switched = true;
                Screen lastScreen = screen;
                screen = targetScreen;
                if (lastScreen != null) lastScreen.hide();
                if (screen != null) {
                    screen.show();
                    screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                }
                drawOverlay(1f);
            } else {
                float alpha = 1f - ((t - 0.5f) / 0.5f);
                if (screen != null) screen.render(delta);
                drawOverlay(alpha);
            }

            if (t >= 1f) {
                transitioning = false;
                targetScreen = null;
            }
        } else {
            if (screen != null) screen.render(delta);
        }
    }

    private void drawOverlay(float alpha) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        transitionBatch.getProjectionMatrix().setToOrtho2D(
            0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        transitionBatch.begin();
        transitionBatch.setColor(0, 0, 0, alpha);
        transitionBatch.draw(whitePixel, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        transitionBatch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (transitionBatch != null) transitionBatch.dispose();
        if (whitePixel != null) whitePixel.dispose();
    }
}
