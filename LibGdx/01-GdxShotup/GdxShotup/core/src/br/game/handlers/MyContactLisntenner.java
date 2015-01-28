package br.game.handlers;

import br.game.entities.B2DSprite;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactLisntenner implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		System.out.println("Begin");
		B2DSprite s = (B2DSprite) contact.getFixtureA().getBody().getUserData();
		B2DSprite s2 = (B2DSprite) contact.getFixtureB().getBody().getUserData();
		
		s.beginContact(s2.getName());
		s2.beginContact(s.getName());
	}

	@Override
	public void endContact(Contact contact) {
		System.out.println("End");
		B2DSprite s = (B2DSprite) contact.getFixtureA().getBody().getUserData();
		B2DSprite s2 = (B2DSprite) contact.getFixtureB().getBody().getUserData();
		
		System.out.println(s);
		System.out.println(s2);
		
		s.endContact(s2.getName());
		s2.endContact(s.getName());		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
