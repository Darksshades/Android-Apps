package br.game.entities;

import br.game.GdxGame;

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

}
