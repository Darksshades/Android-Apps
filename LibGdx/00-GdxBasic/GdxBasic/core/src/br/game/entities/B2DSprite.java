package br.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static br.game.handlers.B2DVars.PPM;

public class B2DSprite {
	
	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	protected float stateTime = 0;
	
	public B2DSprite(Body body) {
		this.body = body;
	}
	
	public void setAnimation(TextureRegion reg, float delay) {
		setAnimation(new TextureRegion[] { reg }, delay);
	}
	
	public void setAnimation(TextureRegion[] reg, float delay) {
		this.animation = new Animation(delay, reg);
		this.width  = reg[0].getRegionWidth();
		this.height = reg[0].getRegionHeight();
	}
	
	public void update(float dt) {
		stateTime += dt;
		if(stateTime > animation.getAnimationDuration() )
			stateTime -= animation.getAnimationDuration();
	}
	
	public void render(SpriteBatch sb) {
		TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
		
		sb.begin();
		sb.draw(currentFrame, 
				body.getPosition().x - width / PPM / 2.0f, 
				body.getPosition().y - height / PPM / 2.0f, 
				width  / PPM / 2.0f, 
				height / PPM / 2.0f, 
				width  / PPM, 
				height / PPM, 
				1.0f, 1.0f, 
				MathUtils.radiansToDegrees*body.getAngle());
		sb.end();
	}
	
	public Body getBody() { return body; }
	public Vector2 getPosition() { return body.getPosition(); }
	public float getWidth() { return width; }
	public float getHeight() { return height; }

}
