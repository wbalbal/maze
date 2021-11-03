package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.tween.SpriteAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;


public class IntromaZe implements Screen {
	
	private SpriteBatch batch;
	private Sprite img_hist_1;
	
	@SuppressWarnings("unused")
	private BitmapFont font = new BitmapFont(Gdx.files.internal("font/terminal.fnt"));
	 
	private TextureAtlas atlas;
	private Array<AtlasRegion> animationFrames;
	private Animation anim_scene_1_btn;
	
	private TweenManager tweenManager;
	
	public boolean lol=true;
	private float animTime ; 
	
	@Override
	public void show() {
		Gdx.graphics.setWindowedMode(800, 600);
		img_hist_1 = new Sprite(new Texture("hist_scene_1/0.png"));
		batch = new SpriteBatch();
		
		atlas =new TextureAtlas(Gdx.files.internal("hist_scene"
				+ "_1_btn/hist_scene_1_btn.pack"));
		animationFrames = atlas.getRegions();
		anim_scene_1_btn = new Animation(0.5f,animationFrames);
		anim_scene_1_btn.setPlayMode(Animation.PlayMode.NORMAL);
		animTime =0;
		
		///
		
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class , new SpriteAccessor() );
		
		Tween.set(img_hist_1, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(img_hist_1, SpriteAccessor.ALPHA, 2).target(1).start(tweenManager);
		Tween.to(img_hist_1, SpriteAccessor.ALPHA, 2).target(0).delay(2).setCallback(new TweenCallback() {
			
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new hist_scene_2());
			}
		}).start(tweenManager);
		
		img_hist_1.setSize(200, 200);
		img_hist_1.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()/2-50);
		
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		animTime += Gdx.graphics.getDeltaTime();
		
		tweenManager.update(delta);
		
		batch.begin();
			img_hist_1.draw(batch);
			batch.draw(anim_scene_1_btn.getKeyFrame(animTime), 350 , 220);
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		lol=false;
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
		img_hist_1.getTexture().dispose();
		atlas.dispose();
	}

}
