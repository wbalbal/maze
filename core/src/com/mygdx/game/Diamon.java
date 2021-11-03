package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Diamon extends PolygonShape{
	private int id;
	
	public int getIdd() {
		return id;
	}

	public Diamon(float height, float width, float x, float y, BodyDef body, FixtureDef fixtr, World world, Body bodDiam, int id){
		
		this.setAsBox(height, width);
		this.id = id;
		body.type = BodyType.StaticBody;
		body.gravityScale = 0;
		body.position.set(x, y);
		fixtr.shape= this;
		bodDiam = world.createBody(body);
		bodDiam.setUserData(this.id);
		bodDiam.createFixture(fixtr).setUserData(this);
		this.dispose();
	}

}
