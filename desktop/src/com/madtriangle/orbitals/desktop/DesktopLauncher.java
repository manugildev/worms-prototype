package com.madtriangle.orbitals.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import orbitals.OrbitalsGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Orbitals";
        config.height = (int) (720);
        config.width = (int) (1080);
        new LwjglApplication(new OrbitalsGame(new ActionResolverDesktop()), config);
    }
}
