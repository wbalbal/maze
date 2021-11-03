package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class rotatingFire extends PolygonShape{
	int id;
	public rotatingFire(float height, float width, float x, float y, BodyDef body, FixtureDef fixtr, World world, Body bodFire, int id){
		this.setAsBox(height, width);
		this.id = id;
		body.type = BodyType.KinematicBody;
		body.gravityScale = 0;
		body.position.set(x, y);
		fixtr.shape = this;
		fixtr.restitution = 0;
	}
}
