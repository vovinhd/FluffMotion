package com.github.vovinhd.Services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.vovinhd.GameLogic.ChainContactListener;
import com.github.vovinhd.GameLogic.GameMode;
import com.github.vovinhd.GameState.Ball;
import com.github.vovinhd.GameState.Brick;
import com.github.vovinhd.GameState.Chain;
import com.github.vovinhd.GameState.CollisionChannelDefinition;

/**
 * Created by vovin on 20/08/2015.
 */
public class LevelLoader {

    //TODO needs cleanup badly

    private static final String TRIGGER_LAYER_NAME = "triggers";
    private static final String COLLISION_LAYER_NAME = "collision";
    private static final String GROUND_LAYER_NAME = "ground";
    private static final String OBJECT_LAYER_NAME = "objects";

    private final String mapPath;
    private final GameMode gameMode;


    private TiledMap map;
    private World world;
    private Chain chain;
    private Stage stage;
    private Integer mapWidth;
    private Integer mapHeight;
    private Integer mapScaleFactor;
    private int mapPixelWidth;
    private int mapPixelHeight;


    private Group brickGroup;

    public LevelLoader(String mapPath, GameMode gameMode) {
        this.gameMode = gameMode;
        this.mapPath = mapPath;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2[] verts = new Vector2[]{
                new Vector2(0, 0),
                new Vector2(rectangle.getWidth(), 0),
                new Vector2(0, rectangle.getHeight()),
                new Vector2(rectangle.getWidth(), rectangle.getHeight())
        };

        polygon.set(verts);
        return polygon;
    }

    public boolean load(Viewport viewport, Batch batch) {
        map = new TmxMapLoader().load(mapPath);
        MapProperties prop = map.getProperties();
        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
        Integer tilePixelWidth = prop.get("tilewidth", Integer.class);
        Integer tilePixelHeight = prop.get("tileheight", Integer.class);
        mapScaleFactor = tilePixelHeight;
        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ChainContactListener(gameMode));
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
        Group ballGroup = new Group();
        Group linkGroup = new Group();
        brickGroup = new Group();

        stage.addActor(linkGroup);
        stage.addActor(ballGroup);
        stage.addActor(brickGroup);


        MapLayer collisionObjectLayer = map.getLayers().get(COLLISION_LAYER_NAME);
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

        MapLayer triggerObjectLayer = map.getLayers().get(TRIGGER_LAYER_NAME);
        MapObjects triggers = triggerObjectLayer.getObjects();

        for (RectangleMapObject rectangleObject : triggers.getByType(RectangleMapObject.class)) {
            Rectangle rect = rectangleObject.getRectangle();
            String trigger = (String) rectangleObject.getProperties().get("trigger");
            if (trigger != null && trigger.equals("PlayerSpawn")) {
                Ball hero = new Ball(rect.getCenter(new Vector2()), Color.BLACK, world);
                ballGroup.addActor(hero);
                chain = new Chain(hero, world, ballGroup, linkGroup);
                chain.addRandomBall();
                chain.addRandomBall();
                stage.addActor(chain);
                continue;
            }
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

        TiledMapTileLayer tiles = (TiledMapTileLayer) map.getLayers().get(OBJECT_LAYER_NAME);


        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                TiledMapTileLayer.Cell cell = tiles.getCell(i, j);
                if (cell != null && cell.getTile().getProperties().get("brick") != null) {
                    Brick brick = new Brick(stage, new Rectangle(tilePixelWidth * i, tilePixelHeight * j, tilePixelWidth, tilePixelHeight), 3, 100, world, gameMode);
                    brickGroup.addActor(brick);
                    tiles.setCell(i, j, null);
                }
            }
        }


        return true;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public Chain getChain() {
        return chain;
    }

    public Stage getStage() {
        return stage;
    }

    public Integer getMapWidth() {
        return mapWidth;
    }

    public Integer getMapHeight() {
        return mapHeight;
    }

    public Integer getMapScaleFactor() {
        return mapScaleFactor * 2;
    }

    public int getMapPixelWidth() {
        return mapPixelWidth;
    }

    public int getMapPixelHeight() {
        return mapPixelHeight;
    }

    public Group getBrickGroup() {
        return brickGroup;
    }

}
