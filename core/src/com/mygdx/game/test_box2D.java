package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class test_box2D implements Screen{
	
	private World              world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	
	private Body               bod;
	private Vector2 mvt = new Vector2();
	
	private int                speed = 300;
	private final float        timeStep = 1/60f;
	private final int          velocityIterations = 8,
							   positionIterations = 3;
	

	@Override
	public void show() {
		world         = new World(new Vector2(0,-9.81f) , false);
		debugRenderer = new Box2DDebugRenderer();
		camera        = new OrthographicCamera(Gdx.graphics.getWidth()/20,Gdx.graphics.getHeight()/20);
		
		//all inputs
		Gdx.input.setInputProcessor(new InputController(){
			@Override
			public boolean keyDown(int keycode) {
				switch(keycode){
				case Keys.Z:
					mvt.y = speed;
					break;
				case Keys.S:
					mvt.y = -speed;
					break;
				case Keys.Q:
					mvt.x = -speed;
					break;
				case Keys.D:
					mvt.x = speed;
					break;
				case Keys.UP:
					camera.zoom -= .2f ;
					break;
				case Keys.DOWN:
					camera.zoom += .2f ;
					break;
				}
				return true;
			}
			
			@Override
			public boolean keyUp(int keycode) {
				switch(keycode){
				case Keys.Z:
					mvt.y = 0;
					break;
				case Keys.S:
					mvt.y = 0;
					break;
				case Keys.Q:
					mvt.x = 0;
					break;
				case Keys.D:
					mvt.x = 0;
					break;
				}
				return true;
			}
		});
		
		//ball is boodyy
		BodyDef ball    = new BodyDef();
		FixtureDef fixtr = new FixtureDef();
		
		
		
		//moving Box
		PolygonShape boxShape = new PolygonShape();
		boxShape    .setAsBox(.5f, 1);
		
		ball.type    = BodyType.DynamicBody;
		ball.position.set(2.5f, 5);
		
		fixtr.shape       =boxShape;
		fixtr.friction    = .75f;
		fixtr.restitution = .1f;
		fixtr.density     = 5;
		bod  = world.createBody(ball);
		bod  .createFixture(fixtr);
		
		boxShape.dispose();
		
		//the ball
		ball.type = BodyType.DynamicBody;
		ball.position.set(0, 1);
		
		CircleShape circle = new CircleShape();
		circle.setPosition  (new Vector2(0, 1.5f));
		circle.setRadius(0.5f);
		
		fixtr.shape       = circle;
		fixtr.density     = 4;
		fixtr.friction    = 0.5f;
		fixtr.restitution = 0.8f;
		
		bod   .createFixture(fixtr);
		circle.dispose();
		
		//
		bod.applyAngularImpulse(5, true);
		
		//the ground
		ChainShape grnd = new ChainShape();
		grnd .createChain(new Vector2[]{ new Vector2(-50,0), new Vector2(50,-10)});
		
		ball.type = BodyType.StaticBody;
		ball.position.set(0, 0);
		//////////ball is the body fixtr is the fixture
		fixtr.shape       = grnd;
		fixtr.friction    = .5f;
		fixtr.restitution = 0;
		world.createBody(ball).createFixture(fixtr);
		grnd .dispose();
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.step(timeStep, velocityIterations, positionIterations);
		bod  .applyForceToCenter(mvt, true);
		
		camera       .position.set(bod.getPosition().x, bod.getPosition().y, 0);
		camera       .update();
		
		debugRenderer.render(world, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height/25;
		camera.viewportWidth  = width/25;
		camera.update();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		world.dispose();
		debugRenderer.dispose();
	}
	

}
