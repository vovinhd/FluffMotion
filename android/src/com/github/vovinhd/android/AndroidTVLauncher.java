package com.github.vovinhd.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.vovinhd.FluffMotion;

/**
 * Created by vovin on 22/09/2015.
 */
public class AndroidTVLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        initialize(new FluffMotion(FluffMotion.DeviceConfiguration.NoTouchScreen), config);
    }
}
