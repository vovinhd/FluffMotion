package com.github.vovinhd.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by vovin on 08/08/2015.
 */
public class Chain extends Actor {
    public Ball root;
    Random rand = new Random(); 
    Vector2 rootPos = new Vector2();
    Texture rect = new Texture("line_c.png");
    private World world;
    private int width;
    private int height;
    private int ballIdPool = 1;
    private int linkIdPool = 0;
    private Group ballGroup;
    private Group linkGroup;
    private Array<Link> links = new Array<>();
    private Array<Ball> balls = new Array<>();

    private Chain(Ball root) {
        this.root = root;
        this.rootPos = root.position;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        balls.add(root);
    }

    public Chain(Ball hero, World world, Group ballGroup, Group linkGroup) {
        this.root = hero;
        this.world = world;
        this.ballGroup = ballGroup;
        this.linkGroup = linkGroup;

        this.balls.add(root);
    }

    private int nextBallId() {
        int id = ballIdPool;
        ballIdPool++;
        return id;
    }

    private int nextLinkId() {
        int id = linkIdPool;
        linkIdPool++;
        return id;
    }

    public void addBall(Ball ball) {
        ball.chain = this;
        Ball current = root;
        balls.add(ball);
        ball.id = nextBallId();
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
        ballGroup.addActor(ball);
        addBall(ball);
    }

    private Link randomLink(Ball parent, Ball child) {
        Link link = new Link(this, parent, child);
        link.length = 50;
        link.angle = 0;
        link.speed = 1;
        link.id = nextLinkId();
        links.add(link);
        linkGroup.addActor(link);
        Gdx.app.log("LINK CREATE", link.toString());
        return link;
    }

    public void reRoot(Ball ball) {
        root = ball;
        Gdx.app.log("root", root.toString());

        for (Ball b : balls) {
//            if(b.id < root.id) {
            b.swapUpDown(root);
            Gdx.app.log("BALL MODIFIED", b.toString() + " UP: " + (b.up != null ? b.up.toString() : "null") + " DOWN: " + (b.down != null ? b.down.toString() : "null"));
//            }
        }

        Gdx.app.log("BALLS", balls.toString());
        Gdx.app.log("LINKS", links.toString());



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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int thickness = 2;
        int x = (int) root.position.x - (int) root.radius;
        int y = (int) root.position.y - (int) root.radius;

        int width = (int) root.radius * 2;
        int height = (int) root.radius * 2;
        batch.draw(rect, x, y, width, thickness);
        batch.draw(rect, x, y, thickness, height);
        batch.draw(rect, x, y + height - thickness, width, thickness);
        batch.draw(rect, x + width - thickness, y, thickness, height);
        super.draw(batch, parentAlpha);
    }


    public void act(float delta) {
        for (Link l : links) {
            l._act(delta);
        }

    }

}