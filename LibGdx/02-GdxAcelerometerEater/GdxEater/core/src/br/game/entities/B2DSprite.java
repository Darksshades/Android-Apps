package br.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static br.game.handlers.B2DVars.PPM;

public class B2DSprite {
	
	protected Body body;
	protected ArrayList<Animation> animations;
	protected float width;
	protected float height;
	protected float stateTime = 0;
	protected int currentAnimation = 0;
	protected boolean animationLoop = true;
	protected boolean isShouldRemove = false;
	protected String name = "sprite";
	
	public B2DSprite(Body body) {
		this.body = body;
		this.animations = new ArrayList<Animation>();
	}
	
	public void setAnimation(TextureRegion reg, float delay) {
		setAnimation(new TextureRegion[] { reg }, delay);
	}
	
	public void setAnimation(TextureRegion[] reg, float delay) {
		this.animations.add(new Animation(delay, reg) );
		this.width  = reg[0].getRegionWidth();
		this.height = reg[0].getRegionHeight();
	}
	
	/**
	 * Create an animation for each row of the sprite
	 * @param reg
	 * @param delay
	 */
	public void setAnimation(TextureRegion[][] reg, float delay) {
		for( TextureRegion[] region : reg) {
			this.animations.add(new Animation(delay, region) );
		}
		
		this.width  = reg[0][0].getRegionWidth();
		this.height = reg[0][0].getRegionHeight();
	}
	
	public void update(float dt) {
		updateAnimation(dt);
	}
	
	protected void updateAnimation(float dt) {
		stateTime += dt;
		if(animationLoop &&
				stateTime > animations.get(currentAnimation).getAnimationDuration() )
			stateTime -= animations.get(currentAnimation).getAnimationDuration();
	}
	
	public void render(SpriteBatch sb) {
		TextureRegion currentFrame = animations.get(currentAnimation).getKeyFrame(stateTime, animationLoop);
		
		sb.begin();
		sb.draw(currentFrame, 
				body.getPosition().x * PPM - width / 2.0f, 
				body.getPosition().y * PPM - height / 2.0f, 
				width / 2.0f, 
				height / 2.0f, 
				width, 
				height, 
				1.0f, 1.0f, 
				MathUtils.radiansToDegrees*body.getAngle());
		sb.end();
	}
	
	public Animation getCurrentAnimation() {
		return animations.get(currentAnimation);
	}
	
	public boolean isAnimationFinished() {
		return animations.get(currentAnimation).isAnimationFinished(stateTime);
	}
	
	public Body getBody() { return body; }
	public Vector2 getPosition() { return body.getPosition(); }
	public float getWidth() { return width; }
	public float getHeight() { return height; }
	public boolean shouldRemove() { return isShouldRemove; }

	public String getName() {
		return name;
	}
	
	public void beginContact(String name) {}
	public void endContact(String name) {}

}
