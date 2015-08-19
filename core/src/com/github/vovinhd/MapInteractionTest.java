package com.github.vovinhd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.vovinhd.GameState.Ball;
import com.github.vovinhd.GameState.Brick;
import com.github.vovinhd.GameState.Chain;
import com.github.vovinhd.GameState.CollisionChannelDefinition;

/**
 * Created by vovin on 08/08/2015.
 */
public class MapInteractionTest extends InputAdapter implements Screen, ContactListener {
    private static float ppt = 16;
    int speed = 35;
    float mapScaleFactor = 1f;
    float cameraZoomFactor = 1f / 128f;
    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;
    Viewport viewport;
    SpriteBatch batch;
    int mapWidth;
    int mapHeight;
    World world;
    Box2DDebugRenderer debugRenderer;
    Stage stage;
    Ball hero;
    Chain chain;
    Array<Rectangle> rectangleArray = new Array<>();
    ShapeRenderer shapeRenderer;
    FluffMotion game;
    private int mapPixelWidth;
    private int mapPixelHeight;
    private Vector2 touchDown = new Vector2();
    private Group brickGroup;
    private Group linkGroup;
    private Group ballGroup;

    public MapInteractionTest(FluffMotion game) {
        this.game = game;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();

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

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        map = new TmxMapLoader().load("desertTest.tmx");
        MapProperties prop = map.getProperties();
        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
        Integer tilePixelWidth = prop.get("tilewidth", Integer.class);
        Integer tilePixelHeight = prop.get("tileheight", Integer.class);
        mapScaleFactor = tilePixelHeight;
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1f);
        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;

        batch = new SpriteBatch();
        viewport = new ScreenViewport(camera);

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(this);
        shapeRenderer = new ShapeRenderer();
        hero = new Ball(new Vector2(300, 300), Color.BLACK, world);
        stage = new Stage(viewport, batch) {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 touchDownAt = new Vector2(screenX, screenY);
                Vector2 touchDownStage = screenToStageCoordinates(touchDownAt);
                Gdx.app.log("TOUCH AT", touchDownStage.toString());
                Actor actor = hit(touchDownStage.x, touchDownStage.y, true);
                if (actor == null) return false;
                if (actor instanceof Ball) {
                    Gdx.app.log("Touched ball ", actor.toString());
                    chain.reRoot((Ball) actor);
                    return true;
                }
                return false;
            }
        };
        ballGroup = new Group();
        linkGroup = new Group();
        brickGroup = new Group();

        ballGroup.addActor(hero);
        stage.addActor(linkGroup);
        stage.addActor(ballGroup);
        stage.addActor(brickGroup);

        chain = new Chain(hero, world, ballGroup, linkGroup);
        chain.addRandomBall();
        chain.addRandomBall();
        chain.addRandomBall();

        stage.addActor(chain);


        int objectLayerId = 1;
        MapLayer collisionObjectLayer = map.getLayers().get(objectLayerId);
        MapObjects objects = collisionObjectLayer.getObjects();

        // there are several other types, Rectangle is probably the most common one
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {

            Rectangle rect = rectangleObject.getRectangle();
            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            bd.position.set(rect.getPosition(new Vector2(0, 0)));
            Body body = world.createBody(bd);
            Shape shape = getRectangle(rectangleObject);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1f;
            fixtureDef.shape = shape;
            fixtureDef.filter.groupIndex = CollisionChannelDefinition.GROUP_ENV;
            fixtureDef.isSensor = false;
            body.createFixture(fixtureDef);

        }

        int triggerLayerId = 2;
        MapLayer triggerObjectLayer = map.getLayers().get(triggerLayerId);
        MapObjects triggers = triggerObjectLayer.getObjects();

        for (RectangleMapObject rectangleObject : triggers.getByType(RectangleMapObject.class)) {
            Rectangle rect = rectangleObject.getRectangle();
            rectangleArray.add(rect);
            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            bd.position.set(rect.getPosition(new Vector2(0, 0)));
            Body body = world.createBody(bd);
            body.setUserData(rectangleObject.getProperties());
            Shape shape = getRectangle(rectangleObject);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1f;
            fixtureDef.shape = shape;
            fixtureDef.filter.groupIndex = CollisionChannelDefinition.GROUP_TRIGGER;

            Fixture f = body.createFixture(fixtureDef);
            f.setUserData(rectangleObject.getProperties());
            Gdx.app.log("TRIGGERD", " created trigger with " + rect.toString());
        }

        TiledMapTileLayer tiles = (TiledMapTileLayer) map.getLayers().get("objects");


        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                TiledMapTileLayer.Cell cell = tiles.getCell(i, j);
                if (cell != null && cell.getTile().getProperties().get("brick") != null) {
                    Brick brick = new Brick(stage, new Rectangle(tilePixelWidth * i, tilePixelHeight * j, tilePixelWidth, tilePixelHeight), 3, world);
                    brickGroup.addActor(brick);
                    tiles.setCell(i, j, null);
                }
            }
        }

        debugRenderer = new Box2DDebugRenderer();

        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(stage);
        im.addProcessor(this);

        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        world.step(delta, 6, 2);
        sweepDeadBodies();
        camera.position.set(chain.root.getPosition().x, chain.root.getPosition().y, camera.position.z);
        constrainViewToMap(camera);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        mapRenderer.setView(camera);
        mapRenderer.render();
        stage.draw();
        debugRenderer.render(world, camera.combined);
