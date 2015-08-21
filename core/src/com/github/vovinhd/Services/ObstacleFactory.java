package com.github.vovinhd.Services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.github.vovinhd.GameLogic.GameMode;
import com.github.vovinhd.GameState.Brick;
import com.github.vovinhd.GameState.Mine;
import com.github.vovinhd.GameState.Obstacle;


/**
 * Created by vovin on 21/08/2015.
 */
public class ObstacleFactory {

    private final Stage parent;
    private final World world;
    private final GameMode gameMode;

    private Array<TextureRegion> brickTextures = new Array<>();
    private Array<TextureRegion> mineTextures = new Array<>();

    public ObstacleFactory(Stage parent, World world, GameMode gameMode) {
        this.parent = parent;
        this.world = world;
        this.gameMode = gameMode;
        brickTextures.add(new TextureRegion(new Texture(Gdx.files.internal("brick_0.png"))));
        brickTextures.add(new TextureRegion(new Texture(Gdx.files.internal("brick_1.png"))));
        brickTextures.add(new TextureRegion(new Texture(Gdx.files.internal("brick_2.png"))));
        mineTextures.add(new TextureRegion(new Texture(Gdx.files.internal("evil_0.png"))));
    }

    public Obstacle create(MapProperties properties, Rectangle rectangle) throws IllegalArgumentException {
        ObstacleType type;
        int maxDamage, value;
        try {
            String obsType = (String) properties.get("obstacle");
            switch (obsType) {
                case "brick":
                    type = ObstacleType.BRICK;
                    break;
                case "mine":
                    type = ObstacleType.MINE;
                    break;
                default:
                    throw new IllegalArgumentException("Obstacle Type " + obsType + " unkown!");
            }
            maxDamage = Integer.parseInt((String) properties.get("maxdamage"));
            value = Integer.parseInt((String) properties.get("value"));
            return create(type, rectangle, maxDamage, value);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Obstacle missing parameters!");
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Obstacle malformed!");
        }
    }

    public Obstacle create(ObstacleType obstacleType, Rectangle rectangle, int maxDamage, int value) {
        Obstacle obstacle;
        switch (obstacleType) {
            case BRICK:
                obstacle = new Brick(parent, rectangle, maxDamage, value, world, gameMode, brickTextures);
                break;
            case MINE:
                obstacle = new Mine(parent, rectangle, maxDamage, value, world, gameMode, mineTextures);
                break;
            default:
                obstacle = null; //cannot be reached if all ObstacleTypes are covered by switch, but required to make IDE shut up
        }
        return obstacle;
    }

    public enum ObstacleType {
        BRICK,
        MINE
    }

}
