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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.vovinhd.GameState.Ball;
import com.github.vovinhd.GameState.Chain;
import com.github.vovinhd.GameState.Link;

import java.util.Random;

/**
 * Created by vovin on 08/08/2015.
 */
public class ChainTestActivity extends ScreenAdapter {

    ShapeRenderer renderer;
    SpriteBatch batch;
    Camera camera;


    Box2DDebugRenderer debugRenderer;
    World world = new World(new Vector2(0, 0), true);


    int width;
    int height;

    Random rand = new Random();
    Chain chain;
    Ball root;
    private Stage stage;

    @Override
    public void show() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        camera = new OrthographicCamera(width, height);
        debugRenderer = new Box2DDebugRenderer();

        root = new Ball(new Vector2(100, 100), Color.BLACK, world);
        stage = new Stage();
        Group group = new Group();
        Group group1 = new Group();

        chain = new Chain(root, world, group, group1);

        chain.addRandomBall();
        chain.addRandomBall();
        chain.addRandomBall();
        chain.addRandomBall();
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

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        drawChain(chain);
        renderer.end();
        debugRenderer.render(world, camera.combined);

    }

    private void drawBall(Ball ball){
        renderer.setColor(ball.getColor());
        renderer.circle(ball.getPosition().x, ball.getPosition().y, ball.getRadius());
    }

    private  void drawLink(Link link) {

        renderer.line(link.parent.position.x, link.parent.position.y, link.child.getPosition().x, link.child.position.y, link.parent.color, link.child.color);
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


}
