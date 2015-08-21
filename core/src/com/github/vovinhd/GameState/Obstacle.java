package com.github.vovinhd.GameState;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.github.vovinhd.GameLogic.GameMode;

/**
 * Created by vovin on 21/08/2015.
 */
public abstract class Obstacle extends Actor {
    protected final GameMode gameMode;
    private Rectangle bounds;
    private Array<TextureRegion> animation;
    private boolean isDead = false;
    private int damageState = 0;
    private int maxDamage;
    private Body body;
    private Fixture fixture;
    private int value;

    public Obstacle(Stage parent, Rectangle rectangle, int maxDamage, int value, World world, GameMode gameMode, Array<TextureRegion> animation) {
        this.gameMode = gameMode;
        this.setStage(parent);
        this.setBounds(rectangle);
        this.setMaxDamage(maxDamage);
        this.setValue(value);
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(rectangle.getPosition(new Vector2(0, 0)));
        setBody(world.createBody(bd));
        Shape shape = getPolygonShapeFromRectangle(rectangle);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.shape = shape;
        fixtureDef.filter.groupIndex = CollisionChannelDefinition.GROUP_ENV;
        fixtureDef.isSensor = false;
        setFixture(getBody().createFixture(fixtureDef));
        getFixture().setUserData(this);
        setAnimation(animation);
    }

    protected static PolygonShape getPolygonShapeFromRectangle(Rectangle rectangle) {

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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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

    public Array<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Array<TextureRegion> animation) {
        this.animation = animation;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Vector2 pos = getBody().getPosition();
        batch.draw(getAnimation().get(getDamageState() * getAnimation().size / getMaxDamage()), pos.x, pos.y);
        super.draw(batch, parentAlpha);
    }

    public abstract void collideWithPlayer(Ball ball);

    public void destroy() {
        setIsDead(true);
        getGameMode().brickDestroyed(this);
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
