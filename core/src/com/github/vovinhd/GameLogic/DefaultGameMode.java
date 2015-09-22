package com.github.vovinhd.GameLogic;

import com.github.vovinhd.FluffMotion;
import com.github.vovinhd.GameState.Obstacle;

/**
 * Created by vovin on 20/08/2015.
 */
public class DefaultGameMode implements GameMode {
    private final FluffMotion game;
    private Score score = new Score();
    private LevelDescriptor levelDescriptor;

    public DefaultGameMode(FluffMotion game) {
        this.game = game;
    }

    public LevelDescriptor getLevelDescriptor() {
        return levelDescriptor;
    }

    public void setLevelDescriptor(LevelDescriptor levelDescriptor) {
        this.levelDescriptor = levelDescriptor;
        score.setLevel(levelDescriptor.getName());
    }

    public int getPoints() {
        return getScore().getPoints();
    }

    public float getTime() {
        return getScore().getTime();
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
        getScore().setPoints(getScore().getPoints() + obstacle.getValue());
    }

    @Override
    public void tick(float deltaTime) {
        getScore().setTime(getScore().getTime() + deltaTime);
    }

    @Override
    public void resetScore() {
        score = new Score();
        score.setLevel(levelDescriptor.getName());
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void setScore(Score score) {
        this.score = score;
    }
}
