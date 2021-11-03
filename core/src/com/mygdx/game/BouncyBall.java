package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class BouncyBall extends CircleShape{
	
	public BouncyBall(float r, float x, float y, BodyDef body, FixtureDef fixtr, World world, Body bod, float force){
		this.setPosition(new Vector2(x,y));
		this.setRadius(r);
		body.type = BodyType.DynamicBody;
		body.gravityScale = 0f;
		fixtr.shape = this;
		fixtr.restitution =1f;
		fixtr.friction=0;
		fixtr.density = 0;
		
	}

}
