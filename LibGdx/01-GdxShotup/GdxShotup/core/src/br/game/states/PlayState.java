package br.game.states;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import br.game.GdxGame;
import static br.game.handlers.B2DVars.PPM;
import br.game.entities.Cave;
import br.game.entities.City;
import br.game.entities.Player;
import br.game.entities.Shot;
import br.game.entities.Slimy;
import br.game.handlers.B2DVars;
import br.game.handlers.GameInput;
import br.game.handlers.GameStateManager;
import br.game.handlers.MyContactLisntenner;

public class PlayState extends GameState {
	
	private BitmapFont font = new BitmapFont();
	
	private World world;
	private Box2DDebugRenderer b2dr;
	private boolean isDrawDebugRender = true;
	
	private Texture background;
	
	private OrthographicCamera b2dCam;
	
	private ArrayList<Shot> shots;
	private ArrayList<Slimy> slimys;
	private ArrayList<City> citys;
	private ArrayList<Cave> caves;
	private Player player;
	
	private float targetX, targetY;
	
	private float deadTimer = 0;
	private float deadTimerLimit = 2.5f;
	private boolean isDeadTimer = false;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		
		shots = new ArrayList<Shot>();
		slimys= new ArrayList<Slimy>();
		citys = new ArrayList<City>();
		caves = new ArrayList<Cave>();
		
		background = GdxGame.res.getTexture("background");
		
		world = new World( new Vector2(0.0f, 0.0f) , true);
		world.setContactListener(new MyContactLisntenner());
		b2dr  = new Box2DDebugRenderer();
		b2dr.setDrawContacts(true);
		
