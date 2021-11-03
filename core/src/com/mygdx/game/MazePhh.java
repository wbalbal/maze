package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class MazePhh implements Screen, InputProcessor {
    	private SpriteBatch batch;
    	
    	private OrthographicCamera camera;
    	private TextureAtlas img, lava, fireballs,opndr,burn,teleglow, imgR, imgL, imgU, imgD;
		private Animation anm, lavanm, frblanm,opndranm ,burnanm, teleglowanm, anmR, anmL, anmU, anmD;
    	private float time = 0f,stateTime = 0, doorTime = 0,statuetime = 0;
    	private Sprite ap,  over, light,overtp, tptile, tutosc, traph, fire2,
    					door, press, key, tower,tele,statue, horn, stopedR, stopedL, 
    					stopedU, stopedD, tele3, tele2, tele1, tele0, statue2;
    	private float posX , posY, tailleX, tailleY, firepos = -535, posX0, posY0;
    	private World world;
    	private enum 			   CurrentState{UP, DOWN, RIGHT, LEFT,
            STOPUP, STOPDOWN, STOPRIGHT, STOPLEFT;};
        private CurrentState       state=CurrentState.STOPRIGHT;
    	private Box2DDebugRenderer dbg;
    	FixtureDef fixtureDeff = new FixtureDef(),fixtureDeff1 = new FixtureDef(),fixtureDeff2 = new FixtureDef();
    	private Body body;
    	private Body body2, bodyf, hb1,hb2;
    	Matrix4 debugMatrix;
    	BitmapFont font;
    	final float PIXELS_TO_METERS = 100f;
		float omega = 0;
    	private BodyDef bodyDef = new BodyDef(), bdff = new BodyDef(),bdff1 = new BodyDef(),bdff2 = new BodyDef();
    	int numbtp=0;
    	boolean move = true, pause= false, tuto= false, dranmbol = false,animated = false, dead = false, keynowned = true,ctcst = false;


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
	public boolean keyDown(int keycode) {
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
			case Keys.UP:
				state=CurrentState.STOPUP;
				break;
			case Keys.DOWN:
				state=CurrentState.STOPDOWN;
				break;
			case Keys.LEFT:
				state=CurrentState.STOPLEFT;
				break;
			case Keys.RIGHT:
				state=CurrentState.STOPRIGHT;
				break;
			default:
				break;
		}
		return true;
	}
	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		System.out.println(screenX);
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	@SuppressWarnings("unused")
	@Override
	public void show() {
    	Gdx.graphics.setWindowedMode(1280, 720);
    	camera = new OrthographicCamera(1280, 720);
    	world = new World(new Vector2(0, 0),true);
    	posX = -10*Gdx.graphics.getWidth()/22 + 5 ; posY = Gdx.graphics.getHeight()/2 - 100 ;
        tailleX = Gdx.graphics.getWidth()/26 ; tailleY = tailleX;
        posX0= posX;
    	posY0 = posY;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX/100 + 0.25f, posY/100 + 0.2f);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(tailleX/200 , tailleY/175);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();
    	 
        Spell firebal = new Spell(bodyf, bdff,  fixtureDeff,firepos/PIXELS_TO_METERS +0.2f, -0.65f,0.15f,0.15f, 1);
        bodyf = world.createBody(bdff);
		bodyf.setUserData(firebal.id);
		bodyf.createFixture(fixtureDeff).setUserData(firebal);
		firebal.dispose();
		Trapfr flm1 = new Trapfr(hb1, bdff1,  fixtureDeff1, 3.2f,2.2f, 0.1f, 0.4f);
        hb1 = world.createBody(bdff1);
		hb1.createFixture(fixtureDeff1).setUserData(flm1);
		flm1.dispose();
		Trapfr flm2 = new Trapfr(hb2, bdff2,  fixtureDeff2, 2.3f,2.2f, 0.1f, 0.4f);
        hb2 = world.createBody(bdff2);
		hb2.createFixture(fixtureDeff1).setUserData(flm2);
		flm1.dispose();
		
       Box b1 = new Box(body2, bodyDef, world, fixtureDef, -4.95f, 3.5f, 0.25f, 2.2f);
        Box b2 = new Box(body2, bodyDef, world, fixtureDef, -6.2f,3.7f, 0.35f, 4.85f);
        Box b3 = new Box(body2, bodyDef, world, fixtureDef, -6.2f,0.6f, 2.80f, 0.01f);
        Box b4 = new Box(body2, bodyDef, world, fixtureDef, -3.64f,1.8f,0.34f, 1.3f); 
        Box b5 = new Box(body2, bodyDef, world, fixtureDef, -2.45f,2.83f,0.305f, 1.4f);
        Box b6 = new Box(body2, bodyDef, world, fixtureDef, -4.85f,0.09f,0.35f, 0.4f);
        Box b7 = new Box(body2, bodyDef, world, fixtureDef, 0f,3.8f,13f, 0.01f);
        Box b8 = new Box(body2, bodyDef, world, fixtureDef, 0f,-3.8f,13f, 0.01f);
        Box b9 = new Box(body2, bodyDef, world, fixtureDef, 6.5f,0f,0.01f, 13f);
        Box b10 = new Box(body2, bodyDef, world, fixtureDef, -6.5f,0f,0.01f, 13f);
        Box b11 = new Box(body2, bodyDef, world, fixtureDef, -1.75f,0.53f, 1f, 0.01f);
        Box b12 = new Box(body2, bodyDef, world, fixtureDef, -1.75f,1.42f, 1f, 0.01f);
        Box b125 = new Box(body2, bodyDef, world, fixtureDef, -1.75f,1.85f, 1f, 0.01f);
        Box b13 = new Box(body2, bodyDef, world, fixtureDef, -3.4f,-0.4f, 1.75f, 0.01f); // l millieu
        Box b14 = new Box(body2, bodyDef, world, fixtureDef, -4.48f,-1.2f, 2.74f, 0.01f);
        Box b15 = new Box(body2, bodyDef, world, fixtureDef, -3f,-1.9f, 2.5f, 0.01f);
        Box b16 = new Box(body2, bodyDef, world, fixtureDef, 0f,-3f,12f, 0.01f);
        Box b17 = new Box(body2, bodyDef, world, fixtureDef, 3.92f,-2f, 2.6f, 0.01f);
        Box b18 = new Box(body2, bodyDef, world, fixtureDef, 1.2f,-1.2f, 0.5f, 0.01f);
        Box b19 = new Box(body2, bodyDef, world, fixtureDef, 5f,-1f, 1.5f, 0.01f);
        Box b20 = new Box(body2, bodyDef, world, fixtureDef, 1.33f,0.25f, 2.3f, 0.01f);
        Box b21 = new Box(body2, bodyDef, world, fixtureDef, 1.9f,1.2f, 2.55f, 0.01f);
        Box b22 = new Box(body2, bodyDef, world, fixtureDef, 0.2f,2.1f, 1.2f, 0.01f);
        Box b23 = new Box(body2, bodyDef, world, fixtureDef, -0.2f,2.85f, 1.38f, 0.01f);
        Box b24 = new Box(body2, bodyDef, world, fixtureDef, 3.3f,2.55f, 1.85f, 0.01f);
        Box b25 = new Box(body2, bodyDef, world, fixtureDef, -0.75f,0.1f, 0.3f, 1.95f);
        Box b26 = new Box(body2, bodyDef, world, fixtureDef, 0.45f,-0.1f, 0.3f, 0.35f);
        Box b27 = new Box(body2, bodyDef, world, fixtureDef, 0.45f,-2.05f, 0.3f, 0.85f);
        Box b28 = new Box(body2, bodyDef, world, fixtureDef, 1.1f,2.5f, 0.3f, 0.4f);
        Box b29 = new Box(body2, bodyDef, world, fixtureDef, 5.3f,1.3f, 0.25f, 1.25f);
        Box b30 = new Box(body2, bodyDef, world, fixtureDef, 3.35f,-1.5f, 0.25f, 0.5f);
        Box b31 = new Box(body2, bodyDef, world, fixtureDef, -5.6f,3.1f, 0.2f, 0.01f);
        
        Iandm lava1 = new Iandm(body2, bodyDef, world, fixtureDef, 3f, -1.2f, 0.15f, 0.5f, 1);
        Iandm lava2 = new Iandm(body2, bodyDef, world, fixtureDef, 4.85f, -0.7f, 2f, 0.05f, 2);
        Iandm sth = new Iandm(body2, bodyDef, world, fixtureDef, -5.5f,0.27f, 0.2f, 0.2f, 3);
        Iandm dr = new Iandm(body2, bodyDef, world, fixtureDef, -1.9f,2.8f, 0.3f, 0.1f, 4);
        Iandm ky =  new Iandm(body2, bodyDef, world, fixtureDef, 1.3f,1.75f, 0.2f, 0.2f, 5);
        Iandm finish =  new Iandm(body2, bodyDef, world, fixtureDef, -6f,-3.4f, 0.3f, 0.2f, 6);
        batch = new SpriteBatch();
        img = new TextureAtlas(Gdx.files.internal("hatim/bin/mainWalkRight/walkRight/right.atlas"));
        lava = new TextureAtlas(Gdx.files.internal("hatim/bin/Lava/lava/lava.atlas"));
        fireballs = new TextureAtlas(Gdx.files.internal("hatim/bin/AOfireball/AOKaton.atlas"));
        burn = new TextureAtlas(Gdx.files.internal("hatim/bin/burn/burn.pack"));
        press = new Sprite(new Texture(Gdx.files.internal("hatim/bin//tutotel/press.png")));
        traph = new Sprite(new Texture(Gdx.files.internal("hatim/bin/AOfireball/head.png")));
        over = new Sprite(new Texture(Gdx.files.internal("hatim/bin/over.png")));
        ap = new Sprite(new Texture(Gdx.files.internal("hatim/bin/lvl1.3.jpg")));
        light = new Sprite(new Texture(Gdx.files.internal("hatim/bin/light.png")));
        anm = new Animation(1f/5f, img.getRegions());
        lavanm = new Animation(1f/5f, lava.getRegions());
        frblanm = new Animation(1f/4f, fireballs.getRegions());
        burnanm = new Animation(1f/4f, burn.getRegions());
        opndr =   new TextureAtlas(Gdx.files.internal("hatim/bin/icedoor/door.pack"));
        opndranm = new Animation(1f/1.5f, opndr.getRegions());
        key = new Sprite(new Texture(Gdx.files.internal("hatim/bin/key.png")));
        tower = new Sprite(new Texture(Gdx.files.internal("hatim/bin/recharge.png")));
        tptile = new Sprite(new Texture(Gdx.files.internal("hatim/bin/tptile.png")));
        tutosc = new Sprite(new Texture(Gdx.files.internal("hatim/bin/tutotel/6.png")));
        door = new Sprite(new Texture(Gdx.files.internal("hatim/bin/glacialdoor.png")));
        teleglow = new TextureAtlas(Gdx.files.internal("hatim/bin/tpeffect2/tele.pack"));
        teleglowanm = new Animation(1f/5f, teleglow.getRegions());
        statue = new Sprite(new Texture(Gdx.files.internal("hatim/bin/statueh/1.png")));
        horn = new Sprite(new Texture(Gdx.files.internal("hatim/bin/statueh/horn.png")));
        Gdx.input.setInputProcessor(this);
        
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
				
				if(fb.getUserData() instanceof Iandm){
					Gdx.app.postRunnable(new Runnable() {
						
						@Override
						public void run() {
							if(fb.getBody().getUserData().equals(1) || fb.getBody().getUserData().equals(2)){
							world.destroyBody(body);
							dead = true;
							}
							if(fb.getBody().getUserData().equals(3)){
								ctcst = true;
							}
						}
					});
				}
				if(fb.getUserData() instanceof Iandm && fb.getBody().getUserData().equals(6)){
					((Game)Gdx.app.getApplicationListener()).setScreen(new gameCleared());
				}
				if(fb.getUserData() instanceof Spell){
					Gdx.app.postRunnable(new Runnable() {
						
						@Override
						public void run() {
							world.destroyBody(body);
							dead = true;
						}
					});
				}
				if(fb.getUserData() instanceof Trapfr){
					Gdx.app.postRunnable(new Runnable() {
						
						@Override
						public void run() {
							world.destroyBody(body);
							dead = true;
						}
						
					});
					
				}
				if(fb.getUserData() instanceof Iandm){
					
							Gdx.app.postRunnable(new Runnable() {
						
							@Override
							public void run() {
								if(fb.getBody().getUserData().equals(4) && !keynowned){
									world.destroyBody(fb.getBody());
									dranmbol = true;
								}
								else if(fb.getBody().getUserData().equals(5)){
									world.destroyBody(fb.getBody());
									keynowned = false;
								}
							}
						});
					
				}
			}
		});
        
        //right
        stopedR = new Sprite(new Texture(Gdx.files.internal("hatim/bin/mainStopRight/6.png")));
    	imgR = new TextureAtlas(Gdx.files.internal("hatim/bin/mainWalkRight/walkRight/right.atlas"));;
    	anmR = new Animation(1f/5f, imgR.getRegions());
    	//up
    	stopedU = new Sprite(new Texture(Gdx.files.internal("hatim/bin/mainStopUp/0.png")));
    	imgU = new TextureAtlas(Gdx.files.internal("hatim/bin/mainWalkUp/walkUp/Up.atlas"));
    	anmU = new Animation(1f/10f, imgU.getRegions());
    	//down
    	stopedD = new Sprite(new Texture(Gdx.files.internal("hatim/bin/mainStopDown/0.png")));
    	imgD = new TextureAtlas(Gdx.files.internal("hatim/bin/mainWalkDown/walkDown/down.atlas"));
    	anmD = new Animation(1f/10f, imgD.getRegions());
    	//left
    	stopedL = new Sprite(new Texture(Gdx.files.internal("hatim/bin/mainStopLeft/6.png")));
    	imgL = new TextureAtlas(Gdx.files.internal("hatim/bin/mainWalkLeft/walkLeft/left.atlas"));
    	anmL = new Animation(1f/5f, imgL.getRegions());
        
    	statue2 = new Sprite(new Texture(Gdx.files.internal("hatim/bin/statueh/2.png")));
    	
    	tele3 = new Sprite(new Texture(Gdx.files.internal("hatim/bin/numtp/x3.png")));
    	tele2 = new Sprite(new Texture(Gdx.files.internal("hatim/bin/numtp/x2.png")));
    	tele1 = new Sprite(new Texture(Gdx.files.internal("hatim/bin/numtp/x1.png")));
    	tele0 = new Sprite(new Texture(Gdx.files.internal("hatim/bin/numtp/x0.png")));
        dbg = new Box2DDebugRenderer();
        dbg.setDrawBodies(false);
		
	}
	@Override
	public void render(float delta) {
    	camera.update();
        // Step the physics simulation forward at a rate of 60hz
        world.step(1f/60f, 6, 2);
        
    	time += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
       
        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,PIXELS_TO_METERS, 0);
        batch.begin();
        
        batch.draw(ap,-Gdx.graphics.getWidth()/2 ,-Gdx.graphics.getHeight()/2 , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        posX = (body.getPosition().x * PIXELS_TO_METERS) - 25;
        posY = (body.getPosition().y * PIXELS_TO_METERS)-stopedD.getHeight()/2;
        batch.draw(tptile, -640, -280, 40, 40);
        batch.draw(tptile, 460, 200, 40, 40);
        batch.draw(tptile, -35, 150, 40, 40);
        batch.draw(tptile, -35, 57, 40, 40);
        batch.draw(tptile, -210, 175, 40, 40);
        batch.draw(tptile, 370, -165, 40, 40);
        batch.draw(tptile, 600, -165, 40, 40);
        batch.draw(tptile, -580, -60, 40, 40);
        batch.draw(tptile, 600, -360, 40, 40);
        batch.draw(tower, -595, 275, 80f, 80f);
       
        if(keynowned){
        	batch.draw(key, 105, 150, 50f, 50f);
        }
       if(move){
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
        	
            batch.draw(anmR.getKeyFrame(time, true), posX,posY, tailleX, tailleY);
            body.setLinearVelocity(2f, 0f);
        
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
        	
            batch.draw(anmU.getKeyFrame(time, true), posX, posY, tailleX, tailleY);
            body.setLinearVelocity(0f, 2f);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
        	
            batch.draw(anmD.getKeyFrame(time, true), posX,posY, tailleX, tailleY);
            body.setLinearVelocity(0f, -2f);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
        	
            batch.draw(anmL.getKeyFrame(time, true), posX,posY, tailleX, tailleY);
            body.setLinearVelocity(-2f, 0f);
        }
        else{
        	body.setLinearVelocity(0f, 0f);
        	switch(state){
    		case STOPDOWN:
    			batch.draw(stopedD, posX,posY , tailleX, tailleY );
    			break;
    		case STOPUP:
    			batch.draw(stopedU, posX,posY , tailleX, tailleY );
    			break;
    		case STOPRIGHT:
    			batch.draw(stopedR, posX,posY , tailleX, tailleY );
    			break;
    		case STOPLEFT:
    			batch.draw(stopedL, posX,posY , tailleX, tailleY );
    			break;
    		default:
    			break;
    	}
        }
       }
        
        batch.draw(traph, -615, -100, 120,85);
        // rotation animation
        
        hb1.setTransform(3.4f,2.1f, omega);
        hb1.setAwake(true);
        hb2.setTransform(2.2f,2.1f, omega -80);
        hb2.setAwake(true);
        
        
        
        batch.draw(burnanm.getKeyFrame(time, true), 300 + 30*(float) Math.sin(omega),172 - 30*(float) Math.cos(omega), 90,90);
        batch.draw(burnanm.getKeyFrame(time, true), 300 - 30*(float) Math.sin(omega),172 + 30*(float) Math.cos(omega), 90,90);
        batch.draw(burnanm.getKeyFrame(time, true), 300 ,172, 90,90);
        batch.draw(burnanm.getKeyFrame(time, true), 170 + 30*(float) Math.sin(omega - 80),167 - 30*(float) Math.cos(omega - 80), 90,90);
        batch.draw(burnanm.getKeyFrame(time, true), 170 - 30*(float) Math.sin(omega - 80),167 + 30*(float) Math.cos(omega - 80), 90,90);
        batch.draw(burnanm.getKeyFrame(time, true), 170 ,167, 90,90);
        omega += 0.01%360;
        batch.draw(over,-Gdx.graphics.getWidth()/2 ,-Gdx.graphics.getHeight()/2 , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(lavanm.getKeyFrame(time, true), 285, -70, 400, 5);
        batch.draw(lavanm.getKeyFrame(time, true), 285, -164, 30, 95);
        
        batch.draw(frblanm.getKeyFrame(time, true), firepos,-75);
        firepos += 5;
        bodyf.setLinearVelocity(2.95f, 0);
        if(firepos == -135){
    		firepos = -535;
    		bodyf.setTransform(firepos/PIXELS_TO_METERS +0.2f, -0.65f, body.getAngle());
    		
        }
        
        	
        if(!animated){
            batch.draw(door, -214, 275, 55,70);
            }
        if(dranmbol){
        	doorTime += Gdx.graphics.getDeltaTime();
        	
        	if(doorTime <= 2.5f){
        		body.setLinearVelocity(0, 0);
        		move = false;
        		///////////////////////////
        		state=CurrentState.STOPDOWN;
        		batch.draw(stopedD, posX,posY , tailleX, tailleY );
        	}
        	else{
        		move = true;
        		doorTime = 800;
        	}
        	batch.draw(opndranm.getKeyFrame(doorTime, false), -225, 275, 80,70 );
        	batch.draw(key, -215 ,  310 + doorTime*2 , 50,50);
        	batch.draw(teleglowanm.getKeyFrame(doorTime, false),-242 ,  290 + doorTime*2, 2*tailleX, 2*tailleY);
        	animated = true;
        }
        if(!ctcst){
            batch.draw(statue, -670, -130, 400,400);
            }
        else{
           	if(statuetime >= 10){
           	/////////////////////////////
           	
           	body.setTransform(5.8f, -3.3f, body.getAngle());
   			batch.draw(statue2, -670, -130, 400,400);
   			move = true;
   			ctcst = false;
           	}
           	else{
           		batch.draw(statue, -670, -130, 400,400);
           		statuetime += 0.1;
            	batch.draw(horn, -670, -140 + statuetime, 400,400);
            	}
            }	
        /////:::light
        batch.draw(light, (body.getPosition().x * PIXELS_TO_METERS - 1450f) - 25  ,
   		     (body.getPosition().y * PIXELS_TO_METERS - 1240f)-stopedD.getHeight()/2);
        //////////::::::::::
        if(posX <= -627 && posX >= -650 && posY <= -255 && posY >= -295){
        	if(tuto){
        		
        		stateTime += Gdx.graphics.getDeltaTime();
        		batch.draw(teleglowanm.getKeyFrame(stateTime, false), posX-80,posY-70, 4*tailleX, 4*tailleY);
        		move = false;
        		if(stateTime >= 1.3){
        			move = true;
        			body.setTransform(0.5f, 1f, body.getAngle());
        			numbtp +=1;
        			////////////////////////////////////
        			stateTime = 0;
            	}
        	}
        	if(!tuto){
        		body.setLinearVelocity(0,0);
        		batch.draw(tutosc, -300,-350, 600, 700);
        		
        		batch.draw(press, -170,-910, 300,1024);
        		 move = false;
        		 pause = true;
        		 if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
        			 	tuto = true;
        			 	body.setTransform(-6.2f, -1.4f, body.getAngle());
        			 	move = true;
        		 }
        	}
        }
       
        
        if(posX <= -25 && posX >= -45 && posY <= 65 && posY >= 30){
        	stateTime += Gdx.graphics.getDeltaTime();
    		batch.draw(teleglowanm.getKeyFrame(stateTime, false), posX-80,posY-70, 4*tailleX, 4*tailleY);
    		move = false;
    		if(stateTime >= 1.3){
    			move = true;
    			stateTime = 0;
    			body.setTransform(0.5f, 1.8f, body.getAngle());
            	numbtp +=1;
            	/////////////////////
        	}
        }
        
        if(posX <= 455 && posX >= 440 && posY <= 200 && posY >= 195){
        	stateTime += Gdx.graphics.getDeltaTime();
    		batch.draw(teleglowanm.getKeyFrame(stateTime, false), posX-80,posY-70, 4*tailleX, 4*tailleY);
    		move = false;
    		if(stateTime >= 1.3){
    			move = true;
    			stateTime = 0;
    			body.setTransform(-5.6f, -2.7f, body.getAngle());
    			numbtp +=1;
    			////////////////////////////
    		}
        }
        if(posX <= -190 && posX >= -215 && posY <= 200 && posY >= 170){
        	stateTime += Gdx.graphics.getDeltaTime();
        	move = false;
        	batch.draw(teleglowanm.getKeyFrame(stateTime, false), posX-80,posY-70, 4*tailleX, 4*tailleY);
        	
    		if(stateTime >= 1.3){
    			move = true;
    			stateTime = 0;
    			body.setTransform(5.8f, -1.3f, body.getAngle());
    			numbtp +=1;
    			//////////////////////////////left
    		}
        }
        if(posX <= 365 && posX >= 360 && posY <= -155 && posY >= -170){
        	stateTime += Gdx.graphics.getDeltaTime();
    		batch.draw(teleglowanm.getKeyFrame(stateTime, false), posX-80,posY-70, 4*tailleX, 4*tailleY);
    		move = false;
    		if(stateTime >= 1.3){
    			move = true;
    			stateTime = 0;
    			body.setTransform(-5.85f, -0.4f, body.getAngle());
    			numbtp +=1;
    			////////////////////////////up
    		}
        }
        
        if(dead){
        	batch.draw(burnanm.getKeyFrame(time, true), posX -70,posY - 65, 4*tailleX, 4*tailleY );
        	((Game)Gdx.app.getApplicationListener()).setScreen(new GameOver());
        }
        if(numbtp == 4){
        	body.setTransform(posX0 / 100f, posY0/100, body.getAngle());
        	numbtp = 0;
        }
        switch(numbtp){
        	case 0: 
        		batch.draw(tele3, 440, -500);
        		break;
        	case 1: 
        		batch.draw(tele2, 440, -475);
        		break;
        	case 2: 
        		batch.draw(tele1, 440, -445);
        		break;
        	case 3: 
        		batch.draw(tele0, 440, -445);
        		break;
        	
        }
        	
        batch.end();
        
        dbg.render(world, debugMatrix);
		
	}
	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}
}