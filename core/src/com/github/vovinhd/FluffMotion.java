package com.github.vovinhd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.vovinhd.GameLogic.DefaultGameMode;
import com.github.vovinhd.GameLogic.GameMode;

public class FluffMotion extends Game {
	SpriteBatch batch;
	Texture img;
	GameMode gameMode = new DefaultGameMode(this);

	@Override
	public void create () {
		setScreen(new MapInteractionTest(this, gameMode));
	}

	public void notifyLevelWon() {
		Gdx.app.log("LevelEvent", "You succeeded at winning the mission");
		setScreen(new ChainTestActivity());
	}
}
