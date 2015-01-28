package br.game.entities;

import br.game.GdxGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class City extends B2DSprite {
	
	protected short state = 0;
	
	public static final short FULL = 0;
	public static final short DAMAGED = 1;
	public static final short DEAD = 2;

	public City(Body body) {
		super(body);
		body.setUserData(this);
		name = "city";
		
		Texture tex = GdxGame.res.getTexture("house");
		TextureRegion[] reg = TextureRegion.split(tex, 16*4, 16*2)[0];
		
		setAnimation(reg, 1.f);
	}
	
	@Override
	public void update(float dt) {
		if(state == FULL)
			stateTime = 0;
		else if(state == DAMAGED)
			stateTime = 1.1f;
		else if(state == DEAD)
			stateTime = 2.1f;
	}
	
	@Override
	public void endContact(String name) {
		if(name == "slimy") {
			state++;
		}
		if(state > DEAD) {
			state = DEAD;
		}
	}
	
	public short getState() { return state; }

}
