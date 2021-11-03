package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class block {

	private int blkX ,blkY , widtH , heighT;
	public Rectangle rl , rr , rt , rb; //rects left right top buttm
	public Texture blockIMG = new Texture(Gdx.files.internal("grnd4.png"));
	
	public block(){}
	
	public block(int x, int y , int l , int L) {
		this.blkX=x;
		this.blkY=y;
		this.widtH=l;
		this.heighT=L;
		
	}

	public int getBlkX() {
		return blkX;
	}

	public int getBlkY() {
		return blkY;
	}

	public int getWidtH() {
		return widtH;
	}

	public int getHeighT() {
		return heighT;
	}
	
	public void Left(){
		rl = new Rectangle();
		rl.set(blkX, blkY+1, widtH-1, heighT-2);
	}
	
	public void Right(){
		rr = new Rectangle();
		rr.set(blkX+1, blkY+1, widtH-1, heighT-2);
	}
	
	public void top(){
		rt = new Rectangle();
		rt.set(blkX+1, blkY+1, widtH-2, heighT-1);
	}
	
	public void btm(){
		rb = new Rectangle();
		rb.set(blkX+1, blkY, widtH-2, heighT-1);
	}
	
	
	public boolean LeftColl(Rectangle r){
		Left();
		if(r.overlaps(rl))
			return true;
		return false;
	}
	
	public boolean RightColl(Rectangle r){
		Right();
		if(r.overlaps(rr))
			return true;
		return false;
	}
	
	public boolean TopColl(Rectangle r){
		top();
		if(r.overlaps(rt))
			return true;
		return false;
	}
	
	public boolean BtmColl(Rectangle r){
		btm();
		if(r.overlaps(rb))
			return true;
		return false;
	}
	
}
