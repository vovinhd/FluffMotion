package com.github.vovinhd.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by vovin on 17/08/2015.
 */
public class Brick extends Actor {

    private Rectangle bounds;
    private Sprite[] sprites;
    private boolean isDead = false;
    private int damageState = 0;
    private int maxDamage;
    private Body body;
    private Fixture fixture;

    public Brick(Stage parent, Rectangle rectangle, int maxDamage, World world) {

        this.setStage(parent);
        this.bounds = rectangle;
        this.maxDamage = maxDamage;
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(rectangle.getPosition(new Vector2(0, 0)));
        body = world.createBody(bd);
        Shape shape = getPolygonShapeFromRectangle(rectangle);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.shape = shape;
        fixtureDef.filter.groupIndex = CollisionChannelDefinition.GROUP_ENV;
        fixtureDef.isSensor = false;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        sprites = new Sprite[maxDamage];

        Texture full = new Texture(Gdx.files.internal("brick_0.png"));
        Texture med = new Texture(Gdx.files.internal("brick_1.png"));
        Texture low = new Texture(Gdx.files.internal("brick_2.png"));
        for (int i = 0; i < maxDamage; i++) {
            Texture current;
            if (i < maxDamage / 3) {
                current = full;
            } else if (i < 2 * maxDamage / 3) {
                current = med;
            } else {
                current = low;
            }
            sprites[i] = new Sprite(current);
            sprites[i].setPosition(body.getPosition().x, body.getPosition().y);
        }


    }

    private static PolygonShape getPolygonShapeFromRectangle(Rectangle rectangle) {

        PolygonShape polygon = new PolygonShape();
        Vector2 position = new Vector2((rectangle.x),
                (rectangle.y));
        Vector2[] verts = new Vector2[]{
                new Vector2(0, 0),
                new Vector2(rectangle.getWidth(), 0),
                new Vector2(0, rectangle.getHeight()),
                new Vector2(rectangle.getWidth(), rectangle.getHeight())
        };

        polygon.set(verts);
        return polygon;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public int getDamageState() {
        return damageState;
    }

    public void setDamageState(int damageState) {
        this.damageState = damageState;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Sprite[] getSprites() {
        return sprites;
    }

    public void setSprites(Sprite[] sprites) {
        this.sprites = sprites;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Vector2 pos = body.getPosition();
        batch.draw(sprites[damageState], pos.x, pos.y);
        super.draw(batch, parentAlpha);
    }

    public void collideWithPlayer(Ball ball) {
        damageState += 1;
        if (damageState >= sprites.length) {
            Gdx.app.log(toString(), String.valueOf(isDead()));

            destroy();
            damageState -= 1;
        }
    }

    public void destroy() {
        isDead = true;
    }


}
