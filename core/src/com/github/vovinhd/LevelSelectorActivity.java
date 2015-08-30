package com.github.vovinhd;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.vovinhd.GameLogic.GameMode;
import com.github.vovinhd.GameLogic.LevelDescriptor;
import com.github.vovinhd.Services.LevelDiscoveryService;

/**
 * Created by vovin on 20/08/2015.
 */
public class LevelSelectorActivity implements Screen {

    Stage stage;
    BitmapFont headingFont;
    BitmapFont font;
    SpriteDrawable levelSelectUp = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("levelSelectButtonUp.png"))));
    SpriteDrawable levelSelectDown = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("levelSelectButtonDown.png"))));
    SpriteDrawable levelLaunchButtonUp = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("levelLaunchButtonUp.png"))));
    SpriteDrawable levelLaunchButtonDown = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("levelLaunchButtonDown.png"))));
    Table parentLayout;
    Table container;
    Table levelSelectionPane;
    Table descriptionPane;
    Label.LabelStyle headingStyle;
    Label.LabelStyle infoStyle;
    TextButton.TextButtonStyle levelSelectionButtonStyle;
    TextButton.TextButtonStyle levelLaunchButtonStyle;
    LevelDescriptor selectedLevel;
    private FluffMotion game;
    private GameMode gameMode;
    private LevelDiscoveryService lds;
    private Label levelName;
    private Label parScore;
    private Label parTime;

    public LevelSelectorActivity(FluffMotion game, GameMode gameMode) {
        this.game = game;
        this.gameMode = gameMode;
        lds = new LevelDiscoveryService();
        lds.loadManifest();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("FiraMono-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterHeading = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterHeading.size = 100;
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 55;
        headingFont = generator.generateFont(parameterHeading);
        font = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        headingStyle = new Label.LabelStyle(headingFont, Color.WHITE);
        infoStyle = new Label.LabelStyle(font, Color.WHITE);
        levelSelectionButtonStyle = new TextButton.TextButtonStyle(levelSelectUp, levelSelectUp, levelSelectDown, font);
        levelLaunchButtonStyle = new TextButton.TextButtonStyle(levelLaunchButtonUp, levelLaunchButtonUp, levelLaunchButtonDown, font);

    }

    @Override
    public void show() {
        stage = new Stage();
        parentLayout = new Table();
        parentLayout.setFillParent(true);
        Gdx.input.setInputProcessor(stage);
        Label heading = new Label("FluffMotion", headingStyle);
        heading.setAlignment(Align.center);
        parentLayout.add(heading).expandX().colspan(2);
        parentLayout.row();

        container = new Table();

        buildLevelSelectionPane();
        buildDescriptionPane();

        parentLayout.add(levelSelectionPane).width(0.5f).expandY();
        parentLayout.add(descriptionPane).expandY();
        stage.addActor(parentLayout);
//
//        parentLayout.setDebug(true);
//        container.setDebug(true);
//        levelSelectionPane.setDebug(true);
//        descriptionPane.setDebug(true);
    }

    private void buildDescriptionPane() {
        descriptionPane = new Table();
        Label _levelName = new Label("Levelname: ", infoStyle);
        levelName = new Label("", infoStyle);
        Label _parScore = new Label("Par Score: ", infoStyle);
        parScore = new Label("", infoStyle);
        Label _parTime = new Label("Par Time: ", infoStyle);
        parTime = new Label("", infoStyle);
        descriptionPane.add(_levelName);
        descriptionPane.add(levelName).expandX();
        descriptionPane.row();
        descriptionPane.add(_parScore);
        descriptionPane.add(parScore).expandX();
        descriptionPane.row();
        descriptionPane.add(_parTime);
        descriptionPane.add(parTime).expandX();
        descriptionPane.row();
        TextButton levelLaunchButton = new TextButton("Launch", levelLaunchButtonStyle);
        levelLaunchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openLevel();
            }
        });
        descriptionPane.add(levelLaunchButton).expand();
    }

    private void buildLevelSelectionPane() {
        levelSelectionPane = new Table();

        Array<LevelDescriptor> levels = lds.getLevels();
        for (int i = 0; i < levels.size; i++) {
            final LevelDescriptor levelDescriptor = levels.get(i);
            TextButton levelSelectButton = new TextButton(" " + levelDescriptor.getShortName() + " ", levelSelectionButtonStyle);
            levelSelectButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectLevel(levelDescriptor);
                }
            });
            Cell<TextButton> cell = levelSelectionPane.add().pad(20, 20, 20, 20);

            cell.setActor(levelSelectButton);


            if (i % 4 == 0) {
                if (i == 0) continue;
                levelSelectionPane.row();
            }
        }
    }

    private void selectLevel(LevelDescriptor levelDescriptor) {
        Gdx.app.log("Selected", levelDescriptor.toString());
        selectedLevel = levelDescriptor;
        levelName.setText(levelDescriptor.getName());
        parScore.setText(String.valueOf(levelDescriptor.getParScore()));
        parTime.setText(String.valueOf(levelDescriptor.getParTime()));
    }

    private void openLevel() {
        if (selectedLevel == null) {
            return;
        }
        Gdx.app.log("Open", selectedLevel.toString());
        game.setScreen(new MapInteractionTest(game, gameMode, selectedLevel));
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
        stage.dispose();
    }
}
