package com.github.vovinhd.GameLogic;

import com.github.vovinhd.GameState.Brick;

/**
 * Created by vovin on 20/08/2015.
 */
public interface GameMode {

    void trigger(String trigger);

    void brickDestroyed(Brick brick);

    void tick(float deltaTime);

    float getTime();

    int getScore();

    void setLevelDescriptor(LevelDescriptor levelDescriptor);
}