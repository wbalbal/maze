package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Title_anime;


public class DesktopLauncher {
	public static void main (String[] argv) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title="maZe";
		config.vSyncEnabled=true;
		config.resizable=false;
		config.useGL30=false;
		config.width=800;
		config.height=600;
		
		new LwjglApplication(new Title_anime(), config);
	}
}