		createPlayer();
		createCitys();
		createCaves();
		
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, GdxGame.V_WIDTH / PPM, GdxGame.V_HEIGHT / PPM);
	}
	
	private void createCaves() {
		BodyDef bdef = new BodyDef();
		Body body;
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Cave c;
		shape.setAsBox( (16*1.5f) / PPM, 16 / PPM );
		fdef.shape = shape;
		bdef.type = BodyType.StaticBody;
		
		//Cave 1
		bdef.position.set(32 / PPM, (16*10) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new Cave(body);
		caves.add(c);
		//Cave 2
		bdef.position.set( (32 + 6*16) / PPM, (16*10) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new Cave(body);
		caves.add(c);
		//Cave 3
		bdef.position.set( (32 + 12*16)/ PPM, (16*10) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new Cave(body);
		caves.add(c);
	}
	
	private void createCitys() {
		BodyDef bdef = new BodyDef();
		Body body;
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		City c;
		fdef.isSensor = true;
		
		shape.setAsBox( 32 / PPM, 6 / PPM );
		fdef.shape = shape;
		bdef.type = BodyType.StaticBody;
		fdef.filter.categoryBits = B2DVars.BIT_HOUSE;
		fdef.filter.maskBits 	 = B2DVars.BIT_ENEMY;
		
		//City 1
		bdef.position.set(32 / PPM, (16*1) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new City(body);
		citys.add(c);
		//City 2
		bdef.position.set( (32 + 6*16) / PPM, (16*1) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new City(body);
		citys.add(c);
		//City 3
		bdef.position.set( (32 + 12*16)/ PPM, (16*1) / PPM);
		body = world.createBody(bdef);
		body.createFixture(fdef);		
		c = new City(body);
		citys.add(c);
	}

	private void createPlayer() {
		BodyDef bdef = new BodyDef();
		Body body;
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		
		// create player
		bdef.position.set(70 / PPM, 50 / PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(3.5f / PPM, 6 / PPM, new Vector2(0, -2 / PPM), 0);
		fdef.shape = shape;
		body.createFixture(fdef);
		
		player = new Player(body);
		player.getCurrentAnimation().setFrameDuration(0.2f);
		body.setLinearVelocity(1, 0);
	}

	private Shot createShot(float x, float y) {
		BodyDef bdef = new BodyDef();
		bdef.position.set(x, y);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		FixtureDef fdef = new FixtureDef();
		fdef.isSensor = true;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(6 / PPM, 6 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_SHOT;
		fdef.filter.maskBits 	 = B2DVars.BIT_ENEMY;
		body.createFixture(fdef);
		
		Shot s = new Shot(body);
		
		shots.add(s);
		
		return s;
	}
	
	private Slimy createSlimy(float x, float y) {
		BodyDef bdef = new BodyDef();
		bdef.position.set(x, y);
		bdef.type = BodyType.DynamicBody;

		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(7 / PPM, 4 / PPM, new Vector2(0, -3.f/PPM), 0);
		FixtureDef fdef = new FixtureDef();
		fdef.isSensor = true;
		fdef.shape = shape;
		fdef.filter.categoryBits = B2DVars.BIT_ENEMY;
		fdef.filter.maskBits 	 = B2DVars.BIT_HOUSE | B2DVars.BIT_SHOT;
		body.createFixture(fdef);
		
		Slimy s = new Slimy(body);
		
		slimys.add(s);
		
		return s;
	}

	@Override
	public void handleInput() {
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			if(GameInput.isDown(Input.Keys.ENTER) ) {
				isDrawDebugRender = !isDrawDebugRender;
			}
		}
		
		if(player.getState() == Player.ATTACK &&
			player.isAnimationFinished()  ) {
			Shot s = createShot(player.getPosition().x, player.getPosition().y);
			player.setState(Player.STAND);
			
			Vector2 force = new Vector2(targetX - player.getPosition().x, player.getPosition().y - targetY);
			force.nor();
			
			s.getBody().setLinearVelocity(force.x, -force.y);
			
			
		}
		
		if(GameInput.isDown() && player.getState() == Player.STAND) {
			targetX = GameInput.x / PPM;
			targetY = (GdxGame.V_HEIGHT - GameInput.y) / PPM;
			
			player.playAttackAnimation();
			player.setState(Player.ATTACK);
		}
		
	}

	@Override
	public void update(float dt) {
		
		if(isDeadTimer) {
			deadTimer+=dt;
			if(deadTimer > deadTimerLimit) 
				gsm.setState(GameStateManager.MENU);
			
			return;
		}
		
		// check input
		handleInput();
		
		world.step(dt, 6, 2);
		
		for(Cave c 		: caves) {
			c.update(dt);
			if(c.isAnimationFinished()) {
				Slimy s = createSlimy(c.getPosition().x, c.getPosition().y);
				s.getBody().setLinearVelocity(0, -1);
				c.resetAnimation();
			}
		}
		int destroyed = 0;
		for(City city 	: citys) {
			city.update(dt);
			if(city.getState() == City.DEAD)
				destroyed++;
		}
		
		if(destroyed >= citys.size())
			isDeadTimer = true;
		
		for(int i = 0; i < slimys.size(); i++) {
			slimys.get(i).update(dt);
			if(slimys.get(i).shouldRemove()) {
				world.destroyBody(slimys.get(i).getBody());
				slimys.remove(i);
				i--;
				continue;
			}

			if(slimys.get(i).getPosition().y < 0) {
				world.destroyBody(slimys.get(i).getBody());
				slimys.remove(i);
				i--;
				continue;
			}
		}
		for(Shot s 		: shots) 	s.update(dt);
		
		player.update(dt);
		
		if(player.getPosition().x < 1)
			player.getBody().setLinearVelocity(1, 0);
		else if(player.getPosition().x > 7)
			player.getBody().setLinearVelocity(-1, 0);
		
		
	}

	@Override
	public void render() {
		// background draw
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.draw(background, 0, 0);
		sb.end();
		
		// HUD draw
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		String fontText = "Play State";
		font.draw(sb, fontText, GdxGame.V_WIDTH/2.f - font.getBounds(fontText).width/2, GdxGame.V_HEIGHT );
		sb.end();
		
		// draw objects
		sb.setProjectionMatrix(cam.combined);
		for(Cave c 		: caves) 	c.render(sb);
		for(City city 	: citys) 	city.render(sb);
		for(Slimy s 	: slimys) 	s.render(sb);
		for(Shot s 		: shots) 	s.render(sb);
		
		player.render(sb);
		
		
		
		// Debug draw
		if(isDrawDebugRender)
			b2dr.render(world, b2dCam.combined);
		
	}

	@Override
	public void dispose() {
		
	}

}

