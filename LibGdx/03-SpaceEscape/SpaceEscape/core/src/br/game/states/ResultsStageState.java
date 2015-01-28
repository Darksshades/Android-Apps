package br.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import br.game.GdxGame;
import br.game.handlers.GameInputProcessor;
import br.game.handlers.GameStateManager;
import br.game.holders.GameInformation;

public class ResultsStageState extends GameState {
	
	private Stage stage;
	private Skin skin;
	private BitmapFont bfont;
	private TextureAtlas atlas;
	private Label heading;
	private Label scores;
	private TextButton buttonExit;
	private TextButton buttonAgain;
	private InputProcessor oldInput;
	private Texture background;
	private GameInformation gameinfo;

	public ResultsStageState(final GameStateManager gsm) {
		super(gsm);
		background = GdxGame.res.getTexture("game_over");
		stage = new Stage();
		oldInput = Gdx.input.getInputProcessor();
		Gdx.input.setInputProcessor(stage);
		bfont=new BitmapFont(Gdx.files.internal("default.fnt"));	
		atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
		skin = new Skin(atlas);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("default-scroll");
		textButtonStyle.down = skin.getDrawable("default-round-large");
		textButtonStyle.font = bfont;
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		
		//Create Buttons
		buttonExit = new TextButton("Make me Exit", textButtonStyle);
		buttonExit.pad(20);
		buttonExit.setSize(100*GameInputProcessor.scaleUp, 50*GameInputProcessor.scaleUp);
		buttonExit.setPosition(50*GameInputProcessor.scaleUp, 20*GameInputProcessor.scaleUp);
		buttonExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gsm.setState(GameStateManager.MENU);
			}
		});
		
		buttonAgain = new TextButton("Play Again", textButtonStyle);
		buttonAgain.pad(20);
		buttonAgain.setSize(100*GameInputProcessor.scaleUp, 50*GameInputProcessor.scaleUp);
		buttonAgain.setPosition(330*GameInputProcessor.scaleUp, 20*GameInputProcessor.scaleUp);
		buttonAgain.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if( gameinfo != null && gameinfo.getMapFile() != null) {
					gsm.message = gameinfo.getMapFile();
					gsm.setState(GameStateManager.PLAY_STAGE);
				} else { 
					gsm.setState(GameStateManager.PLAY);
				}
			}
		});
		
		LabelStyle headingStyle = new LabelStyle(bfont, Color.RED);
				
		if(gsm.message instanceof GameInformation ) {
			gameinfo = (GameInformation)gsm.message;
		}
		
		String headString = "You lose";
		int scorePt = 0;
		if(gameinfo != null) {
			scorePt = gameinfo.getScore();
			if(gameinfo.isWin())
				headString = "You win";
		} 
		
		
		String scoreString = "Score: " + scorePt;
		
		heading = new Label(headString, headingStyle);
		heading.setFontScale(4.f);
		
		bfont.setScale(4.f);
		heading.setPosition(
(GdxGame.V_WIDTH/2.f - bfont.getBounds(headString).width/3.f)*GameInputProcessor.scaleUp,
(GdxGame.V_HEIGHT - 40 - bfont.getBounds(headString).height)*GameInputProcessor.scaleUp
				);
		
		bfont.setScale(3.f);
				
		scores = new Label(scoreString, headingStyle);
		scores.setFontScale(3.f);
		scores.setPosition(
				(GdxGame.V_WIDTH/2.f - bfont.getBounds(scoreString).width/4.f)*GameInputProcessor.scaleUp,
				(GdxGame.V_HEIGHT/1.6f - bfont.getBounds(scoreString).height)*GameInputProcessor.scaleUp
				);
		
		bfont.setScale(1.f);
		
		stage.addActor(heading);
		stage.addActor(scores);
		stage.addActor(buttonExit);
		stage.addActor(buttonAgain);
		
		
//		BitmapFont bfont = new BitmapFont();
//		bfont.setScale(1.0f);
//		
//		
//		Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);
//		pixmap.setColor(Color.GREEN);
//		pixmap.fill();
//
//
//		Store the default libgdx font under the name "default".
//		skin.add("default",bfont);

		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
//		TextButtonStyle textButtonStyle = new TextButtonStyle();
//		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
//		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
//		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
//		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
//
//		textButtonStyle.font = skin.getFont("default");
//
//		skin.add("default", textButtonStyle);
//		
//		LabelStyle labelStyle = new LabelStyle();
//		labelStyle.font = bfont;
//		
//		skin.add("label", labelStyle);
//
//		// Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
//		final TextButton textButton=new TextButton("PLAY",textButtonStyle);
//		final Label label = new Label("Label", skin, "label");
//		label.setPosition(100,100);
//		textButton.setPosition(200, 200);
//		stage.addActor(textButton);
//		stage.addActor(label);
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float dt) {
		stage.act(dt);
	}

	@Override
	public void render() {
		
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.draw(background, 0, GdxGame.V_HEIGHT-background.getHeight());
		sb.end();
		

		stage.draw();
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();		
		Gdx.input.setInputProcessor(new GameInputProcessor());
		Gdx.input.setInputProcessor(oldInput);
	}

}
