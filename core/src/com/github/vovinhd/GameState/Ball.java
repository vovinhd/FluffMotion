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
    public int id = 0;
    public Vector2 position = new Vector2();
    public Color color = new Color();
    public Link down;
    public Chain chain;
    public boolean facingUp = false;
    float radius = 10.0f;
    float weight = 1f;
    Link up;
    Texture texture = new Texture(Gdx.files.internal("tempball.png"));
    Texture texture_ = new Texture(Gdx.files.internal("tempball1.png"));

    Sprite sprite = new Sprite(texture);
    Sprite sprite_ = new Sprite(texture_);
    Body body;
    BodyDef bodyDef;
    Fixture fixture;
    FixtureDef fixtureDef;
    public Ball(Vector2 position, Color color) {
        this.position = position;
        this.radius = (int) sprite.getHeight() / 2;
        this.color = color;
        this.setSize(radius * 2, radius * 2);
    }

    public Ball(Vector2 position, Color color, World world) {
        this(position, color);
        this.initPhysics(world);
    }

    private void swapSprites() {
        Sprite spriteTemp = sprite;
        sprite = sprite_;
        sprite_ = spriteTemp;
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

    public void bounceOff(Vector2 collisionNormal) {
        Gdx.app.log("Bounce", toString());
        if (getUp() != null) {
            getUp().speed = -getUp().speed;


        } else {
            Gdx.app.log("ERROR", "Non ball was bounced of a Wall");
        }
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
        this.setPosition(position.x - radius, position.y - radius);
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
        //down._act(delta);
    }

    void swapUpDown(Ball root) {
        // if(down == null || up == null) return;
        if (id < root.id && !facingUp) {
            if (down != null) down.swapParentChild();

        } else if (id >= root.id && facingUp) {
            if (up != null) up.swapParentChild();

        } else {
            return;
        }
        Link tmp = up;
        up = down;
        down = tmp;
        facingUp = !facingUp;
        swapSprites();


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ball)) return false;

        Ball ball = (Ball) o;

        if (Float.compare(ball.radius, radius) != 0) return false;
        if (Float.compare(ball.weight, weight) != 0) return false;
        if (!position.equals(ball.position)) return false;
        if (down != null ? !down.equals(ball.down) : ball.down != null) return false;
        return !(up != null ? !up.equals(ball.up) : ball.up != null);

    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + (down != null ? down.hashCode() : 0);
        result = 31 * result + (radius != +0.0f ? Float.floatToIntBits(radius) : 0);
        result = 31 * result + (weight != +0.0f ? Float.floatToIntBits(weight) : 0);
        result = 31 * result + (up != null ? up.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ball{" +
                "id=" + id +
                " ,upID=" + (up != null ? up.id : "null") +
                " ,downID=" + (down != null ? down.id : "null") +
                '}';
    }
}