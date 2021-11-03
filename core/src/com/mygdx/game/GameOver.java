package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOver implements Screen{

	private float animTime;
	private Sprite screen, choix;
	private SpriteBatch batch;
	private int choice=0;

	@Override
	public void show() {
		Gdx.graphics.setWindowedMode(800, 600);
		batch = new SpriteBatch();
		screen = new Sprite(new Texture(Gdx.files.internal("GameOverScreen.jpg")));
		choix = new Sprite(new Texture(Gdx.files.internal("choice1.png")));
		
		Gdx.input.setInputProcessor(new InputController(){
			@Override
			public boolean keyDown(int keycode) {
				switch(keycode){
				case Keys.UP:
					if(choice==1)
						choice=0;
					else
						choice=1;
					break;
				case Keys.DOWN:
					if(choice==1)
						choice=0;
					else
						choice=1;
					break;
				case Keys.ENTER:
					if(choice==1)
						Gdx.app.exit();
					else
						((Game)Gdx.app.getApplicationListener()).setScreen(new right_bfr_lvl_1());
					break;
			    }
				return true;
			}
		});
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		animTime += Gdx.graphics.getDeltaTime();
		batch.begin();
			batch.draw(screen,0,0,800,600);
			if (choice==0)
				batch.draw(choix, 280, 220, 50, 50);
			else
				batch.draw(choix, 280, 140, 50, 50);
				
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		screen.getTexture().dispose();
	}

}
