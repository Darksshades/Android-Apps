package droidz.game.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Droidz {
	private Bitmap bitmap;
	private float x;
	private float y;
	private boolean touched;
	private int speedX = 0;
	private int speedY = 0;
	private boolean bouncy = false;
	private int 	screenW;
	
	public Droidz(Bitmap bitmap, int x, int y) {
		this.x = x;
		this.y = y;
		this.bitmap = bitmap;
	}
	
	/**
	 * Draw the Droidz on the canvas
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		if(bitmap != null)
			canvas.drawBitmap(bitmap, x- (bitmap.getWidth()/2), y - (bitmap.getHeight()/2), null);
	}
	
	/**
	 * Handle user interaction with Droidz
	 * @param eventX
	 * @param eventY
	 */
	public void handleActionDown(int eventX, int eventY) {
		if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2))) {
			if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {
				setTouched(true);
			}else {
				setTouched(false);
			}
		} else { setTouched(false);}
	}
	
	public void update(float delta) {
		float incrementX = speedX * (float)(delta);
		float incrementY = speedY * (float)(delta);
		//Log.i("Droidz increment", "x+" + incrementX + " Speed added.");
		//Log.i("Droidz increment", "y+" + incrementY + " Speed added.");
		x += incrementX;
		y += incrementY;
		
		if(x > screenW-bitmap.getWidth()/2) {
			x = screenW - bitmap.getWidth()/2;
			if(bouncy) {
				speedX = -speedX;
			} else {
				speedX = 0;
			}
		} else
		if(x < bitmap.getWidth()/2) {
			x = bitmap.getWidth()/2;
			if(bouncy) {
				speedX = -speedX;
			} else {
				speedX = 0;
			}
		}
	}

	/* Getters and Setters */
	
	public void setBouncy(boolean bouncy) {
		this.bouncy = bouncy;
	}
	
	public void setScreenWidth(int width) {
		this.screenW = width;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getX() {
		return (int)Math.floor(x);
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int)Math.floor(y);
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return bitmap.getWidth();
	}
	
	public int getHeight() {
		return bitmap.getHeight();
	}

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}
	
	
}
