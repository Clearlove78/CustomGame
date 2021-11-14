
package com.example.customgame;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class GameView extends View {

	private Paint paint;
	private Paint textPaint;
	private Paint textPaint2;
	private CombatAircraft combatAircraft = null;
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private List<Sprite> spritesNeedAdded = new ArrayList<Sprite>();
	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private float density = getResources().getDisplayMetrics().density;//screen density
	public static final int STATUS_GAME_STARTED = 1;//start
	public static final int STATUS_GAME_PAUSED = 2;//pause
	public static final int STATUS_GAME_OVER = 3;//over
	public static final int STATUS_GAME_DESTROYED = 4;//destroy
	public static final int STATUS_GAME_Finish = 5;//finish
	private int status = STATUS_GAME_DESTROYED;//destroy status
	private long frame = 0;//frame
	private long score = 0;//score
	private float fontSize = 20;
	private float fontSize2 = 20;
	private float borderSize = 2;
	private Rect continueRect = new Rect();//Rect
	private static final int TOUCH_MOVE = 1;//move
	private static final int TOUCH_SINGLE_CLICK = 2;//single click
	private static final int TOUCH_DOUBLE_CLICK = 3;//double click
	//200ms
	private static final int singleClickDurationTime = 200;
	//300ms
	private static final int doubleClickDurationTime = 300;
	private long lastSingleClickTime = -1;
	private long touchDownTime = -1;
	private long touchUpTime = -1;
	private float touchX = -1;
	private float touchY = -1;
	private int x = 0;
	private int y = -2720;
	private int x1 = 0;
	private int y1 = -2100;
	private Bitmap back;
	private Bitmap back2;


	public GameView(Context context) {
		super(context);
		init(null, 0);
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		final TypedArray a = getContext().obtainStyledAttributes(
				attrs, R.styleable.GameView, defStyle, 0);
		a.recycle();
		//new paint
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		//set text
		textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
		textPaint.setColor(0xff000000);
		textPaint2 = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
		textPaint2.setColor(Color.rgb(255,255,255));
		fontSize *= density;
		fontSize2 *= density;
		textPaint.setTextSize(fontSize);
		textPaint2.setTextSize(fontSize);
		borderSize *= density;
	}

	public void start(int[] bitmapIds){
		destroy();
		for(int bitmapId : bitmapIds){
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapId);
			bitmaps.add(bitmap);
		}
		startWhenBitmapsReady();
	}

	private void startWhenBitmapsReady(){
		combatAircraft = new CombatAircraft(bitmaps.get(0));
		status = STATUS_GAME_STARTED;
		postInvalidate();
	}

	public void finish(){
		destroyNotRecyleBitmaps();
	}

	private void restart(){
		destroyNotRecyleBitmaps();
		startWhenBitmapsReady();
	}

	public void pause(){
		//pause
		status = STATUS_GAME_PAUSED;
	}

	private void resume(){
		//resume
		status = STATUS_GAME_STARTED;
		postInvalidate();
	}

	private long getScore(){
		//get score
		return score;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		onSingleClick(touchX, touchY);
		//background image recursion
		if(End.level==1) {
			back = BitmapFactory.decodeResource(getResources(), R.drawable.aircraft);
			logic();
		}
		if(End.level==2) {
			back = BitmapFactory.decodeResource(getResources(), R.drawable.background);
			logic();
		}
		if(End.level==3) {
			back = BitmapFactory.decodeResource(getResources(), R.drawable.backimage3);
			logic();
		}
		if(End.level==4) {
			back = BitmapFactory.decodeResource(getResources(), R.drawable.backimage4);
			logic();
		}
		canvas.drawBitmap(back,0, x,null);
		canvas.drawBitmap(back,0, y,null);
		super.onDraw(canvas);
		if(End.level == 1) {
			if (score > 1000) {
				status = STATUS_GAME_Finish;
			}
		}
		if(End.level == 2) {
			if (score > 2000) {
				status = STATUS_GAME_Finish;
			}
		}
		if(End.level == 3) {
			if (score > 3000) {
				status = STATUS_GAME_Finish;
			}
		}
		if(End.level == 4) {
			if (score > 4000) {
				status = STATUS_GAME_Finish;
			}
		}
		if(End.level == 5) {
			if (score > 5000) {
				status = STATUS_GAME_Finish;
			}
		}
		if (status == STATUS_GAME_STARTED) {
			drawGameStarted(canvas);
		} else if (status == STATUS_GAME_PAUSED) {
			drawGamePaused(canvas);
		} else if (status == STATUS_GAME_OVER) {
			drawGameOver(canvas);
		} else if (status == STATUS_GAME_Finish) {
			drawGameFinish(canvas);
		}
	}
	//image recursion x y
	public void logic(){
		x+=10;
		y+=10;
		if(x>=back.getHeight()){
			x =  y - back.getHeight();
		}
		if(y>=back.getHeight()){
			y =  x - back.getHeight();

		}
	}



	private void drawGameStarted(Canvas canvas){

		drawScore(canvas);

		//set aircraft location
 		if(frame == 0){
			float centerX = canvas.getWidth() / 2;
			float centerY = canvas.getHeight() - combatAircraft.getHeight() / 2;
			combatAircraft.centerTo(centerX, centerY);
		}

		//add spritesNeedAdded to sprites
		if(spritesNeedAdded.size() > 0){
			sprites.addAll(spritesNeedAdded);
			spritesNeedAdded.clear();
		}

		//destroy bullet when hit enemy
		destroyBulletsFrontOfCombatAircraft();

		//remove destroyed Sprite
		removeDestroyedSprites();

		//random add sprites
		if(frame % 30 == 0){
			createRandomSprites(canvas.getWidth());
		}
		frame++;

		//sprites, bullet
		Iterator<Sprite> iterator = sprites.iterator();
		while (iterator.hasNext()){
			Sprite s = iterator.next();
			if(!s.isDestroyed()){
				s.draw(canvas, paint, this);
			}
			if(s.isDestroyed()){
				iterator.remove();
			}
		}

		if(combatAircraft != null){
			//aircraft
			combatAircraft.draw(canvas, paint, this);
			if(combatAircraft.isDestroyed()){
				//game over
				status = STATUS_GAME_OVER;
			}
			postInvalidate();
		}
	}

	//pause
	private void drawGamePaused(Canvas canvas){
		drawScore(canvas);

		for(Sprite s : sprites){
			s.onDraw(canvas, paint, this);
		}
		if(combatAircraft != null){
			combatAircraft.onDraw(canvas, paint, this);
		}

		//Dialog and score
		drawScoreDialog(canvas, "Continue");

		if(lastSingleClickTime > 0){
			postInvalidate();
		}
	}

	//game over
	private void drawGameOver(Canvas canvas){
		//Game Over and score
		drawScoreDialog(canvas, "Restart");
		if(lastSingleClickTime > 0){
			postInvalidate();
		}
	}

	//finish
	private void drawGameFinish(Canvas canvas){
		drawScoreDialog(canvas, "Next level");
	}

	private void drawScoreDialog(Canvas canvas, String operation){
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();
		float originalFontSize = textPaint.getTextSize();
		Paint.Align originalFontAlign = textPaint.getTextAlign();
		int originalColor = paint.getColor();
		Paint.Style originalStyle = paint.getStyle();
		int w1 = (int)(20.0 / 360.0 * canvasWidth);
		int w2 = canvasWidth - 2 * w1;
		int buttonWidth = (int)(140.0 / 360.0 * canvasWidth);

		int h1 = (int)(150.0 / 558.0 * canvasHeight);
		int h2 = (int)(60.0 / 558.0 * canvasHeight);
		int h3 = (int)(124.0 / 558.0 * canvasHeight);
		int h4 = (int)(76.0 / 558.0 * canvasHeight);
		int buttonHeight = (int)(42.0 / 558.0 * canvasHeight);

		canvas.translate(w1, h1);
		//background
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(0xFFD7DDDE);
		Rect rect1 = new Rect(0, 0, w2, canvasHeight - 2 * h1);
		canvas.drawRect(rect1, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(0xFF515151);
		paint.setStrokeWidth(borderSize);
		//paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		canvas.drawRect(rect1, paint);
		//score
		textPaint.setTextSize(fontSize2);
		textPaint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText("Score:", w2 / 2, (h2 - fontSize2) / 2 + fontSize2, textPaint);
		canvas.translate(0, h2);
		canvas.drawLine(0, 0, w2, 0, paint);
		//score
		String allScore = String.valueOf(getScore());
		canvas.drawText(allScore, w2 / 2, (h3 - fontSize2) / 2 + fontSize2, textPaint);
		canvas.translate(0, h3);
		canvas.drawLine(0, 0, w2, 0, paint);
		//button
		Rect rect2 = new Rect();
		rect2.left = (w2 - buttonWidth) / 2;
		rect2.right = w2 - rect2.left;
		rect2.top = (h4 - buttonHeight) / 2;
		rect2.bottom = h4 - rect2.top;
		canvas.drawRect(rect2, paint);
		//continue and restart
		canvas.translate(0, rect2.top);
		canvas.drawText(operation, w2 / 2, (buttonHeight - fontSize2) / 2 + fontSize2, textPaint);
		continueRect = new Rect(rect2);
		continueRect.left = w1 + rect2.left;
		continueRect.right = continueRect.left + buttonWidth;
		continueRect.top = h1 + h2 + h3 + rect2.top;
		continueRect.bottom = continueRect.top + buttonHeight;

		//reset
		textPaint.setTextSize(originalFontSize);
		textPaint.setTextAlign(originalFontAlign);
		paint.setColor(originalColor);
		paint.setStyle(originalStyle);
	}

	//score
	private void drawScore(Canvas canvas){
		//pause
		Bitmap pauseBitmap = status == STATUS_GAME_STARTED ? bitmaps.get(8) : bitmaps.get(9);
		RectF pauseBitmapDstRecF = getPauseBitmapDstRecF();
		float pauseLeft = pauseBitmapDstRecF.left;
		float pauseTop = pauseBitmapDstRecF.top;
		canvas.drawBitmap(pauseBitmap, pauseLeft, pauseTop, paint);
		//score
		float scoreLeft = pauseLeft + pauseBitmap.getWidth() + 20 * density;
		float scoreTop = fontSize + pauseTop + pauseBitmap.getHeight() / 2 - fontSize / 2;
		canvas.drawText(score + "", scoreLeft, scoreTop, textPaint2);
		if(End.level==1) {
			canvas.drawText("Level: 1", 800, scoreTop, textPaint2);
		}
		if(End.level==2) {
			canvas.drawText("Level: 2", 800, scoreTop, textPaint2);
		}
		if(End.level==3) {
			canvas.drawText("Level: 3", 800, scoreTop, textPaint2);
		}
		if(End.level==4) {
			canvas.drawText("Level: 4", 800, scoreTop, textPaint2);
		}
		if(End.level==5) {
			canvas.drawText("Level: 5", 800, scoreTop, textPaint2);
		}
	}

	private void destroyBulletsFrontOfCombatAircraft(){
		if(combatAircraft != null){
			float aircraftY = combatAircraft.getY();
			List<Bullet> aliveBullets = getAliveBullets();
			for(Bullet bullet : aliveBullets){
				if(aircraftY <= bullet.getY()){
					bullet.destroy();
				}
			}
		}
	}

	private void removeDestroyedSprites(){
		Iterator<Sprite> iterator = sprites.iterator();
		while (iterator.hasNext()){
			Sprite s = iterator.next();
			if(s.isDestroyed()){
				iterator.remove();
			}
		}
	}

	//generate Sprite
	private void createRandomSprites(int canvasWidth){
		Sprite sprite = null;
		int speed = 2;
		int callTime = Math.round(frame / 30);
		if((callTime + 1) % 25 == 0){
			//bullet award
			sprite = new BulletAward(bitmaps.get(8));
		}
		else{
			int[] nums = {0,0,0,0,0,1,0,0,1,0,0,0,0,1,1,1,1,1,1,2};
			int index = (int)Math.floor(nums.length*Math.random());
			int type = nums[index];
			if(type == 0){
				//small enemy
				sprite = new SmallEnemyPlane(bitmaps.get(4));
			}
			else if(type == 1){
				//middle enemy
				sprite = new MiddleEnemyPlane(bitmaps.get(5));
			}
			else if(type == 2){
				//big enemy
				sprite = new BigEnemyPlane(bitmaps.get(6));
			}
			if(type != 2){
				if(Math.random() < 0.33){
					speed = 4;
				}
			}
		}

		if(sprite != null){
			float spriteWidth = sprite.getWidth();
			float spriteHeight = sprite.getHeight();
			float x = (float)((canvasWidth - spriteWidth)*Math.random());
			float y = -spriteHeight;
			sprite.setX(x);
			sprite.setY(y);
			if(sprite instanceof AutoSprite){
				AutoSprite autoSprite = (AutoSprite)sprite;
				autoSprite.setSpeed(speed);
			}
			addSprite(sprite);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		int touchType = resolveTouchType(event);
		if(status == STATUS_GAME_STARTED){
			if(touchType == TOUCH_MOVE){
				if(combatAircraft != null){
					combatAircraft.centerTo(touchX, touchY);
				}
			}
		}else if(status == STATUS_GAME_PAUSED){
			if(lastSingleClickTime > 0){
				postInvalidate();
			}
		}else if(status == STATUS_GAME_OVER){
			if(lastSingleClickTime > 0){
				postInvalidate();
			}
		} else if(status == STATUS_GAME_Finish){
			if(lastSingleClickTime > 0){
				Intent intent=new Intent(getContext(), End.class);
				getContext().startActivity(intent);
			}
		}
		return true;
	}

	private int resolveTouchType(MotionEvent event){
		int touchType = -1;
		int action = event.getAction();
		touchX = event.getX();
		touchY = event.getY();
		if(action == MotionEvent.ACTION_MOVE){
			long deltaTime = System.currentTimeMillis() - touchDownTime;
			if(deltaTime > singleClickDurationTime){
				//Move touch point
				touchType = TOUCH_MOVE;
			}
		}else if(action == MotionEvent.ACTION_DOWN){
			//touch down
			touchDownTime = System.currentTimeMillis();
		}else if(action == MotionEvent.ACTION_UP){
			//touch up
			touchUpTime = System.currentTimeMillis();
			//calculate time touch down and up
			long downUpDurationTime = touchUpTime - touchDownTime;
			//single click
			if(downUpDurationTime <= singleClickDurationTime){
				//calculate click time
				long twoClickDurationTime = touchUpTime - lastSingleClickTime;

				if(twoClickDurationTime <=  doubleClickDurationTime){
					//double click
					touchType = TOUCH_DOUBLE_CLICK;
					//reset
					lastSingleClickTime = -1;
					touchDownTime = -1;
					touchUpTime = -1;
				}else{
					//single click
					lastSingleClickTime = touchUpTime;
				}
			}
		}
		return touchType;
	}



	private void onSingleClick(float x, float y){
		if(status == STATUS_GAME_STARTED){
			if(isClickPause(x, y)){
				//press pause
				pause();
			}
		}else if(status == STATUS_GAME_PAUSED){
			if(isClickContinueButton(x, y)){
				//continue
				resume();
			}
		}else if(status == STATUS_GAME_OVER){
			if(isClickRestartButton(x, y)){
				//restart
				restart();
			}
		}
		else if(status == STATUS_GAME_Finish){
			if(isClickFinishButton(x, y)){
				//finish
				finish();
			}
		}
	}

	//click pause
	private boolean isClickPause(float x, float y){
		RectF pauseRecF = getPauseBitmapDstRecF();
		return pauseRecF.contains(x, y);
	}

	//click continue
	private boolean isClickContinueButton(float x, float y){
		return continueRect.contains((int)x, (int)y);
	}

	//click finish
	private boolean isClickFinishButton(float x, float y){
		return continueRect.contains((int)x, (int)y);
	}

	//click restart
	private boolean isClickRestartButton(float x, float y){
		return continueRect.contains((int)x, (int)y);
	}

	private RectF getPauseBitmapDstRecF(){
		Bitmap pauseBitmap = status == STATUS_GAME_STARTED ? bitmaps.get(8) : bitmaps.get(9);
		RectF recF = new RectF();
		recF.left = 15 * density;
		recF.top = 15 * density;
		recF.right = recF.left + pauseBitmap.getWidth();
		recF.bottom = recF.top + pauseBitmap.getHeight();
		return recF;
	}

	//destroy
	private void destroyNotRecyleBitmaps(){
		//set destroy
		status = STATUS_GAME_DESTROYED;

		//frame
		frame = 0;

		//score
		score = 0;

		//destroy aircraft
		if(combatAircraft != null){
			combatAircraft.destroy();
		}
		combatAircraft = null;

		//destroy enemy, bullet
		for(Sprite s : sprites){
			s.destroy();
		}
		sprites.clear();
	}

	public void destroy(){
		destroyNotRecyleBitmaps();

		//Bitmap
		for(Bitmap bitmap : bitmaps){
			bitmap.recycle();
		}
		bitmaps.clear();
	}

	public void addSprite(Sprite sprite){
		spritesNeedAdded.add(sprite);
	}

	//add score
	public void addScore(int value){
		score += value;
	}

	public int getStatus(){
		return status;
	}

	public float getDensity(){
		return density;
	}

	public Bitmap getYellowBulletBitmap(){
		return bitmaps.get(2);
	}

	public Bitmap getBlueBulletBitmap(){
		return bitmaps.get(3);
	}

	public Bitmap getExplosionBitmap(){
		return bitmaps.get(1);
	}

	//get enemy
	public List<EnemyPlane> getAliveEnemyPlanes(){
		List<EnemyPlane> enemyPlanes = new ArrayList<EnemyPlane>();
		for(Sprite s : sprites){
			if(!s.isDestroyed() && s instanceof EnemyPlane){
				EnemyPlane sprite = (EnemyPlane)s;
				enemyPlanes.add(sprite);
			}
		}
		return enemyPlanes;
	}

	//get bullet awards
	public List<BulletAward> getAliveBulletAwards(){
		List<BulletAward> bulletAwards = new ArrayList<BulletAward>();
		for(Sprite s : sprites){
			if(!s.isDestroyed() && s instanceof BulletAward){
				BulletAward bulletAward = (BulletAward)s;
				bulletAwards.add(bulletAward);
			}
		}
		return bulletAwards;
	}

	//get bullet
	public List<Bullet> getAliveBullets(){
		List<Bullet> bullets = new ArrayList<Bullet>();
		for(Sprite s : sprites){
			if(!s.isDestroyed() && s instanceof Bullet){
				Bullet bullet = (Bullet)s;
				bullets.add(bullet);
			}
		}
		return bullets;
	}
}