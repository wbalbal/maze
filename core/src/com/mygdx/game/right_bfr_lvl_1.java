package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.tween.SpriteAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class right_bfr_lvl_1 implements Screen{
	
	private SpriteBatch        batch;
	private Texture            bck,bckLight;
	private Sprite             spr, sprLight, sprRight, sprLeft, sprDown, sprUp, 
							   sprSwitchOn, sprSwitchOff, upOfWalls, doorspr, corn, fireRotation, FireSwitch1, FireSwitch2;
	
	private TweenManager       tweenManager;
	
	private World 			   world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private TextureAtlas 	   atlasWR, atlasWL, atlasWU, atlasWD, atlasJews,atlasFireball,atlasFireballLeft,
									atlasLava, atlasDoor, atlasDemon, atlasScroll1;
	private Animation          An_walkingMan/*<-right*/, LeftMan, UpMan, DownMan, jews, fireball, fireballLeft
									, Lava, door, demon, scroll1;
	private Body 			   bod ,bod2, bodDiam, bodLava, bodspk, bodspk2, bodFire, bodFire2, 
								bodFire00, bodFire01, bodFire02, fireTrap1, fireTrap2, fireTrap3, fireTrap4;
	private block 			   bloq;
	private Vector2            mvt = new Vector2();
	private boolean			   switcher1=true, switcher3=true, diam1Collected=true, doortimer=false,
							   diam2Collected=true, corncolected = false, fireOn = true, doorOpened = false, scroll1collected=false;
	private int 			   velocityIterations = 8, positionIterations = 3;
	private float              timeStep = 1/60f, animTime, animetime2, animetime3, animetime4, spr_x=10, spr_y=250,
							   spr_height=60, spr_width=40, fireballX=-20, fireballY=7, fireballLX=20, fireballLY=9,
							   fireballX2 = -40, fireballY2 = 7, fireballLX2 = 40, fireballLY2 = 9;
	private enum 			   CurrentState{UP, DOWN, RIGHT, LEFT,
			                       STOPUP, STOPDOWN, STOPRIGHT, STOPLEFT;};
	private CurrentState       state=CurrentState.STOPRIGHT;
	
	private Array<Body>        tmpbodies = new Array<Body>();
	private Array<AtlasRegion> animationFrames;
	
	private Sprite 			   spikeSpr, replay;
	private BouncyBall ball;
	Music Bckmusic =Gdx.audio.newMusic(Gdx.files.internal("Audio_S/Ancient+Egyptian+Music+-+Pharaoh+Ramses+II.mp3"));
	Music fireballSnd = Gdx.audio.newMusic(Gdx.files.internal("Audio_S/Flame Arrow-SoundBible.com-618067908.mp3"));
	Music demonScream = Gdx.audio.newMusic(Gdx.files.internal("Audio_S/demonScream.mp3"));
	
	@SuppressWarnings("unused")
	@Override
	public void show() {
		Gdx.graphics.setWindowedMode(800, 600);
		world         = new World(new Vector2(0, -9.8f), false);
		debugRenderer = new Box2DDebugRenderer();
		camera  	  = new OrthographicCamera(Gdx.graphics.getWidth()/20,Gdx.graphics.getHeight()/20);
		batch		  = new SpriteBatch();
		//camera.zoom/=.9f;
		
		Bckmusic.play();
		if(!Bckmusic.isPlaying())
			Bckmusic.setVolume(.3f);
		fireballSnd.setVolume(.2f);
		spikeSpr = new Sprite(new Texture("spiky0.png"));
		//controlling sprite mvt and walking state
		Gdx.input.setInputProcessor(new InputController(){
			@Override
			public boolean keyDown(int keycode) {
				switch(keycode){
				case Keys.UP:
					mvt.y=8;
					state=CurrentState.UP;
					break;
				case Keys.DOWN:
					mvt.y=-8;
					state=CurrentState.DOWN;
					break;
				case Keys.RIGHT:
					mvt.x=8;
					state=CurrentState.RIGHT;
					break;
				case Keys.LEFT:
					mvt.x=-8;
					state=CurrentState.LEFT;
					break;
				case Keys.ESCAPE:
					scroll1collected = false;
					animetime4 = 0;
					break;
				case Keys.F:
					System.out.println(spr_x+" | "+spr_y);
					if(spr_x+11.5f<3 && spr_x+11.5f>0 && spr_y+10.25f<3 && spr_y+10.25f>0) {
						switcher1=!switcher1;
					}
					//if(spr_x+1.5f<1 && spr_x+1.5f>0 && spr_y+10.25f<3 && spr_y+10.25f>0)
					if(spr_x-10.5f<4 && spr_x-10.5f>0 && spr_y+10.25f<3 && spr_y+10.25f>0)
						switcher3=!switcher3;
					break;
				}
				return true;
			}
			@Override
			public boolean keyUp(int keycode) {
				switch(keycode){
				case Keys.UP:
					mvt.y=0;
					mvt.x=0;
					state=CurrentState.STOPUP;
					break;
				case Keys.DOWN:
					mvt.y=0;
					mvt.x=0;
					state=CurrentState.STOPDOWN;
					break;
				case Keys.RIGHT:
					mvt.x=0;
					mvt.y=0;
					state=CurrentState.STOPRIGHT;
					break;
				case Keys.LEFT:
					mvt.x=0;
					mvt.y=0;
					state=CurrentState.STOPLEFT;
					break;
				}
				return true;
			}
			
			/////
			@Override
			public boolean touchUp (int x, int y, int pointer, int button) {
				if(x>762 && x<779 && y>20 && y<39){
					((Game) Gdx.app.getApplicationListener()).setScreen(new right_bfr_lvl_1());
				}
				System.out.println(x+" | "+y);
				return true;
			}
			/////
		});
		
		//contact with items
		world.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				
			}
			
			@Override
			public void endContact(Contact contact) {
				
			}
			
			@Override
			public void beginContact(Contact contact) {
				final Fixture fa = contact.getFixtureA();
				final Fixture fb = contact.getFixtureB();
			
				if(fa==null || fb==null) return;
				if(fa.getUserData() instanceof MainChara_box && fb.getUserData() instanceof BouncyBall){
					((Game)Gdx.app.getApplicationListener()).setScreen(new GameOver());
				}
				
				if(fb.getUserData() instanceof Diamon && fb.getBody().getUserData().equals(2)){
					diam2Collected=true;
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							world.destroyBody(fb.getBody());
						}
					});
				}if(fb.getUserData() instanceof Diamon && fb.getBody().getUserData().equals(1)){
					diam1Collected=true;
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							world.destroyBody(fb.getBody());
						}
					});
				}if(fb.getUserData() instanceof Diamon && fb.getBody().getUserData().equals(99)){
					corncolected=!corncolected;
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							world.destroyBody(fb.getBody());
						}
					});
				}if(fb.getUserData() instanceof Lava || fa.getUserData() instanceof Lava){
					((Game)Gdx.app.getApplicationListener()).setScreen(new GameOver());
				}if(fb.getUserData() instanceof ObstacleBox ){
					if(switcher1 && switcher3){
						Gdx.app.postRunnable(new Runnable() {
							
							@Override
							public void run() {
								world.destroyBody(fb.getBody());
							}
						});
					}
					
				}if(fa.getUserData() instanceof ObstacleBox){
					if(switcher1 && switcher3){
						Gdx.app.postRunnable(new Runnable() {
							
							@Override
							public void run() {
								world.destroyBody(fa.getBody());
							}
						});
					}
				}if(fb.getUserData() instanceof Diamon && fb.getBody().getUserData().equals(55)){
					bod2.applyLinearImpulse(-500, -500, 0, 0, true);
				}if(fb.getUserData() instanceof Diamon && fb.getBody().getUserData().equals(17)){
					scroll1collected = true;
				}if(fa.getUserData() instanceof rotatingFire && fa.getBody().getUserData().equals(1)){
					System.out.println("gameOver");
					((Game)Gdx.app.getApplicationListener()).setScreen(new GameOver());
				}if(fa.getUserData() instanceof rotatingFire && fb.getUserData() instanceof rotatingFire){
					//those are not really fire traps juste 3gz to new class :p :p
					fireOn = false;
					Gdx.app.postRunnable(new Runnable() {
						
						@Override
						public void run() {
							world.destroyBody(fireTrap1);
							world.destroyBody(fireTrap2);
							world.destroyBody(fireTrap3);
							world.destroyBody(fireTrap4);
						}
					});
				}
				
			}
		});
		
		//fade effect
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.set(spr, SpriteAccessor.ALPHA   ).target(0).start(tweenManager);
		Tween.to (spr, SpriteAccessor.ALPHA, 2).target(1).start(tweenManager);
		
		//the top of the walls
		upOfWalls = new Sprite(new Texture(Gdx.files.internal("topOfWall.png")));
		//closed door
		doorspr = new Sprite(new Texture(Gdx.files.internal("door/0.png")));
		//the background texture and the "light"
		bck   	 = new Texture(Gdx.files.internal("1thMape.png"));
		spr      = new Sprite(bck);
		bckLight = new Texture(Gdx.files.internal("darkTOlightGradient.png"));
		sprLight = new Sprite(bckLight);
		//le switch
		sprSwitchOn  = new Sprite(new Texture(Gdx.files.internal("switch/switchON.png" )));
		sprSwitchOff = new Sprite(new Texture(Gdx.files.internal("switch/switchOFF.png")));
		//corn
		corn = new Sprite(new Texture(Gdx.files.internal("corn.png")));
		//fireRotation
		fireRotation = new Sprite(new Texture(Gdx.files.internal("fireRotat.png")));
		//fireSwitch1 et 2
		FireSwitch1 = new Sprite(new Texture(Gdx.files.internal("Fireswitch1.png")));
		FireSwitch2 = new Sprite(new Texture(Gdx.files.internal("Fireswitch2.png")));
		//
		replay = new Sprite(new Texture(Gdx.files.internal("replay.png")));
		
		//init de l'animation walking right
		atlasWR         = new TextureAtlas(Gdx.files.internal("mainWalk/mainWalkRight/walkRight/right.atlas"));
		animationFrames = atlasWR.getRegions();
		An_walkingMan   = new Animation(0.1f, animationFrames);
		An_walkingMan   .setPlayMode(Animation.PlayMode.LOOP);
		//le stopRight
		sprRight        = new Sprite(new Texture(Gdx.files.internal("mainWalk/mainStopRight/6.png")));
		
		//inti de l'animation walking left
		atlasWL         = new TextureAtlas(Gdx.files.internal("mainWalk/mainWalkLeft/walkLeft/Left.atlas"));
		animationFrames = atlasWL.getRegions();
		LeftMan         = new Animation(0.1f, animationFrames);
		LeftMan         .setPlayMode(Animation.PlayMode.LOOP);
		//le stopLeft
		sprLeft         = new Sprite(new Texture(Gdx.files.internal("mainWalk/mainStopLeft/6.png")));
		
		//inti de l'animation walking up
		atlasWL         = new TextureAtlas(Gdx.files.internal("mainWalk/mainWalkUP/WalkUp/Up.atlas"));
		animationFrames = atlasWL.getRegions();
		UpMan           = new Animation(0.1f, animationFrames);
		UpMan           .setPlayMode(Animation.PlayMode.LOOP);
		//le stopUp
		sprUp           = new Sprite(new Texture(Gdx.files.internal("mainWalk/mainStopUP/0.png")));
		
		//inti de l'animation walking down
		atlasWL         = new TextureAtlas(Gdx.files.internal("mainWalk/mainWalkDown/WalkDown/down.atlas"));
		animationFrames = atlasWL.getRegions();
		DownMan         = new Animation(0.1f, animationFrames);
		DownMan         .setPlayMode(Animation.PlayMode.LOOP);
		//le stopDown
		sprDown         = new Sprite(new Texture(Gdx.files.internal("mainWalk/mainStopDown/0.png")));
		
		//init de l'animaO dimonds
		atlasJews	    = new TextureAtlas(Gdx.files.internal("diamonds/diams/diam.atlas"));
		animationFrames = atlasJews.getRegions();
		jews   			= new Animation(0.2f, animationFrames);
		jews			.setPlayMode(PlayMode.LOOP);
		
		//init du fireball animO
		atlasFireball   = new TextureAtlas(Gdx.files.internal("AOfireball/AOkaton.atlas"));
		animationFrames = atlasFireball.getRegions();
		fireball        = new Animation(0.2f, animationFrames);
		fireball        .setPlayMode(PlayMode.LOOP);
		//fireball Left
		atlasFireballLeft = new TextureAtlas(Gdx.files.internal("AOfireballLeft/AOkaton.atlas"));
		animationFrames=atlasFireballLeft.getRegions();
		fireballLeft=new Animation(0.2f, animationFrames);
		fireballLeft.setPlayMode(PlayMode.LOOP);
		
		//init du lava
		atlasLava = new TextureAtlas(Gdx.files.internal("lava/lava.atlas"));
		animationFrames = atlasLava.getRegions();
		Lava = new Animation(0.1f, animationFrames);
		Lava.setPlayMode(PlayMode.LOOP);
		
		//init du door
		atlasDoor = new TextureAtlas(Gdx.files.internal("door/door.atlas"));
		animationFrames = atlasDoor.getRegions();
		door = new Animation(0.5f, animationFrames);
		door.setPlayMode(PlayMode.NORMAL);
		
		//init du screaming demon
		atlasDemon = new TextureAtlas(Gdx.files.internal("demon/demonScrm.atlas"));
		animationFrames = atlasDemon.getRegions();
		demon = new Animation(.3f, animationFrames);
		demon.setPlayMode(PlayMode.NORMAL);
		
		//inti du scroll1
		atlasScroll1 = new TextureAtlas(Gdx.files.internal("scroll1/scroll1.atlas"));
		animationFrames = atlasScroll1.getRegions();
		scroll1 = new Animation(.5f, animationFrames);
		scroll1.setPlayMode(PlayMode.NORMAL);
		
		//definition du body et fixture
		BodyDef   body   = new BodyDef();
		FixtureDef fixtr = new FixtureDef();
		FixtureDef fixtrItems = new FixtureDef();
		
		//1
		BlockBox blok1     = new BlockBox(.5f*6 , .5f   , -20+3   ,   1    , body, fixtr, world, bod);
		//2
		BlockBox blok2     = new BlockBox(.5f*11, .5f   ,-20+5.5f ,  -3.5f , body, fixtr, world, bod);
		//3
		BlockBox blok3     = new BlockBox(.5f   , .5f*4 ,-20+10.5f,  -1    , body, fixtr, world, bod);
		BlockBox blok3B    = new BlockBox(.5f   , .5f*5 ,-20+6.5f ,  .5f*6 , body, fixtr, world, bod);
		//4
		BlockBox blok4     = new BlockBox(.5f*9 , .5f   ,-20+15.5f,  .5f   , body, fixtr, world, bod);
		BlockBox blok4B    = new BlockBox(.5f*7 , .5f   ,-20+26.5f,  .5f   , body, fixtr, world, bod);
		//5
		BlockBox blok5     = new BlockBox(.5f   , .5f*7 ,-20+30.5f,  -.5f*4, body, fixtr, world, bod);
		ObstacleBox obstcl = new ObstacleBox(.5f, .5f*5, -20+30.5f, .5f*8  , body, fixtr, world, bod);
		BouncyBall ball    = new BouncyBall(1, 5, 2f, body, fixtr, world, bodspk, 300);
		bodspk = world.createBody(body);
		bodspk.createFixture(fixtr).setUserData(ball);;
		bodspk.applyForceToCenter(0, 300, true);
		ball.dispose();
		BouncyBall ball2   = new BouncyBall(1, 3, -8f, body, fixtr, world, bodspk2, 300);
		bodspk2 = world.createBody(body);
		bodspk2.createFixture(fixtr).setUserData(ball2);;
		bodspk2.applyForceToCenter(0, 300, true);
		ball2.dispose();
		BlockBox blok5B    = new BlockBox(.5f   , .5f*10 ,-20+39.5f,  .5f, body, fixtr, world, bod);
		//6
		BlockBox blok6     = new BlockBox(.5f*9 , .5f   ,-20+35.5f,  .5f*12, body, fixtr, world, bod);
		BlockBox blok6b    = new BlockBox(.5f*9 , .5f   ,-20+35.5f, -.5f*10, body, fixtr, world, bod);
		//7
		BlockBox blok7     = new BlockBox(.5f*17, .5f   ,-20+13.5f, -.5f*16, body, fixtr, world, bod);
		BlockBox blok7B    = new BlockBox(.5f*7 , .5f   ,-20+32.5f, -.5f*16, body, fixtr, world, bod);
		BlockBox blok7BB   = new BlockBox(.5f*5 , .5f   ,-20+7.5f , -.5f*22, body, fixtr, world, bod);
		BlockBox blok7BBB  = new BlockBox(.5f*4 , .5f   ,-20+31f  , -.5f*22, body, fixtr, world, bod);
		//8
		BlockBox blok8     = new BlockBox(.5f   , .5f*2 ,-20+5.5f , -.5f*19, body, fixtr, world, bod);
		BlockBox blok8B    = new BlockBox(.5f   , .5f*2 ,-20+15.5f, -.5f*19, body, fixtr, world, bod);
		BlockBox blok8BB   = new BlockBox(.5f   , .5f*3 ,-20+25.5f, -.5f*18, body, fixtr, world, bod);
		BlockBox blok8BBB  = new BlockBox(.5f   , .5f*2 ,-20+29.5f, -.5f*19, body, fixtr, world, bod);
		BlockBox blok8BBBB = new BlockBox(.5f   , .5f*2 ,-20+35.5f, -.5f*19, body, fixtr, world, bod);
		
		///boxes de bordures
		BlockBox borderl = new BlockBox(.1f   , 15 ,-20-.1f, 0, body, fixtr, world, bod);
		BlockBox borderr = new BlockBox(.1f   , 15 , 20.1f, 0, body, fixtr, world, bod);
		BlockBox borderu = new BlockBox( 20   , .1f,0, 15, body, fixtr, world, bod);
		BlockBox borderd = new BlockBox(20   , .1f ,0, -15, body, fixtr, world, bod);
		BlockBox borderud = new BlockBox(20   , .1f ,0, 14, body, fixtr, world, bod);
		BlockBox borderudd = new BlockBox(10.8f  , .1f ,2.8f, 12, body, fixtr, world, bod);
		//rotating fireball
		rotatingFire fire = new rotatingFire(1.5f, .3f, -2, -3.75f, body, fixtr, world, bodFire, 1);
		bodFire = world.createBody(body);
		bodFire.setUserData(fire.id);
		bodFire.createFixture(fixtr).setUserData(fire);
		bodFire.setAngularVelocity(.5f);
		fire.dispose();
		//rotating fireball
		rotatingFire fire2 = new rotatingFire(1.5f, .3f, 4, -3.75f, body, fixtr, world, bodFire2, 1);
		bodFire2 = world.createBody(body);
		bodFire2.setUserData(fire2.id);
		bodFire2.createFixture(fixtr).setUserData(fire2);
		bodFire2.setAngularVelocity(-.5f);
		fire2.dispose();
		
		//switch
		rotatingFire FireSwitch00 = new rotatingFire(1, .3f, -19.1f, 11f, body, fixtr, world, bodFire00, 0);
		fixtr.restitution = 0;
		body.type = BodyType.StaticBody; /////
		bodFire00 = world.createBody(body);
		bodFire00.setUserData(FireSwitch00.id);
		bodFire00.createFixture(fixtr).setUserData(FireSwitch00);
		FireSwitch00.dispose();
		//switch
		rotatingFire FireSwitch01 = new rotatingFire(1, .3f, -19.1f, 12f, body, fixtr, world, bodFire01, 0);
		fixtr.restitution = 0;
		body.type = BodyType.StaticBody; /////
		bodFire01 = world.createBody(body);
		bodFire01.setUserData(FireSwitch01.id);
		bodFire01.createFixture(fixtr).setUserData(FireSwitch01);
		FireSwitch01.dispose();
		
		//switch button
		rotatingFire notfire = new rotatingFire(.1f, .4f, -17.3f, 11.4f, body, fixtr, world, bodFire02, 0);
		fixtr.restitution = 0;
		body.type = BodyType.DynamicBody; /////
		bodFire02 = world.createBody(body);
		bodFire02.setUserData(notfire.id);
		bodFire02.createFixture(fixtr).setUserData(notfire);;
		notfire.dispose();
		
		//line of fire need to be turned off
		rotatingFire fireLine1 = new rotatingFire(.5f, .4f, 0, 0, body, fixtr, world, fireTrap1, 1);
		body.type = BodyType.DynamicBody; /////
		fireTrap1 = world.createBody(body);
		fireTrap1.setUserData(fireLine1.id);
		fireTrap1.createFixture(fixtr).setUserData(fireLine1);;
		fireLine1.dispose();
		
		//line of fire need to be turned off
		rotatingFire fireLine2 = new rotatingFire(.5f, .4f, 0, 0, body, fixtr, world, fireTrap2, 1);
		body.type = BodyType.DynamicBody; /////
		fireTrap2 = world.createBody(body);
		fireTrap2.setUserData(fireLine2.id);
		fireTrap2.createFixture(fixtr).setUserData(fireLine2);;
		fireLine2.dispose();
		
		//line of fire need to be turned off
		rotatingFire fireLine3 = new rotatingFire(.5f, .4f, 0, 0, body, fixtr, world, fireTrap3, 1);
		body.type = BodyType.DynamicBody; /////
		fireTrap3 = world.createBody(body);
		fireTrap3.setUserData(fireLine3.id);
		fireTrap3.createFixture(fixtr).setUserData(fireLine3);;
		fireLine3.dispose();
		
		//line of fire need to be turned off
		rotatingFire fireLine4 = new rotatingFire(.5f, .4f, 0, 0, body, fixtr, world, fireTrap4, 1);
		body.type = BodyType.DynamicBody; /////
		fireTrap4 = world.createBody(body);
		fireTrap4.setUserData(fireLine4.id);
		fireTrap4.createFixture(fixtr).setUserData(fireLine4);;
		fireLine4.dispose();
		
		//Lava Box
		Lava lava   = new Lava(.5f, .5f*4, -20+15.5f, -.5f*25, body, fixtr, world, bodLava);
		Lava lavaB  = new Lava(.5f, .5f*4, -20+25.5f, -.5f*25, body, fixtr, world, bodLava);
		Lava lavaBB = new Lava(.5f, .5f*4, -20+35.5f, -.5f*25, body, fixtr, world, bodLava);
		
		
		//a freaking box for sprite
		MainChara_box box = new MainChara_box(.75f, 1.4f, (spr_x+spr_width /2-Gdx.graphics.getWidth() /2)/20
				, (spr_y+spr_height/2-Gdx.graphics.getHeight()/2)/20, body, fixtrItems, world, bod2);
		bod2   =world.createBody(body);
		bod2   .createFixture(fixtr).setUserData(box);
		bod2   .setFixedRotation(true);
		box    .dispose();
		
		//diams
		Diamon boxDiam  = new Diamon(.35f, .55f, -20+1.4f, -.5f*14+.1f, body, fixtrItems, world, bodDiam, 1); 
		//2ndDiam
		Diamon boxDiam2 = new Diamon(.35f, .55f, -20+1.4f, -.5f*22+.1f, body, fixtrItems, world, bodDiam, 2); 
		//scroll1 collection
		Diamon scroll1collection = new Diamon(.5f, .5f, -10, -1, body, fixtrItems, world, bodDiam, 17);
		//a corn taking a diamon box
		Diamon corn = new Diamon(1, .5f, 18, -3.5f, body, fixtrItems, world, bodDiam, 99);
		//rossor
		Diamon rossor = new Diamon(.1f, 1, 19f, 4f, body, fixtrItems, world, bodDiam, 55);
		
		bod2.setUserData(An_walkingMan);
		bod2.setUserData(LeftMan);
		bod2.setUserData(UpMan);
		bod2.setUserData(DownMan);
		bodspk.setUserData(spikeSpr);
		bodspk2.setUserData(spikeSpr);
		////!!!!!!!!!!!!!!!!!!!!!!!veryyy important(to remove debug draw)!!!!!!!!!!!!!!!!!////////
		debugRenderer.setDrawBodies(false);
		bloq    = new block(0,0,10,10);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		animTime +=Gdx.graphics.getDeltaTime();
		bod2 	 .setLinearVelocity(mvt);
		batch	 .setProjectionMatrix(camera.combined);
		batch	 .begin();
			if(doortimer)
				animetime2+=2*Gdx.graphics.getDeltaTime();
			if(corncolected)
				animetime3+=Gdx.graphics.getDeltaTime();
			if(scroll1collected)
				animetime4+=Gdx.graphics.getDeltaTime();
			
			batch.draw(spr, 0-Gdx.graphics.getWidth()/40, 0-Gdx.graphics.getHeight()/40,40,30);
			if(doorOpened && spr_x>5 && spr_x<8 && spr_y>10){
				((Game)Gdx.app.getApplicationListener()).setScreen(new level2());
			}
			if(corncolected && spr_x>5 && spr_x<8 && spr_y>7 && diam1Collected & diam2Collected)
				doorOpened =true;
			if(doorOpened){
				doortimer=!doortimer;
				batch.draw(door.getKeyFrame(animetime2), 5,9.5f,3,3);
			}else{
				batch.draw(doorspr, 5, 9.5f, 3, 3);
			}
			world.getBodies(tmpbodies);
			
			for(Body body : tmpbodies){
				if(body.getUserData() !=null && body.getUserData() instanceof Sprite){
					batch.draw(spikeSpr, bodspk2.getPosition().x+2, bodspk2.getPosition().y-9, 2, 2);
					batch.draw(spikeSpr, bodspk .getPosition().x+4, bodspk .getPosition().y+1, 2, 2);
					//FireSwitch
					batch.draw(FireSwitch2, bodFire02.getPosition().x-.9f, bodFire02.getPosition().y-.3f, 1, .6f);
					batch.draw(FireSwitch1, bodFire01.getPosition().x-1, bodFire01.getPosition().y-1.3f, 2, 1.6f);
					//firerotaO
					fireRotation.setOrigin(fireRotation.getWidth()/2,fireRotation.getHeight()/2);
					fireRotation.setSize(3, 1);
					fireRotation.setRotation(bodFire.getAngle() * MathUtils.radiansToDegrees);
					fireRotation.setPosition(bodFire.getPosition().x-fireRotation.getWidth()/2
							, bodFire.getPosition().y-fireRotation.getHeight()/2);
					fireRotation.setOrigin(fireRotation.getWidth()/2, fireRotation.getHeight()/2);
					fireRotation.draw(batch);
					
					fireRotation.setRotation(bodFire2.getAngle() * MathUtils.radiansToDegrees);
					fireRotation.setPosition(bodFire2.getPosition().x-fireRotation.getWidth()/2
							, bodFire2.getPosition().y-fireRotation.getHeight()/2);
					fireRotation.draw(batch);
				}
				if(body.getUserData() !=null && body.getUserData() instanceof Animation){
					spr_x = bod2.getPosition().x;
					spr_y = bod2.getPosition().y;
					
					switch(state){
					case RIGHT:
						batch.draw(An_walkingMan.getKeyFrame(animTime), spr_x-.75f, spr_y-1.4f, .75f*2, 1.4f*2);
						break;
					case LEFT:
						batch.draw(LeftMan.getKeyFrame(animTime), spr_x-.75f, spr_y-1.4f, .75f*2, 1.4f*2);
						break;
					case UP:
						batch.draw(UpMan.getKeyFrame(animTime), spr_x-.75f, spr_y-1.4f, .75f*2, 1.4f*2);
						break;
					case DOWN:
						batch.draw(DownMan.getKeyFrame(animTime), spr_x-.75f, spr_y-1.4f, .75f*2, 1.4f*2);
						break;
					case STOPRIGHT:
						batch.draw(sprRight, spr_x-.75f, spr_y-1.4f, .75f*2, 1.4f*2);
						break;
					case STOPLEFT:
						batch.draw(sprLeft, spr_x-.75f, spr_y-1.4f, .75f*2, 1.4f*2);
						break;
					case STOPUP:
						batch.draw(sprUp, spr_x-.75f, spr_y-1.4f, .75f*2, 1.4f*2);
						break;
					case STOPDOWN:
						batch.draw(sprDown, spr_x-.75f, spr_y-1.4f, .75f*2, 1.4f*2);
						break;
					default:
						break;
					}
				}
			}
			//diams
			
			if(!diam1Collected)
				batch.draw(jews.getKeyFrame(animTime), -20+1, -.5f*15, .75f, 1.25f);
			if(!diam2Collected)
				batch.draw(jews.getKeyFrame(animTime+3), -20+1, -.5f*23, .75f, 1.25f);
			if(!corncolected)
				batch.draw(corn, 17, -4, 2, 1);
			
			//fireballs
			if(fireOn){
				//
				fireTrap1.setTransform(fireballLX+.5f, fireballLY+.5f, 0);
				fireTrap2.setTransform(fireballLX2+.5f, fireballLY2+.5f, 0);
				fireTrap3.setTransform(fireballX+.5f, fireballY+.5f, 0);
				fireTrap4.setTransform(fireballX2+.5f, fireballY2+.5f, 0);
				//
				batch.draw(fireball.getKeyFrame(animTime), fireballX, fireballY, 1, 1);
				batch.draw(fireballLeft.getKeyFrame(animTime),fireballLX, fireballLY, 1, 1);
				batch.draw(fireball.getKeyFrame(animTime), fireballX2, fireballY2, 1, 1);
				batch.draw(fireballLeft.getKeyFrame(animTime),fireballLX2, fireballLY2, 1, 1);
				if(fireballX<-19 || fireballX2<-19 && fireballX2>-21)
					fireballSnd.play();
				if(fireballX>18)
					fireballX=-20;
				if(fireballLX<-18)
					fireballLX=20;
				if(fireballX2>18)
					fireballX2=-20;
				if(fireballLX2<-18)
					fireballLX2=20;
				fireballX+=0.2f;
				fireballLX-=0.2f;
				fireballX2+=0.2f;
				fireballLX2-=0.2f;
			}
			
			//Lava
			for(int i=0;i<5;i++){
				batch.draw(Lava.getKeyFrame(animTime), -Gdx.graphics.getWidth()/40+15,
						-11.5f-i,1,1);
				batch.draw(Lava.getKeyFrame(animTime), -Gdx.graphics.getWidth()/40+25,
						-11.5f-i,1,1);
				batch.draw(Lava.getKeyFrame(animTime), -Gdx.graphics.getWidth()/40+35,
						-11.5f-i,1,1);
			}
			
			//DRAWING ALL THE WALLS
			
			//1
			for(int i=0;i<6;i++)
				batch.draw(bloq.blockIMG,-Gdx.graphics.getWidth()/40+i,
						.5f-(bloq.getBlkY()/Gdx.graphics.getHeight())/40, 1, 1);	
			//2
			for(int i=0;i<11;i++)
				batch.draw(bloq.blockIMG,-Gdx.graphics.getWidth()/40+i,
						-4, 1, 1);
			//3
			for(int i=0;i<3;i++)
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+10,1-4+i
						, 1, 1);
			//3 BIS
			for(int i=0;i<5;i++)
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+6,
						.5f+i, 1, 1);
			//4
			for(int i=0;i<20;i++){
				if (i==10) i+=3;
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+10+i,
			  			0, 1, 1);}
			//5
			for(int i=0;i<10;i++){
				if(switcher1 && switcher3 && i==5)
					i+=5;
				
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+30,
						-4.5f+i, 1, 1);
			}
			//6
			for(int i=0;i<10;i++){
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+30+i,
						5.5f, 1, 1);
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+30+i,
						-5.5f, 1, 1);
				}
			//7
			for(int i=0;i<30;i++){
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+5+i,-8.5f, 1, 1);
				if (i==16) i+=8;
				if(i<5)
					batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+5+i,
							-8.5f-3, 1, 1);
				if(23<i && i<28)
					batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+5+i,
							-8.5f-3, 1, 1);
			}
			//8
			for(int i=0;i<3;i++){
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+5,
						-8.5f-i, 1, 1);
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+15,
						-8.5f-i, 1, 1);
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+25,
						-8.5f-i, 1, 1);
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+29,
						-8.5f-i, 1, 1);
				batch.draw(bloq.blockIMG, -Gdx.graphics.getWidth()/40+35,
						-8.5f-i, 1, 1);
			}
			if(!switcher1)
				batch.draw(sprSwitchOff , -.5f*23, -.5f*20-.25f, 1.5f, 1.5f);
			else
				batch.draw(sprSwitchOn  , -.5f*23, -.5f*20-.25f, 1.5f, 1.5f);
			
			if(!switcher3){
				batch.draw(sprSwitchOff , .5f*20 , -.5f*20-.25f, 1.5f, 1.5f);
			}else
				batch.draw(sprSwitchOn  , .5f*20 , -.5f*20-.25f, 1.5f, 1.5f);
				
			//draw des top of lights
			batch.draw(upOfWalls,-20,-15,40,30);
			
			//tr9i3a pour the light
			if(!corncolected || animetime3>3)
				batch.draw(sprLight, -Gdx.graphics.getWidth()/40 -20 +spr_x 
					, -Gdx.graphics.getHeight()/40 -15 +spr_y,40*2,30*2);
			
			//if switches are on
			if(animetime3>1 && animetime3<3 && corncolected)
				demonScream.play();
			if(animetime3<5 && corncolected){
				Bckmusic.setVolume(.05f);
				batch.draw(demon.getKeyFrame(animetime3), -20, -15, 40, 30);
			}else
				Bckmusic.setVolume(.3f);
			if(scroll1collected)
				batch.draw(scroll1.getKeyFrame(animetime4), -10, -15, 20, 30);
			batch.draw(replay, 18, 13, 1, 1);
		batch.end();
		//camera.position.set(bod2.getPosition().x, bod2.getPosition().y, 0);
		//camera.zoom = .8f;
		camera.update();
		world.step(timeStep, velocityIterations, positionIterations);
		//tweenManager.update(delta);  it gliching a little for now
		debugRenderer.render(world, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		Bckmusic.pause();
		fireballSnd.pause();
	}

	@Override
	public void resume() {
		Bckmusic.play();
		fireballSnd.play();
	}

	@Override
	public void hide() {
		Bckmusic.stop();
		fireballSnd.stop();
	}

	@Override
	public void dispose() {

		batch    .dispose();
		ball	 .dispose();
		bck      .dispose();
		bckLight .dispose();
		world    .dispose();
		atlasWR  .dispose();
		atlasWD  .dispose();
		atlasWL  .dispose();
		atlasWU  .dispose();
		atlasJews.dispose();
		spr     .getTexture().dispose();
		sprLight.getTexture().dispose();
		sprDown .getTexture().dispose();
		sprLeft .getTexture().dispose();
		sprLight.getTexture().dispose();
		sprUp   .getTexture().dispose();
		spikeSpr.getTexture().dispose();
		sprSwitchOn .getTexture().dispose();
		sprSwitchOff.getTexture().dispose();
		Bckmusic   .dispose();
		fireballSnd.dispose();
	}

}
