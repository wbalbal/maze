package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.tween.ActorAccessor;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class TiltleAnimation implements Screen {

	private SpriteBatch batch , batch2;     //i'll change these damn names
	private TextureAtlas atlas , atlas2 , atlas_btn , atlas_torch;
	private Array<AtlasRegion> animationFrames , 
					animationFrames2 , animationFrames_torch;
	private Animation anim1 , anim2 , anim_torch;   
	private Texture img2;
	private Sprite imgBck;
	
	private Stage stage;
	private Skin skinStartmaZe;
	private Table table;
	private TextButton btnStartGame;
	private TweenManager tweenManager;
	
	float animTime , animTime2;
	
	int edge;
	
	float x_Bck=0 , y_Bck=0;

	Music music =Gdx.audio.newMusic(Gdx.files.internal("Audio_S/Se"
			+ "arch Art _ OpenGameArt.org.ogg"));
	
	@Override
	public void show() {
		Gdx.graphics.setWindowedMode(800, 600);
		img2 = new Texture("simple_pyramide.png");
		
		imgBck = new Sprite(new Texture("intro_maze_bck.jpg"));
		
		batch = new SpriteBatch();
		batch2 = new SpriteBatch();

		
		atlas = new TextureAtlas(Gdx.files.internal("ani"
				+ "mationTitre/intre_maZe.pack"));
		animationFrames = atlas.getRegions();
		anim1 = new Animation(0.5f,animationFrames);  //0.5 FRAME TIME
		anim1.setPlayMode(Animation.PlayMode.LOOP);
		animTime=0.0f;
		
		atlas2 = new TextureAtlas(Gdx.files.internal("fire"
				+ "Backg/fire_backgrnd.pack"));
		animationFrames2 = atlas2.getRegions();
		anim2 = new Animation(0.2f,animationFrames2);
		anim2.setPlayMode(Animation.PlayMode.LOOP);
		animTime2 =0.0f;
		
		//animation torche
		atlas_torch = new TextureAtlas(Gdx.files.internal("t"
				+ "orcheINTRO/torche_intr.pack"));
		animationFrames_torch = atlas_torch.getRegions();
		anim_torch = new Animation(0.1f,animationFrames_torch);
		anim_torch.setPlayMode(Animation.PlayMode.LOOP);
		
		music.play();
		
		//pr le bouttn
		stage = new Stage();
		
		Gdx.input.setInputProcessor(stage);
		
		atlas_btn = new TextureAtlas("btnStart/btn_start.pack");
		skinStartmaZe = new  Skin(atlas_btn);
		table = new Table(skinStartmaZe);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), 
								Gdx.graphics.getHeight());
		
		TextButtonStyle textbuttonstyle = new TextButtonStyle();
		textbuttonstyle.up = skinStartmaZe.getDrawable("btn_up_start");
		textbuttonstyle.down = skinStartmaZe.getDrawable("btn_down_start");
		textbuttonstyle.pressedOffsetX = 1;
		textbuttonstyle.pressedOffsetY = -1;
		textbuttonstyle.font = new BitmapFont(Gdx.files.internal("f"
				+ "ont/v5_Xtender.fnt") , false); 
		btnStartGame = new TextButton("Play",textbuttonstyle);
		btnStartGame.pad(20);
		btnStartGame.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).
						setScreen(new IntromaZe());
			}
		});
		
		//encore pr le boutton
		table.row();
		table.add(btnStartGame);
		stage.addActor(table);
		
		btnStartGame.setPosition(700, 400);
		btnStartGame.setWidth(100);
		
		//pr l'effet fade in du boutton
		tweenManager =  new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor() );
		
		Timeline.createSequence().beginSequence()
			.push(Tween.from(btnStartGame, ActorAccessor.ALPHA, 2.5f).target(0))
			.end().start(tweenManager);
	}
	
	@Override
	public void render(float delta) {

		if(Gdx.input.isKeyPressed(Keys.SPACE))
			((Game)Gdx.app.getApplicationListener()).setScreen(new IntromaZe());
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		animTime += Gdx.graphics.getDeltaTime();

		batch2.begin();
			//Random random = new Random();
			//zoomX = random.nextInt(Gdx.graphics.getWidth()) - 20 ;
			imgBck.setSize(800*2, 800*2);
			
			//animation du background
			if(x_Bck==0 && y_Bck==0)
				edge=0;
			if(x_Bck==-800 && y_Bck==0)
				edge=1;
			if(x_Bck==-800 && y_Bck==-800)
				edge=2;
			if(x_Bck==0 && y_Bck==-800)
				edge=3;
			
			if(edge==0)
				x_Bck-=2;
			if(edge==1)
				y_Bck-=2;
			if(edge==2)
				x_Bck+=2;
			if(edge==3)
				y_Bck+=2;
			
			imgBck.setPosition(x_Bck, y_Bck);
			imgBck.draw(batch2);
		batch2.end();
		
		batch.begin();
			//batch.draw(anim2.getKeyFrame(animTime),0,0);
			batch.draw(img2,0,0);
			batch.draw(anim1.getKeyFrame(animTime),-30,80); //titre et walking feet
			batch.draw(anim_torch.getKeyFrame(animTime),380,300,150,150);
			batch.draw(anim_torch.getKeyFrame(animTime+2),0,450,150,150);
		batch.end();

		
		tweenManager.update(delta);
		
		btnStartGame.setPosition(Gdx.graphics.getWidth()-(170 + 10), 470);
		btnStartGame.setWidth(150);
		btnStartGame.setHeight(100);
		
		//pr le boutton
		stage.act(delta);
		stage.draw();
	}
	

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		//music.pause();
	}

	@Override
	public void resume() {
		//music.play();
	}
	
	@Override
	public void hide() {
		music.stop();
		btnStartGame.clear();
	}

	@Override
	public void dispose() {
		batch.dispose();
		batch2.dispose();
		atlas.dispose();
		atlas2.dispose();
		music.dispose();
		btnStartGame.clear();
		
		stage.dispose();
		atlas_btn.dispose();
		skinStartmaZe.dispose();
	}

}
