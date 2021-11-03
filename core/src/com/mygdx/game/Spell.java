package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Spell extends PolygonShape{
	int id;
	public Spell(Body b,BodyDef bf, FixtureDef fxtr, float posX, float posY, float taiX, float taiY, int id){
		this.setAsBox(taiX, taiY);
		bf.type = BodyType.DynamicBody;
		bf.position.set(posX, posY);
		fxtr.shape = this;
		this.id = id;
		
	}
}
