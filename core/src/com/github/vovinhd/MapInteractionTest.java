package com.github.vovinhd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.vovinhd.GameLogic.GameMode;
import com.github.vovinhd.GameLogic.LevelDescriptor;
import com.github.vovinhd.GameState.Brick;
import com.github.vovinhd.GameState.Chain;
import com.github.vovinhd.Input.PointerInputAdapter;
import com.github.vovinhd.Input.PointerInputMultiplexer;
import com.github.vovinhd.Services.LevelLoader;

/**
 * Created by vovin on 08/08/2015.
 */
public class MapInteractionTest implements Screen {

    private static final String MAP_PATH_PREFIX = "Levels/";
    private final GameMode gameMode;
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
    Chain chain;
    ShapeRenderer shapeRenderer;
    FluffMotion game;
    private Hud hud;
    private Group brickGroup;
    private int mapPixelWidth;
    private int mapPixelHeight;

    private LevelDescriptor levelDescriptor; 
    
    public MapInteractionTest(FluffMotion game, GameMode gameMode) {
        this.game = game;
        this.gameMode = gameMode;
    }

    public MapInteractionTest(FluffMotion game, GameMode gameMode, LevelDescriptor levelDescriptor) {
        this.game = game;
        this.gameMode = gameMode;
        this.levelDescriptor = levelDescriptor;
        gameMode.setLevelDescriptor(levelDescriptor);
    }

    public float getMapScaleFactor() {
        return mapScaleFactor;
    }

    public float getCameraZoomFactor() {
        return cameraZoomFactor;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        batch = new SpriteBatch();
        viewport = new ScreenViewport(camera);
        shapeRenderer = new ShapeRenderer();
        hud = new Hud(gameMode, viewport, batch);

        LevelLoader loader = new LevelLoader((levelDescriptor != null ? MAP_PATH_PREFIX + levelDescriptor.getMapPath() : "desertTest.tmx"), gameMode);
        loader.load(viewport, batch);

        map = loader.getMap();
        world = loader.getWorld();
        chain = loader.getChain();
        stage = loader.getStage();
        brickGroup = loader.getBrickGroup();
        mapHeight = loader.getMapHeight();
        mapWidth = loader.getMapWidth();
        mapPixelHeight = loader.getMapPixelHeight();
        mapPixelWidth = loader.getMapPixelWidth();
        mapScaleFactor = loader.getMapScaleFactor();

        mapRenderer = new OrthogonalTiledMapRenderer(map, batch);
        debugRenderer = new Box2DDebugRenderer();

        InputMultiplexer im = new PointerInputMultiplexer();
        im.addProcessor(stage);
        im.addProcessor(new PointerInputAdapter(this));

        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        world.step(delta, 6, 2);
        gameMode.tick(delta);
        sweepDeadBodies();
        camera.position.set(chain.root.getPosition().x, chain.root.getPosition().y, camera.position.z);
        constrainViewToMap(camera);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        mapRenderer.setView(camera);
        mapRenderer.render();
        stage.draw();
        hud.render(delta);
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


    public OrthographicCamera getCamera() {
        return camera;
    }
}
