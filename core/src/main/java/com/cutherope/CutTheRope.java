package com.cutherope;

import com.badlogic.gdx.Game;

public class CutTheRope extends Game {

    @Override
    public void create() {
        setScreen(new LoginScreen(this));
    }
}