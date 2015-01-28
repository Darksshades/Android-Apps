package br.game.entities;

import br.game.GdxGame;
import br.game.handlers.B2DVars;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Shot extends B2DSprite {

	public Shot(Body body) {
		super(body);
		body.setUserData(this);
		name = "shot";
		
		Texture tex = GdxGame.res.getTexture("shot");
		TextureRegion[][] reg = TextureRegion.split(tex, 16, 16);
		
		setAnimation(reg, 0.2f);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		if(body.getPosition().x < 0 || body.getPosition().x > GdxGame.V_WIDTH/B2DVars.PPM) {
			isShouldRemove = true;
		} else
		if(body.getPosition().y < 0 || body.getPosition().y > GdxGame.V_HEIGHT/B2DVars.PPM) {
			isShouldRemove = true;
		}
	}

}
