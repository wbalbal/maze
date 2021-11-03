package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MainChara_box extends PolygonShape{
	
	public MainChara_box(float height, float width, float x, float y, BodyDef body, FixtureDef fixtr, World world, Body bod){
		this.setAsBox(height, width);
		body.type = BodyType.DynamicBody;
		body.gravityScale = 0;
		body.position.set(x, y);
		fixtr.shape = this;
		fixtr  .density = 0;
		fixtr  .friction = 0;
		fixtr  .restitution = 0;
		
	}
}
