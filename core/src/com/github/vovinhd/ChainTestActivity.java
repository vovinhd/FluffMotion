package com.github.vovinhd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Map;
import java.util.Random;

/**
 * Created by vovin on 08/08/2015.
 */
public class ChainTestActivity extends ScreenAdapter {

    ShapeRenderer renderer;
    SpriteBatch batch;
    Camera camera;

    int width;
    int height;

    Random rand = new Random();
    Chain chain;
    Ball root;

    @Override
    public void show() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        camera = new OrthographicCamera(width, height);

        root = new Ball();
        root.position = new Vector2(width/2, height/2);
        root.radius = 75;
        root.color = Color.BLACK;
        chain = new Chain(root);
        chain.addBall();
        chain.addBall();
        chain.addBall();
        chain.addBall();
    }

    @Override
    public void render(float delta) {
        chain.act(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //draw actual graphics
        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        drawChain(chain);
        renderer.end();

    }

    private void drawBall(Ball ball){
        renderer.setColor(ball.color);
        renderer.circle(ball.position.x, ball.position.y, ball.radius);
    }

    private  void drawLink(Link link) {

        renderer.line(link.parent.position.x, link.parent.position.y, link.child.position.x, link.child.position.y, link.parent.color, link.child.color);
    }

    private void drawChain(Chain chain) {
        Ball current = chain.root;
        while (true) {
            drawBall(current);
            if(current.down == null) break;
            drawLink(current.down);
            current = current.down.child;
        }
    }

    public class Ball {
        Vector2 position = new Vector2();
        float radius = 10.0f;
        Color color = new Color();
        Link down;

        void act(float delta) {
            if (down == null) return;
            down.act(delta);
        }
    }

    public class Link {
        Ball parent;
        Ball child;
        float angle;
        float speed;
        int length;

        public Link(Ball parent, Ball child) {
            this.parent = parent;
            this.child = child;
        }

        void act(float delta) {
            if (child == null) return;
            angle += speed * delta;
            Vector2 newPosChild = new Vector2();
            newPosChild.x = parent.position.x + length * (float) Math.cos(angle);
            newPosChild.y = parent.position.y + length * (float) Math.sin(angle);
            child.position = newPosChild;
            child.act(delta);
        }
    }

    public class Chain {
        Vector2 rootPos = new Vector2();
        Ball root;

        public Chain(Ball root) {
            this.root = root;
            this.rootPos = root.position;
        }

        public void addBall() {
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

        void act(float delta) {
            root.act(delta);
        }

    }
}
