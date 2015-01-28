package br.game.entities;

import static br.game.handlers.B2DVars.PPM;

import java.util.ArrayList;

import br.game.GdxGame;
import br.game.entities.shot.BulletPattern;
import br.game.entities.shot.MovementData;
import br.game.entities.shot.ShotPattern;
import br.game.handlers.B2DVars;
import br.game.handlers.GameInputProcessor;
import br.game.states.PlayStageState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class SpaceShoter extends B2DSprite {

	public SpaceShoter(Body body, World world) {
		this(body, world, 0);
	}
	public SpaceShoter(Body body, World world, int graphicsId) {
		super(body);
		
		this.world = world;
		name = "enemy";
		
		Texture tex = null;
		if(graphicsId == 0){
			tex = GdxGame.res.getTexture("alien");
		}else if(graphicsId == 1) {
			tex = GdxGame.res.getTexture("ship");
			setStartHP(15);
		} else
			tex = GdxGame.res.getTexture("space_ship");
		
		PolygonShape s = (PolygonShape)body.getFixtureList().get(0).getShape();
		s.setAsBox(tex.getWidth()/4.f/B2DVars.PPM, tex.getHeight()/3.f/B2DVars.PPM);
			
		TextureRegion[][] reg = TextureRegion.split(tex, tex.getWidth(), tex.getHeight());
		
		setAnimation(reg, 0.47f);
		
		moveData = new ArrayList<MovementData>();
	}
	
	private float attackTimer = 1.0f;
	
	public ShotPattern shotPattern = null;
	public ArrayList<MovementData> moveData= null;
	private float moveTimer = 0;
	private float hp = 2;
	private float maxhp = 2;
	
	public float getHP() { return hp; }
	public void setStartHP(float hp) { this.hp = this.maxhp = hp; }
	ShapeRenderer shapeRenderer = new ShapeRenderer();
	private float hitTimer = 0f;
	
	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		
		if(hitTimer <= 0.f) return;
		
		float alpha = 0.8f;
		if(hitTimer < 0.8f) alpha = hitTimer;

		// Disables normal spritebatch
		sb.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0f, 0, 1.0f, alpha);
		shapeRenderer.identity();
		shapeRenderer.rect(
				(-width/2.f + body.getPosition().x*B2DVars.PPM)*GameInputProcessor.scaleUp, 
				(-height/2.f + body.getPosition().y*B2DVars.PPM)*GameInputProcessor.scaleUp, 
				width*GameInputProcessor.scaleUp, 4.f*GameInputProcessor.scaleUp);
		//HP
		shapeRenderer.setColor(0f, 1.0f, 1.0f, alpha);
		float hpPercent = hp / maxhp;
		shapeRenderer.rect(
				(-width/2.f + body.getPosition().x*B2DVars.PPM)*GameInputProcessor.scaleUp, 
				(-height/2.f + body.getPosition().y*B2DVars.PPM)*GameInputProcessor.scaleUp, 
				(width*hpPercent)*GameInputProcessor.scaleUp, 4.f*GameInputProcessor.scaleUp);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		// Finishes bar, re-enable spritebatch
		sb.begin(); 
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		hitTimer -= dt;
		if(hitTimer < 0.f) hitTimer = 0;
	
		if(body.getPosition().x*B2DVars.PPM < GdxGame.cam.position.x-GdxGame.V_WIDTH/2.f  ) { 
			isShouldRemove = true;
		} 
		
		
		if(body.getPosition().x - PlayStageState.player.getPosition().x > 14.f) 
			return;
		//Old Attack
		if(shotPattern == null) {
			attackTimer -= dt;
			if(attackTimer <= 0) {
				attackTimer += 1.0f;
				shot(-2.4f, 0);
				shot(-2.4f, -0.3f);
				shot(-2.4f, 0.3f);
			}
		} //Patterns attack
		else
			shotPattern.doExecute(dt, body.getLinearVelocity());
		
		// Movement
		if(moveData.isEmpty() == false)
		{
			//If timer has passed, remove from list, next action
			if(moveTimer > moveData.get(0).delay ) {
				moveData.remove(0);
				// Check if next one should target player, done now to be made only once
				if(moveData.isEmpty() == false) {
					if(moveData.get(0).targetPlayer) {
						float dx = PlayStageState.player.getPosition().x - body.getPosition().x;
						float dy = PlayStageState.player.getPosition().y - body.getPosition().y;
						moveData.get(0).angle += MathUtils.atan2(dy, dx);
					}
				}
			}
			else {
				//Add curvature, in radians
				moveData.get(0).angle += moveData.get(0).curvature*dt;
				body.setLinearVelocity( 
						moveData.get(0).velocity*MathUtils.cos(moveData.get(0).angle)
						,moveData.get(0).velocity*MathUtils.sin(moveData.get(0).angle)
						);
				//Adds relative velocity independent of real velocity
				body.setLinearVelocity(body.getLinearVelocity().x + moveData.get(0).relativeVelocityX, 
						body.getLinearVelocity().y + moveData.get(0).relativeVelocityY);
			}
			moveTimer += dt;
		}
	}
	
	public World world;

	private void shot(float vx, float vy) {
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
		
		bbody.setLinearVelocity(vx, vy);
		Shot s = new Shot(bbody);
		s.name = "enemy";
		s.bulletPattern = new BulletPattern(bbody, BulletPattern.SWAY);
		
		
		PlayStageState.objects.add(s);
	}
	@Override
	public void beginContact(String name) {
		if(name == "shot") {
			state = DIE;
			hp -= 1.f;
			if(hp <= 0) isShouldRemove = true;
			else hitTimer = 2.f;
				
		}
	}

}
