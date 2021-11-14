package com.example.customgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.List;

/**
 * 敌机类，从上向下沿直线运动
 */
public class EnemyPlane extends AutoSprite {

	private int power = 1;//enemy power
	private int value = 0;//score

	public EnemyPlane(Bitmap bitmap){
		super(bitmap);
	}

	public void setPower(int power){
		this.power = power;
	}

	public int getPower(){
		return power;
	}

	public void setValue(int value){
		this.value = value;
	}

	public int getValue(){
		return value;
	}

	@Override
	protected void afterDraw(Canvas canvas, Paint paint, GameView gameView) {
		super.afterDraw(canvas, paint, gameView);

		//bullet hit enemy
		if(!isDestroyed()){

			List<Bullet> bullets = gameView.getAliveBullets();
			for(Bullet bullet : bullets){
				//bullet and enemy in same location
				Point p = getCollidePointWithOther(bullet);
				if(p != null){
					//destroy when hit
					bullet.destroy();
					power--;
					if(power <= 0){
						//explode
						explode(gameView);
						return;
					}
				}
			}
		}
	}

	//explode enemy aircraft
	public void explode(GameView gameView){
		//explode
		float centerX = getX() + getWidth() / 2;
		float centerY = getY() + getHeight() / 2;
		Bitmap bitmap = gameView.getExplosionBitmap();
		Explosion explosion = new Explosion(bitmap);
		explosion.centerTo(centerX, centerY);
		gameView.addSprite(explosion);

		//add score
		gameView.addScore(value);
		destroy();
	}
}
