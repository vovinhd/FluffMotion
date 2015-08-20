package com.github.vovinhd.GameLogic;

/**
 * Created by vovin on 20/08/2015.
 */
public class LevelDescriptor {
    private String name;
    private String shortName;
    private String mapPath;
    private int parScore;
    private float parTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMapPath() {
        return mapPath;
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    public int getParScore() {
        return parScore;
    }

    public void setParScore(int parScore) {
        this.parScore = parScore;
    }

    public float getParTime() {
        return parTime;
    }

    public void setParTime(float parTime) {
        this.parTime = parTime;
    }

    @Override
    public String toString() {
        return "LevelDescriptor{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", mapPath='" + mapPath + '\'' +
                ", parScore=" + parScore +
                ", parTime=" + parTime +
                '}';
    }
}
