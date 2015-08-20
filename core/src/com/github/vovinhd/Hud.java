package com.github.vovinhd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.vovinhd.GameLogic.GameMode;

import java.text.DecimalFormat;

/**
 * Created by vovin on 20/08/2015.
 */
public class Hud {

    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"<>";
    private final GameMode gameMode;
    BitmapFont font;
    DecimalFormat timeFormat = new DecimalFormat();
    private Stage stage;
    private Label score;
    private Label timeTaken;
    private Label.LabelStyle hudLabelStyle;

    public Hud(GameMode gameMode, Viewport viewport, Batch batch) {
        this.gameMode = gameMode;
        stage = new Stage();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("FiraMono-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        hudLabelStyle = new Label.LabelStyle(font, Color.WHITE);
        score = new Label("Score", hudLabelStyle);
        score.setAlignment(Align.center);
        score.setPosition(stage.getWidth() / 3 - score.getWidth() / 2, stage.getHeight() - 100);
        timeTaken = new Label("Time", hudLabelStyle);
        timeTaken.setPosition(stage.getWidth() * 2 / 3 - timeTaken.getWidth() / 2, stage.getHeight() - 100);
        stage.addActor(score);
        stage.addActor(timeTaken);
        timeFormat.setMaximumFractionDigits(2);
    }

    public void render(float delta) {
        stage.act(delta);
        timeTaken.setText(timeFormat.format(gameMode.getTime()));
        score.setText(String.valueOf(gameMode.getScore()));
        stage.draw();

    }

}
