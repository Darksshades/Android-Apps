package br.game.entities;

import br.game.GdxGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Cave extends B2DSprite{

	public Cave(Body body) {
		super(body);
		body.setUserData(this);
		name = "cave";

		Texture tex = GdxGame.res.getTexture("cave");
		TextureRegion[] reg = TextureRegion.split(tex, 16*3, 16*2)[0];
		
		setAnimation(reg, 1.2f);
		animationLoop = false;
	}

	public void resetAnimation() {
		stateTime = 0;
		
	}

}
