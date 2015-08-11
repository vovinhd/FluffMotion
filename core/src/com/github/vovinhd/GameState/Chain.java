package com.github.vovinhd.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by vovin on 08/08/2015.
 */
public class Chain extends Actor {
    public Ball root;
    Random rand = new Random(); 
    Vector2 rootPos = new Vector2();
    private World world;
    private int width;
    private int height;
    private Stage stage;

    private Array<Link> links = new Array<>();
    private Array<Ball> balls = new Array<>();

    private Chain(Ball root) {
        this.root = root;
        this.rootPos = root.position;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        balls.add(root);

    }

    public Chain(Ball root, World world, Stage stage) {
        this(root);
        this.world = world;
        this.stage = stage;
    }

    public void addBall(Ball ball) {
        Ball current = root;
        balls.add(ball);
        while (true) {
            if(current.down == null) {
                current.down = randomLink(current, ball);
                break;
            } else {
                current = current.down.child;
            }
        }
    }

    public void addRandomBall() {
        Ball ball = randomBall();
        ball.initPhysics(world);
        stage.addActor(ball);
        addBall(ball);
    }

    private Link randomLink(Ball parent, Ball child) {
        Link link = new Link(parent, child);
        link.length = 50;
        link.angle = rand.nextFloat() * 2 * (float) Math.PI;
        link.speed = 2;
        links.add(link);
        return link;
    }

    public void reRoot(Ball ball) {
        root = ball;
        for (Link link : links) {
            link.swapParentChild();
        }
        for (Ball b : balls) {
            b.swapUpDown();
        }

    }

    public void rootUp() {
        if (root.getUp() != null) reRoot(root.getUp().parent);
    }

    public void rootDown() {
        if (root.getDown() != null) reRoot(root.getDown().child);

    }

    private Ball randomBall() {
        Vector2 position = new Vector2();
        position.x = rand.nextInt(width + 1);
        position.y = rand.nextInt(height + 1);
        int radius = rand.nextInt(6) + 12;
        Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
        if (world != null)
            return new Ball(position, color, world);
        else
            return new Ball(position, color);
    }

    public void act(float delta) {
        root.act(delta);
        if (root.getUp() != null) {
            root.getUp().act(delta);
        }
    }

}