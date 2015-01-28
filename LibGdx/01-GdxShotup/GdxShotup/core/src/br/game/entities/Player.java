package br.game.entities;

import br.game.GdxGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends B2DSprite {

	public static final short STAND = 0;
	public static final short ATTACK = 1;
	public static final short DIE = 2;
	
	protected short state = 0;
	public Player(Body body) {
		super(body);
		body.setUserData(this);
		name = "player";
		
		Texture tex = GdxGame.res.getTexture("mage");
		TextureRegion[][] reg = TextureRegion.split(tex, 16, 16);
		
		setAnimation(reg, 0.5f);
	}
	
	@Override
	public void update(float dt) {
		
		if(state == Player.STAND) {
			stateTime = 0;
			updateAnimation(0);
		} else
			updateAnimation(dt);
	}
	
	public void setState(short state) {
		this.state = state;
	}
	
	public int getState() {
		return this.state;
	}

	public void playAttackAnimation() {
		changeAnimation(0, false);
	}
	
	public void playDeathAnimation() {
		changeAnimation(1, false);
	}
	
	private void changeAnimation(int index, boolean loop) {
		animationLoop = loop;
		stateTime = 0;
		currentAnimation = index;
	}

}
