package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class hist_scene_2 implements Screen{

	private SpriteBatch batch;
	private TextureAtlas atlas ;
	private Array<AtlasRegion> animationFrames ;
	private Animation anim;
	private float animTime ;
	
	@Override
	public void show() {
		Gdx.graphics.setWindowedMode(800, 600);
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("hist_scene_2/hist_scene_2.pack"));
		animationFrames = atlas.getRegions();
		anim = new Animation(0.2f,animationFrames);
		anim.setPlayMode(Animation.PlayMode.NORMAL);
		animTime =0;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		animTime += Gdx.graphics.getDeltaTime();
		batch.begin();
			batch.draw(anim.getKeyFrame(animTime), Gdx.graphics.getWidth()/2-200
					, Gdx.graphics.getHeight()/2-200,400,400);
		batch.end();
		if(Gdx.input.isKeyPressed(Keys.F))
			((Game)Gdx.app.getApplicationListener()).setScreen(new right_bfr_lvl_1());
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}


	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void hide() {
		
	}
	

}
