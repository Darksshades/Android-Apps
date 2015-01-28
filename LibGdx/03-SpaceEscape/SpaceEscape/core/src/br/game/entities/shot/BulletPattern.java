package br.game.entities.shot;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class BulletPattern {

	public static final int NONE = 0;
	public static final int SWAY = 1;
	public static final int CIRCULAR = 2;
	public static final int FEINT = 3;
	
	private Body body;
	private int id;
	private boolean ended = false;
	
	public float delta = 0;
	public float alpha = 0;
	public float beta  = 0;
	private float updateTimer = 0;
	
	public float timerSpeed = 1.f;
	
	public float powerA = 1.f;
	public float speedA = 1.f;
	
	public BulletPattern(Body body, int id) {
		this.body = body;
		this.id = id;
		
		if(id == NONE) ended = true;
	}
	
	public void doExecute(float dt) {
		updateTimer -= dt*timerSpeed;
		if(updateTimer > 0 || ended) return;
		
		while(updateTimer <= 0) {
			if(id == SWAY) {
				sway();
			} else
			if(id == CIRCULAR) {
				circular();
			} else
			if(id == FEINT) {
				feint();
			}	
		}
	}
	
	public void sway() {
		updateTimer += 0.02f;
		
		alpha += 360*0.02f;
		
		float x = body.getPosition().x;
		float y = body.getPosition().y + (MathUtils.cosDeg(alpha*speedA)/32.f)*powerA;
		body.setTransform(x, y, body.getAngle());
		
	}
	private float dx = 0;
	private float dy = 0;
	public void circular() {
		updateTimer = 0.02f;
		
		alpha += 360*0.02f;
		
		dx = (MathUtils.cosDeg(alpha*speedA)/32.f)*powerA - dx;
		dy = (MathUtils.cosDeg(alpha*speedA)/32.f)*powerA;
		float x = body.getPosition().x + dx;
		float y = body.getPosition().y + dy;
		body.setTransform(x, y, body.getAngle());
	}
	
	public void feint() {
		updateTimer = 0.02f;
		
		alpha += 1*0.02f;
		beta +=  1*0.02f;
		
		float x = body.getPosition().x;
		float y = body.getPosition().y;
		
		if( beta < 2.f ) {
			x += MathUtils.cos(alpha);
			y += MathUtils.cos(alpha);
			body.setTransform(x, y, body.getAngle());
		} else {
			body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y-0.1f);
		}
	}

}
