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
    Link up;
    Link down;

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Link getUp() {
        return up;
    }

    public void setUp(Link up) {
        this.up = up;
    }

    public Link getDown() {
        return down;
    }

    public void setDown(Link down) {
        this.down = down;
    }

    public void act(float delta) {
        if (down == null) return;
        down.act(delta);
    }
}