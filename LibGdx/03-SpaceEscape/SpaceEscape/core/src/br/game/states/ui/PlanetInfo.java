package br.game.states.ui;

import br.game.GdxGame;
import br.game.entities.B2DSprite;
import br.game.handlers.B2DVars;
import br.game.handlers.GameInputProcessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class PlanetInfo extends B2DSprite{
	public PlanetInfo(Body body, float size, String title, String text, String loadstage) {
		super(body);
		this.title = title;
		this.text = text;
		this.scale = size;
		this.loadstage = loadstage;
		
		body.setAngularVelocity(MathUtils.random(-0.6f, 0.6f));
		
		changeTexture();
		
		
	}
	
	public void setCanPlay(boolean play) { this.canPlay = play; } 

	public String title;
	public String text;
	
	public boolean isSelected() {
		return selected;
	}
	
	boolean selected = false;
	boolean canPlay = true;
	private int nContacts = 0;
	
	ShapeRenderer shapeRenderer = new ShapeRenderer();
	public String loadstage;

	public void render(SpriteBatch sb, BitmapFont font) {
		if(canPlay) currentAnimation = 1;
		
		this.render(sb);
		
		if( !selected || !canPlay) return;
		
		
		
		sb.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0.5f, 0, 0.5f, 0.8f);
		shapeRenderer.identity();
		shapeRenderer.rect(
				(-width*scale/2.f + body.getPosition().x*B2DVars.PPM)*GameInputProcessor.scaleUp, 
				(-height*scale/2.f + body.getPosition().y*B2DVars.PPM)*GameInputProcessor.scaleUp, 
				width*scale*GameInputProcessor.scaleUp, height*scale*GameInputProcessor.scaleUp);
		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
		sb.begin();
		
		font.setScale(1.0f);
		font.draw(sb, title, 
				body.getPosition().x*B2DVars.PPM - font.getBounds(title).width/2.f, 
				body.getPosition().y*B2DVars.PPM + height*scale/2.f);
		font.setScale(0.9f);
		font.draw(sb, text, 
				body.getPosition().x*B2DVars.PPM - font.getBounds(text).width/2.f, 
				body.getPosition().y*B2DVars.PPM);
		
	}
	
	private void changeTexture() {
		Texture tex = GdxGame.res.getTexture("planet");
		
		body.getFixtureList().get(0).setUserData("center");
		body.getFixtureList().get(0).getShape().setRadius( scale*tex.getWidth()/2.f/B2DVars.PPM);
		body.getFixtureList().get(1).getShape().setRadius( scale*tex.getWidth()/1.6f/B2DVars.PPM);
		TextureRegion[][] reg = TextureRegion.split(tex, tex.getWidth(), (int)(tex.getHeight()/2.f) );
		
		animationLoop = false;
		
		setAnimation(reg, 0.1f);
	}
	
	@Override
	public void beginContact(String name) {
		nContacts++;
		selected = true;
	}
	
	@Override
	public void endContact(String name) {
		nContacts--;
		if(nContacts <= 0)
			selected = false;
	}

	public boolean isActive() {
		return selected && canPlay;
	}

}
