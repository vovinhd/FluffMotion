package com.github.vovinhd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by vovin on 08/08/2015.
 */
public class MapLoaderTestActivity extends InputAdapter implements Screen {

    float MAPSCALEFACTOR = 1f/32f;
    float CAMERAZOOMFACTOR = 1f/4f;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    int mapPixelWidth;
    int mapPixelHeight;
    private int tilePixelWidth;
    private int tilePixelHeight;
    private int mapWidth;
    private int mapHeight;


    @Override
    public void show() {

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        map = new TmxMapLoader().load("desertTest.tmx");

        MapProperties prop = map.getProperties();

        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
        tilePixelWidth = prop.get("tilewidth", Integer.class);
        tilePixelHeight = prop.get("tileheight", Integer.class);

        renderer = new OrthogonalTiledMapRenderer(map, MAPSCALEFACTOR);


        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;


        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
          camera.setToOrtho(false, width * MAPSCALEFACTOR * CAMERAZOOMFACTOR, height * MAPSCALEFACTOR * CAMERAZOOMFACTOR);
    }

    private Vector2 touchDown =new Vector2();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchDown.set(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 touchDrag = new Vector2(screenX, screenY);
        Vector2 delta = touchDrag.cpy().sub(touchDown);
        delta.scl(-1, 1);
        delta.scl(MAPSCALEFACTOR * CAMERAZOOMFACTOR);

        Gdx.app.log("CAMERA MOVE: ", delta.toString());
        touchDown = touchDrag;
        camera.translate(delta);

        constrainViewToMap(camera);

        return true;
    }



    private void constrainViewToMap(OrthographicCamera camera) {
        if(camera.position.x - camera.viewportWidth / 2 < 0)
            camera.position.x = camera.viewportWidth / 2;
        else if(camera.position.x + camera.viewportWidth / 2 > mapWidth)
            camera.position.x = mapWidth - camera.viewportWidth / 2;
        if(camera.position.y - camera.viewportHeight / 2 < 0)
            camera.position.y = camera.viewportHeight / 2;
        else if(camera.position.y + camera.viewportHeight / 2 > mapHeight)
            camera.position.y = mapHeight - camera.viewportHeight / 2;
        camera.update();
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


