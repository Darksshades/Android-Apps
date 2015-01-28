package br.game.entities;

import br.game.GdxGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import static br.game.handlers.B2DVars.PPM;

public class Player extends B2DSprite {
	
	protected short state = 0;
	public Player(Body body) {
		super(body);
		body.setUserData(this);
		name = "player";
		
		Texture tex = GdxGame.res.getTexture("space_ship");
		TextureRegion[][] reg = TextureRegion.split(tex, 36, 25);
		
		setAnimation(reg, 0.47f);
	}
	
	@Override
	public void beginContact(String name) {
		if(name == "meteor" || name == "enemy") {
			isShouldRemove = true;
		}
	}
	
	@Override
	public void update(float dt) {
		
		if(state == Player.STAND) {
			stateTime = 0;
			updateAnimation(0);
		} else {
			updateAnimation(dt);
		}
		
		if(body.getPosition().y > (GdxGame.V_HEIGHT - height/4.f)/PPM) {
			body.setTransform(body.getPosition().x, (GdxGame.V_HEIGHT-height/4.f)/PPM, 0);
		} else if(body.getPosition().y < 0 + height/PPM/4.f) {
			body.setTransform(body.getPosition().x, height/PPM/4.f, 0);
		}
	}
	
	public void playAttackAnimation() {
		changeAnimation(0, false);
		setState(Player.ATTACK);
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
