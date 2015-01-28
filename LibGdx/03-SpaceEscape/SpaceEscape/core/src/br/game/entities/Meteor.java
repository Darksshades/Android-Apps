package br.game.entities;

import br.game.GdxGame;
import br.game.handlers.B2DVars;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Meteor extends B2DSprite{
	
	private int hp = 1;
	private short id = 0;
	private boolean m_isExploding = false;
	
	public static final short SMALL = 1;
	public static final short SMALL_2 = 2;
	public static final short MEDIUM = 3;
	public static final short BIG = 4;
	public static final short LARGE = 5;
	public static final short MASSIVE = 6;
	

	public Meteor(Body body, short id) {
		super(body);
		body.setUserData(this);
		name = "meteor";
		
		this.id = id;
		
		changeSize();
	}

	private void changeSize() {
		hp = 1;
		Texture tex = null;
		if(id == 0){
			tex = GdxGame.res.getTexture("meteor");
		} else
		if(id == SMALL) {
			tex = GdxGame.res.getTexture("meteor_1");
		}else
		if(id == SMALL_2) {
			tex = GdxGame.res.getTexture("meteor_2");
		}else
		if(id == MEDIUM) {
			tex = GdxGame.res.getTexture("meteor_3");
		}else
		if(id == BIG) {
			tex = GdxGame.res.getTexture("meteor_4");
		}else
		if(id == LARGE) {
			tex = GdxGame.res.getTexture("meteor_5");
		}else
		if(id == MASSIVE) {
			tex = GdxGame.res.getTexture("meteor_6");
		}
		
		body.getFixtureList().get(0).getShape().setRadius( tex.getWidth()/2.f/B2DVars.PPM);
		TextureRegion[][] reg = TextureRegion.split(tex, tex.getWidth(), tex.getHeight());
		
		setAnimation(reg, 0.1f);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		if(body.getPosition().x*B2DVars.PPM < GdxGame.cam.position.x-GdxGame.V_WIDTH/2.f  ) { 
			isShouldRemove = true;
		} else
		if(body.getPosition().y + height/2.f/B2DVars.PPM< 0 || body.getPosition().y > -height/2.f/B2DVars.PPM + GdxGame.V_HEIGHT/B2DVars.PPM) {
			body.setLinearVelocity(body.getLinearVelocity().x, -body.getLinearVelocity().y);
		}
	}
	
	@Override
	public void beginContact(String name) {
		if(name == "shot") {
			hp--;
			if(hp <= 0){
				id--;
				if(id <= 0) {
					isShouldRemove = true;
					hp = 0;
				} else {
					explode();
				}
				
			}
		}
	}

	private void explode() {
		changeSize();
		m_isExploding  = true;
	}
	
	public short getType() { return id; }
	
	public void setHP(int hp) {
		this.hp = hp;
	}
	
	public int getHP() { return hp; }

	public boolean isExploding() { return m_isExploding; }
	
	public void setExploding(boolean exploding) { this.m_isExploding = exploding; }

}
