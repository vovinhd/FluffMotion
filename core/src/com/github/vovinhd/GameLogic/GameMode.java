package com.github.vovinhd.GameLogic;

import com.github.vovinhd.GameState.Obstacle;

/**
 * Created by vovin on 20/08/2015.
 */
public interface GameMode {

    void trigger(String trigger);

    void brickDestroyed(Obstacle obstacle);

    void tick(float deltaTime);

    float getTime();

    int getScore();

    void setLevelDescriptor(LevelDescriptor levelDescriptor);
}