package com.github.vovinhd.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.github.vovinhd.GameLogic.GameMode;

/**
 * Created by vovin on 17/08/2015.
 */
public class Brick extends Obstacle {

    public Brick(Stage parent, Rectangle rectangle, int maxDamage, int value, World world, GameMode gameMode, Array<TextureRegion> animation) {
        super(parent, rectangle, maxDamage, value, world, gameMode, animation);
    }

    @Override
    public void collideWithPlayer(Ball ball) {
        setDamageState(getDamageState() + 1);
        if (getDamageState() >= getMaxDamage()) {
            Gdx.app.log(toString(), String.valueOf(isDead()));
            destroy();
            setDamageState(getDamageState() - 1);
        }
    }
}
