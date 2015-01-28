package br.game.entities.shot;

import static br.game.handlers.B2DVars.PPM;
import br.game.GdxGame;
import br.game.entities.Shot;
import br.game.handlers.B2DVars;
import br.game.states.PlayStageState;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class ShotPattern {
	public static final int Burst = 1;
	public static final int Circular = 2;
	public static final int Swiff = 3;
	public static final int Normal1 = 4;
	public static final int Concentrated1 = 5;
	public static final int Concentrated2 = 6;
	public static final int Concentrated3 = 7;
	public static final int Boss1 = 8;
	
	private Body body;
	private World world;
	private int id;
	private boolean ended = false;
	
	private float alpha = 0;
	private float beta  = 0;
	private float updateTimer = 0;
	private boolean alternate = true;
	private float timerDelay = 0.f;
	
	public float timerSpeed = 1.f;	
	public float startDelay = 0.f;
	
	public ShotPattern(Body body, World world, int id) {
		this.body = body;
		this.id = id;
		this.world = world;
		if(id == 0) ended = true;
	}
	
	public void doExecute(float dt, Vector2 speed) {
		
		if(timerDelay+dt < startDelay) {
			timerDelay += dt;
			return;
		}			
			
		updateTimer -= dt*timerSpeed;
		
		if(updateTimer > 0 || ended) return;
		
		while(updateTimer <= 0) {
			if(id == Burst) {
				burst(speed);
			} else
			if(id == Circular) {
				circular(speed);
			} else
			if(id == Swiff) {
				swiff(speed);
			} else
			if(id == Normal1) {
				normal1(speed);
			} else
			if(id == Concentrated1) {
				concentrated1(speed);
			} else
			if(id == Concentrated2) {
				concentrated2(speed);
			} else
			if(id == Concentrated3) {
				concentrated3(speed);
			} else
			if(id == Boss1) {
				boss1(speed);
			}
		}
	}
	
	private void normal1(Vector2 speed) {
		updateTimer += 0.02;
		
		alpha += 1*0.02f;
		
		if(alpha < 3.f) return;
		
		alpha -= 3.f;
		
		Shot s = fire();
		s.bulletData.add( new MovementData(2.f, 3.f, 0, 0, false) );
		s = fire();
		s.bulletData.add( new MovementData(2.f, 3.f, 20, 0, false) );
		s = fire();
		s.bulletData.add( new MovementData(2.f, 3.f, -20, 0, false) );
		
	}
	
	private void concentrated1(Vector2 speed) {
		updateTimer += 0.02;
		
		alpha += 1*0.02f;
		
		if(alpha < 2.f) return;
		
		alpha -= 2.f;
		
		Shot s = fire();
		s.bulletData.add( new MovementData(1.f, 3.f, 0, 0, false) );
		s.bulletData.add( new MovementData(2.5f, speed.x, 0, 0, false) );
		s.bulletData.add( new MovementData(6.f, 3.f, 0 , 0, true) );
		
		s = fire();
		s.bulletData.add( new MovementData(1.f, 3.f, 20, 0, false) );
		s.bulletData.add( new MovementData(2.5f, speed.x, 0, 0, false) );
		s.bulletData.add( new MovementData(6.f, 3.f, 0 , 0, true) );
		s = fire();
		s.bulletData.add( new MovementData(1.f, 3.f, -20, 0, false) );
		s.bulletData.add( new MovementData(2.5f, speed.x, 0, 0, false) );
		s.bulletData.add( new MovementData(6.f, 3.f, 0 , 0, true) );
		
	}
	
	private void concentrated2(Vector2 speed) {
		updateTimer += 0.02;
		
		alpha += 360*0.02f;
		
		if(alpha > 180.f) {
			alpha = -360;
			beta = 0;
		}
		
		if(alpha > beta + 10.f) {
			beta = alpha; 
			
			Shot s = fire();
			s.bulletData.add( new MovementData(0.5f, 3.f, 180+alpha-90, 0, false) );
			s.bulletData.add( new MovementData(2.5f-alpha/360.f, -speed.x, 0, 0, false) );
			s.bulletData.add( new MovementData(6.f, 3.f, 0 , 0, true) );
		}
	}
	
	private void concentrated3(Vector2 speed) {
		updateTimer += 0.02;
		
		alpha += 360*0.02f;
		
		
		
		if(alternate && alpha > 360.f) {
			alpha = -60;
			beta = 0;
			alternate = false;
		} else 
		if(!alternate && alpha > 360.f) {
			alpha = -360*2-60;
			beta = 0;
			alternate = true;
		}
		
		if(alpha > beta + 60.f) {
			beta = alpha; 
			
			Shot s = fire();
			s.bulletData.add( new MovementData(0.2f, 4.f, -90, 0, false, 0, 0) );
			s.bulletData.add( new MovementData(2.2f, 3.f, 0, 3, false, speed.x, 0) );
			s.bulletData.add( new MovementData(6.5f, 2.f, 0, 0, true) );
		}
	}
	
	private void boss1(Vector2 speed) {
		updateTimer += 0.02;
		
		alpha += 360*0.02f;
		
		
		
		if(alternate && alpha > 90.f) {
			alpha = -60;
			beta = 0;
			alternate = false;
		} else 
		if(!alternate && alpha > 90.f) {
			alpha = -360*2-60;
			beta = 0;
			alternate = true;
			Shot s = fire();
			s.bulletData.add( new MovementData(1.5f, 2.f, 1, -0.1f, false, 0, 0) );
			s.bulletData.add( new MovementData(3.5f, 2.f, 0, 0.1f, false, 0, 0) );
			s = fire();
			s.bulletData.add( new MovementData(1.5f, 2.f, -1, 0.1f, false, 0, 0) );
			s.bulletData.add( new MovementData(3.5f, 2.f, 1, -0.1f, false, 0, 0) );
		}
		
		if(alpha > beta + 10.f) {
			beta = alpha; 
			
			Shot s = fire();
			s.bulletData.add( new MovementData(3.f, 2.f, 90+alpha-180, -0.8f, false, 0, 0) );
			s.bulletData.add( new MovementData(8.5f, 2.f, -0.2f, 0, true) );
			
			s = fire();
			s.bulletData.add( new MovementData(3.f, 2.f, 90-alpha, 0.8f, false, 0, 0) );
			s.bulletData.add( new MovementData(8.5f, 2.f, -0.2f, 0, true) );
		}
	}

	public void burst(Vector2 speed) {
		updateTimer += 0.02f;
		
		alpha += 1*0.02f;
		//Read backwards
		
		if(alpha > 4.5f) {
			shot(-2.2f, 0.f, BulletPattern.NONE);
			alpha -= 4.5f;
			alternate = true; //res
		}else
		if(alpha > 4.f) {
			if(alternate) {
				shot(-2.2f, 0.f, BulletPattern.NONE);
				alternate = false;
			}
		} else
		if(alpha > 2.4f) {
			if(!alternate) {
				Shot s = shot(-2.2f, 0f, BulletPattern.SWAY);
				s.bulletPattern.speedA = 0.8f;
				s.bulletPattern.powerA = 1.5f;
				
				s = shot(-2.2f, 0f, BulletPattern.SWAY);
				s.bulletPattern.speedA = 0.8f;
				s.bulletPattern.powerA = 1.5f;
				s.bulletPattern.alpha = 180f;
				alternate = true;
			}
		}else
		if(alpha > 1.f) {
			if(alternate) {
				shot(-2.4f, 0.f, BulletPattern.CIRCULAR);
				alternate = false;
			}
		}
	}
	
	public void circular(Vector2 speed) {
		updateTimer += 0.02f;
		
		alpha += 1*0.02f;
		
		if(alpha > beta + 0.5f) {
			beta = alpha; 
			
			shot( 1.1f*MathUtils.cos(alpha), MathUtils.sin(alpha), BulletPattern.NONE);
		}
	}
	
	public void swiff(Vector2 speed) {
		updateTimer += 0.02f;
		
		alpha += 90*0.02f;
		
		if(alpha > 360*6.f) {
			alpha -= 360*6.f;
			beta = alpha;
		}
		
		if(     
				(alpha < 50f) || 
				(alpha > 360*3.f && alpha < 360*3+50) 
		){
			if(alpha > beta + 10.f ) {
				beta = alpha;
				float offset = 145;
				float dir = 1;
				if(alpha > 720.f) {
					offset = 145+50;
					dir = -1;
				}
				shot( 1f*MathUtils.cosDeg(offset+alpha*dir), 0, BulletPattern.SWAY);
			}
		} 
	}
	
	
	private Shot fire() {
		GdxGame.res.getSound("laser_shot").play();
		BodyDef bdef = new BodyDef();
		Body bbody;
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
		fdef.filter.maskBits     = B2DVars.BIT_PLAYER;
		fdef.isSensor = true;
	
		bdef.position.set(this.body.getPosition());
		bdef.type = BodyType.KinematicBody;
		bbody = world.createBody(bdef);
		
		shape.setAsBox(6.f / PPM, 3 / PPM);
		fdef.shape = shape;
		bbody.createFixture(fdef);
		
		bbody.setLinearVelocity(0, 0);
		Shot s = new Shot(bbody);
		s.name = "enemy";
		s.bulletPattern = null;
		
		
		PlayStageState.objects.add(s);
		
		return s;
	}
	
	private Shot shot(float vx, float vy, int pattern) {
		GdxGame.res.getSound("laser_shot").play();
		BodyDef bdef = new BodyDef();
		Body bbody;
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
		fdef.filter.maskBits     = B2DVars.BIT_PLAYER;
		fdef.isSensor = true;
	
		bdef.position.set(this.body.getPosition());
		bdef.type = BodyType.KinematicBody;
		bbody = world.createBody(bdef);
		
		shape.setAsBox(6.f / PPM, 3 / PPM);
		fdef.shape = shape;
		bbody.createFixture(fdef);
		
		bbody.setLinearVelocity(body.getLinearVelocity().x + vx, body.getLinearVelocity().y + vy);
		Shot s = new Shot(bbody);
		s.name = "enemy";
		s.bulletPattern = new BulletPattern(bbody, pattern);
		
		
		PlayStageState.objects.add(s);
		
		return s;
	}

}
