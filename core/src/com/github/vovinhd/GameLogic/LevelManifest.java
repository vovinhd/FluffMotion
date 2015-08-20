package com.github.vovinhd.GameLogic;

import com.badlogic.gdx.utils.Array;

/**
 * Created by vovin on 20/08/2015.
 */
public class LevelManifest {

    private Array<LevelDescriptor> levels;

    public Array<LevelDescriptor> getLevels() {
        return levels;
    }

    public void setLevels(Array<LevelDescriptor> levels) {
        this.levels = levels;
    }
}
