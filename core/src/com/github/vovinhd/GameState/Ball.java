package com.github.vovinhd.GameState;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by vovin on 08/08/2015.
 */
public class Ball {
    Vector2 position = new Vector2();
    float radius = 10.0f;
    float weight = 1f;
    Color color = new Color();
    Link down;

    void act(float delta) {
        if (down == null) return;
        down.act(delta);
    }
}