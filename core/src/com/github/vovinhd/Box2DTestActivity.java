package com.github.vovinhd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

/**
 * Created by vovin on 19/08/2015.
 */
public class Box2DTestActivity extends InputAdapter implements Screen {

    final float PIXELS_TO_METERS = 100f;
    private final FluffMotion parent;
    Box2DDebugRenderer renderer;
    Camera camera;
    World world;
    private Body ballC;
    private Body ballA;
    private Body ballB;
    private int width;
    private int height;

    public Box2DTestActivity(FluffMotion parent) {
        this.parent = parent;
        world = new World(new Vector2(0, 0), false);
        ballA = addBall(new Vector2(0, 0), 1, BodyDef.BodyType.DynamicBody);
        ballB = addBall(new Vector2(2, 0), 1, BodyDef.BodyType.DynamicBody);
        ballC = addBall(new Vector2(4, 0), 1, BodyDef.BodyType.DynamicBody);
        join(ballA, ballB);
        join(ballB, ballC);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(15, 15);
        renderer = new Box2DDebugRenderer();
        Gdx.input.setInputProcessor(this);
        ballA.setType(BodyDef.BodyType.StaticBody);

    }


    private Body addBall(Vector2 pos, int radius, BodyDef.BodyType type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(pos);
        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        shape.setPosition(new Vector2(0, 0));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    private void join(Body bodyA, Body bodyB) {
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.bodyA = bodyA;
        jointDef.bodyB = bodyB;
        jointDef.length = 4;
        DistanceJoint j = (DistanceJoint) world.createJoint(jointDef);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 force = new Vector2();
        force.x = screenX / (screenX / 10) - 10 / 2;
        force.y = -(screenY / (screenY / 10) - 10 / 2);
        Gdx.app.log("Force", force.toString());
        ballB.applyForceToCenter(force, true);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        // On right or left arrow set the velocity at a fixed rate in that

        if (keycode == Input.Keys.RIGHT)
            ballC.setLinearVelocity(10f, 0f);
        if (keycode == Input.Keys.LEFT)
            ballC.setLinearVelocity(-1f, 0f);

        if (keycode == Input.Keys.UP)
            ballB.applyForceToCenter(0f, 100f, true);
        if (keycode == Input.Keys.DOWN)
            ballB.applyForceToCenter(0f, -10f, true);
        return true;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(delta, 6, 2);
        camera.update();
        renderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
