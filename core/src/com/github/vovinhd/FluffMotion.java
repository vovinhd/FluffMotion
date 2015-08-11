package com.github.vovinhd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FluffMotion extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		setScreen(new MapInteractionTest(this));
	}

	public void notifyLevelWon() {
		Gdx.app.log("LevelEvent", "You succeeded at winning the mission");
		setScreen(new ChainTestActivity());
	}
}
