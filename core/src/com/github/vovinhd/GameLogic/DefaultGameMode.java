package com.github.vovinhd.GameLogic;

import com.github.vovinhd.FluffMotion;
import com.github.vovinhd.GameState.Obstacle;

/**
 * Created by vovin on 20/08/2015.
 */
public class DefaultGameMode implements GameMode {
    private FluffMotion game;
    private int score;
    private float time;
    private LevelDescriptor levelDescriptor;

    public DefaultGameMode(FluffMotion game) {
        this.game = game;
    }

    public LevelDescriptor getLevelDescriptor() {
        return levelDescriptor;
    }

    public void setLevelDescriptor(LevelDescriptor levelDescriptor) {
        this.levelDescriptor = levelDescriptor;
    }

    public int getScore() {
        return score;
    }

    public float getTime() {
        return time;
    }

    @Override
    public void trigger(String trigger) {
        switch (trigger) {
            case Trigger.EXIT_TRIGGER:
                game.notifyLevelWon(this, levelDescriptor);
                break;
        }
    }

    @Override
    public void brickDestroyed(Obstacle obstacle) {
        score += obstacle.getValue();
    }

    @Override
    public void tick(float deltaTime) {
        time += deltaTime;
    }
}
