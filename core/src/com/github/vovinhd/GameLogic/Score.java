package com.github.vovinhd.GameLogic;

public class Score {
    public int points;
    public float time;
    public String level;

    public Score() {
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Score{" +
                "points=" + points +
                ", time=" + time +
                ", level='" + level + '\'' +
                '}';
    }
}