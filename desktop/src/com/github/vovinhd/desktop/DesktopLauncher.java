package com.github.vovinhd.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.vovinhd.FluffMotion;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1680;
        config.height = 1050;
        config.overrideDensity = 200;
        new LwjglApplication(new FluffMotion(), config);
	}
}
