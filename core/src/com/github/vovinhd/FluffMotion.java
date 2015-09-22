package com.github.vovinhd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.vovinhd.GameLogic.DefaultGameMode;
import com.github.vovinhd.GameLogic.GameMode;
import com.github.vovinhd.GameLogic.LevelDescriptor;

public class FluffMotion extends Game {
	SpriteBatch batch;
	Texture img;
	GameMode gameMode = new DefaultGameMode(this);

	public FluffMotion() {
	}

	public FluffMotion(DeviceConfiguration... flags) {

	}


	@Override
	public void create () {
		setScreen(new LevelSelectorActivity(this, gameMode));
	}

	public void notifyLevelWon(GameMode gameMode, LevelDescriptor levelDescriptor) {
		Gdx.app.log("LevelEvent", "You succeeded at winning the mission");
		setScreen(new PostLevelActivity(levelDescriptor, gameMode, this));
	}

	public enum DeviceConfiguration {
		NoTouchScreen
	}
}
