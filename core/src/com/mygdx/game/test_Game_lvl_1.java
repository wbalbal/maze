package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class test_Game_lvl_1 implements Screen{

	private Texture dirt_bck , block , stp;
	private SpriteBatch batch;
	private TextureAtlas atlas , atlas2 , atlas3;
	private Array<AtlasRegion> animationFrame , animationFrame2 , animationFrame3;
	private Animation anim , anim2 , anim3;
	
	private enum currentState{
		WALKINGR , WALKINGL , WALKINGD , WALKINGU , STOPING
	}
	currentState State;
	
	float animeTime;

	private int spriteX =0, spriteY=0;
	private block blok;
	
	private Rectangle rect;
	
	public Array<block> blk = new Array<block>();
	private boolean collL , collR , collT , collB;
	
	@Override
	public void show() {

		dirt_bck = new Texture(Gdx.files.internal("bckgrnd.png"));
		//block = new Texture(Gdx.files.internal("grnd4.png"));
		blok=new block(200, 200, 30, 30);
		
		for(int i=1; i<5; i++){
			blk.add(new block(200,200+30*i,30,30));
		}
		
		stp = new Texture(Gdx.files.internal("5.png"));
		
		batch = new SpriteBatch();
		//stoper animation
		atlas = new TextureAtlas(Gdx.files.internal("stop/stoper.pack"));
		animationFrame =atlas.getRegions();
		anim = new Animation(0.2f, animationFrame);
		anim.setPlayMode(Animation.PlayMode.LOOP);
		animeTime=0.0f;
		//walking animation right
		atlas2= new TextureAtlas(Gdx.files.internal("walk/walker.pack"));
		animationFrame2 = atlas2.getRegions();
		anim2 = new Animation(0.1f , animationFrame2);
		anim2.setPlayMode(Animation.PlayMode.LOOP);
		//walking animation left
		atlas3= new TextureAtlas(Gdx.files.internal("walkerL/walk/walkerLeft.pack"));
		animationFrame3 = atlas3.getRegions();
		anim3 = new Animation(0.1f , animationFrame3);
		anim3.setPlayMode(Animation.PlayMode.LOOP);
		
		spriteX=0*Gdx.graphics.getWidth()/2;
		spriteY=Gdx.graphics.getHeight()/2;
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		rect = new Rectangle();
		rect.set(spriteX, spriteY, 45, 50);
		animeTime+=Gdx.graphics.getDeltaTime();
		collL=false;
		collR=false;
		collT=false;
		collB=false;
		for(int i=0;i<blk.size;i++){
			if(blk.get(i).LeftColl(rect))
				collL=true;
			if(blk.get(i).RightColl(rect))
				collR=true;
			if(blk.get(i).TopColl(rect))
				collT=true;
			if(blk.get(i).BtmColl(rect))
				collB=true;
			
		}
		if(Gdx.input.isKeyPressed(Keys.D) && collL==false){  //walking right 
			State=currentState.WALKINGR;
			spriteX++;
		}
		else if(Gdx.input.isKeyPressed(Keys.Q) && collR==false){  //walking left
			State=currentState.WALKINGL;
			spriteX--;
		}
		else if(Gdx.input.isKeyPressed(Keys.Z) && collB==false){   //walking up
			State=currentState.WALKINGU;
			spriteY++;
		}
		else if(Gdx.input.isKeyPressed(Keys.S) && collT==false){    //walking down
			State=currentState.WALKINGD;
			spriteY--;
		}else
			State=currentState.STOPING;
		
		batch.begin();
			batch.draw(dirt_bck,0,0);
			//batch.draw(blok.blockIMG,blok.getBlkX(),blok.getBlkY(),blok.getWidtH(),blok.getHeighT());
			for(int i=0;i<blk.size;i++){
				batch.draw(blok.blockIMG,blk.get(i).getBlkX(),blk.get(i).getBlkY());
			}
			if(State==currentState.STOPING)
				batch.draw(stp,spriteX,spriteY);
			if(State==currentState.WALKINGR || State==currentState.WALKINGD)
				batch.draw(anim2.getKeyFrame(animeTime),spriteX,spriteY);
			if(State==currentState.WALKINGL || State==currentState.WALKINGU)
				batch.draw(anim3.getKeyFrame(animeTime),spriteX,spriteY);
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
		block.dispose();
		dirt_bck.dispose();
		stp.dispose();
	}

}