//
//        shapeRenderer.setProjectionMatrix(camera.projection);
//        shapeRenderer.setTransformMatrix(camera.view);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        for (Rectangle rect : rectangleArray) {
//            shapeRenderer.rect(rect.x, rect.y, rect.getWidth(), rect.getHeight());
//        }
//        shapeRenderer.end();

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width * mapScaleFactor * cameraZoomFactor, height * mapScaleFactor * cameraZoomFactor);

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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchDown.set(screenX, screenY);
        return true;
    }

//    @Override
//    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        Vector2 touchDrag = new Vector2(screenX, screenY);
//        Vector2 delta = touchDrag.cpy().sub(touchDown);
//        delta.scl(-1, 1);
//        delta.scl(mapScaleFactor * cameraZoomFactor);
//
//        Gdx.app.log("CAMERA MOVE: ", delta.toString());
//        touchDown = touchDrag;
//        camera.translate(delta);
//
//         constrainViewToMap(camera);
//
//        return true;
//    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {

            case Input.Keys.P:
                cameraZoomFactor = cameraZoomFactor / 2;
                camera.setToOrtho(false, Gdx.graphics.getWidth() * mapScaleFactor * cameraZoomFactor, Gdx.graphics.getHeight() * mapScaleFactor * cameraZoomFactor);

                break;
            case Input.Keys.O:
                cameraZoomFactor = cameraZoomFactor * 2;
                camera.setToOrtho(false, Gdx.graphics.getWidth() * mapScaleFactor * cameraZoomFactor, Gdx.graphics.getHeight() * mapScaleFactor * cameraZoomFactor);

                break;

        }

        return super.keyDown(keycode);
    }

    private void sweepDeadBodies() {
        for (Actor actor : brickGroup.getChildren()) {
            if (actor instanceof Brick) {
                Brick b = (Brick) actor;

                if (b.isDead()) {
                    Gdx.app.log("Sweeping", b.toString());

                    world.destroyBody(b.getBody());
                    b.getBody().setUserData(null);
                    b.setBody(null);
                    brickGroup.removeActor(b);
                }
            }
        }
    }

    private void constrainViewToMap(OrthographicCamera camera) {
        if (camera.position.x - camera.viewportWidth / 2 < 0)
            camera.position.x = camera.viewportWidth / 2;
        else if (camera.position.x + camera.viewportWidth / 2 > mapPixelWidth)
            camera.position.x = mapPixelWidth - camera.viewportWidth / 2;
        if (camera.position.y - camera.viewportHeight / 2 < 0)
            camera.position.y = camera.viewportHeight / 2;
        else if (camera.position.y + camera.viewportHeight / 2 > mapPixelHeight)
            camera.position.y = mapPixelHeight - camera.viewportHeight / 2;
        camera.update();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Gdx.app.log("Collision", fixtureA.toString() + " <- A | B -> " + fixtureB.toString());
        if (fixtureA.getUserData() != null && fixtureA.getUserData() instanceof Ball) {
            resolveContact(fixtureA, fixtureB);
        } else if (fixtureB.getUserData() != null && fixtureB.getUserData() instanceof Ball) {
            resolveContact(fixtureB, fixtureA);
        }

    }

    private void resolveContact(Fixture ballFixture, Fixture collisionFixture) {
        if (collisionFixture.getUserData() == null) {
            bounceOff((Ball) ballFixture.getUserData());
        } else if (collisionFixture.getUserData() instanceof MapProperties) {
            MapProperties properties = (MapProperties) collisionFixture.getUserData();
            if (properties.get("exit") != null) {
                game.notifyLevelWon();
            }
        } else if (collisionFixture.getUserData() instanceof Brick) {
            Brick brick = (Brick) collisionFixture.getUserData();
            brick.collideWithPlayer((Ball) ballFixture.getUserData());
            bounceOff((Ball) ballFixture.getUserData());
        }

    }

    public void bounceOff(Ball ball) {
        ball.bounceOff();
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

//        if (fixtureA.getUserData() != null && fixtureA.getUserData() instanceof Ball) {
//            if (fixtureB.getUserData() != null) {
//                contact.setEnabled(false);
//            }
//        } else if (fixtureB.getUserData() != null && fixtureB.getUserData() instanceof Ball) {
//            if (fixtureA.getUserData() != null) {
//                contact.setEnabled(false);
//            }
//        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
