package com.github.vovinhd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.github.vovinhd.GameLogic.GameMode;
import com.github.vovinhd.GameLogic.LevelDescriptor;
import com.github.vovinhd.persistence.ProgressTracker;

/**
 * Created by vovin on 20/08/2015.
 */
public class PostLevelActivity extends ScreenAdapter {

    private final LevelDescriptor levelDescriptor;
    private final GameMode gameMode;
    private final FluffMotion game;
    private final ProgressTracker progressTracker = ProgressTracker.getInstance();
    Stage stage;
    BitmapFont headingFont;
    BitmapFont font;
    SpriteDrawable levelSelectUp = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("levelSelectButtonUp.png"))));
    SpriteDrawable levelSelectDown = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("levelSelectButtonDown.png"))));
    SpriteDrawable levelLaunchButtonUp = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("levelLaunchButtonUp.png"))));
    SpriteDrawable levelLaunchButtonDown = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("levelLaunchButtonDown.png"))));
    TextButton.TextButtonStyle levelSelectionButtonStyle;
    TextButton.TextButtonStyle levelLaunchButtonStyle;

    Label.LabelStyle headingStyle;
    Label.LabelStyle infoStyle;

    public PostLevelActivity(LevelDescriptor levelDescriptor, GameMode gameMode, FluffMotion game) {

        this.levelDescriptor = levelDescriptor;
        this.gameMode = gameMode;
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("FiraMono-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterHeading = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterHeading.size = 50;
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        headingFont = generator.generateFont(parameterHeading);
        font = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        headingStyle = new Label.LabelStyle(headingFont, Color.WHITE);
        infoStyle = new Label.LabelStyle(font, Color.WHITE);
        levelSelectionButtonStyle = new TextButton.TextButtonStyle(levelSelectUp, levelSelectUp, levelSelectDown, font);
        levelLaunchButtonStyle = new TextButton.TextButtonStyle(levelLaunchButtonUp, levelLaunchButtonUp, levelLaunchButtonDown, font);


        Table parentLayout = new Table();
        Gdx.input.setInputProcessor(stage);
        Label heading = new Label("FluffMotion", headingStyle);
        heading.setAlignment(Align.center);
        parentLayout.add(heading).colspan(2);
        parentLayout.row();
        Label par = new Label("Par", infoStyle);
        Label you = new Label("Run", infoStyle);
        parentLayout.add(par);
        parentLayout.add(you);
        parentLayout.row();
        Label parScore = new Label(String.valueOf("Score: " + levelDescriptor.getParScore()), infoStyle);
        Label score = new Label(String.valueOf(gameMode.getPoints()), infoStyle);
        parentLayout.add(parScore);
        parentLayout.add(score);
        parentLayout.row();
        Label parTime = new Label(String.valueOf("Time: " + levelDescriptor.getParTime()), infoStyle);
        Label time = new Label(String.valueOf(gameMode.getTime()), infoStyle);
        parentLayout.add(parTime);
        parentLayout.add(time);
        parentLayout.row();


        TextButton playAgainButton = new TextButton("Play again", levelLaunchButtonStyle);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                replay();
            }
        });

        TextButton levelListButton = new TextButton("Level List", levelLaunchButtonStyle);
        levelListButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelList();
            }
        });

        parentLayout.add(playAgainButton);
        parentLayout.add(levelListButton);
        parentLayout.setFillParent(true);
        stage.addActor(parentLayout);
        progressTracker.saveLevelProgress(gameMode.getScore());
        progressTracker.saveProgress();
        gameMode.resetScore();
    }

    private void levelList() {
        game.setScreen(new LevelSelectorActivity(game, gameMode));
    }

    private void replay() {
        game.setScreen(new MapInteractionTest(game, gameMode, levelDescriptor));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
