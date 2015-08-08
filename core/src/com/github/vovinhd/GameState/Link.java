package com.github.vovinhd.GameState;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by vovin on 08/08/2015.
 */
public class Link extends Actor{
    Ball parent;
    Ball child;
    float angle;
    float speed;
    int length;
    float springConstant;

    public Link(Ball parent, Ball child) {
        this.parent = parent;
        this.child = child;
    }

    @Override
    public void act(float delta) {
        if (child == null) return;
        angle += speed * delta;
        Vector2 newPosChild = new Vector2();
        newPosChild.x = parent.position.x + length * (float) Math.cos(angle);
        newPosChild.y = parent.position.y + length * (float) Math.sin(angle);
        child.position = newPosChild;
        child.act(delta);
    }
}
