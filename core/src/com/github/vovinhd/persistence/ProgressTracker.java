package com.github.vovinhd.persistence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.github.vovinhd.GameLogic.Score;

import java.io.InputStream;


/**
 * Created by vovin on 22/09/2015.
 */
public class ProgressTracker {

    private static final String PROGRESS_FILE_NAME = "progress.json";
    private static ProgressTracker instance;
    private Json json = new Json();

    private Array<Score> scores = new Array<>();

    private FileHandle progressFile = Gdx.files.local(PROGRESS_FILE_NAME);

    private ProgressTracker() {
        loadProgress();
    }

    public static ProgressTracker getInstance() {
        if (instance == null) {
            instance = new ProgressTracker();
        }
        return instance;
    }

    public void loadProgress() {
        if (progressFile.exists()) {
            InputStream inputStream = progressFile.read();
            scores = json.fromJson(Array.class, Score.class, inputStream);
            if (scores == null) scores = new Array<>();
        }
    }

    public void saveProgress() {
        Gdx.app.log(getClass().getSimpleName(), "Saving " + scores.toString() + " to " + progressFile.path());
        json.toJson(scores, Array.class, Score.class, progressFile);
    }

    public void saveLevelProgress(Score score) {
        for (Score s : scores) {
            if (s.getLevel().equals(score.getLevel())) {
                s.setPoints(score.getPoints());
                s.setTime(score.getTime());
                return;
            }
        }
        scores.add(score);
    }


    public Score getScoreForLevel(String name) {
        if (scores == null || scores.size == 0) return null;
        for (Score s : scores) {
            if (s.getLevel().equals(name)) return s;
        }
        return null;
    }
}
