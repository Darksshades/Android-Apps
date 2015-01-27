package droidz.game;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import droidz.game.data.Droidz;
import droidz.helloworld.R;

public class DroidzHabitatPanel extends SurfaceView implements
	SurfaceHolder.Callback{
	
	private static final String TAG = DroidzHabitatPanel.class.getSimpleName();
	
	private MainDroidzThread thread;
	private Droidz droid;
	private Droidz droidVirus;
	private int    score;
	private float  difficulty;
	Paint paint = new Paint(); 
	
	
	final Handler mHandler = new Handler();
	
	private Random generator = new Random();
	
	private void initContent(Context context) {
		//Intercepts Events
		getHolder().addCallback(this);
		
		droid = new Droidz(BitmapFactory.decodeResource(getResources(), R.drawable.droidz),50,50);
		droidVirus = new Droidz(BitmapFactory.decodeResource(getResources(), R.drawable.virus),0,0);
				
		thread = new MainDroidzThread(getHolder(), this);
		score  = 0;
		difficulty = 1.f;
		
		//Text
		paint.setColor(Color.WHITE); 
		paint.setTextSize(20); 
		
		//Set focusable to be able to intercept events
		setFocusable(true);
	}
	
	public DroidzHabitatPanel(Context context) {
		super(context);
		initContent(context);
	}
	
	public DroidzHabitatPanel(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		initContent(context);
	}
	
	public DroidzHabitatPanel(Context context, AttributeSet attributeSet, int defStyles) {
		super(context, attributeSet, defStyles);
		initContent(context);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//Log.d(TAG, "onDraw called");
		super.onDraw(canvas);
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		droid.draw(canvas);
		droidVirus.draw(canvas);
		canvas.drawText("Score: " + score + ", Difficulty: " + difficulty, getWidth()-300, 50, paint);
		//super.draw(canvas);
	}
	
	public void update(float deltaTime) {
		droid.update(deltaTime);
		
		if(isCollisionDroids(droid, droidVirus) ) {
			Log.i(TAG, "Collided!");
			droid.setX(generator.nextInt(getWidth() - droidVirus.getWidth()));
			droid.setY(0);
			int initialSpeed = (int)(10*difficulty*difficulty) + 
					(int)(generator.nextInt(100 + (int)getHeight()/10)*difficulty);
			
			int maxSpeed = (int)(getHeight()-getHeight()*0.3f);
			if(initialSpeed > maxSpeed ) initialSpeed = (int)maxSpeed; 
			
			droid.setSpeedY(initialSpeed);
			
			//Increase score, before difficulty
			score 	   += 10 + (int)Math.floor(10*(difficulty-1.f) );
			
			if(difficulty > 1.5f) {
				//Starts 40 - 140
				int speedX = (int)(10*difficulty*difficulty) + 
						(int)(generator.nextInt((int)(50*difficulty) ) );
				int dir = generator.nextInt(2);
				speedX = speedX > getWidth()*0.7? (int)(getWidth()*0.7): speedX;
				speedX = dir == 1? speedX : -speedX;
				droid.setSpeedX(speedX);
				
				if(difficulty > 2.5f) {
					//Starts with 66% of beeing bouncy, increases
					int bounce = generator.nextInt( 1 + (int)(difficulty) );
					boolean doBounce = bounce == 1? false : true;
					droid.setBouncy(doBounce);
				}
			}
			
			//Increase difficulty
			difficulty += 0.05f;
			
			droidVirus.setTouched(false);
			droidVirus.setX((int)(getWidth()/2.f - droidVirus.getWidth()/2.f));
			droidVirus.setY(getHeight() - droidVirus.getHeight() - 50);
			
			Log.i(TAG, "Speed: " + initialSpeed);
			//For Toast
//			final int speed = initialSpeed;
//			((Activity)getContext()).runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					final Toast t = Toast.makeText(((Activity)getContext()), "Speed: " + speed, Toast.LENGTH_SHORT);
//					t.show();
//			});
			
		
		} 
		
		final int finalScore = score;
		//If droid go below screen, lose
		if(droid.getY() > getHeight() - 50) {
			((Activity)getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast t = Toast.makeText(((Activity)getContext()), "You Lose: " + finalScore, Toast.LENGTH_LONG);
					t.show();
				}
			});
			thread.setRunning(false);
			((Activity)getContext()).finish();
		}
		
	}
	
	protected boolean isCollisionDroids(Droidz d1, Droidz d2) {
		int top1 = d1.getY();
		int left1 = d1.getX();
		int right1 = d1.getX() + d1.getWidth();
		int bottom1 = d1.getY() + d1.getHeight();
		
		int top2 = d2.getY();
		int left2 = d2.getX();
		int right2 = d2.getX() + d2.getWidth();
		int bottom2 = d2.getY() + d2.getHeight();
		
		if( top1 > bottom2 ||
			right1 < left2 ||
			left1  > right2||
			bottom1 < top2 )
				return false;
			else
				return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
		droidVirus.setTouched(false);
		droidVirus.setX((int)(getWidth()/2.f - droidVirus.getWidth()/2.f));
		droidVirus.setY(getHeight() - droidVirus.getHeight() - 50);
		
		
		droid.setScreenWidth(getWidth());
		droid.setX(generator.nextInt(getWidth() - droidVirus.getWidth()));		
		int initialSpeed = generator.nextInt(50 + (int)getHeight()/10);
		droid.setSpeedY(initialSpeed);
		
		Log.i(TAG, "Width: " + getWidth() + " height: " + getHeight());		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread.setRunning(false);
		boolean retry = true;
		while(retry) {
			try {
				thread.join();
				retry = false;
			} catch(InterruptedException e) {
				//Try again, shutting down thread
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			droidVirus.handleActionDown((int)event.getX(), (int)event.getY());
			
			Log.d(TAG, "Coods: x= " + event.getX() + ", y= " + event.getY());
		}
		
		if(event.getAction() == MotionEvent.ACTION_MOVE) {
			if(droidVirus.isTouched()) {
				droidVirus.setX((int)event.getX());
				droidVirus.setY((int)event.getY());
			}
		}
		if(event.getAction() == MotionEvent.ACTION_UP) {
			if(droidVirus.isTouched()) {
				droidVirus.setTouched(false);
			}
		}
		
		return true;
	}

}
