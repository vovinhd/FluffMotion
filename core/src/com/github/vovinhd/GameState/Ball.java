package com.github.vovinhd.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by vovin on 08/08/2015.
 */
public class Ball extends Actor {
    public Vector2 position = new Vector2();
    public Color color = new Color();
    public Link down;
    float radius = 10.0f;
    float weight = 1f;
    Link up;
    Texture texture = new Texture(Gdx.files.internal("tempball.png"));
    Sprite sprite = new Sprite(texture);

    Body body;
    BodyDef bodyDef;
    Fixture fixture;
    FixtureDef fixtureDef;

    public Ball(Vector2 position, Color color) {
        this.position = position;
        this.radius = (int) sprite.getHeight() / 2;
        this.color = color;
    }

    public Ball(Vector2 position, Color color, World world) {
        this(position, color);
        this.initPhysics(world);
    }

    public void initPhysics(World world) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        shape.setPosition(new Vector2(0, 0));
        fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.shape = shape;
        fixtureDef.filter.groupIndex = CollisionChannelDefinition.GROUP_CHAIN;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);
        shape.dispose();

    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public void setBodyDef(BodyDef bodyDef) {
        this.bodyDef = bodyDef;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public void setFixtureDef(FixtureDef fixtureDef) {
        this.fixtureDef = fixtureDef;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        this.body.setTransform(position, 0);
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, sprite.getX(), sprite.getY());
    }

    @Override
    public void act(float delta) {
        setPosition(body.getPosition());
        sprite.setPosition(position.x - radius, position.y - radius);
        sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);

        if (down == null) return;
        down._act(delta);
    }

    void swapUpDown() {
        Link tmp = up;
        up = down;
        down = tmp;
    }
}