package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class groundBox extends PolygonShape{
	private int id;
	
	public groundBox(float height, float width, float x, float y, BodyDef body, FixtureDef fixtr, World world, Body bod, int id){
		this.id = id;
		this.setAsBox(height, width);
		body.type = BodyType.StaticBody;
		body.gravityScale = 0;
		body.position.set(x, y);
		fixtr.shape = this;
		fixtr.friction=0;
		bod = world.createBody(body);
		bod.setUserData(this.id);
		bod.createFixture(fixtr).setUserData(this);
		this.setAsBox(height+.1f, width);
		body.position.set(x, y-.2f);
		bod.createFixture(fixtr);
		this.dispose();
	}
}

