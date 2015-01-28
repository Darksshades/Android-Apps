package br.game.entities;

import java.util.ArrayList;

import br.game.GdxGame;
import br.game.entities.shot.MovementData;
import br.game.entities.shot.BulletPattern;
import br.game.handlers.B2DVars;
import br.game.states.PlayStageState;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class Shot extends B2DSprite {

	public Shot(Body body) {
		super(body);
		body.setUserData(this);
		name = "shot";
		
		Texture tex = GdxGame.res.getTexture("shot_01");
		TextureRegion[][] reg = TextureRegion.split(tex, 15, 5);
		
		setAnimation(reg, 0.1f);
		
		bulletData = new ArrayList<MovementData>();
		
		//GdxGame.res.getSound("laser_shot").play();
	}
	
	public BulletPattern bulletPattern = null;
	public ArrayList<MovementData> bulletData = null;
	float shotTimer = 0;
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		if(body.getPosition().x*B2DVars.PPM < GdxGame.cam.position.x-GdxGame.V_WIDTH/2.f  || 
			body.getPosition().x > GdxGame.cam.position.x-GdxGame.V_WIDTH/2.f + GdxGame.V_WIDTH/B2DVars.PPM) {
			isShouldRemove = true;
		} else
		if(body.getPosition().y < 0 || body.getPosition().y > GdxGame.V_HEIGHT/B2DVars.PPM) {
			isShouldRemove = true;
		}
		
		if(bulletPattern != null) {
			bulletPattern.doExecute(dt);
		} 
		
		if(bulletData.isEmpty() == false)
		{
			if(shotTimer > bulletData.get(0).delay ) {
				bulletData.remove(0);
				if(bulletData.isEmpty() == false) {
					if(bulletData.get(0).targetPlayer) {
						float dx = PlayStageState.player.getPosition().x - body.getPosition().x;
						float dy = PlayStageState.player.getPosition().y - body.getPosition().y;
						bulletData.get(0).angle += MathUtils.atan2(dy, dx);
					}
				}
			}
			else {
				bulletData.get(0).angle += bulletData.get(0).curvature*dt;
				body.setLinearVelocity( 
						bulletData.get(0).velocity*MathUtils.cos(bulletData.get(0).angle)
						,bulletData.get(0).velocity*MathUtils.sin(bulletData.get(0).angle)
						);
				
				body.setLinearVelocity(body.getLinearVelocity().x + bulletData.get(0).relativeVelocityX, 
						body.getLinearVelocity().y + bulletData.get(0).relativeVelocityY);
			}
			shotTimer += dt;
		}
	}
	
	@Override
	public void beginContact(String name) {
		if(name == "meteor" || name == "enemy") {
			explode();
		} else if(name == "player"){
			explode();
		}
	}

	private void explode() {
		isShouldRemove = true;
	}

}
