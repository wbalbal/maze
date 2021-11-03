package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img_maze_intro,img_titre_maZe;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img_maze_intro = new Texture("pharaoh_0.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
			batch.draw(img_maze_intro, 0, 0);		
		batch.end();
	}
}
