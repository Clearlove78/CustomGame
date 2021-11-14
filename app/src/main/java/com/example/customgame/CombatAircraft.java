package com.example.customgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.List;

public class CombatAircraft extends Sprite {
	private boolean collide = false;
	private int bombAwardCount = 0;

	private boolean single = true;
	private int doubleTime = 0;
	private int maxDoubleTime = 140;


	private long beginFlushFrame = 0;
	private int flushTime = 0;
	private int flushFrequency = 16;
	private int maxFlushTime = 10;

	public CombatAircraft(Bitmap bitmap){
		super(bitmap);
	}

	@Override
	protected void beforeDraw(Canvas canvas, Paint paint, GameView gameView) {
		if(!isDestroyed()){
			//aircraft in canvas
			validatePosition(canvas);

			//fire bullet
			if(getFrame() % 7 == 0){
				fight(gameView);
			}
		}
	}

	private void validatePosition(Canvas canvas){
		if(getX() < 0){
			setX(0);
		}
		if(getY() < 0){
			setY(0);
		}
		RectF rectF = getRectF();
		int canvasWidth = canvas.getWidth();
		if(rectF.right > canvasWidth){
			setX(canvasWidth - getWidth());
		}
		int canvasHeight = canvas.getHeight();
		if(rectF.bottom > canvasHeight){
			setY(canvasHeight - getHeight());
		}
	}

	//fire bullet
	public void fight(GameView gameView){
		//destory
		if(collide || isDestroyed()){
			return;
		}

		float x = getX() + getWidth() / 2;
		float y = getY() - 5;
		if(single){
			//single bullet
			Bitmap yellowBulletBitmap = gameView.getYellowBulletBitmap();
			Bullet yellowBullet = new Bullet(yellowBulletBitmap);
			yellowBullet.moveTo(x, y);
			gameView.addSprite(yellowBullet);
		}
		else{
			//double bullet
			float offset = getWidth() / 4;
			float leftX = x - offset;
			float rightX = x + offset;
			Bitmap blueBulletBitmap = gameView.getBlueBulletBitmap();

			Bullet leftBlueBullet = new Bullet(blueBulletBitmap);
			leftBlueBullet.moveTo(leftX, y);
			gameView.addSprite(leftBlueBullet);

			Bullet rightBlueBullet = new Bullet(blueBulletBitmap);
			rightBlueBullet.moveTo(rightX, y);
			gameView.addSprite(rightBlueBullet);

			doubleTime++;
			if(doubleTime >= maxDoubleTime){
				single = true;
				doubleTime = 0;
			}
		}
	}

	protected void afterDraw(Canvas canvas, Paint paint, GameView gameView){
		if(isDestroyed()){
			return;
		}

		if(!collide){
			List<EnemyPlane> enemies = gameView.getAliveEnemyPlanes();
			for(EnemyPlane enemyPlane : enemies){
				Point p = getCollidePointWithOther(enemyPlane);
				if(p != null){
					//aircraft hit enemy
					explode(gameView);
					destroy();
					break;
				}
			}
		}

		//get award
		if(!collide){
			//bullet award
			List<BulletAward> bulletAwards = gameView.getAliveBulletAwards();
			for(BulletAward bulletAward : bulletAwards){
				Point p = getCollidePointWithOther(bulletAward);
				if(p != null){
					bulletAward.destroy();
					single = false;
					doubleTime = 0;
				}
			}
		}
	}

	//explode
	private void explode(GameView gameView){
		if(!collide){
			collide = true;
			setVisibility(false);
			float centerX = getX() + getWidth() / 2;
			float centerY = getY() + getHeight() / 2;
			Explosion explosion = new Explosion(gameView.getExplosionBitmap());
			explosion.centerTo(centerX, centerY);
			gameView.addSprite(explosion);
			beginFlushFrame = getFrame() + explosion.getExplodeDurationFrame();
		}
	}

	//get bomb count
	public int getBombCount(){
		return bombAwardCount;
	}

	//bomb use
	public void bomb(GameView gameView){
		if(collide || isDestroyed()){
			return;
		}

		if(bombAwardCount > 0){
			List<EnemyPlane> enemyPlanes = gameView.getAliveEnemyPlanes();
			for(EnemyPlane enemyPlane : enemyPlanes){
				enemyPlane.explode(gameView);
			}
			bombAwardCount--;
		}
	}

	public boolean isCollide(){
		return collide;
	}

	public void setNotCollide(){
		collide = false;
	}
}
