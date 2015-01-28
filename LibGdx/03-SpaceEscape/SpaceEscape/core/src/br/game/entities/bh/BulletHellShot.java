package br.game.entities.bh;

import br.game.GdxGame;
import br.game.entities.B2DSprite;
import br.game.handlers.B2DVars;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class BulletHellShot extends B2DSprite {

	public BulletHellShot(Body body) {
		super(body);
		name = "shot";
		
		Texture tex = GdxGame.res.getTexture("shot_01");
		TextureRegion[][] reg = TextureRegion.split(tex, 15, 5);
		
		setAnimation(reg, 0.1f);
	}
	
	private float moveTimer = 0.02f;
	private float alpha = 0;
	private float delta = 0;
	@Override
	public void update(float dt) {
		super.update(dt);
		
		if(body.getPosition().x*B2DVars.PPM < GdxGame.cam.position.x-GdxGame.V_WIDTH/2.f ||
				body.getPosition().x*B2DVars.PPM > GdxGame.cam.position.x+GdxGame.V_WIDTH/2.f) { 
			isShouldRemove = true;
		} else
		if(body.getPosition().y + height/2.f/B2DVars.PPM< 0 || body.getPosition().y > -height/2.f/B2DVars.PPM + GdxGame.V_HEIGHT/B2DVars.PPM) {
			isShouldRemove = true;
		}
		
		moveTimer -= dt;
		if(moveTimer < 0) {
			moveTimer += 0.02f;
		} else 
			return;
		
		float newX = GdxGame.V_WIDTH/B2DVars.PPM - delta - MathUtils.cos(delta);
		float newY = (float) (5 - MathUtils.sin(delta)*MathUtils.cos(delta)/2.f + MathUtils.cos(delta) -
				MathUtils.sin(2*delta)*MathUtils.cos(alpha)*MathUtils.sin(alpha/3.f)/4.f
				);
		
		delta += 0.05f;
		if(delta > 1) alpha += 0.07f;
		
		body.setTransform(newX, newY, 0);
		
		
		
		
	}

}
