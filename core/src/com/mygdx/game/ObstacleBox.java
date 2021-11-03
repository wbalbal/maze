package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ObstacleBox extends PolygonShape{
public ObstacleBox(float height, float width, float x, float y, BodyDef body, FixtureDef fixtr, World world, Body bod){
		
		this.setAsBox(height, width);
		body.type = BodyType.StaticBody;
		body.gravityScale = 0;
		body.position.set(x, y);
		fixtr.shape = this;
		bod = world.createBody(body);
		bod.createFixture(fixtr).setUserData(this);
		this.dispose();
	}

}
