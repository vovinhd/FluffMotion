package com.github.vovinhd.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.github.vovinhd.MapInteractionTest;

/**
 * Created by vovin on 20/08/2015.
 */
public class PointerInputAdapter extends InputAdapter {
    private Vector2 touchDown = new Vector2();
    private MapInteractionTest gameController;

    public PointerInputAdapter(MapInteractionTest gameController) {
        this.gameController = gameController;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchDown.set(screenX, screenY);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {

        float cameraZoomFactor = gameController.getCameraZoomFactor();
        float mapScaleFactor = gameController.getMapScaleFactor();
        OrthographicCamera camera = gameController.getCamera();

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

}
