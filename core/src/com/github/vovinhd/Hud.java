package com.github.vovinhd;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.vovinhd.GameLogic.GameMode;

/**
 * Created by vovin on 20/08/2015.
 */
public class Hud {

    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"<>";
    private final GameMode gameMode;
    BitmapFont font;
    private Stage stage;
    private Label score;
    private Label timeTaken;


    public Hud(GameMode gameMode, Viewport viewport, Batch batch) {
        this.gameMode = gameMode;
        stage = new Stage(viewport, batch);
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();

    }

}
