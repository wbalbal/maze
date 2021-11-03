package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.tween.SpriteAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class gameCleared implements Screen{

	private SpriteBatch batch;
	private Sprite screen, screen2, screen3;
	private float animTime, y;
	private TweenManager tweenManager;

	@Override
	public void show() {
		Gdx.graphics.setWindowedMode(800, 600);
		batch = new SpriteBatch();
		screen = new Sprite(new Texture(Gdx.files.internal("GameCleared.jpg")));
		screen2 = new Sprite(new Texture(Gdx.files.internal("GameCleared2.png")));
		screen3 = new Sprite(new Texture(Gdx.files.internal("GameCleared3.png")));
		
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class , new SpriteAccessor() );		
		
		Tween.set(screen2, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(screen2, SpriteAccessor.ALPHA, 2).target(1).start(tweenManager);
		Tween.to(screen2, SpriteAccessor.ALPHA, 2).target(0).delay(2).start(tweenManager);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		animTime += Gdx.graphics.getDeltaTime();
		tweenManager.update(delta);
		if(animTime<10)
			y = -450+animTime*40;
		batch.begin();
			System.out.println(animTime);
			batch.draw(screen ,0,0,800,600);
			batch.draw(screen2,0,0,800,600);
			batch.draw(screen3,0,y,800,600);
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
		
	}

}
