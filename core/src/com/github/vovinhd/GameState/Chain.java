package com.github.vovinhd.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by vovin on 08/08/2015.
 */
public class Chain extends Actor {
    Random rand = new Random(); 
    Vector2 rootPos = new Vector2();
    Ball root;
    private int width;
    private int height;

    public Chain(Ball root) {
        this.root = root;
        this.rootPos = root.position;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

    }

    public void addRandomBall() {
        Ball current = root;
        while (true) {
            if(current.down == null) {
                Ball ball = randomBall();
                current.down = randomLink(current, ball);
                current = ball;
                break;
            } else {
                current = current.down.child;
            }
        }
    }

    private Link randomLink(Ball parent, Ball child) {
        Link link = new Link(parent, child);
        link.length = rand.nextInt(100) + 50;
        link.angle = rand.nextFloat() * 2 * (float) Math.PI;
        link.speed = (rand.nextFloat() * (float) Math.PI / 2) - ((float) Math.PI /4);
        return link;

    }

    private Ball randomBall() {
        Ball ball = new Ball();

        ball.position.x  = rand.nextInt(width + 1);
        ball.position.y = rand.nextInt(height + 1);
        ball.radius  = rand.nextInt(6) + 12;
        ball.color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);

        return ball;
    }

    public void act(float delta) {
        root.act(delta);
    }

}