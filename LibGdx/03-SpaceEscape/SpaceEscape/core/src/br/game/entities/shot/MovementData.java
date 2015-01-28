package br.game.entities.shot;

import com.badlogic.gdx.math.MathUtils;

public class MovementData {
	
	public MovementData(float delay, float velocity, float angleDegrees, float curvature, boolean targetPlayer){	
		this(delay, velocity, angleDegrees, curvature, targetPlayer, 0, 0);
	}
	
	public MovementData(float delay, float velocity, float angleDegrees, float curvature, boolean targetPlayer, float relativeVelocityX, float relativeVelocityY){
		this.delay = delay;
		this.velocity = velocity;
		if(!targetPlayer)
			this.angle = (angleDegrees-180)*MathUtils.degreesToRadians;
		else
			this.angle = angleDegrees*MathUtils.degreesToRadians;
		this.curvature = curvature;
		this.targetPlayer = targetPlayer;
		this.relativeVelocityX = relativeVelocityX;
		this.relativeVelocityY = relativeVelocityY;
	}

	public float angle;
	public float curvature;
	public float delay;
	public float velocity;
	public float relativeVelocityX;
	public float relativeVelocityY;
	public boolean targetPlayer;
	//public int 	 id; // Latter for changing graphics through a factory;
}
