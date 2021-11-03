package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Iandm extends PolygonShape {
	int id;
	public Iandm(Body b,BodyDef bf, World w,FixtureDef fxtr, float posX, float posY, float taiX, float taiY, int id){
		this.setAsBox(taiX, taiY);
		bf.type = BodyType.StaticBody;
		bf.position.set(posX, posY);
		fxtr.shape = this;
		this.id = id;
		b = w.createBody(bf);
		b.setUserData(this.id);
		b.createFixture(fxtr).setUserData(this);
		this.dispose();
	}
}
