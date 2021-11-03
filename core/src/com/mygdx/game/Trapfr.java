package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Trapfr extends PolygonShape{
	
	public Trapfr (Body b,BodyDef bf, FixtureDef fxtr, float posX, float posY, float taiX, float taiY){
		this.setAsBox(taiX, taiY);
		bf.type = BodyType.DynamicBody;
		bf.position.set(posX, posY);
		fxtr.shape = this;
		
	}
}
