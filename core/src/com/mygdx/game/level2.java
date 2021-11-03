package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class level2 implements Screen{
	
	private World world;
	private Box2DDebugRenderer debugRendrer;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private Body body, bodymain,bodyEnnemy0, bodyEnnemy, bodyEnnemy2, bodyEnnemy3, bodyEnnemy4, bodDiam;
	private Vector2 mvt = new Vector2();
	private int speed = 10;
	private float animeTime ;
	private boolean grounded = false, diam1Collected=false;
	private Sprite imgBck, sprRight, sprLeft, blockq, blockq11, blockq21, sprJump;
	
	private TextureAtlas atlasMainL, atlasMainR, atlasJews, atlaslavaMon;
	private Animation walkingLeft, walkingRight , jews, lavaMon;
	private Array<AtlasRegion> animationFrames;
	private enum  CurrentState{UP, DOWN, RIGHT, LEFT,
				  STOPUP, STOPDOWN, STOPRIGHT, STOPLEFT, JUMPING;};
    private CurrentState  state=CurrentState.STOPRIGHT;
	
	private final float timestep = 1/60f;
	private final int velocityIterations = 8, positionIterations = 3;

	@SuppressWarnings("unused")
	@Override
	public void show() {
		Gdx.graphics.setWindowedMode(800, 600);
		world = new World(new Vector2(0,-22.81f), false);
		debugRendrer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth()/20, Gdx.graphics.getHeight()/20);
		//camera.zoom += .8f*2;
		batch = new SpriteBatch();
		//right walking
		atlasMainR = new TextureAtlas(Gdx.files.internal("mainWalk/mainWalkRight/walkRight/right.atlas"));
		animationFrames = atlasMainR.getRegions();
		walkingRight = new Animation(.1f, animationFrames);
		walkingRight.setPlayMode(PlayMode.LOOP);
		//left walking
		atlasMainL = new TextureAtlas(Gdx.files.internal("mainWalk/mainWalkLeft/walkLeft/Left.atlas"));
		animationFrames = atlasMainL.getRegions();
		walkingLeft = new Animation(.1f, animationFrames);
		walkingLeft.setPlayMode(PlayMode.LOOP);
		//lavaMonster
		atlaslavaMon = new TextureAtlas(Gdx.files.internal("lavaMon/lavaMon.atlas"));
		animationFrames = atlaslavaMon.getRegions();
		lavaMon = new Animation(.2f, animationFrames);
		lavaMon.setPlayMode(PlayMode.LOOP);
		
		BodyDef bodDef = new BodyDef();
		FixtureDef fixtrItems = new FixtureDef();
		
		world.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
			
			@Override
			public void endContact(Contact contact) {
				final Fixture faa = contact.getFixtureA();
				final Fixture fbb = contact.getFixtureB();
				
				if(fbb.getUserData() instanceof groundBox){
					grounded=false;
				}
			}
			
			@Override
			public void beginContact(Contact contact) {
				final Fixture fa = contact.getFixtureA();
				final Fixture fb = contact.getFixtureB();
				
				if(fb.getUserData() instanceof groundBox){
					grounded=true;
				}if(fb.getUserData() instanceof walkingEnemy){
					((Game)Gdx.app.getApplicationListener()).setScreen(new GameOver());
				}if(fb.getUserData() instanceof Diamon && fb.getBody().getUserData().equals(1)){
					diam1Collected=true;
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							world.destroyBody(fb.getBody());
						}
					});
				}
			}
		});
		
		imgBck = new Sprite(new Texture(Gdx.files.internal("level2back.jpg")));
		
		Gdx.input.setInputProcessor(new InputController(){
			@Override
			public boolean keyDown(int keycode) {
				switch(keycode){
				case Keys.LEFT:
					mvt.x = -speed;
					state=CurrentState.LEFT;
					break;
				case Keys.RIGHT:
					mvt.x = speed;
					state=CurrentState.RIGHT;
					break;
				case Keys.UP:
					//camera.zoom -= .8f ;
					break;
				case Keys.DOWN:
					//camera.zoom += .8f ;
					break;
				case Keys.SPACE:
					if(grounded){
						bodymain.applyLinearImpulse(0, 7000, 0, 0, true);
						state=CurrentState.JUMPING;
					}
					break;
				}
				return true;
			}
			
			@Override
			public boolean keyUp(int keycode) {
				switch(keycode){
				case Keys.LEFT:
					mvt.x = 0;
					state=CurrentState.STOPLEFT;
					break;
				case Keys.RIGHT:
					mvt.x = 0;
					state=CurrentState.STOPRIGHT;
					break;
				}
				return true;
			}
		});
		
		
		
		BodyDef ball = new BodyDef();
		FixtureDef fixtr = new FixtureDef();
		
		PolygonShape boxShape = new PolygonShape();
		boxShape    .setAsBox(1, 2);
		ball.type    = BodyType.DynamicBody;
		ball.position.set(-20, 8);
		fixtr.shape       =boxShape;
		fixtr.friction    = .5f;
		fixtr.restitution = .1f;
		fixtr.density     = 60;
		bodymain  = world.createBody(ball);
		bodymain  .createFixture(fixtr);
		boxShape.dispose();
		
		//le stop right
		sprRight = new Sprite(new Texture(Gdx.files.internal("mainWalk/mainStopRight/6.png")));
		//le stopleft
		sprLeft = new Sprite(new Texture(Gdx.files.internal("mainWalk/mainStopLeft/6.png")));
		//le jumper
		sprJump = new Sprite(new Texture(Gdx.files.internal("mainWalk/mainStopRight/jumpR.png")));
		//blocks
		blockq = new Sprite(new Texture(Gdx.files.internal("lvl2Blck.png")));
		//medium bloq
		blockq21 = new Sprite(new Texture(Gdx.files.internal("lvl2Blck21.png")));
		//lil block
		blockq11 = new Sprite(new Texture(Gdx.files.internal("lvl2Blck11.png")));
		//init de l'animaO dimonds
		atlasJews	    = new TextureAtlas(Gdx.files.internal("diamonds/diams/diam.atlas"));
		animationFrames = atlasJews.getRegions();
		jews   			= new Animation(0.2f, animationFrames);
		jews			.setPlayMode(PlayMode.LOOP);
		
		groundBox ground0 = new groundBox(70, 1, 30, -1.5f, ball, fixtr, world, body, 0);
		
		groundBox ground1 = new groundBox(3, 1, -20, 4.6f, ball, fixtr, world, body, 1);
		groundBox ground2 = new groundBox(3, 1, -2, 3, ball, fixtr, world, body, 1);
		groundBox ground3 = new groundBox(3, 1, 10, 6, ball, fixtr, world, body, 1);
		groundBox ground4 = new groundBox(3, 1, 27, 7, ball, fixtr, world, body, 1);
		groundBox ground5 = new groundBox(3, 1, 40, 5, ball, fixtr, world, body, 1);
		groundBox ground6 = new groundBox(3, 1, 52, 5, ball, fixtr, world, body, 1);
		groundBox ground7 = new groundBox(3, 1, 70, 5, ball, fixtr, world, body, 1);
		groundBox ground8 = new groundBox(3, 1, 85, 5, ball, fixtr, world, body, 1);
		groundBox ground9 = new groundBox(3, 1, 97, 9, ball, fixtr, world, body, 1);
		groundBox ground9bis = new groundBox(1, 1, 99 , 12, ball, fixtr, world, body, 1);
		groundBox ground10 = new groundBox(3, 1, 88 , 16, ball, fixtr, world, body, 1);
		groundBox ground11 = new groundBox(2, 1, 80, 20, ball, fixtr, world, body, 1);
		groundBox ground12 = new groundBox(2, 1, 70, 24, ball, fixtr, world, body, 1);
		groundBox ground13 = new groundBox(2, 1, 64, 27, ball, fixtr, world, body, 1);
		groundBox ground14 = new groundBox(1, 1, 70, 31.5f, ball, fixtr, world, body, 1);
		groundBox ground15 = new groundBox(2, 1, 64, 27, ball, fixtr, world, body, 1);
		groundBox ground16 = new groundBox(1, 1, 60, 33, ball, fixtr, world, body, 1);
		groundBox ground17 = new groundBox(1, 1, 48, 33, ball, fixtr, world, body, 1);
		Diamon boxDiam  = new Diamon(.5f, 1, 35, 30, bodDef, fixtrItems, world, bodDiam, 1);
		
		//limit borders
		groundBox borderr = new groundBox(.1f, 30, -40, 30, ball, fixtr, world, body, 1);
		groundBox borderL = new groundBox(.1f, 30, 100, 30, ball, fixtr, world, body, 1);
		
		walkingEnemy en0 = new walkingEnemy(1, 1, -10, .65f, ball, fixtr, world, bodyEnnemy0, 99);
		bodyEnnemy0 = world.createBody(ball);
		bodyEnnemy0.setUserData(99);
		bodyEnnemy0.createFixture(fixtr).setUserData(en0);
		bodyEnnemy0.setLinearVelocity(1, 0);
		bodyEnnemy0.setFixedRotation(true);
		en0.dispose();
		
		walkingEnemy en1 = new walkingEnemy(1, 1, 20, .65f, ball, fixtr, world, bodyEnnemy, 99);
		bodyEnnemy = world.createBody(ball);
		bodyEnnemy.setUserData(99);
		bodyEnnemy.createFixture(fixtr).setUserData(en1);
		bodyEnnemy.setLinearVelocity(1, 0);
		bodyEnnemy.setFixedRotation(true);
		en1.dispose();
		
		walkingEnemy en2 = new walkingEnemy(1, 1, 40, .65f, ball, fixtr, world, bodyEnnemy2, 99);
		bodyEnnemy2 = world.createBody(ball);
		bodyEnnemy2.setUserData(99);
		bodyEnnemy2.createFixture(fixtr).setUserData(en1);
		bodyEnnemy2.setLinearVelocity(1, 0);
		bodyEnnemy2.setFixedRotation(true);
		en2.dispose();
		
		walkingEnemy en3 = new walkingEnemy(1, 1, 65, .65f, ball, fixtr, world, bodyEnnemy3, 99);
		bodyEnnemy3 = world.createBody(ball);
		bodyEnnemy3.setUserData(99);
		bodyEnnemy3.createFixture(fixtr).setUserData(en1);
		bodyEnnemy3.setLinearVelocity(1, 0);
		bodyEnnemy3.setFixedRotation(true);
		en3.dispose();
		
		walkingEnemy en4 = new walkingEnemy(1, 1, 80, .65f, ball, fixtr, world, bodyEnnemy4, 99);
		bodyEnnemy4 = world.createBody(ball);
		bodyEnnemy4.setUserData(99);
		bodyEnnemy4.createFixture(fixtr).setUserData(en1);
		bodyEnnemy4.setLinearVelocity(1, 0);
		bodyEnnemy4.setFixedRotation(true);
		en4.dispose();
		debugRendrer.setDrawBodies(false);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		animeTime+=Gdx.graphics.getDeltaTime();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
			batch.draw(imgBck, -40, -9.5f, 140, 60);
			//batch.draw(walkingRight.getKeyFrame(animeTime), bodymain.getPosition().x-1, bodymain.getPosition().y-2, 2, 4);
			switch(state){
				case RIGHT:
					batch.draw(walkingRight.getKeyFrame(animeTime), bodymain.getPosition().x-1, bodymain.getPosition().y-2, 2, 4);
					break;
				case LEFT:
					batch.draw(walkingLeft.getKeyFrame(animeTime), bodymain.getPosition().x-1, bodymain.getPosition().y-2, 2, 4);
					break;
				case STOPRIGHT:
					batch.draw(sprRight, bodymain.getPosition().x-1, bodymain.getPosition().y-2, 2, 4);
					break;
				case STOPLEFT:
					batch.draw(sprLeft, bodymain.getPosition().x-1, bodymain.getPosition().y-2, 2, 4);
					break;
				case STOPDOWN:
					batch.draw(sprRight, bodymain.getPosition().x-1, bodymain.getPosition().y-2, 2, 4);
					break;
				case JUMPING:
					if(bodymain.getLinearVelocity().x>0)
						batch.draw(sprJump, bodymain.getPosition().x-1, bodymain.getPosition().y-2, 2, 4);
					else if(bodymain.getLinearVelocity().x<0)
						batch.draw(sprJump, bodymain.getPosition().x-1+2, bodymain.getPosition().y-2, -2, 4);
					else
						batch.draw(sprRight, bodymain.getPosition().x-1, bodymain.getPosition().y-2, 2, 4);
						
					break;
				default:
					break;
			}
			
			batch.draw(blockq, -20-3, 4.6f-1, 6, 2); //1
			batch.draw(blockq, -2-3, 3-1, 6, 2); //2
			batch.draw(blockq, 10-3, 6-1, 6, 2); //3
			batch.draw(blockq, 27-3, 7-1, 6, 2); //4
			batch.draw(blockq, 40-3, 5-1, 6, 2); //5
			batch.draw(blockq, 52-3, 5-1, 6, 2); //6
			batch.draw(blockq, 70-3, 5-1, 6, 2); //7
			batch.draw(blockq, 85-3, 5-1, 6, 2); //8
			batch.draw(blockq, 97-3, 9-1, 6, 2); //9
			batch.draw(blockq11, 99-1, 12-1, 2, 2); //10
			batch.draw(blockq, 88-3, 16-1, 6, 2); //11
			batch.draw(blockq21, 80-2, 20-1, 4, 2); //12
			batch.draw(blockq21, 70-2, 24-1, 4, 2); //13
			batch.draw(blockq21, 64-2, 27-1, 4, 2); //14
			batch.draw(blockq11, 70-1, 31.5f-1, 2, 2); //15
			batch.draw(blockq21, 64-2, 27-1, 4, 2); //16 not encore placé
			batch.draw(blockq11, 60-1, 33-1, 2, 2); //17
			batch.draw(blockq11, 48-1, 33-1, 2, 2); //18
			
			//ennemies
			if(bodyEnnemy.getLinearVelocity().x>0)
				batch.draw(lavaMon.getKeyFrame(animeTime), bodyEnnemy.getPosition().x-1, bodyEnnemy.getPosition().y-1, 2,2);
			if(bodyEnnemy.getLinearVelocity().x<=0)
				batch.draw(lavaMon.getKeyFrame(animeTime), bodyEnnemy.getPosition().x+1, bodyEnnemy.getPosition().y-1, -2,2);
			
			if(bodyEnnemy0.getLinearVelocity().x>0)
				batch.draw(lavaMon.getKeyFrame(animeTime), bodyEnnemy0.getPosition().x-1, bodyEnnemy0.getPosition().y-1, 2,2);
			if(bodyEnnemy0.getLinearVelocity().x<=0)
				batch.draw(lavaMon.getKeyFrame(animeTime), bodyEnnemy0.getPosition().x+1, bodyEnnemy0.getPosition().y-1, -2,2);
			
			if(bodyEnnemy2.getLinearVelocity().x>0)
				batch.draw(lavaMon.getKeyFrame(animeTime), bodyEnnemy2.getPosition().x-1, bodyEnnemy2.getPosition().y-1, 2,2);
			if(bodyEnnemy2.getLinearVelocity().x<=0)
				batch.draw(lavaMon.getKeyFrame(animeTime), bodyEnnemy2.getPosition().x+1, bodyEnnemy2.getPosition().y-1, -2,2);
			
			if(bodyEnnemy3.getLinearVelocity().x>0)
				batch.draw(lavaMon.getKeyFrame(animeTime), bodyEnnemy3.getPosition().x-1, bodyEnnemy3.getPosition().y-1, 2,2);
			if(bodyEnnemy3.getLinearVelocity().x<=0)
				batch.draw(lavaMon.getKeyFrame(animeTime), bodyEnnemy3.getPosition().x+1, bodyEnnemy3.getPosition().y-1, -2,2);
			
			if(bodyEnnemy4.getLinearVelocity().x>0)
				batch.draw(lavaMon.getKeyFrame(animeTime), bodyEnnemy4.getPosition().x-1, bodyEnnemy4.getPosition().y-1, 2,2);
			if(bodyEnnemy4.getLinearVelocity().x<=0)
				batch.draw(lavaMon.getKeyFrame(animeTime), bodyEnnemy4.getPosition().x+1, bodyEnnemy4.getPosition().y-1, -2,2);
			
			//diamon
			if(!diam1Collected)
				batch.draw(jews.getKeyFrame(animeTime), 35-.5f, 30-1, 1,2);
			else{
				if(bodymain.getPosition().x > 97 && grounded)
					((Game)Gdx.app.getApplicationListener()).setScreen(new MazePhh());
			}
		batch.end();
		//System.out.println(bodymain.getPosition().x+" | "+bodymain.getPosition().y);
		bodymain.setLinearVelocity(mvt.x, bodymain.getLinearVelocity().y);;
		bodymain.setFixedRotation(true);
		
		if(bodyEnnemy0.getPosition().x<-15)
			bodyEnnemy0.setLinearVelocity(1, 0);
		if(bodyEnnemy0.getPosition().x>5)
			bodyEnnemy0.setLinearVelocity(-1, 0);
		
		if(bodyEnnemy.getPosition().x<15)
			bodyEnnemy.setLinearVelocity(1, 0);
		if(bodyEnnemy.getPosition().x>25)
			bodyEnnemy.setLinearVelocity(-1, 0);
		
		if(bodyEnnemy2.getPosition().x<35)
			bodyEnnemy2.setLinearVelocity(1, 0);
		if(bodyEnnemy2.getPosition().x>55)
			bodyEnnemy2.setLinearVelocity(-1, 0);
		
		if(bodyEnnemy3.getPosition().x<60)
			bodyEnnemy3.setLinearVelocity(1, 0);
		if(bodyEnnemy3.getPosition().x>70)
			bodyEnnemy3.setLinearVelocity(-1, 0);
		
		if(bodyEnnemy4.getPosition().x<75)
			bodyEnnemy4.setLinearVelocity(1, 0);
		if(bodyEnnemy4.getPosition().x>92)
			bodyEnnemy4.setLinearVelocity(-1, 0);
		
		world.step(timestep, velocityIterations, positionIterations);
		camera.position.set(bodymain.getPosition().x-.5f, bodymain.getPosition().y-.5f, 0);
		if(bodymain.getPosition().x<-19)
			camera.position.set(-19, bodymain.getPosition().y-.5f, 0);
		if(bodymain.getPosition().x>80)
			camera.position.set(80, bodymain.getPosition().y, 0);
		if(bodymain.getPosition().y>38)
			camera.position.set(bodymain.getPosition().x, 38, 0);
		camera.update();
		debugRendrer.render(world, camera.combined);
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
		imgBck.getTexture().dispose();
		batch.dispose();
		atlasMainL.dispose();
		atlasMainR.dispose();
	}

}
