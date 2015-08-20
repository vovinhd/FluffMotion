package com.github.vovinhd.GameLogic;

import com.github.vovinhd.FluffMotion;
import com.github.vovinhd.GameState.Brick;

/**
 * Created by vovin on 20/08/2015.
 */
public class DefaultGameMode implements GameMode {
    private FluffMotion game;
    private int score;
    private float time;

    public DefaultGameMode(FluffMotion game) {
        this.game = game;
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
                game.notifyLevelWon();
                break;
        }
    }

    @Override
    public void brickDestroyed(Brick brick) {
        score += brick.getValue();
    }

    @Override
    public void tick(float deltaTime) {
        time += deltaTime;
    }
}
