package br.game.entities;

import br.game.GdxGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Slimy extends B2DSprite {

	public Slimy(Body body) {
		super(body);
		body.setUserData(this);
		name = "slimy";
		
		Texture tex = GdxGame.res.getTexture("slimy");
		TextureRegion[][] reg = TextureRegion.split(tex, 16, 16);
		
		setAnimation(reg, 0.2f);
	}
	
	public void playDeathAnimation() {
		currentAnimation = 1;
		stateTime = 0;
		animationLoop = false;
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		if(animationLoop == false) {
			body.setActive(false);
		}
		if(isAnimationFinished()) {
			isShouldRemove = true;
		}
	}
	
	@Override
	public void beginContact(String name) {
		if(name == "shot") {
			playDeathAnimation();
		}
	}

}
