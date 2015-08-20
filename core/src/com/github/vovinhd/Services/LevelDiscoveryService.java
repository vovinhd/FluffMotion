package com.github.vovinhd.Services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.github.vovinhd.GameLogic.LevelDescriptor;
import com.github.vovinhd.GameLogic.LevelManifest;

/**
 * Created by vovin on 20/08/2015.
 */
public class LevelDiscoveryService {

    private final static String levelManifestPath = "Levels/levels.json";
    private Array<LevelDescriptor> levels;

    public Array<LevelDescriptor> getLevels() {
        return levels;
    }

    public Array<LevelDescriptor> loadManifest() {
        Json json = new Json();
        json.setIgnoreUnknownFields(true);
        levels = json.fromJson(LevelManifest.class, Gdx.files.internal(levelManifestPath)).getLevels();

        return levels;
    }
}
