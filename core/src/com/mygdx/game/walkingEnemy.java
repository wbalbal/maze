package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class walkingEnemy extends PolygonShape{
	private int id;
	public walkingEnemy(float width,float height,float x,float y,BodyDef body,FixtureDef fixtr,World world,Body bod,int id){
		this.setAsBox(width, height);
		this.id = id;
		body.type = BodyType.DynamicBody;
		body.position.set(x, y);
		fixtr.shape = this;
		fixtr.density = 999;
		
	}
}
